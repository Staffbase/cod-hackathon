package com.hbs.mageline.controller

import com.hbs.mageline.database.entity.Channel
import com.hbs.mageline.database.entity.Image
import com.hbs.mageline.database.entity.Post
import com.hbs.mageline.database.repository.ImageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import kotlin.jvm.Throws

@Controller // This means that this class is a Controller
@RequestMapping(path=["/images"])
internal class ImageController {
    @Autowired
    lateinit var imageRepository: ImageRepository

    @get:GetMapping
    @get:ResponseBody
    val allPictures: List<String>
        get() = imageRepository.findAll().map { image: Image -> image.id }

    @PostMapping(path = ["/new"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @ResponseBody
    fun uploadImages(@RequestParam("files") multipartImages: List<MultipartFile>): ResponseEntity<MutableIterable<String>> {
        val ids: ArrayList<String> = ArrayList()

        for (image in multipartImages) {
            val dbImage = Image()
            dbImage.name = image.name
            dbImage.type = image.contentType ?: ""
            dbImage.content = image.bytes
            val savedItem = imageRepository.save(dbImage)
            ids.add(savedItem.id)
        }

        return ResponseEntity.ok(ids)
    }

    @GetMapping("/{imageId}", produces = [MediaType.IMAGE_JPEG_VALUE])
    @ResponseBody
    fun downloadImage(@PathVariable imageId: String): Resource {
        val image: ByteArray = imageRepository.findById(imageId)
                .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }
                .content
        return ByteArrayResource(image)
    }
}