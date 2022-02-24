package com.hbs.mageline.controller

import com.hbs.mageline.database.entity.*
import com.hbs.mageline.database.repository.*
import com.hbs.mageline.database.repository.ImageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.util.*

class NewPostBody {
    lateinit var title: String
    lateinit var message: String
    lateinit var userId: String
    lateinit var pictures: MutableList<String>
}

class NewChannelBody {
    lateinit var title: String
    lateinit var description: String
}

@Controller // This means that this class is a Controller
@RequestMapping(path=["/channels"])
class PostController {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var postRepository: PostRepository

    @Autowired
    private lateinit var commentRepository: CommentRepository

    @Autowired
    private lateinit var imageRepository: ImageRepository

    @Autowired
    private lateinit var channelRepository: ChannelRepository

    @get:GetMapping
    @get:ResponseBody
    val allChannels: MutableIterable<Channel>
        get() = channelRepository.findAll()

    @PostMapping(path = ["/new"])
    @ResponseBody
    fun createChannel(@RequestBody body: NewChannelBody): ResponseEntity<Channel> {
        val channel = Channel()
        channel.description = body.description
        channel.title = body.title
        channel.startPosts()

        channelRepository.save(channel)

        return ResponseEntity.ok(channel)
    }

    @GetMapping(path = ["/{channelId}/posts"])
    @ResponseBody
    fun allPosts(@PathVariable channelId: String): ResponseEntity<MutableIterable<Post>> {
        val channel = allChannels.find { channel ->
            if (channel.id == channelId) {
                return@find true
            }
            return@find false
        } ?: return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)

        return ResponseEntity.ok(channel.getPosts())
    }

    // Create Post
    @PostMapping(path = ["/{channelId}/posts/new"]) // Map ONLY POST Requests
    @ResponseBody
    fun createPost(@PathVariable channelId: String, @RequestBody body: NewPostBody): ResponseEntity<Post> {
        val channel = allChannels.find { channel ->
            if (channel.id == channelId) {
                return@find true
            }
            return@find false
        } ?: return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)

        val allUsers = userRepository.findAll()
        val user = allUsers.find { user ->
            if (user.id == body.userId) {
                return@find true
            }
            return@find false
        } ?: return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)

        val post = Post()
        post.profile = user.profile
        post.message = body.message
        post.title = body.title
        post.lastUpdated = LocalDateTime.now()
//        post.likes = LinkedList()
        post.comments = LinkedList()

        val imageList = LinkedList<Image>()
        for (imageId in body.pictures) {
            val image = imageRepository.findById(imageId)
            if (image.isPresent) {
                imageList.add(image.get())
            } else {
                return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
            }
        }

        post.setImages(imageList)

        postRepository.save(post)

        channel.getPosts().add(post)
        channelRepository.save(channel)

        return ResponseEntity.ok(post)
    }

    // Edit Post
    @PostMapping(path = ["/{channelId}/posts/{postId}/edit"]) // Map ONLY POST Requests
    @ResponseBody
    fun editPost(@PathVariable channelId: String, @PathVariable postId: String, @RequestBody body: NewPostBody): ResponseEntity<Post> {
        val channel = allChannels.find { channel ->
            if (channel.id == channelId) {
                return@find true
            }
            return@find false
        } ?: return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)

        val allUsers = userRepository.findAll()
        val user = allUsers.find { user ->
            if (user.id == body.userId) {
                return@find true
            }
            return@find false
        } ?: return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)

        val post = channel.getPosts().find { post ->
            if (post.id == postId) {
                return@find true
            }
            return@find false
        } ?: return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)

        if (post.profile.id != user.profile.id) {
            return ResponseEntity(HttpStatus.FORBIDDEN)
        }

        post.title = body.title
        post.message = body.message
        post.lastUpdated = LocalDateTime.now()

        val imageList = LinkedList<Image>()
        for (imageId in body.pictures) {
            val image = imageRepository.findById(imageId)
            if (image.isPresent) {
                imageList.add(image.get())
            } else {
                return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
            }
        }

        post.setImages(imageList)

        postRepository.save(post)
        channelRepository.save(channel)

        return ResponseEntity.ok(post)
    }

    @GetMapping(path = ["/{channelId}/posts/{postId}/comments"])
    @ResponseBody
    fun getComments(@PathVariable channelId: String, @PathVariable postId: String): ResponseEntity<Iterable<Comment>> {
        val channel = allChannels.find { channel ->
            if (channel.id == channelId) {
                return@find true
            }
            return@find false
        } ?: return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)

        val post = channel.getPosts().find { post ->
            if (post.id == postId) {
                return@find true
            }
            return@find false
        } ?: return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)

        return ResponseEntity.ok(post.comments)
    }

    @GetMapping(path = ["/{channelId}/posts/{postId}/pictures"])
    @ResponseBody
    fun getPictures(@PathVariable channelId: String, @PathVariable postId: String): ResponseEntity<Iterable<String>> {
        val channel = allChannels.find { channel ->
            if (channel.id == channelId) {
                return@find true
            }
            return@find false
        } ?: return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)

        val post = channel.getPosts().find { post ->
            if (post.id == postId) {
                return@find true
            }
            return@find false
        } ?: return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)

        return ResponseEntity.ok(post.getImagesIds())
    }


    @PostMapping(path = ["/{channelId}/posts/{postId}/like/{userId}"])
    @ResponseBody
    fun likePost(@PathVariable channelId: String, @PathVariable postId: String, @PathVariable userId: String): ResponseEntity<Post> {
        val channel = allChannels.find { channel ->
            if (channel.id == channelId) {
                return@find true
            }
            return@find false
        } ?: return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)

        val allUsers = userRepository.findAll()
        val user = allUsers.find { user ->
            if (user.id == userId) {
                return@find true
            }
            return@find false
        } ?: return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)

        val post = channel.getPosts().find { post ->
            if (post.id == postId) {
                return@find true
            }
            return@find false
        } ?: return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)

        val profile = ProfileReturn()
        profile.id = user.profile.id
        profile.email = user.profile.email
        profile.name = user.profile.name
        profile.phone = user.profile.phone
        profile.picture = user.profile.picture

        for (like in post.likes) {
            if ( profile.id == like.id ) {
                post.likes.remove(like)
                break
            }
        }
        post.likes.add(profile)

        postRepository.save(post)
        channelRepository.save(channel)

        return ResponseEntity.ok(post)
    }

    @PostMapping(path = ["/{channelId}/posts/{postId}/comments"])
    @ResponseBody
    fun commentPost(@PathVariable channelId: String, @PathVariable postId: String, @RequestBody body: NewPostBody): ResponseEntity<Comment> {
        val channel = allChannels.find { channel ->
            if (channel.id == channelId) {
                return@find true
            }
            return@find false
        } ?: return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)

        val allUsers = userRepository.findAll()
        val user = allUsers.find { user ->
            if (user.id == body.userId) {
                return@find true
            }
            return@find false
        } ?: return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)

        val post = channel.getPosts().find { post ->
            if (post.id == postId) {
                return@find true
            }
            return@find false
        } ?: return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)

        val comment = Comment()
        comment.message = body.message
        comment.profile = user.profile
        comment.sent = LocalDateTime.now()
        commentRepository.save(comment)

        post.comments.add(comment)
        postRepository.save(post)
        channelRepository.save(channel)

        return ResponseEntity.ok(comment)
    }
}