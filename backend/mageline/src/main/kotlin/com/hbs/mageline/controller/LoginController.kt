package com.hbs.mageline.controller

import com.hbs.mageline.database.entity.User
import com.hbs.mageline.database.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

class LoginBody {
    lateinit var username: String
    lateinit var password: String
}

class LoginResponse(var sessionId: String?, var error: String? = null) {
}

@Controller // This means that this class is a Controller
class LoginController {
    @Autowired // This means to get the bean called userRepository
    private lateinit var userRepository: UserRepository

    @PostMapping("/login")
    @ResponseBody
    fun performLogin(@RequestBody body: LoginBody): ResponseEntity<User> {
        val allUsers = userRepository.findAll() // This returns a JSON or XML with the users
        for (user in allUsers) {
            if (user.username == body.username.lowercase() && user.password == body.password) {
                user.generateSessionId()
                userRepository.save(user)
                return ResponseEntity.ok(user)
            }
        }

        return ResponseEntity(HttpStatus.UNAUTHORIZED)
    }

    @GetMapping("/login/{sessionId}")
    @ResponseBody
    fun verifyLogin(@RequestParam sessionId: String?): ResponseEntity<User> {
        val allUsers = userRepository.findAll() // This returns a JSON or XML with the users
        for (user in allUsers) {
            if (user.sessionId == sessionId) {
                return ResponseEntity.ok(user)
            }
        }

        return ResponseEntity(HttpStatus.UNAUTHORIZED)
    }


}