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
import java.util.*

class EditUserBody {
    lateinit var oldUsername: String
    lateinit var oldPassword: String
    lateinit var newUsername: String
    lateinit var newPassword: String
    lateinit var name: String
    lateinit var email: String
    lateinit var phone: String
    lateinit var picture: String
}

class NewUserBody {
    lateinit var name: String
    lateinit var email: String
    lateinit var phone: String
    lateinit var picture: String
    lateinit var username: String
    lateinit var password: String
}

@Controller // This means that this class is a Controller
@RequestMapping(path=["/users"])
class UserController {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var profileRepository: ProfileRepository

    @get:GetMapping
    @get:ResponseBody
    val allUsers: MutableIterable<User>
        get() = userRepository.findAll()

    @PostMapping
    @ResponseBody
    fun createUser(@RequestBody body: NewUserBody): ResponseEntity<User> {
        val user = allUsers.find { user ->
            if (user.username == body.username.lowercase()) {
                return@find true
            }
            return@find false
        }

        if (user != null) {
            return ResponseEntity(null, HttpStatus.FORBIDDEN)
        }

        val profile = Profile()
        profile.name = body.name
        profile.email = body.email
        profile.phone = body.phone
        profile.picture = body.picture
        profileRepository.save(profile)

        val newUser = User()
        newUser.username = body.username.lowercase()
        newUser.password = body.password
        newUser.sessionId = ""
        newUser.profile = profile
        newUser.contacts = LinkedList()
        userRepository.save(newUser)

        return ResponseEntity.ok(newUser)
    }

    @DeleteMapping
    @ResponseBody
    fun removeUser(@RequestBody body: LoginBody): ResponseEntity<User> {
        for (user in allUsers) {
            if (user.username == body.username && user.password == body.password) {
                userRepository.delete(user)
                return ResponseEntity.ok(user)
            }
        }

        return ResponseEntity(HttpStatus.FORBIDDEN)
    }

    @PutMapping(path = ["/{id}"])
    @ResponseBody
    fun editUser(@PathVariable id: String, @RequestBody body: EditUserBody): ResponseEntity<User> {
        val user = allUsers.find { user ->
            if (user.id == id) {
                return@find true
            }
            return@find false
        } ?: return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)

        if (user.username != body.oldUsername || user.password != body.oldPassword) {
            return ResponseEntity(HttpStatus.FORBIDDEN)
        }

        user.username = body.newUsername
        user.password = body.newPassword
        user.profile.name = body.name
        user.profile.email = body.email
        user.profile.phone = body.phone
        user.profile.picture = body.picture

        profileRepository.save(user.profile)
        userRepository.save(user)

        return ResponseEntity.ok(user)
    }
}