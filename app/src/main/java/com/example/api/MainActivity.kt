package com.example.api

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.api.ui.theme.ApiTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.HttpException
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ApiTheme {
                val navController = rememberNavController()

                val quizData = fetchQuizData()

                if (quizData != null) {

                    NavHost(navController, startDestination = "home_screen") {
                        composable("home_screen") {
                            HomeScreen(navController)
                        }
                        composable("game_screen") {
                            GameScreen(navController, quizData!!)
                        }

                        composable(
                            route = "stats_screen/{respostesCorrectes}/{incorrectes}/{tempsPassat}", // Asegúrate de que esté definida con 3 parámetros
                            arguments = listOf(
                                navArgument("respostesCorrectes") { type = NavType.IntType },
                                navArgument("incorrectes") { type = NavType.IntType },
                                navArgument("tempsPassat") { type = NavType.IntType }  // Añadir el argumento del tiempo
                            )
                        ) { backStackEntry ->
                            StatsScreen(navController, backStackEntry)
                        }
                    }

                }
            }
        }
    }
}
@Composable
fun fetchQuizData(): List<Pregunta>? {
    val quizData = produceState<List<Pregunta>?>(initialValue = null) {
        val preguntas = try {
            RetrofitInstance.api.getQuizData()
        } catch (e: IOException) {
            null // Error de red
        } catch (e: HttpException) {
            null
        }
        value = preguntas
    }
    return quizData.value
}

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://dam.inspedralbes.cat:23333/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: QuizApiService by lazy {
        retrofit.create(QuizApiService::class.java)
    }
}
fun enviarEstadisticasAlServidor(
    userId: String?,
    preguntas: List<PreguntaEstadistica>
) {
    val estadisticas = Estadisticas(
        userId = userId?: "",
        preguntas = preguntas
    )

    // Enviar estadisticas usando Retrofit (en una corrutina)
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.enviarEstadisticas(estadisticas)
            if (response.isSuccessful) {
                println("Estadísticas enviadas con éxito")
            } else {
                println("Error al enviar estadísticas: ${response.code()}")
            }
        } catch (e: Exception) {
            println("Error: ${e.localizedMessage}")
        }
    }
}

data class Estadisticas(
    val userId: String,
    val preguntas: List<PreguntaEstadistica>
)

data class PreguntaEstadistica(
    val questionId: String,
    val questionText: String,
    val correct: Boolean,
    val timeElapsed: Int
)
