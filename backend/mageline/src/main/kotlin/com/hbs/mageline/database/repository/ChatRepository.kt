package com.hbs.mageline.database.repository

import com.hbs.mageline.database.entity.Chat
import org.springframework.data.repository.CrudRepository

interface ChatRepository: CrudRepository<Chat, String> {
}