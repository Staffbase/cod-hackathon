package com.hbs.mageline.database.entity

import com.hbs.mageline.util.StringGenerator
import java.time.LocalDateTime
import javax.persistence.*
import kotlin.jvm.Transient

@Entity
class Comment {
    @Id
    var id: String = StringGenerator.generate(15)

    @OneToOne
    lateinit var profile: Profile

    @Transient
    lateinit var message: String

    lateinit var sent: LocalDateTime
}