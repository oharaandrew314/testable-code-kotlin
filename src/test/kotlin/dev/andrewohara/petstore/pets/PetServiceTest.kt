package dev.andrewohara.petstore.pets

import dev.andrewohara.petstore.images.ImageService
import dev.andrewohara.petstore.images.thirdparty.FakeThirdPartyImageBackend
import dev.andrewohara.petstore.images.thirdparty.ThirdPartyImageClient
import dev.andrewohara.petstore.images.thirdparty.ThirdPartyImageDto
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class PetServiceTest {

    private val petsDao = PetsDao.mock()
    private val thirdPartyImageBackend = FakeThirdPartyImageBackend()

    private val testObj = PetService(
        pets = petsDao,
        images = ImageService(
            client = ThirdPartyImageClient(thirdPartyImageBackend)
        )
    )

    @Test
    fun `get missing pet`() {
        testObj.get(Pet.Id(123)) shouldBe null
    }

    @Test
    fun `get pet`() {
        val id = petsDao.create("Tigger")

        testObj.get(id) shouldBe Pet(
            id = id,
            name = "Tigger",
            photoUrls = emptyList()
        )
    }

    @Test
    fun `create pet`() {
        val result = testObj.create("Tigger")

        result.name shouldBe "Tigger"
        result.photoUrls.shouldBeEmpty()
    }

    @Test
    fun `add image`() {
        val id = petsDao.create("Tigger")

        val content = "foobarbaz".toByteArray()
        val updated = content.inputStream().use { data ->
            testObj.uploadImage(id, "image/png", data)
        }

        // test service result
        updated shouldBe Pet(
            id = id,
            name = "Tigger",
            photoUrls = listOf("http://images.fake/image0")
        )

        // test side-effect on the image repo
        thirdPartyImageBackend.images.shouldContainExactly(
            ThirdPartyImageDto(
                id = "image0",
                url = "http://images.fake/image0",
                contentType = "image/png",
                size = content.size
            )
        )
    }
}