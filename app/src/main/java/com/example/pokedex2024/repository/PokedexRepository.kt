package com.example.pokedex2024.repository

import com.example.pokedex2024.data.ApiPokedex
import kotlinx.coroutines.flow.flow

import javax.inject.Inject

class PokedexRepository @Inject constructor(private val apiPokemon: ApiPokedex) {


    suspend fun getPokemon() = flow {
        val response = apiPokemon.getPokemon()
        if (response.isSuccessful) {
            emit(response.body()?.results)
        }
    }
}