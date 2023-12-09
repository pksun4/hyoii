package com.hyoii.config

import org.apache.commons.lang3.StringUtils
import org.hibernate.boot.model.naming.Identifier
import org.hibernate.boot.model.naming.PhysicalNamingStrategy
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment
import java.util.*

class ImprovedNamingStrategy : PhysicalNamingStrategy {
    override fun toPhysicalCatalogName(identifier: Identifier?, jdbcEnv: JdbcEnvironment?): Identifier? {
        return convert(identifier)
    }

    override fun toPhysicalColumnName(identifier: Identifier?, jdbcEnv: JdbcEnvironment?): Identifier? {
        return convert(identifier)
    }

    override fun toPhysicalSchemaName(identifier: Identifier?, jdbcEnv: JdbcEnvironment?): Identifier? {
        return convert(identifier)
    }

    override fun toPhysicalSequenceName(identifier: Identifier?, jdbcEnv: JdbcEnvironment?): Identifier? {
        return convert(identifier)
    }

    override fun toPhysicalTableName(identifier: Identifier?, jdbcEnv: JdbcEnvironment?): Identifier? {
        return convert(identifier)
    }

    private fun convert(identifier: Identifier?): Identifier? {
        if (identifier == null || StringUtils.isBlank(identifier.text)) {
            return identifier
        }
        val regex = "([a-z])([A-Z])"
        val replacement = "$1_$2"
        val newName = identifier.text.replace(regex.toRegex(), replacement).lowercase(Locale.getDefault())
        return Identifier.toIdentifier(newName)
    }
}
