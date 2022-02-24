package com.hbs.mageline.util

import com.hbs.mageline.database.entity.*
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.OneToOne

class ChannelResponse {
    lateinit var id: String
    lateinit var title: String
    lateinit var description: String

    constructor(channel: Channel? = null) {
        channel?.let {
            id = it.id
            title = it.title
            description = it.description
        }
    }
}

class PostResponse {
    lateinit var id: String
    lateinit var title: String
    lateinit var message: String
    lateinit var userId: String
    lateinit var pictures: MutableList<String>
    var shares: Int = 0
    lateinit var lastUpdated: LocalDateTime
    lateinit var profile: Profile

    constructor(post: Post?) {
        post?.let {
            id = it.id
            title = it.title
            message = it.message
            userId = it.profile.id
            pictures = it.getImagesIds()
            shares = it.shares
            lastUpdated = it.lastUpdated
            profile = it.profile
        }
    }
}

class CommentResponse {
    lateinit var id: String
    lateinit var userId: String
    lateinit var message: String
    lateinit var sent: LocalDateTime

    constructor(comment: Comment?) {
        comment?.let {
            id = it.id
            message = it.message
            userId = it.profile.id
            sent = it.sent
        }
    }
}