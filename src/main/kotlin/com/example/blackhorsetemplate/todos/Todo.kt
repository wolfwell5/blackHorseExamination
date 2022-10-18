package com.example.blackhorsetemplate.todos

import javax.persistence.*


@Table(name = "todos")
@Entity
data class Todo(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    var task: String
)
