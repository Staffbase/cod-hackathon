package com.hbs.mageline.database.entity

import com.hbs.mageline.util.StringGenerator
import java.time.LocalDateTime
import javax.persistence.*
import kotlin.collections.ArrayList

@Entity
class Post {
    @Id
    var id: String = StringGenerator.generate(15)

    lateinit var title: String
    lateinit var message: String

    var shares: Int = 0

    @OneToOne
    lateinit var profile: Profile

    private var likes: ArrayList<ProfileReturn> = ArrayList()

    @OneToMany
    lateinit var comments: MutableList<Comment>

    @OneToMany
    private lateinit var pictures: MutableList<Image>

    lateinit var lastUpdated: LocalDateTime

    fun getImagesIds(): MutableList<String> {
        val ids: ArrayList<String> = ArrayList()

        for (image in pictures) {
            ids.add(image.id)
        }

        return ids
    }

    fun setImages(images: MutableList<Image>) {
        pictures = images
    }
}