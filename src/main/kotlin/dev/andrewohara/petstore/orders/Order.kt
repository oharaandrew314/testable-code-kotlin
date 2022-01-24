package dev.andrewohara.petstore.orders

import dev.andrewohara.petstore.pets.Pet
import java.time.Instant

//data class Order(
//    val id: Id,
//    val petId: Pet.Id,
//    val quantity: Long,
//    val shipDate: Instant?,
//    val status: Status
//){
//    val complete
//        get() = status == Status.Delivered
//
//    @JvmInline value class Id(val value: Long)
//
//    enum class Status { Placed, Approved, Delivered }
//}