package com.example.pokedex2024.data

import com.example.pokedex2024.model.PokemonResponse
import com.example.pokedex2024.utils.Constants.Companion.ENDPOINT
import retrofit2.Response
import retrofit2.http.GET

interface ApiPokedex {

    @GET(ENDPOINT)
    suspend fun getPokemon(): Response<PokemonResponse>
}