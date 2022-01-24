package dev.andrewohara.petstore.images.thirdparty

import dev.andrewohara.petstore.images.PetPhoto
import org.http4k.core.*
import org.http4k.format.Jackson
import org.http4k.lens.Header
import org.http4k.lens.Path
import org.http4k.lens.string
import java.io.IOException
import java.io.InputStream

class ThirdPartyImageClient(private val backend: HttpHandler) {

    object Lenses {
        val contentType = Header.CONTENT_TYPE

        val image = Jackson.autoBody<ThirdPartyImageDto>().toLens()
    }

    object Paths {
        const val upload = "/v1/images"
    }

    fun upload(contentType: String, content: InputStream): PetPhoto? {
        val response = Request(Method.POST, Paths.upload)
            .with(Lenses.contentType of ContentType(contentType))
            .body(content)
            .let(backend)

        return when(response.status) {
            Status.OK -> Lenses.image(response).toPetPhoto()
            Status.NOT_ACCEPTABLE -> null
            else -> throw IOException("Error uploading image: $response")
        }
    }

    companion object
}

