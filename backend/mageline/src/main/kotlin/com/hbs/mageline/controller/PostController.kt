package com.hbs.mageline.controller

import com.hbs.mageline.database.entity.*
import com.hbs.mageline.database.repository.*
import com.hbs.mageline.database.repository.ImageRepository
import com.hbs.mageline.util.ChannelResponse
import com.hbs.mageline.util.CommentResponse
import com.hbs.mageline.util.PostResponse
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

class NewCommentBody {
    lateinit var message: String
    lateinit var userId: String
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
    val allChannelsResponse: List<ChannelResponse>
        get() = channelRepository.findAll().map { ChannelResponse(channel = it) }

    @PostMapping
    @ResponseBody
    fun createChannel(@RequestBody body: NewChannelBody): ResponseEntity<ChannelResponse> {
        val channel = Channel()
        channel.description = body.description
        channel.title = body.title
        channel.startPosts()

        channelRepository.save(channel)

        return ResponseEntity.ok(ChannelResponse(channel = channel))
    }

    @GetMapping(path = ["/{channelId}/posts"])
    @ResponseBody
    fun allPosts(@PathVariable channelId: String): ResponseEntity<List<PostResponse>> {
        var allChannels: MutableIterable<Channel> = channelRepository.findAll()
        val channel = allChannels.find { channel ->
            if (channel.id == channelId) {
                return@find true
            }
            return@find false
        } ?: return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)

        return ResponseEntity.ok(channel.getPosts().map { PostResponse(post = it) })
    }

    // Create Post
    @PostMapping(path = ["/{channelId}/posts"]) // Map ONLY POST Requests
    @ResponseBody
    fun createPost(@PathVariable channelId: String, @RequestBody body: NewPostBody): ResponseEntity<PostResponse> {
        var allChannels: MutableIterable<Channel> = channelRepository.findAll()
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

        return ResponseEntity.ok(PostResponse(post = post))
    }

    // Edit Post
    @PutMapping(path = ["/{channelId}/posts/{postId}"]) // Map ONLY POST Requests
    @ResponseBody
    fun editPost(@PathVariable channelId: String, @PathVariable postId: String, @RequestBody body: NewPostBody): ResponseEntity<PostResponse> {
        var allChannels: MutableIterable<Channel> = channelRepository.findAll()
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

        return ResponseEntity.ok(PostResponse(post = post))
    }

    @GetMapping(path = ["/{channelId}/posts/{postId}/comments"])
    @ResponseBody
    fun getComments(@PathVariable channelId: String, @PathVariable postId: String): ResponseEntity<Iterable<CommentResponse>> {
        var allChannels: MutableIterable<Channel> = channelRepository.findAll()
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

        return ResponseEntity.ok(post.comments.map { CommentResponse(comment = it) })
    }

    @GetMapping(path = ["/{channelId}/posts/{postId}/pictures"])
    @ResponseBody
    fun getPictures(@PathVariable channelId: String, @PathVariable postId: String): ResponseEntity<Iterable<String>> {
        var allChannels: MutableIterable<Channel> = channelRepository.findAll()
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

    @PostMapping(path = ["/{channelId}/posts/{postId}/comments"])
    @ResponseBody
    fun commentPost(@PathVariable channelId: String, @PathVariable postId: String, @RequestBody body: NewCommentBody): ResponseEntity<CommentResponse> {
        var allChannels: MutableIterable<Channel> = channelRepository.findAll()
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

        return ResponseEntity.ok(CommentResponse(comment = comment))
    }
}