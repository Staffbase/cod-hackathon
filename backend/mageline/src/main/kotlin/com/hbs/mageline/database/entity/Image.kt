package com.hbs.mageline.database.entity

import com.hbs.mageline.util.StringGenerator
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Lob


@Entity
class Image {
    @Id
    var id: String = StringGenerator.generate(15)

    @Lob
    lateinit var content: ByteArray

    lateinit var name: String
    lateinit var type: String
}