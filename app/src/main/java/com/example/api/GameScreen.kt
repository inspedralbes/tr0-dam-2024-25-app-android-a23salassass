package com.example.api

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.delay

@Composable
fun GameScreen(navController: NavHostController, preguntas: List<Pregunta>) {
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var respostesCorrectes by remember { mutableIntStateOf(0) }
    var incorrectes by remember { mutableIntStateOf(0) }
    val currentQuestion = preguntas[currentQuestionIndex]
    var tempsPassat by remember { mutableIntStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<Resposta?>(null) }

    // Lista para almacenar las estadísticas de cada pregunta
    val listaDePreguntasEstadisticas = remember { mutableStateListOf<PreguntaEstadistica>() }

    // Lógica para incrementar el tiempo pasado
    LaunchedEffect(key1 = currentQuestionIndex) {
        tempsPassat = 0 // Reinicia el tiempo para la nueva pregunta
        while (currentQuestionIndex < preguntas.size) {
            delay(1000L)
            tempsPassat++
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = currentQuestion.pregunta)

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = rememberAsyncImagePainter(currentQuestion.imatge),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth().height(200.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar las respuestas
        currentQuestion.respostes.forEach { resposta ->
            Button(onClick = {
                selectedAnswer = resposta // Almacenar la respuesta seleccionada

                if (resposta.correcta) {
                    respostesCorrectes++
                } else {
                    incorrectes++
                }

                // Guardar la estadística de la pregunta actual antes de pasar a la siguiente
                val preguntaEstadistica = PreguntaEstadistica(
                    questionId = currentQuestion.id.toString(),
                    questionText = currentQuestion.pregunta,
                    correct = resposta.correcta, // Verificar si la respuesta fue correcta
                    timeElapsed = tempsPassat
                )
                listaDePreguntasEstadisticas.add(preguntaEstadistica)

                // Avanzar a la siguiente pregunta si no es la última
                if (currentQuestionIndex < preguntas.size - 1) {
                    currentQuestionIndex++
                    selectedAnswer = null // Reiniciar la respuesta seleccionada
                } else {
                    // Enviar estadísticas al servidor cuando se terminen las preguntas
                    val userId: String? = null // Aquí asignas el valor de userId
                    enviarEstadisticasAlServidor(userId, listaDePreguntasEstadisticas)

                    // Navegar a la pantalla de estadísticas
                    navController.navigate("stats_screen/$respostesCorrectes/$incorrectes/$tempsPassat")
                }
            }) {
                Text(text = resposta.resposta)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Pregunta ${currentQuestionIndex + 1} de ${preguntas.size}")
        Text(text = "Tiempo transcurrido: $tempsPassat segundos")
    }
}
