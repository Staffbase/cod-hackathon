package com.hbs.mageline.database.entity

import com.hbs.mageline.util.StringGenerator
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
class Channel {
    @Id
    var id: String = StringGenerator.generate(15)

    lateinit var title: String
    lateinit var description: String

    @OneToMany
    private lateinit var posts: MutableList<Post>

    fun getPosts(): MutableList<Post>{
        return posts
    }

    fun startPosts() {
        posts = LinkedList()
    }
}