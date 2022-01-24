package dev.andrewohara.petstore.pets

import org.h2.jdbcx.JdbcDataSource
import java.util.*


fun PetsDao.Companion.mock(): PetsDao {
    val source = JdbcDataSource().apply {
        setURL("jdbc:h2:mem:${UUID.randomUUID()};MODE=MySQL;DB_CLOSE_DELAY=-1")
        this.user = "sa"
        this.password = ""

        connection.use { conn ->
            conn.prepareStatement("CREATE TABLE pets (id INT(64) PRIMARY KEY NOT NULL AUTO_INCREMENT, name TEXT NOT NULL)").executeUpdate()
            conn.prepareStatement("CREATE TABLE photos (id INT(64) PRIMARY KEY NOT NULL AUTO_INCREMENT, pet_id INT(64), url TEXT NOT NULL)").executeUpdate()
        }
    }
    return PetsDao(source)
}