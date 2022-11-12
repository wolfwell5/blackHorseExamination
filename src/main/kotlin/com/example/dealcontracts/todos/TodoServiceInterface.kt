package com.example.dealcontracts.todos


interface TodoServiceInterface {
    fun save(todo: Todo): Todo
    fun findAllAndGetFirst(): Todo
}
