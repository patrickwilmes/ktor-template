package identifier

import arrow.core.Either
import arrow.core.getOrElse
import failure.Failure
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

sealed interface UrnSuffix {
    val value: Any

    data class UuidSuffix(override val value: String = generateRandomUUID()) : UrnSuffix

    companion object {
        fun determineSuffixForUrnString(urnString: String): UrnSuffix =
            { urn: String ->
                urn.split(":")[2]
            }.let { urnIdPartFunc ->
                UuidSuffix(value = urnIdPartFunc(urnString))
            }
    }
}

/**
 * URN to represent identifier of the format urn:domain:UUID.
 * The urn type make those parts of an urn accessible in the code.
 */
@Serializable(with = URNSerializer::class)
class URN private constructor(
    private val domain: Domain,
    val suffix: UrnSuffix,
) {
    override fun toString() = "urn:${domain.name.lowercase()}:${suffix.value}"

    fun normalizeForIO() = toString().replace(":", "_")

    override fun hashCode(): Int {
        return domain.hashCode() + suffix.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other !is URN) {
            return false
        }
        return other.domain == domain && other.suffix == suffix
    }

    companion object {

        fun createFromNormalizedString(normalizedUrnString: String): URN = normalizedUrnString.replace("_", ":").let {
            createFromStringOrGetRandom(urnString = it)
        }

        /*
        Use this one to signal that we are in a context where it's impossible to get a wrong URN string.
        If we really get a wrong URN here, someone messed with the database, which is a big NO-NO.
         */
        fun createFromStringOrGetRandom(urnString: String): URN = createFromString(urn = urnString).getOrElse {
            assemble(domain = Domain.Undefined, suffix = UrnSuffix.UuidSuffix())
        }

        fun toUrnOrNull(urn: String): URN? =
            when (val possibleUrn = createFromString(urn)) {
                is Either.Left -> null
                is Either.Right -> possibleUrn.value
            }

        /**
         * When a new domain entity is created, this function should be used to generate
         * a new [URN]. A [URN] with [UrnSuffix.UuidSuffix] will be generated.
         *
         * @param domain [Domain] represents the business domain, the [URN] should be generated for
         *
         * @return [URN] with [UrnSuffix.UuidSuffix]
         */
        fun forDomain(domain: Domain): URN = URN(domain = domain, suffix = UrnSuffix.UuidSuffix())

        fun createFromString(urn: String): Either<Failure, URN> {
            if (urn.count { it == ':' } != 2) {
                return Either.Left(Failure.BasicFailure(message = "URN format is wrong! Expected 2 colons! Example urn:domain:UUID"))
            }
            val parts = urn.split(":")
            val domain = parts[1].replaceFirstChar { it.titlecaseChar() }
            return Either.Right(
                URN(
                    domain = Domain.values().first { it.name.lowercase() == domain.lowercase() },
                    suffix = UrnSuffix.determineSuffixForUrnString(urn),
                )
            )
        }

        fun assemble(domain: Domain, suffix: UrnSuffix): URN =
            URN(domain = domain, suffix = suffix)

    }
}

class URNSerializer : KSerializer<URN> {
    override val descriptor = PrimitiveSerialDescriptor(
        serialName = "URN",
        kind = PrimitiveKind.STRING
    )

    override fun deserialize(decoder: Decoder): URN =
        decoder.decodeString().let { urnString ->
            UrnSuffix.determineSuffixForUrnString(urnString = urnString).let { urnSuffix ->
                URN.assemble(domain = determineDomainFromUrnString(urnString), suffix = urnSuffix)
            }
        }

    override fun serialize(encoder: Encoder, value: URN) {
        encoder.encodeString(value.toString())
    }

}
