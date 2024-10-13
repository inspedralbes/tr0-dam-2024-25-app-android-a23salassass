package com.example.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface QuizApiService {
    @GET("preguntas")
    suspend fun getQuizData(): List<Pregunta>

    @POST("estadisticas")
    suspend fun enviarEstadisticas(
        @Body estadisticas: Estadisticas
    ): retrofit2.Response<Void>
}

data class Pregunta(
    val id: Int,
    val pregunta: String,
    val respostes: List<Resposta>,
    val imatge: String
)

data class Resposta(
    val id: Int,
    val resposta: String,
    val correcta: Boolean,
    val count: Int? = 0
)



