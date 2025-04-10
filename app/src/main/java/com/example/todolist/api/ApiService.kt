package com.example.todolist.api
import com.example.todolist.models.Task
import retrofit2.http.GET
interface ApiService {
    @GET("todos")
    suspend fun getTodos(): List<Task>

}