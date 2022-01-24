package dev.andrewohara.petstore.pets

import dev.andrewohara.petstore.images.ImageService
import java.io.InputStream
import java.lang.IllegalStateException

class PetService(private val pets: PetsDao, private val images: ImageService) {

    fun get(id: Pet.Id): Pet? {
        return pets[id]
    }

    fun create(name: String): Pet {
        val id = pets.create(name)
        return pets[id] ?: throw IllegalStateException("Pet was not created")
    }

    fun uploadImage(petId: Pet.Id, contentType: String, content: InputStream): Pet? {
        pets[petId] ?: return null

        val photoRef = images.uploadImage(contentType, content) ?: return null
        pets.addImage(petId, photoRef.url)

        return pets[petId]
    }
}