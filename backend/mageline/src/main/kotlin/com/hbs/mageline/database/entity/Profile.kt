package com.hbs.mageline.database.entity

import com.hbs.mageline.util.StringGenerator
import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class Profile {
    @Id
    var id: String = StringGenerator.generate(15)

    lateinit var picture: String
    lateinit var name: String
    lateinit var email: String
    lateinit var phone: String
}