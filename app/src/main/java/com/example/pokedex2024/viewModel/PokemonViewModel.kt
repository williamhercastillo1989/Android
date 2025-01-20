package com.example.pokedex2024.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.pokedex2024.data.PokemonDataSource
import com.example.pokedex2024.repository.PokedexRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(private val repository: PokedexRepository) :
    ViewModel() {

    val pokemonPage = Pager(PagingConfig(pageSize = 3)){
        PokemonDataSource(repository)
    }.flow.cachedIn(viewModelScope)

}