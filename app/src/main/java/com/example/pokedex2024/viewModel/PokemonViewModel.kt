package com.example.pokedex2024.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex2024.repository.PokedexRepository
import com.example.pokedex2024.views.UiState
import com.example.pokedex2024.views.UiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(private val repository: PokedexRepository) :
    ViewModel() {


    private val _pokemonFlow = MutableStateFlow<UiState>(UiState.Loading)
    val pokemonFlow: StateFlow<UiState> = _pokemonFlow.asStateFlow()


    init {
        loadPokemon()
    }

    private fun loadPokemon() {
        viewModelScope.launch {

            repository.getPokemon().flowOn(Dispatchers.IO)
                .catch { error ->
                    UiState.Error(error)
                }
                .collect { result ->
                    _pokemonFlow.value = Success(result!!)
                }
        }
    }

}