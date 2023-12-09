package com.hyoii.config

import com.zaxxer.hikari.HikariDataSource
import jakarta.persistence.EntityManagerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@ComponentScan(basePackages = ["com.hyoii.config", "com.hyoii.domain"])
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = ["com.hyoii.domain"]
)
@Import(QueryDslConfig::class)
class DataSourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    fun dataConfig() = DataSourceProperties()

    @Bean
    @Primary
    fun dataSource(@Qualifier("dataConfig") dataConfig: DataSourceProperties) = HikariDataSource().apply {
        driverClassName = dataConfig.driverClassName
        jdbcUrl = dataConfig.url
        username = dataConfig.username
        password = dataConfig.password
    }

    @Bean
    @Primary
    fun entityManagerFactory(
        dataSource: HikariDataSource
    ) : EntityManagerFactory? = LocalContainerEntityManagerFactoryBean().apply {
        jpaVendorAdapter = HibernateJpaVendorAdapter()
        persistenceUnitName = "mall"
        setDataSource(dataSource)
        setPackagesToScan("com.hyoii.domain")
        setJpaPropertyMap(jpaProperties())
        afterPropertiesSet()
    }.`object`

    @Bean
    @Primary
    fun transactionManager(
        @Qualifier("entityManagerFactory") entityManagerFactory: EntityManagerFactory
    ) = JpaTransactionManager(entityManagerFactory)

    private fun jpaProperties() = mapOf(
        "hibernate.hbm2ddl.auto" to HIBERNATE_DDL_AUTO,
        "hibernate.show_sql" to HIBERNATE_SHOW_SQL,
        "hibernate.format_sql" to HIBERNATE_FORMAT_SQL,
        "hibernate.highlight_sql" to HIBERNATE_HIGHLIGHT_SQL,
        "hibernate.id.new_generator_mapping" to NEW_GENERATOR_MAPPING,
        "hibernate.dialect" to HIBERNATE_DIALECT
    )

    companion object {
        private const val HIBERNATE_DDL_AUTO = "update"
        private const val HIBERNATE_SHOW_SQL = true
        private const val HIBERNATE_FORMAT_SQL = true
        private const val HIBERNATE_HIGHLIGHT_SQL = true
        private const val NEW_GENERATOR_MAPPING = false
        private const val HIBERNATE_DIALECT = "org.hibernate.dialect.MySQLDialect"
    }
}
