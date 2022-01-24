package dev.andrewohara.petstore.images.thirdparty

import org.http4k.core.*
import org.http4k.format.Jackson.auto
import org.http4k.lens.Header
import java.io.IOException
import java.io.InputStream

class ThirdPartyImageClient(private val backend: HttpHandler) {

    companion object {
        const val uploadPath = "/v1/images"
        val imageBody = Body.auto<ThirdPartyImageDto>().toLens()
    }

    fun upload(contentType: String, content: InputStream): String? {
        val response = Request(Method.POST, uploadPath)
            .with(Header.CONTENT_TYPE of ContentType(contentType))
            .body(content)
            .let(backend)

        return when(response.status) {
            Status.OK -> imageBody(response).url
            Status.NOT_ACCEPTABLE -> null
            else -> throw IOException("Error uploading image: $response")
        }
    }
}

data class ThirdPartyImageDto(
    val id: String,
    val url: String,
    val contentType: String,
    val size: Int
)