package com.diligentia.matzoo

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.diligentia.matzoo.ui.theme.MatZooTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MatZooTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var question by remember { mutableStateOf(generateQuestion()) }
    var answer by remember { mutableStateOf("") }
    var feedback by remember { mutableStateOf("") }
    var correctCount by remember { mutableStateOf(0) }
    var incorrectCount by remember { mutableStateOf(0) }
    var showDialog by remember { mutableStateOf(false) }
    var isAnswerCorrect by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Dodawanie")
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Rozwiąż następujące zadanie:",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = question.first,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = answer,
                    onValueChange = { answer = it },
                    label = { Text("Twoja odpowiedź") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    val correctAnswer = question.second
                    isAnswerCorrect = answer.toIntOrNull() == correctAnswer
                    feedback = if (isAnswerCorrect) {
                        correctCount++
                        MediaPlayer.create(context, R.raw.correct).start()
                        "Poprawnie!"
                    } else {
                        incorrectCount++
                        MediaPlayer.create(context, R.raw.incorrect).start()
                        "Niepoprawnie. Spróbuj ponownie!"
                    }
                    showDialog = true
                }) {
                    Text("Sprawdź")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Poprawne odpowiedzi: $correctCount")
                Text("Niepoprawne odpowiedzi: $incorrectCount")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    question = generateQuestion()
                    answer = ""
                    feedback = ""
                }) {
                    Text("Nowe zadanie")
                }

                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        confirmButton = {
                            TextButton(onClick = { showDialog = false }) {
                                Text("OK")
                            }
                        },
                        text = {
                            Text(feedback)
                        }
                    )
                }
            }
        }
    )
}

fun generateQuestion(): Pair<String, Int> {
    val num1 = Random.nextInt(1, 100)
    val num2 = Random.nextInt(1, 100)
    val questionText = "$num1 + $num2 = ?"
    val correctAnswer = num1 + num2
    return Pair(questionText, correctAnswer)
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MatZooTheme {
        MainScreen()
    }
}
