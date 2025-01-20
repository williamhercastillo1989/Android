package com.example.pokedex2024.repository

import com.example.pokedex2024.data.ApiPokedex
import com.example.pokedex2024.model.PokemonResponse
import kotlinx.coroutines.delay
import retrofit2.Response

import javax.inject.Inject

class PokedexRepository @Inject constructor(private val apiPokemon: ApiPokedex) {


//    fun getPokemon() = flow {
//        val response = apiPokemon.getPokemon()
//        if (response.isSuccessful) {
//            emit(response.body()?.results)
//        }
//    }

    suspend fun  getPokemonPaging(offset:Int): Response<PokemonResponse> {
        delay(1000)
        return  apiPokemon.getPaginationPokemon(offset)
    }
}