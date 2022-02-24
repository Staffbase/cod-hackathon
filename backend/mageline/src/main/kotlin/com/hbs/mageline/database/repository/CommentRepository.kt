package com.hbs.mageline.database.repository

import com.hbs.mageline.database.entity.Comment
import org.springframework.data.repository.CrudRepository

interface CommentRepository: CrudRepository<Comment, String> {
}