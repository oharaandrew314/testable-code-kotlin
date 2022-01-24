package dev.andrewohara.petstore.api

import dev.andrewohara.petstore.pets.Pet

data class PetDto(
    val id: Long,
    val name: String,
    val photoUrls: List<String>
)

fun Pet.toDto() = PetDto(
    id = id,
    name = name,
    photoUrls = photoUrls
)

data class PetCreateDto(
    val name: String
)