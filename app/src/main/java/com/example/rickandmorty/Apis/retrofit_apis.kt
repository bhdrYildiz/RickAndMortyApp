package com.example.rickandmorty.Apis

import com.example.rickandmorty.CharacterDetails
import com.example.rickandmorty.LocationModelMain
import com.example.rickandmorty.Modals.LocationDetail
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface retrofit_apis {

    @GET("location")
    suspend fun getLocations(@Query("page") page: Int): LocationModelMain
    @GET("location/{locationId}")
    suspend fun getLocationDetails(@Path("locationId") locationId: Int): LocationDetail
    @GET("character/{characterId}")
    suspend fun getCharacterDetails(@Path("characterId") characterId: Int): CharacterDetails
    @GET("character/{characterId}")
    suspend fun getMoreDetailCharacter(@Path("characterId") characterId: String): CharacterDetails
}