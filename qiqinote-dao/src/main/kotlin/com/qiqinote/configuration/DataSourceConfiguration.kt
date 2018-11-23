package com.qiqinote.configuration

import com.alibaba.druid.pool.DruidDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import javax.sql.DataSource

/**
 * Created by vanki on 2018/3/20 15:11.
 */
@Configuration
class DataSourceConfiguration {
    @Autowired
    private lateinit var env: Environment

    @Bean
    fun namedParameterJdbcTemplate(dataSource: DataSource) = NamedParameterJdbcTemplate(dataSource)

    @Bean(initMethod = "getLogWriter", destroyMethod = "close")
    fun dataSource(): DruidDataSource {
        val ds = DruidDataSource()
        ds.driverClassName = env["db.driverClassName"]
        ds.url = env["db.url"]
        ds.username = env["db.username"]
        ds.password = env["db.password"]
        ds.initialSize = env["db.initialSize"]!!.toInt()
        ds.maxActive = env["db.maxActive"]!!.toInt()
        ds.minIdle = env["db.minIdle"]!!.toInt()
        ds.maxWait = env["db.maxWait"]!!.toLong()
        ds.setConnectionProperties(env["db.connectionProperties"])
        ds.validationQuery = env["db.validationQuery"]
        ds.isTestOnBorrow = env["db.testOnBorrow"]!!.toBoolean()
        ds.isTestOnReturn = env["db.testOnReturn"]!!.toBoolean()
        ds.isTestWhileIdle = env["db.testWhileIdle"]!!.toBoolean()
        ds.timeBetweenEvictionRunsMillis = env["db.timeBetweenEvictionRunsMillis"]!!.toLong()
        ds.setFilters("stat")
        return ds
    }
}