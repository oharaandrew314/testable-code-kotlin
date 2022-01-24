package dev.andrewohara.petstore.images

import dev.andrewohara.petstore.images.thirdparty.ThirdPartyImageClient
import java.io.InputStream

/**
 * Images are hosted by some 3rd-party repository.
 * This service acts as a proxy to that repository, and caches their URLs.
 */
class ImageService(private val client: ThirdPartyImageClient) {

    /*
     * Upload an image and return its metadata.  Returns null if it wasn't accepted.
     */
    fun uploadImage(contentType: String, content: InputStream): PetPhoto? {
        return client.upload(
            contentType = contentType,
            content = content
        )
    }
}