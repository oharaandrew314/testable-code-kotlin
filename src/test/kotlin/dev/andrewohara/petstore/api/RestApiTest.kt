package dev.andrewohara.petstore.api

import dev.andrewohara.petstore.images.thirdparty.FakeThirdPartyImageBackend
import dev.andrewohara.petstore.images.thirdparty.ThirdPartyImageClient
import dev.andrewohara.petstore.images.thirdparty.ThirdPartyImageDto
import dev.andrewohara.petstore.pets.Pet
import dev.andrewohara.petstore.pets.PetService
import dev.andrewohara.petstore.pets.PetsDao
import dev.andrewohara.petstore.pets.mock
import io.kotest.matchers.be
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.http4k.core.*
import org.http4k.kotest.shouldHaveBody
import org.http4k.kotest.shouldHaveStatus
import org.http4k.lens.Header
import org.junit.jupiter.api.Test

class RestApiTest {

    private val imageBackend = FakeThirdPartyImageBackend()
    private val petService = PetService(
        pets = PetsDao.mock(),
        images = ThirdPartyImageClient(imageBackend)
    )

    private val testObj = RestApi(petService).toHttpHandler()

    @Test
    fun `get missing pet`() {
        val response = Request(Method.GET, RestApi.petPath)
            .with(RestApi.petIdLens of 123)
            .let(testObj)

        response shouldHaveStatus Status.NOT_FOUND
    }

    @Test
    fun `get pet`() {
        val pet = petService.create("Smokie")

        val response = Request(Method.GET, RestApi.petPath)
            .with(RestApi.petIdLens of pet.id)
            .let(testObj)

        response shouldHaveStatus Status.OK

        response.shouldHaveBody(RestApi.petLens, be(Pet(
            id = pet.id,
            name = "Smokie",
            photoUrls = emptyList()
        )))
    }

    @Test
    fun `create pet with empty body`() {
        val response = Request(Method.POST, RestApi.petsPath)
            .let(testObj)

        response shouldHaveStatus Status.BAD_REQUEST
    }

    @Test
    fun `create pet`() {
        val response = Request(Method.POST, RestApi.petsPath)
            .with(RestApi.nameLens of "Tigger")
            .let(testObj)

        response shouldHaveStatus Status.OK

        // test response body
        val created = RestApi.petLens(response)
        created.name shouldBe "Tigger"

        // test side effects
        petService.get(created.id) shouldBe Pet(
            id = created.id,
            name = "Tigger",
            photoUrls = emptyList()
        )
    }

    @Test
    fun `upload image for missing pet`() {
        val content = "I'm not a real png".toByteArray()

        val response = Request(Method.POST, RestApi.uploadImagePath)
            .with(RestApi.petIdLens of 123)
            .with(Header.CONTENT_TYPE of ContentType("image/png"))
            .body(content.inputStream())
            .let(testObj)

        response shouldHaveStatus Status.NOT_FOUND
    }

    @Test
    fun `upload image`() {
        val pet = petService.create("Bandit")

        val content = "I'm not a real png".toByteArray()

        val response = Request(Method.POST, RestApi.uploadImagePath)
            .with(RestApi.petIdLens of pet.id)
            .with(Header.CONTENT_TYPE of ContentType("image/png"))
            .body(content.inputStream())
            .let(testObj)

        response shouldHaveStatus Status.OK
        response.shouldHaveBody(RestApi.petLens, be(Pet(
            id = pet.id,
            name = "Bandit",
            photoUrls = listOf("http://images.fake/image0")
        )))

        // test side effects
        imageBackend.images.shouldContainExactly(
            ThirdPartyImageDto(
                id = "image0",
                contentType = "image/png",
                size = content.size,
                url = "http://images.fake/image0"
            )
        )
    }
}