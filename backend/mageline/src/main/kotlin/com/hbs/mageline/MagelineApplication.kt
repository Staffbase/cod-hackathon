package com.hbs.mageline

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MagelineApplication

fun main(args: Array<String>) {
	runApplication<MagelineApplication>(*args)
}
