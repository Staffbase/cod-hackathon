package com.hbs.mageline.controller

import com.hbs.mageline.database.entity.Chat
import com.hbs.mageline.database.entity.ChatMessage
import com.hbs.mageline.database.entity.Profile
import com.hbs.mageline.database.repository.ChatMessageRepository
import com.hbs.mageline.database.repository.ChatRepository
import com.hbs.mageline.database.repository.ProfileRepository
import com.hbs.mageline.database.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class NewChatBody {
    lateinit var participantIds: List<String>
}

class NewChatResponse(chat: Chat?, error: String? = null)

class ReceiveChatMessageBody {
    lateinit var senderId: String
    lateinit var date: String
}

class ChatMessageResponse(
        val senderId: String,
        val message: String,
        var sent: String? = null,
        var received: String? = null
)

class ReceiveChatMessageResponse(messages: List<ChatMessageResponse>?, error: String? = null)

class NewChatMessageBody {
    lateinit var senderId: String
    lateinit var message: String
}

class NewChatMessageResponse(date: String?, error: String? = null)

@Controller
@RequestMapping(path=["/chats"])
class ChatController {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var profileRepository: ProfileRepository

    @Autowired
    private lateinit var chatRepository: ChatRepository

    @Autowired
    private lateinit var chatMessageRepository: ChatMessageRepository

    @get:GetMapping
    @get:ResponseBody
    val allChats: MutableIterable<Chat>
        get() = chatRepository.findAll()

    fun fetchParticipants(body: NewChatBody): ArrayList<Profile> {
        val allProfiles = profileRepository.findAll()
        val participants = ArrayList<Profile>()
        for (profile in allProfiles) {
            if (body.participantIds.contains(profile.id)) {
                participants.add(profile)
            }
        }
        return participants
    }

    @PostMapping(path = ["/new"])
    @ResponseBody
    fun createChat(@RequestBody body: NewChatBody): NewChatResponse {
        val participants = fetchParticipants(body)
        var existingChat: Chat? = null
        for (chat in allChats) {
            var allIn = true
            for (participant in participants) {
                if (!chat.participants.contains(participant)) {
                    allIn = false
                    break
                }
            }
            if (allIn) {
                existingChat = chat
                break
            }
        }

        if (existingChat != null) {
            return NewChatResponse(existingChat)
        }

        val newChat = Chat()
        newChat.participants.addAll(participants)
        chatRepository.save(newChat)

        return NewChatResponse(newChat)
    }


    @GetMapping(path = ["/{id}"])
    @ResponseBody
    fun getMessages(@PathVariable id: String, @RequestBody body: ReceiveChatMessageBody): ReceiveChatMessageResponse {

        val chat = allChats.find {
            if (it.id == id) {
                return@find true
            }
            return@find false
        } ?: return ReceiveChatMessageResponse(null, "chat $id invalid")

        val messageList = ArrayList<ChatMessageResponse>()
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val dateTime = LocalDateTime.parse(body.date, formatter)

        for (message in chat.getMessages()) {
            if (message.sent.isAfter(dateTime)) {
                if (!message.isMessageReceived()) {
                    message.setMessageReceived()
                    message.received = LocalDateTime.now()
                }

                messageList.add(
                    ChatMessageResponse(
                        message.sender.id,
                        message.message,
                        formatter.format(message.sent),
                        formatter.format(message.received)
                    )
                )
            }
        }

        return ReceiveChatMessageResponse(messageList)
    }

    @PostMapping(path = ["/{id}"])
    @ResponseBody
    fun sendMessage(@PathVariable id: String, @RequestBody body: NewChatMessageBody): NewChatMessageResponse {
        val chat = allChats.find {
            if (it.id == id)
                return@find true
            return@find false
        } ?: return NewChatMessageResponse(null, "chat $id invalid")

        val profile = chat.participants.find {
            if (it.id == body.senderId)
                return@find true
            return@find false
        } ?: return NewChatMessageResponse(null, "user ${body.senderId} not on chat $id")

        val chatMessage = ChatMessage()
        chatMessage.message = body.message
        chatMessage.sender = profile
        chatMessage.sent = LocalDateTime.now()

        chatMessageRepository.save(chatMessage)

        chat.getMessages().add(chatMessage)
        chatRepository.save(chat)

        return NewChatMessageResponse("")
    }
}