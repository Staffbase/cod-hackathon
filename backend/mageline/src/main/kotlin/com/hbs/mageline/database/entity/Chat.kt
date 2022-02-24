package com.hbs.mageline.database.entity

import com.hbs.mageline.util.StringGenerator
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
class Chat {
    @Id
    var id: String = StringGenerator.generate(15)

    @OneToMany
    lateinit var participants: MutableList<Profile>

    @OneToMany
    private var messages: MutableList<ChatMessage> = LinkedList<ChatMessage>()

    fun getMessages(): MutableList<ChatMessage> {
        return messages
    }
}