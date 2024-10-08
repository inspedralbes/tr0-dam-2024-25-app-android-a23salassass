package com.example.pr01

data class Resposta(
    val id: Int,
    val count: Int,
    val resposta: String,
    val correcta: Boolean
)

data class Pregunta(
    val id: Int,
    val pregunta: String,
    val respostes: List<Resposta>,
    val imatge: String
)

data class Quiz(
    val preguntes: List<Pregunta>
)


