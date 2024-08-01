@file:Suppress("SqlNoDataSourceInspection")

package io.mds.springbootdemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import java.util.*

@SpringBootApplication
class SpringBootDemoApplication

fun main(args: Array<String>) {
	runApplication<SpringBootDemoApplication>(*args)
}


@Table("MESSAGES")
data class Message(@Id val id: String?, val text: String)


interface MessageRepository : CrudRepository<Message, String>


@Service
class MessageService(private val db: MessageRepository) {
	fun findMessages(): List<Message> = db.findAll().toList()

	fun findMessageById(id: String): Optional<Message> = db.findById(id)

	fun save(message: Message): Message {
		return db.save(message)
	}

//	fun <T : Any> Optional<out T>.toList(): List<T> = if (isPresent) listOf(get()) else emptyList()
}


@RestController
class MessageController(private val service: MessageService) {
	@GetMapping("/")
	fun index(): List<Message> = service.findMessages()

	@GetMapping("/{id}")
	fun index(@PathVariable id: String): Optional<Message> = service.findMessageById(id)

	@PostMapping("/")
	fun post(@RequestBody message: Message): Message {
		return service.save(message)
	}
}