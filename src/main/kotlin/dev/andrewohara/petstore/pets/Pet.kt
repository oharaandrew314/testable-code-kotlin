package dev.andrewohara.petstore.pets

data class Pet(
    val id: Id,
    val name: String,
    val photoUrls: List<String>
) {
    @JvmInline value class Id(val value: Long)
}

