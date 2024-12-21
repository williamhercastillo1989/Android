package com.example.pokedex2024.model

data class PokemonResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Pokemon>,
)

data class Pokemon(
    val name: String,
    val url: String,
){

    val namePokemon: String
        get() = name.replaceFirstChar { it.uppercase() }

    val imageUrl: String
        inline get() {
            val index = url.split("/".toRegex()).dropLast(1).last()
            return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/" +
                    "pokemon/other/official-artwork/$index.png"
        }
}
