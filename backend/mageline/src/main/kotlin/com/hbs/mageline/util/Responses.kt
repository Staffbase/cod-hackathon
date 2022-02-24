package com.hbs.mageline.util

import com.hbs.mageline.database.entity.Channel

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