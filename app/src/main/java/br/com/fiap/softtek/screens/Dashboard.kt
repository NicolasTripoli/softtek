package br.com.fiap.softtek.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.softtek.components.MenuHeader
import br.com.fiap.softtek.ui.theme.DarkBlue
import br.com.fiap.softtek.ui.theme.LightGrey
import kotlin.random.Random

@Composable
fun Dashboard(navController: NavController) {
    val scrollState = rememberScrollState()

    MenuHeader(
        pageTitle = "Dashboard",
        currentRoute = "dashboard",
        navController = navController,
        showBackButton = true
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

@Composable
fun MoodCard(emoji: String, label: String, borderColor: Color, textColor: Color) {
    Card(
        modifier = Modifier
            .width(90.dp)
            .height(90.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, borderColor),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = emoji,
                fontSize = 32.sp
            )
            Text(
                text = label,
                color = textColor,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
        }
    }
}