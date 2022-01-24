package dev.andrewohara.petstore.pets

import dev.andrewohara.petstore.images.thirdparty.ThirdPartyImageClient
import java.io.InputStream
import java.lang.IllegalStateException

class PetService(private val pets: PetsDao, private val images: ThirdPartyImageClient) {

    fun get(id: Long): Pet? {
        return pets[id]
    }

    fun create(name: String): Pet {
        val id = pets.create(name)
        return pets[id] ?: throw IllegalStateException("Pet was not created")
    }

    fun uploadImage(petId: Long, contentType: String, content: InputStream): Pet? {
        pets[petId] ?: return null

        val url = images.upload(contentType, content) ?: return null
        pets.addImage(petId, url)

        return pets[petId]
    }
}