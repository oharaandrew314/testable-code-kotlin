package dev.andrewohara.petstore.api

import com.fasterxml.jackson.annotation.JsonProperty
import dev.andrewohara.petstore.pets.Pet
import java.time.Instant

//data class OrderDto(
//    val id: Long,
//    val petId: Long,
//    val quantity: Long,
//    val shipDate: Instant?,
//    val status: Status,
//    val complete: Boolean
//) {
//    enum class Status {
//        @JsonProperty(value = "placed") Placed,
//        @JsonProperty(value = "approved") Approved,
//        @JsonProperty(value = "delivered") Delivered
//    }
//}

data class PetDto(
    val id: Long,
    val name: String,
    val photoUrls: List<String>
)

fun Pet.toDto() = PetDto(
    id = id.value,
    name = name,
    photoUrls = photoUrls
)

data class PetCreateDto(
    val name: String
)