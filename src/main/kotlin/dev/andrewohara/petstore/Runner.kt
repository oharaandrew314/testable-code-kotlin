package dev.andrewohara.petstore

import com.mysql.cj.jdbc.MysqlDataSource
import dev.andrewohara.petstore.api.RestApi
import dev.andrewohara.petstore.images.thirdparty.ThirdPartyImageClient
import dev.andrewohara.petstore.pets.PetService
import dev.andrewohara.petstore.pets.PetsDao
import org.http4k.client.JavaHttpClient
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters
import org.http4k.server.SunHttp
import org.http4k.server.asServer

fun main() {
    val imagesHost = System.getenv("IMAGES_HOST")
    val imagesApiKey = System.getenv("IMAGES_API_KEY")

    val dbHost = System.getenv("DB_HOST")
    val dbUser = System.getenv("DB_USER")
    val dbPass = System.getenv("DB_PASS")
    val dbName = System.getenv("DB_NAME")

    val dataSource = MysqlDataSource().apply {
        databaseName = dbName
        serverName = dbHost
        user = dbUser
        password = dbPass
    }

    val thirdPartyImagesClient = ClientFilters.SetHostFrom(Uri.of(imagesHost))
        .then(ClientFilters.BearerAuth(imagesApiKey))
        .then(JavaHttpClient())
        .let { ThirdPartyImageClient(it) }

    val petService = PetService(
        pets = PetsDao(dataSource),
        images = thirdPartyImagesClient
    )

    RestApi(petService)
        .toHttpHandler()
        .asServer(SunHttp(port = 8000))
        .start()
        .block()
}