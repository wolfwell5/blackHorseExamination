package com.example.blackhorsetemplate.todos


interface TodoServiceInterface {
    fun save(todo: Todo): Todo
    fun findAllAndGetFirst(): Todo
}
