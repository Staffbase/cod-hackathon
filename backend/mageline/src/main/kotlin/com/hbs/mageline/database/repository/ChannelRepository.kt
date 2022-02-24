package com.hbs.mageline.database.repository

import com.hbs.mageline.database.entity.Channel
import org.springframework.data.repository.CrudRepository

interface ChannelRepository : CrudRepository<Channel, String> {
}