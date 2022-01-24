package dev.andrewohara.petstore.pets

data class Pet(
    val id: Long,
    val name: String,
    val photoUrls: List<String>
)