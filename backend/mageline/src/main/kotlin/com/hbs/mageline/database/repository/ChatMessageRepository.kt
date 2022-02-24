package com.hbs.mageline.database.repository

import com.hbs.mageline.database.entity.ChatMessage
import org.springframework.data.repository.CrudRepository

interface ChatMessageRepository: CrudRepository<ChatMessage, String> {
}