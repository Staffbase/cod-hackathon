package com.hbs.mageline.database.entity

import com.hbs.mageline.util.StringGenerator
import java.util.*
import javax.persistence.*

@Entity
class User {
    @Id
    var id: String = StringGenerator.generate(15)

    // Variables
    lateinit var username: String
    lateinit var password: String
    lateinit var sessionId: String

    @OneToOne
    lateinit var profile: Profile

    @OneToMany
    lateinit var contacts: MutableList<Profile>

    fun generateSessionId() {
        sessionId = StringGenerator.generate(30)
    }
}