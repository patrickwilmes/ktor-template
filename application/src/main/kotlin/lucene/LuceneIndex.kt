package lucene

import arrow.core.Either
import boot.InvalidateIndicesAndDirectoriesEvent
import failure.Failure
import failure.trap
import identifier.URN
import io.ktor.server.application.*
import kotlinx.coroutines.runBlocking
import logging.dbgln
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser
import org.apache.lucene.search.IndexSearcher

fun Application.indexPurgingListener() {
    environment.monitor.subscribe(InvalidateIndicesAndDirectoriesEvent) {
        dbgln("Event received for purging indices ...")
        runBlocking {
            LuceneIndex.clearIndex()
        }
        dbgln("Indices purged!")
    }
}

object LuceneIndex {
    private lateinit var analyzer: StandardAnalyzer
    suspend fun build(indexRegister: IndexRegister, documents: Sequence<Document>): Either<Failure, Unit> =
        trap(contextName = "LuceneIndex.buildIndex") {
            analyzer = StandardAnalyzer()
            val indexWriter = IndexWriter(indexRegister.directory, IndexWriterConfig(analyzer))
            documents.forEach {
                indexWriter.addDocument(it)
            }
            indexWriter.close()
        }

    suspend fun build(indexRegister: IndexRegister, documents: Set<Document>): Either<Failure, Unit> =
        trap(contextName = "LuceneIndex.buildIndex") {
            analyzer = StandardAnalyzer()
            val indexWriter = IndexWriter(indexRegister.directory, IndexWriterConfig(analyzer))
            documents.forEach {
                indexWriter.addDocument(it)
            }
            indexWriter.close()
        }

    fun search(indexRegister: IndexRegister, needle: String, fields: Array<String>, topCount: Int = 100): List<URN> =
        search(indexRegister, needle, fields, topCount = topCount) {
            URN.createFromStringOrGetRandom(urnString = it.get("idString"))
        }

    fun <T> search(indexRegister: IndexRegister, needle: String, fields: Array<String>, topCount: Int = 100, mappingFun: (Document) -> T): List<T> {
        if (needle.isEmpty()) return emptyList()

        val reader = DirectoryReader.open(indexRegister.directory)
        val searcher = IndexSearcher(reader)
        val query = MultiFieldQueryParser(fields, analyzer).parse("$needle*")

        val results = searcher.search(query, topCount)
        return results
            .scoreDocs
            .map { searcher.doc(it.doc) }
            .map(mappingFun)
    }

    fun clearIndex() {
        IndexRegister.values().forEach {
            val writer = IndexWriter(it.directory, IndexWriterConfig(StandardAnalyzer()))
            writer.deleteAll()
            writer.flush()
            writer.close()
        }
    }

    suspend fun invalidate(indexRegister: IndexRegister, documents: Set<Document>) {
        IndexWriter(indexRegister.directory, IndexWriterConfig(analyzer))
            .deleteAll()
        build(indexRegister, documents)
    }
}
