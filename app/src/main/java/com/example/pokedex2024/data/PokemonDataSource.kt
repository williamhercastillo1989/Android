package com.example.pokedex2024.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pokedex2024.model.Pokemon
import com.example.pokedex2024.repository.PokedexRepository
import com.example.pokedex2024.utils.Constants.Companion.POKEMON_OFFSET
import com.example.pokedex2024.utils.Constants.Companion.POKEMON_STARTING_OFFSET

class PokemonDataSource(private val repo: PokedexRepository) : PagingSource<Int, Pokemon>(){

    override fun getRefreshKey(state: PagingState<Int, Pokemon>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        return try {
            val nextPageNumber = params.key ?: POKEMON_STARTING_OFFSET
            val response = repo.getPokemonPaging(nextPageNumber * POKEMON_OFFSET)
            LoadResult.Page(
                data = response.body()!!.results,
                prevKey = null,
                nextKey = if (response.body()!!.results.isNotEmpty()) nextPageNumber + 1 else null
            )
        }catch (e:Exception){
            LoadResult.Error(e)
        }

    }
}