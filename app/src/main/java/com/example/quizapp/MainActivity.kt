package com.example.quizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import android.media.MediaPlayer
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext


//class for quiz questions
data class Question(val text: String, val answers: List<String>, val correctIndex: Int)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {                //sets up Jetpack Compose UI
            QuizAppTheme {
                QuizApp()
            }
        }
    }
}

//wraps content in styling
@Composable
fun QuizAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            background = Color.White,
            onBackground = Color.Black,
            primary = Color(0xFF6200EE),
            onPrimary = Color.White
        ),
        typography = Typography(),
        content = content
    )
}

//shows startscreen or quizscreen depending on the state
@Composable
fun QuizApp() {
    var quizStarted by remember { mutableStateOf(false) }

    if (!quizStarted) {
        StartScreen(onStartClick = { quizStarted = true })
    } else {
        QuizScreen(onRestart = { quizStarted = false })
    }
}

//Startscreen with Hogwartswappen und startbutton
@Composable
fun StartScreen(onStartClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.fogbg),
            contentDescription = "Nebel Hintergrund",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.hogwartswappen),
                contentDescription = "Hogwarts Wappen",
                modifier = Modifier
                    .size(180.dp)
                    .padding(bottom = 32.dp)
            )

            Text(
                "Harry Potter Quiz",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Button(
                onClick = onStartClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    "Quiz starten",
                    color = Color(0xFF1A237E),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun QuizScreen(onRestart: () -> Unit) {         //läuft nur einmal wenn das composable Quizscreen aufgerufen wird
    val context = LocalContext.current

    //MediaPlayer einmal erstellt dann geloopt; funtioniert noch nicht so gut (Musik stoppt manchmal einfach)
    val mediaPlayer = remember {
        MediaPlayer.create(context, R.raw.bgmusic).apply {
            isLooping = true
            start()
        }
    }

    //Bei schließen der App oder ändern des Fensters wird die Musik gestoppt (Unit = nicht abhängig)
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }

    //list of questions and answers
    val questions = remember {
        listOf(
            Question("Wie heißt Harry Potters Eule?", listOf("Hedwig", "Crookshanks", "Scabbers"), 0),
            Question("Welcher Zauberspruch öffnet Türen?", listOf("Alohomora", "Expelliarmus", "Lumos"), 0),
            Question("Wie heißt der/die Schulleiterin von Hogwarts zu Beginn?", listOf("Severus Snape", "Albus Dumbledore", "Minerva McGonagall"), 1),
            Question("Was ist Voldemorts richtiger Name?", listOf("Tom Riddle", "Gellert Grindelwald", "Lucius Malfoy"), 0),
            Question("Welches Haus gehört Harry Potter an?", listOf("Slytherin", "Ravenclaw", "Gryffindor"), 2),
            Question("Wie heißt Hagrid mit Vornamen?", listOf("Rubeus", "Horace", "Argus"), 0),
            Question("Welches Tier ist das Wappentier von Hufflepuff?", listOf("Adler", "Dachs", "Löwe"), 1),
            Question("Was ist der Name von Harrys bestem Freund mit rotem Haar?", listOf("Ron Weasley", "Neville Longbottom", "Draco Malfoy"), 0),
            Question("Welcher Zauberer ist bekannt als 'Der Dunkle Lord'?", listOf("Voldemort", "Albus Dumbledore", "Sirius Black"), 0),
            Question("Was ist das magische Sportspiel, das Harry spielt?", listOf("Quidditch", "Wizard Chess", "Dueling"), 0)
        )
    }

    //mutableStateOf to decide when to show next question, selected answer, result, score and when to proceed to the next question
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedAnswerIndex by remember { mutableStateOf(-1) }
    var score by remember { mutableStateOf(0) }
    var showResult by remember { mutableStateOf(false) }
    var proceedToNext by remember { mutableStateOf(false) }

    //when quiz is over -> result screen
    if (showResult) {
        ResultScreen(score, questions.size, onRestart)
        return
    }

    val question = questions[currentQuestionIndex]

    //design layout von quizscreen
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.fogbg),
            contentDescription = "Nebel Hintergrund",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = question.text,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            )

            //farbe der antworten und ein click pro frage
            question.answers.forEachIndexed { index, answer ->
                val backgroundColor = when {
                    selectedAnswerIndex == -1 -> Color(0xFFFFD700)
                    index == question.correctIndex -> Color(0xFF4CAF50)
                    index == selectedAnswerIndex -> Color(0xFFB71C1C)
                    else -> Color(0xFFFFD700)
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .padding(vertical = 8.dp)
                        .background(backgroundColor, shape = RoundedCornerShape(16.dp))
                        .clickable(enabled = selectedAnswerIndex == -1) {
                            selectedAnswerIndex = index
                            if (index == question.correctIndex) score++
                            proceedToNext = true
                        }
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = answer,
                        color = Color(0xFF1A237E),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                "Punkte: $score",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }

    //delay zwischen fragen und bevor antwortanzeige kommt (1.5 sec)
    LaunchedEffect(proceedToNext) {
        if (proceedToNext) {
            delay(1500)
            selectedAnswerIndex = -1
            if (currentQuestionIndex + 1 < questions.size) {
                currentQuestionIndex++
            } else {
                showResult = true
            }
            proceedToNext = false
        }
    }
}

//resultscreen mit score von total und bild mit charakter abhängig vom score
@Composable
fun ResultScreen(score: Int, total: Int, onRestart: () -> Unit) {
    val resultImage = when {
        score == 0 -> R.drawable.filch
        score < 5 -> R.drawable.wurmschwanz
        score < 10 -> R.drawable.harry
        else -> R.drawable.dumbeldore

    }

    //design layout von result screen
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.fogbg),
            contentDescription = "Hintergrund",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = resultImage),
                contentDescription = "Ergebnisbild",
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 24.dp)
                    .border(4.dp, Color(0xFFFFD700), CircleShape)
                    .clip(CircleShape)

            )


            Text(
                text = "Quiz beendet!",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Du hast $score von $total richtig!",
                textAlign = TextAlign.Center,
                color = Color.White,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onRestart,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    "Zurück zum Start",
                    color = Color(0xFF1A237E),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
