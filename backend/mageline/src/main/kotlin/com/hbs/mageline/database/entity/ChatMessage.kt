package com.hbs.mageline.database.entity

import com.hbs.mageline.util.StringGenerator
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class ChatMessage {
    @Id
    var id: String = StringGenerator.generate(15)

    @OneToOne
    lateinit var sender: Profile

    lateinit var message: String
    lateinit var sent: LocalDateTime
    lateinit var received: LocalDateTime

    private var isReceived: Boolean = false

    fun isMessageReceived(): Boolean {
        return isReceived
    }

    fun setMessageReceived() {
        isReceived = true
    }
}