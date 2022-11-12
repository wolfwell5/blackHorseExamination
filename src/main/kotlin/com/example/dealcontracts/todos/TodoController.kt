package com.example.dealcontracts.todos

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/todos")
class TodoController(var todoService: TodoService) {

    @PostMapping
    fun create(@RequestBody todoRequest: TodoRequest): ResponseEntity<Void> {
        todoService.save(Todo(task = todoRequest.task!!))
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @GetMapping
    fun getJustPostedTodo(): ResponseEntity<String> {
        val findAll = todoService.findAllAndGetFirst()
        return ResponseEntity.ok(findAll.task)
    }

    @PostMapping("/rabbit")
    fun postMessage(@RequestBody todoRequest: TodoRequest): ResponseEntity<String> {
        todoService.postMessage(todoRequest)
        return ResponseEntity.ok("nothing")
    }

}

