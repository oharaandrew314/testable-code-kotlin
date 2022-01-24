package dev.andrewohara.petstore.images.thirdparty

import org.http4k.core.*
import org.http4k.length
import org.http4k.routing.bind
import org.http4k.routing.routes

class FakeThirdPartyImageBackend: HttpHandler {

    var nextId = 0
    val images = mutableListOf<ThirdPartyImageDto>()

    private fun upload(request: Request): Response {
        val id = "image${nextId++}"

        val content = request.body.payload

        val image = ThirdPartyImageDto(
            id = id,
            contentType = ThirdPartyImageClient.Lenses.contentType(request)
                ?.value
                ?: return Response(Status.BAD_REQUEST).body("no content-type"),
            size = content.length(),
            url = "http://images.fake/$id"
        )

        images += image

        return Response(Status.OK).with(ThirdPartyImageClient.Lenses.image of image)
    }

    private val routes = routes(
        ThirdPartyImageClient.Paths.upload bind Method.POST to ::upload
    )

    override fun invoke(request: Request) = routes(request)
}