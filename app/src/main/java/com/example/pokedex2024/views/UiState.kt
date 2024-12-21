package com.example.pokedex2024.views

import com.example.pokedex2024.model.Pokemon

sealed interface UiState {

    object Loading : UiState

    data class Success(val data: List<Pokemon>) : UiState

    data class Error(val throwable: Throwable? = null) : UiState
}