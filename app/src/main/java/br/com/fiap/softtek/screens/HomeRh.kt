package br.com.fiap.softtek.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.softtek.R
import br.com.fiap.softtek.components.MenuHeaderRh
import br.com.fiap.softtek.ui.theme.DarkBlue
import br.com.fiap.softtek.ui.theme.LightGrey
import kotlin.random.Random

@Composable
fun HomeRh(navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    val cardSpacing = when {
        screenHeight < 600.dp -> 8.dp
        screenHeight < 700.dp -> 12.dp
        else -> 16.dp
    }

    val cardPadding = when {
        screenWidth < 360.dp -> 12.dp
        else -> 16.dp
    }

    val frasesMotivacionais = listOf(
        "Acredite em você mesmo!",
        "Você é mais forte do que imagina.",
        "Cada dia é uma nova chance.",
        "Nunca é tarde para recomeçar."
    )

    val desafiosDia = listOf(
        "Hoje tente ficar 5 minutos offline",
        "Mande uma mensagem carinhosa para alguém",
        "Respire fundo por 1 minuto",
        "Faça um elogio sincero a alguém hoje",
        "Tome um copo de água e aprecie o momento",
        "Sorria para 3 pessoas diferentes hoje",
        "Faça uma pequena pausa e observe a natureza",
        "Agradeça por algo bom que aconteceu hoje",
        "Organize sua mesa de trabalho por 5 minutos",
        "Pratique 2 minutos de alongamento"
    )

    val fraseAleatoria = remember { frasesMotivacionais.random() }
    val desafioDoDia = remember { desafiosDia.random() }
    var humorSelecionado by remember { mutableStateOf("") }
    var desafioConcluido by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()


    val humoresUltimaSemana = remember {
        List(7) { Random.nextInt(1, 4) } // 1 = Triste, 2 = Neutro, 3 = Feliz
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(DarkBlue, LightGrey, DarkBlue),
                )
            )
    ) {
        MenuHeaderRh(
            pageTitle = "Home",
            currentRoute = "home",
            navController = navController
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Quick Stats Section
                QuickStatsSection()

                // Mood Distribution Section
                MoodDistributionSection()

                // Achievements Section
                AchievementsSection()
            }
        }
    }
}

@Composable
private fun QuickStatsSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Resumo de Atividades",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DarkBlue
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem("Total de\nCheck-ins", "15")
                StatItem("Sequência\nAtual", "7")
                StatItem("Objetivos\nAlcançados", "3")
            }
        }
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBlue
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun MoodDistributionSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Análise de Estado Emocional",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DarkBlue
            )

            MoodProgressBar("Estado Positivo", 0.7f, Color(0xFF2E7D32))
            MoodProgressBar("Estado Neutro", 0.2f, Color(0xFF616161))
            MoodProgressBar("Estado Negativo", 0.1f, Color(0xFF37474F))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.weight(1f))
                MoodCard("😄", "Feliz", Color(0xFFB2DFDB), Color(0xFF388E3C))
                Spacer(modifier = Modifier.width(16.dp))
                MoodCard("😐", "Neutro", Color(0xFFFFE0B2), Color(0xFFE65100))
                Spacer(modifier = Modifier.width(16.dp))
                MoodCard("😞", "Triste", Color(0xFFB3E5FC), Color(0xFF1565C0))
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun MoodProgressBar(label: String, progress: Float, color: Color) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                color = DarkBlue,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
            Text(
                text = "${(progress * 100).toInt()}%",
                color = color,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = color,
            trackColor = Color.LightGray.copy(alpha = 0.3f)
        )
    }
}

@Composable
private fun AchievementsSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Objetivos e Conquistas",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DarkBlue
            )

            AchievementItem(
                "Comprometimento Inicial",
                "Completou 7 dias consecutivos de monitoramento",
                "Em Andamento"
            )
            AchievementItem(
                "Desenvolvimento Pessoal",
                "Completou 5 desafios de autodesenvolvimento",
                "Concluído"
            )
            AchievementItem(
                "Autoconsciência",
                "Registrou e analisou padrões emocionais por 30 dias",
                "Pendente"
            )
        }
    }
}

@Composable
private fun AchievementItem(title: String, description: String, status: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = DarkBlue
            )
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Card(
            colors = CardDefaults.cardColors(
                containerColor = when(status) {
                    "Concluído" -> Color(0xFFE8F5E9)
                    "Em Andamento" -> Color(0xFFFFF3E0)
                    else -> Color(0xFFF5F5F5)
                }
            ),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                text = status,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = when(status) {
                    "Concluído" -> Color(0xFF2E7D32)
                    "Em Andamento" -> Color(0xFFE65100)
                    else -> Color(0xFF616161)
                }
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewHomeRh() {
    val navController = rememberNavController()
    HomeRh(navController)
}
