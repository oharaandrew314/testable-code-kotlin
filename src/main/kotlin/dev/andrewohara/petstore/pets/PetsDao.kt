package dev.andrewohara.petstore.pets

import java.io.IOException
import javax.sql.DataSource

class PetsDao(private val dataSource: DataSource) {

    private object Sql {
        const val get = """
            SELECT pets.*, GROUP_CONCAT(photos.url) photo_urls
            FROM pets
            LEFT JOIN photos ON photos.pet_id = pets.id
            WHERE pets.id = ?
            GROUP BY pets.id
        """
        const val insertPet = "INSERT INTO pets (name) VALUES (?)"
        const val insertImage = "INSERT INTO photos(pet_id, url) VALUES (?, ?)"
    }

    operator fun get(id: Long): Pet? {
        dataSource.connection.use { conn ->
            conn.prepareStatement(Sql.get).use { stmt ->
                stmt.setLong(1, id)

                stmt.executeQuery().use { rs ->
                    if (!rs.next()) return null

                    return Pet(
                        id = rs.getLong("id"),
                        name = rs.getString("name"),
                        photoUrls = rs.getString("photo_urls")
                            ?.split(",")
                            ?: emptyList()
                    )
                }
            }
        }
    }

    fun create(name: String): Long {
        dataSource.connection.use { conn ->
            conn.prepareStatement(Sql.insertPet, arrayOf("id")).use { stmt ->
                stmt.setString(1, name)

                stmt.executeUpdate()

                stmt.generatedKeys.use { rs ->
                    if (!rs.next()) throw IOException("Failed to create pet")
                    return rs.getLong("id")
                }
            }
        }
    }

    fun addImage(petId: Long, url: String) {
        dataSource.connection.use { conn ->
            conn.prepareStatement(Sql.insertImage).use { stmt ->
                stmt.setLong(1, petId)
                stmt.setString(2, url)

                stmt.executeUpdate()
            }
        }
    }

    companion object
}