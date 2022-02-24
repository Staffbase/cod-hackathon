package com.hbs.mageline.database.repository

import com.hbs.mageline.database.entity.Post
import org.springframework.data.repository.CrudRepository

interface PostRepository : CrudRepository<Post, String> {
}