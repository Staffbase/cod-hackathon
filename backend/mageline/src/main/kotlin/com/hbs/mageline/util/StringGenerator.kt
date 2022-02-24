package com.hbs.mageline.util

import kotlin.streams.asSequence

class StringGenerator {
    companion object {
        fun generate(size: Long): String {
            val source = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890"
            return java.util.Random().ints(size, 0, source.length)
                    .asSequence()
                    .map(source::get)
                    .joinToString("")
        }
    }
}