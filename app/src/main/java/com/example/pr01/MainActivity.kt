package com.example.pr01
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.gson.Gson

import java.io.InputStreamReader

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.pr01.ui.theme.PR01Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val quizData = loadQuizData()
        setContent {
            PR01Theme {
                QuizScreen(quizData)

                }
            }
        }

private fun loadQuizData():Quiz {
    val inputStream = resources.openRawResource(R.raw.quiz_data)
    val reader = InputStreamReader(inputStream)
    return Gson().fromJson(reader, Quiz::class.java)

}

@Composable
fun QuizScreen(quiz: Quiz) {
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    val currentQuestion = quiz.preguntes[currentQuestionIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicText(text = currentQuestion.pregunta)

        Spacer(modifier = Modifier.height(16.dp))


        Image(
            painter = rememberImagePainter(currentQuestion.imatge),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth().height(200.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar las respuestas
        currentQuestion.respostes.forEach { resposta ->
            Button(onClick = { /*
            crear el metodo que el usuario quiera*/
            }
            ) {
                Text(text = resposta.resposta
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            if (currentQuestionIndex < quiz.preguntes.size - 1) {
                currentQuestionIndex++
            }
        }) {
            Text("Següent")
        }

        val cambioColor : Button = findViewById(R.id.buttonDePrueba)
        //Cambia el color del texto luego de hacer click
        cambioColor.setOnClickListener { cambioColor.setTextColor(Color.parseColor("#9E9E9E")) }

    }
}

}
