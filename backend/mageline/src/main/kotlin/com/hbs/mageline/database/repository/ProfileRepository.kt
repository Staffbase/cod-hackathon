package com.hbs.mageline.database.repository

import com.hbs.mageline.database.entity.Profile
import org.springframework.data.repository.CrudRepository

interface ProfileRepository: CrudRepository<Profile, String> {
}