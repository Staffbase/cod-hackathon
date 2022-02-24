package com.hbs.mageline.database.repository

import com.hbs.mageline.database.entity.Image
import org.springframework.data.jpa.repository.JpaRepository

interface ImageRepository: JpaRepository<Image, String> {

}