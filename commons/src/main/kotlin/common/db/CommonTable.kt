package common.db

import Entity
import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.continuations.ensureNotNull
import failure.Failure
import failure.trap
import functions.mapToSet
import identifier.URN
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.wrap
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.math.ceil

data class ResultList<T>(
    val data: List<T>,
    val totalElements: ULong = 0U,
    val totalPages: UInt = 0U,
)

/**
 * IndexableContent is used for all domain types that are
 * having a custom search index. The interface provides
 * a function for selecting all data out of a database
 * without paging. This function must never be used
 * in performance critical szenarios.
 */
interface IndexableContent<T> {
    /**
     * The selectAllForIndexing function is used for gathering
     * all data from the database to put it into a lucene
     * index.
     */
    suspend fun selectAllForIndexing(): Either<Failure, Set<T>>
}

interface ReadAddon<T> {
    suspend fun selectById(id: URN): Either<Failure, T>
    suspend fun selectAll(page: UInt, size: UInt, vararg sorting: Pair<Expression<*>, SortOrder>): Either<Failure, ResultList<T>>
}

interface CrudRepository<T> : ReadAddon<T> {
    suspend fun create(entity: T): Either<Failure, URN>
    suspend fun update(entity: T): Either<Failure, URN>
    suspend fun delete(id: URN): Either<Failure, Unit>
}

abstract class MappingTable<T : Entity>(open val name: String) : Table(name = name) {
    abstract fun mapToTable(elem: T, insertStatement: InsertStatement<*>)
    abstract fun mapToDomain(row: ResultRow): T
    abstract fun mapToUpdate(elem: T, stmt: UpdateBuilder<*>)
}

abstract class AbstractTable<T : Entity>(override val name: String) : MappingTable<T>(name = name) {
    abstract val id: Column<String>
}

suspend inline fun <reified T : Entity> execInsert(contextName: String, table: AbstractTable<T>, element: T): Either<Failure, URN> = trap(
    contextName = contextName,
) {
    transaction {
        table.insert {
            table.mapToTable(element, it)
        }[table.id].let { idString ->
            URN.createFromStringOrGetRandom(urnString = idString)
        }
    }
}

suspend inline fun <reified T : Entity> execSelectSingleByStatement(contextName: String, table: AbstractTable<T>, op: Op<Boolean>): Either<Failure, T> =
    either {
        val possibleEntity = trap(
            contextName = contextName,
        ) {
            transaction {
                table.select { op }
                    .limit(1)
                    .map {
                        table.mapToDomain(it)
                    }.firstOrNull()
            }
        }.bind()
        ensureNotNull(possibleEntity) { Failure.NotFoundFailure(message = "No such entity in context") }
    }

suspend inline fun <reified T : Entity> execSelectById(contextName: String, table: AbstractTable<T>, id: URN): Either<Failure, T> =
    execSelectSingleByStatement(contextName, table, table.id eq id)

suspend inline fun <reified T : Entity> execUpdate(contextName: String, table: AbstractTable<T>, elem: T): Either<Failure, URN> =
    trap(
        contextName = contextName,
    ) {
        transaction {
            table.update({ table.id eq elem.id }) {
                table.mapToUpdate(elem, it)
            }
        }
        elem.id
    }

suspend inline fun <reified T : Entity> execSelectAll(contextName: String, table: AbstractTable<T>, page: UInt, size: UInt, vararg sorting: Pair<Expression<*>, SortOrder>): Either<Failure, ResultList<T>> =
    trap(
        contextName = contextName,
    ) {
        transaction {
            val total = table.selectAll().count()
            val data = table.selectAll()
                .limit(size.toInt(), (page * size).toLong())
                .orderBy(*sorting)
                .map { table.mapToDomain(it) }
            ResultList(data = data, totalElements = total.toULong(), totalPages = ceil(total.toDouble() / size.toInt()).toUInt())
        }
    }

suspend inline fun <reified T : Entity> execSelectAllUnPaged(contextName: String, table: AbstractTable<T>): Either<Failure, Set<T>> =
    trap(
        contextName = contextName,
    ) {
        transaction {
            table.selectAll()
                .mapToSet { table.mapToDomain(it) }
        }
    }

suspend inline fun <reified T : Entity> execDelete(contextName: String, table: AbstractTable<T>, id: URN): Either<Failure, Unit> = trap(
    contextName = contextName,
) {
    transaction {
        table.deleteWhere {
            table.id eq id
        }
    }
}

infix fun <T> ExpressionWithColumnType<T>.eq(t: URN): Op<Boolean> = EqOp(this, wrap(t.toString()))
