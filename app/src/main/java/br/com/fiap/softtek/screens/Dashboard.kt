package br.com.fiap.softtek.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.softtek.components.MenuHeader
import br.com.fiap.softtek.factory.RetrofitFactory
import br.com.fiap.softtek.model.SentimentalResponse
import br.com.fiap.softtek.model.UsuarioDesafioResponse
import br.com.fiap.softtek.ui.theme.DarkBlue
import br.com.fiap.softtek.utils.UserDataManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun Dashboard(navController: NavController) {
    val context = LocalContext.current.applicationContext

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
            MoodDistributionSection(context)

            // Achievements Section
            AchievementsSection(context)
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
private fun MoodDistributionSection(context: Context) {
    var sentimentos by remember { mutableStateOf<List<SentimentalResponse>>(emptyList()) }

    fun buscarSentimentos(cpf: String) {
        val service = RetrofitFactory().getSofttekMapService(context)
        service.getSentimentosCheck(cpf).enqueue(object : Callback<List<SentimentalResponse>> {
            override fun onResponse(
                call: Call<List<SentimentalResponse>>,
                response: Response<List<SentimentalResponse>>
            ) {
                if (response.isSuccessful) {
                    sentimentos = response.body() ?: emptyList()
                } else {
                    Log.e("MoodDistribution", "Erro ao buscar sentimentos: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<SentimentalResponse>>, t: Throwable) {
                Log.e("MoodDistribution", "Falha na rede: ${t.message}")
            }
        })
    }

    LaunchedEffect(Unit) {
        val cpfUsuario = UserDataManager.getCpf(context) ?: ""
        buscarSentimentos(cpfUsuario)
    }

    // Calculando proporções
    val total = sentimentos.size.takeIf { it > 0 } ?: 1
    val positivos = sentimentos.count { it.sentimento == "Feliz" } / total.toFloat()
    val neutros = sentimentos.count { it.sentimento == "Neutro" } / total.toFloat()
    val negativos = sentimentos.count { it.sentimento == "Triste" } / total.toFloat()

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

            MoodProgressBar("Estado Positivo", positivos, Color(0xFF2E7D32))
            MoodProgressBar("Estado Neutro", neutros, Color(0xFF616161))
            MoodProgressBar("Estado Negativo", negativos, Color(0xFF37474F))

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
private fun AchievementsSection(context: Context) {
    var desafios by remember { mutableStateOf<List<UsuarioDesafioResponse>>(emptyList()) }

    fun carregarDesafios(
        cpf: String,
        onResult: (List<UsuarioDesafioResponse>?) -> Unit
    ) {
        val service = RetrofitFactory().getSofttekMapService(context)
        service.getTodosDesafiosUsuario(cpf)
            .enqueue(object : Callback<List<UsuarioDesafioResponse>> {
                override fun onResponse(
                    call: Call<List<UsuarioDesafioResponse>>,
                    response: Response<List<UsuarioDesafioResponse>>
                ) {
                    if (response.isSuccessful) {
                        onResult(response.body())
                    } else {
                        Log.e("DesafiosUsuario", "Erro: ${response.code()} - ${response.message()}")
                        onResult(null)
                    }
                }

                override fun onFailure(call: Call<List<UsuarioDesafioResponse>>, t: Throwable) {
                    Log.e("DesafiosUsuario", "Falha: ${t.message}")
                    onResult(null)
                }
            })
    }

    LaunchedEffect(Unit) {
        val cpfUsuario = UserDataManager.getCpf(context) ?: ""
        carregarDesafios(cpfUsuario) { resultado ->
            resultado?.let { desafios = it }
        }
    }

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

            if (desafios.isEmpty()) {
                Text(
                    text = "Nenhum desafio registrado ainda.",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            } else {
                desafios.forEach { desafio ->
                    AchievementItem(
                        titulo = desafio.desafioDiario.titulo,
                        descricao = desafio.desafioDiario.descricao,
                        status = "Concluído em ${desafio.dataFinalizado}"
                    )
                }
            }
        }
    }
}

@Composable
private fun AchievementItem(titulo: String, descricao: String, status: String) {
    val statusColor = when {
        status.startsWith("Concluído") -> Color(0xFFE8F5E9)
        status.startsWith("Em Andamento") -> Color(0xFFFFF3E0)
        else -> Color(0xFFF5F5F5)
    }

    val textColor = when {
        status.startsWith("Concluído") -> Color(0xFF2E7D32)
        status.startsWith("Em Andamento") -> Color(0xFFE65100)
        else -> Color(0xFF616161)
    }

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
                text = titulo,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = DarkBlue
            )
            Text(
                text = descricao,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Card(
            colors = CardDefaults.cardColors(containerColor = statusColor),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                text = status,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = textColor
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