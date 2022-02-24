package com.hbs.mageline.database.repository

import com.hbs.mageline.database.entity.User
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, String> {
}