package dev.andrewohara.petstore.pets

import org.h2.jdbcx.JdbcDataSource
import java.util.*

fun PetsDao.Companion.mock(): PetsDao {
    val source = JdbcDataSource().apply {
        setURL("jdbc:h2:mem:test;MODE=MySQL;DB_CLOSE_DELAY=-1")
        this.user = "sa"
        this.password = ""

        connection.use { conn ->
            val sql = javaClass.classLoader.getResourceAsStream("init.sql")!!.reader().readText()
            conn.prepareStatement(sql).use { stmt ->
                stmt.executeUpdate()
            }
        }
    }
    return PetsDao(source)
}