package dev.andrewohara.petstore.api

import dev.andrewohara.petstore.pets.Pet
import dev.andrewohara.petstore.pets.PetService
import org.http4k.core.*
import org.http4k.filter.ServerFilters
import org.http4k.format.Jackson
import org.http4k.lens.Header
import org.http4k.lens.Path
import org.http4k.lens.long
import org.http4k.routing.bind
import org.http4k.routing.routes

class RestApi(private val pets: PetService) {

    object Paths {
        const val pets = "/pet"
        val pet = "/pet/${Lenses.petId}"
        val uploadImage = "/pet/${Lenses.petId}/uploadImage"
    }

    object Lenses {
        val petId = Path.long().of("petId")

        val pet = Jackson.autoBody<PetDto>().toLens()
        val petCreate = Jackson.autoBody<PetCreateDto>().toLens()
    }

    private fun getPet(request: Request): Response {
        val id = Pet.Id(Lenses.petId(request))
        val pet = pets.get(id) ?: return Response(Status.NOT_FOUND)

        return Response(Status.OK).with(Lenses.pet of pet.toDto())
    }

    private fun createPet(request: Request): Response {
        val data = Lenses.petCreate(request)

        val pet = pets.create(name = data.name)

        return Response(Status.OK).with(Lenses.pet of pet.toDto())
    }

    private fun uploadImage(request: Request): Response {
        val contentType = Header.CONTENT_TYPE(request) ?: return Response(Status.BAD_REQUEST)
        val petId = Pet.Id(Lenses.petId(request))

        val updatedPet = request.body.use { body ->
            pets.uploadImage(petId, contentType.value, body.stream)
        } ?: return Response(Status.NOT_FOUND)

        return Response(Status.OK).with(Lenses.pet of updatedPet.toDto())
    }

    fun toHttpHandler() : HttpHandler {
        val routes = routes(
            Paths.pet bind Method.GET to ::getPet,
            Paths.pets bind Method.POST to ::createPet,
            Paths.uploadImage bind Method.POST to ::uploadImage
        )

        return ServerFilters.CatchLensFailure().then(routes)
    }
}