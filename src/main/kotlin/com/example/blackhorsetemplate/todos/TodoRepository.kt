package com.example.blackhorsetemplate.todos

import com.example.blackhorsetemplate.todos.Todo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TodoRepository : JpaRepository<Todo, Long>
