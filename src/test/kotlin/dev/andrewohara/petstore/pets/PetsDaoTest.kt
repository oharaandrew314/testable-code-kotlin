package dev.andrewohara.petstore.pets

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class PetsDaoTest {

    private val testObj = PetsDao.mock()

    @Test
    fun `get missing`() {
        testObj[123] shouldBe null
    }

    @Test
    fun `create and get`() {
        val id = testObj.create("Tigger")
        testObj[id] shouldBe Pet(
            id = id,
            name = "Tigger",
            photoUrls = emptyList()
        )
    }

    @Test
    fun `add photo urls and get`() {
        val id = testObj.create("Tigger")
        testObj.addImage(id, "http://foo.jpg")
        testObj.addImage(id, "http://bar.jpg")

        testObj[id] shouldBe Pet(
            id = id,
            name = "Tigger",
            photoUrls = listOf("http://foo.jpg", "http://bar.jpg")
        )
    }
}