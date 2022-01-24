package dev.andrewohara.petstore.api

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

    companion object {
        val petIdLens = Path.long().of("petId")
        val petLens = Jackson.autoBody<PetDto>().toLens()
        val petCreateLens = Jackson.autoBody<PetCreateDto>().toLens()

        const val petsPath = "/pet"
        val petPath = "/pet/${petIdLens}"
        val uploadImagePath = "/pet/${petIdLens}/uploadImage"
    }

    private fun getPet(request: Request): Response {
        val id = petIdLens(request)
        val pet = pets.get(id) ?: return Response(Status.NOT_FOUND)

        return Response(Status.OK).with(petLens of pet.toDto())
    }

    private fun createPet(request: Request): Response {
        val data = petCreateLens(request)

        val pet = pets.create(name = data.name)

        return Response(Status.OK).with(petLens of pet.toDto())
    }

    private fun uploadImage(request: Request): Response {
        val contentType = Header.CONTENT_TYPE(request) ?: return Response(Status.BAD_REQUEST)
        val petId = petIdLens(request)

        val updatedPet = request.body.use { body ->
            pets.uploadImage(petId, contentType.value, body.stream)
        } ?: return Response(Status.NOT_FOUND)

        return Response(Status.OK).with(petLens of updatedPet.toDto())
    }

    fun toHttpHandler() : HttpHandler {
        val routes = routes(
            petPath bind Method.GET to ::getPet,
            petsPath bind Method.POST to ::createPet,
            uploadImagePath bind Method.POST to ::uploadImage
        )

        return ServerFilters.CatchLensFailure().then(routes)
    }
}