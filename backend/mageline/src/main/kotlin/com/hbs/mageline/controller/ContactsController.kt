package com.hbs.mageline.controller

import com.hbs.mageline.database.entity.Profile
import com.hbs.mageline.database.entity.User
import com.hbs.mageline.database.repository.ProfileRepository
import com.hbs.mageline.database.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

class AddContactBody {
    lateinit var userId: String
    lateinit var contactId: String
}

@Controller
@RequestMapping(path=["/contacts"])
class ContactsController {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var profileRepository: ProfileRepository

    @get:GetMapping
    @get:ResponseBody
    val allContacts: MutableIterable<Profile>
        get() = profileRepository.findAll()

    private val allUsers: MutableIterable<User>
        get() = userRepository.findAll()

    private fun fetchItems(body: AddContactBody): Triple<User?, Profile?, String?> {
        val user = allUsers.find { user ->
            if (user.id == body.userId) {
                return@find true
            }
            return@find false
        } ?: return Triple(null, null, "user does not exists")

        val profile = allContacts.find { contact ->
            if (contact.id == body.contactId) {
                return@find true
            }
            return@find false
        } ?: return Triple(null, null, "profile invalid")

        return Triple(user, profile, null)
    }

    @PostMapping
    @ResponseBody
    fun addContact(@RequestBody body: AddContactBody): ResponseEntity<User> {
        val (user, profile, error) = fetchItems(body)

        if (error != null) {
            return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }

        if (user!!.contacts.contains(profile!!)) {
            return ResponseEntity(HttpStatus.FORBIDDEN)
        }

        user.contacts.add(profile)
        userRepository.save(user)

        return ResponseEntity.ok(user)
    }

    @DeleteMapping
    @ResponseBody
    fun removeContact(@RequestBody body: AddContactBody): ResponseEntity<User> {
        val (user, profile, error) = fetchItems(body)

        if (error != null) {
            return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }

        val contactList = user!!.contacts
        contactList.remove(profile)

        userRepository.save(user)

        return ResponseEntity.ok(user)
    }
}