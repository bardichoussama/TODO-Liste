package com.example.todolist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.models.Task
import com.example.todolist.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel() {

    private val _todos = MutableStateFlow<List<Task>>(emptyList())
    val todos: StateFlow<List<Task>> = _todos

    init {
        fetchTodosFromApi()  // Fetch tasks only when the ViewModel is initialized
    }

    private fun fetchTodosFromApi() {
        viewModelScope.launch {
            try {
                // Fetch tasks from the API (initial load only)
                val todoList = RetrofitClient.api.getTodos().take(10) // limit for testing
                _todos.value = todoList
            } catch (e: Exception) {
                // Handle error if necessary
                _todos.value = listOf(Task(0, "Error: ${e.message}", false))
            }
        }
    }

    fun toggleTodo(id: Int) {
        _todos.value = _todos.value.map {
            if (it.id == id) it.copy(completed = !it.completed) else it
        }
    }

    fun deleteTodo(id: Int) {
        _todos.value = _todos.value.filter { it.id != id }
    }

    fun addTodo(title: String) {
        val newTodo = Task(
            id = (_todos.value.maxOfOrNull { it.id } ?: 0) + 1,
            title = title,
            completed = false
        )

        // Add the new task locally (without calling the API again)
        _todos.value = _todos.value + newTodo
    }
}