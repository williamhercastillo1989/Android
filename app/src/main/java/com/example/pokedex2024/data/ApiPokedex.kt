package com.example.pokedex2024.data

import com.example.pokedex2024.model.PokemonResponse
import com.example.pokedex2024.utils.Constants.Companion.ENDPOINT
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiPokedex {

//    @GET(ENDPOINT)
//    suspend fun getPokemon(): Response<PokemonResponse>

    @GET(ENDPOINT)
    suspend fun getPaginationPokemon(@Query("offset") offset: Int): Response<PokemonResponse>
}