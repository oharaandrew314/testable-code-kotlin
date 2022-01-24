package dev.andrewohara.petstore.images.thirdparty

import org.http4k.core.*
import org.http4k.length
import org.http4k.lens.Header
import org.http4k.routing.bind
import org.http4k.routing.routes

class FakeThirdPartyImageBackend: HttpHandler {

    private var nextId = 0
    val images = mutableListOf<ThirdPartyImageDto>()

    private fun upload(request: Request): Response {
        val id = "image${nextId++}"

        val image = ThirdPartyImageDto(
            id = id,
            contentType = Header.CONTENT_TYPE(request)?.value
                ?: return Response(Status.BAD_REQUEST).body("no content-type"),
            size = request.body.payload.length(),
            url = "http://images.fake/$id"
        )

        images += image

        return Response(Status.OK).with(ThirdPartyImageClient.imageBody of image)
    }

    private val routes = routes(
        ThirdPartyImageClient.uploadPath bind Method.POST to ::upload
    )

    override fun invoke(request: Request) = routes(request)
}