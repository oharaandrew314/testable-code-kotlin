package dev.andrewohara.petstore.images.thirdparty

import dev.andrewohara.petstore.images.PetPhoto

data class ThirdPartyImageDto(
    val id: String,
    val url: String,
    val contentType: String,
    val size: Int
)

fun ThirdPartyImageDto.toPetPhoto() = PetPhoto(
    url = url,
    contentType = contentType
)