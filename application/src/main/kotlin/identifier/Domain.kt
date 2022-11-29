package identifier

enum class Domain {
    Undefined,
}

fun determineDomainFromUrnString(urnString: String): Domain =
    urnString.split(":")[1].let { domainPart ->
        domainPart.replaceFirstChar { firstChar -> firstChar.titlecaseChar() }
            .let { upperCasedDomainPart ->
                Domain.values().first { it.name.lowercase() == upperCasedDomainPart.lowercase() }
            }
    }
