package br.com.fiap.softtek.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.softtek.R
import br.com.fiap.softtek.components.MenuHeader
import androidx.compose.ui.tooling.preview.Preview
import br.com.fiap.softtek.ui.theme.DarkBlue
import br.com.fiap.softtek.ui.theme.LightGrey
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.random.Random

@Composable
fun Home(navController: NavController) {
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
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.1f)
                .blur(radius = 15.dp),
            contentScale = ContentScale.Fit
        )

        MenuHeader(
            pageTitle = "Home",
            currentRoute = "home",
            navController = navController
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = cardPadding, vertical = cardPadding / 2),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(cardSpacing)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.92f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(cardPadding),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "✨ Frase do Dia ✨",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.SemiBold,
                                color = DarkBlue,
                                letterSpacing = 0.5.sp
                            )
                        )

                        Text(
                            text = fraseAleatoria,
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Medium,
                                color = DarkBlue,
                                letterSpacing = 0.3.sp,
                                lineHeight = 28.sp
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                }

                // Card do Humor
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.92f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(cardPadding),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Como você está se sentindo hoje?",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.SemiBold,
                                color = DarkBlue,
                                letterSpacing = 0.3.sp
                            ),
                            textAlign = TextAlign.Center
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                HumorButton("😄", "Feliz", humorSelecionado == "Feliz") { humorSelecionado = it }
                                Spacer(modifier = Modifier.width(16.dp))
                                HumorButton("😐", "Neutro", humorSelecionado == "Neutro") { humorSelecionado = it }
                                Spacer(modifier = Modifier.width(16.dp))
                                HumorButton("😞", "Triste", humorSelecionado == "Triste") { humorSelecionado = it }
                            }
                        }

                        if (humorSelecionado.isNotEmpty()) {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = DarkBlue.copy(alpha = 0.1f)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Você está se sentindo: $humorSelecionado",
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily.SansSerif,
                                        fontWeight = FontWeight.Medium,
                                        color = DarkBlue,
                                        letterSpacing = 0.2.sp
                                    ),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.92f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(cardPadding),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "📊 Análise de Humor",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.SemiBold,
                                color = DarkBlue,
                                letterSpacing = 0.5.sp
                            )
                        )

                        HumorGraph(
                            humores = humoresUltimaSemana,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .padding(8.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            LegendaItem("😄 Feliz", Color(0xFF4CAF50))
                            LegendaItem("😐 Neutro", Color(0xFFFFA726))
                            LegendaItem("😞 Triste", Color(0xFF90A4AE))
                        }

                        // Estatísticas rápidas
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            val humorMaisFrequente = when(humoresUltimaSemana.groupBy { it }
                                .maxByOrNull { it.value.size }?.key) {
                                3 -> "Feliz 😄"
                                2 -> "Neutro 😐"
                                else -> "Triste 😞"
                            }

                            EstatisticaItem(
                                titulo = "Humor mais frequente",
                                valor = humorMaisFrequente,
                                color = DarkBlue
                            )
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.92f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(cardPadding),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "🎯 Desafio do Dia",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.SemiBold,
                                color = DarkBlue,
                                letterSpacing = 0.5.sp
                            )
                        )

                        Text(
                            text = desafioDoDia,
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Medium,
                                color = DarkBlue,
                                letterSpacing = 0.3.sp,
                                lineHeight = 24.sp
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )

                        Button(
                            onClick = { desafioConcluido = !desafioConcluido },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (desafioConcluido)
                                    Color(0xFF4CAF50)
                                else
                                    DarkBlue
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth(0.8f)
                        ) {
                            Text(
                                text = if (desafioConcluido) "Concluído ✅" else "Marcar como concluído",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily.SansSerif,
                                    fontWeight = FontWeight.Medium,
                                    letterSpacing = 0.3.sp
                                ),
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HumorButton(emoji: String, label: String, isSelected: Boolean, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .width(90.dp)
            .clickable { onClick(label) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 6.dp else 2.dp,
            pressedElevation = 1.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isSelected -> when (label) {
                    "Feliz" -> Color(0xFF4CAF50).copy(alpha = 0.2f)
                    "Neutro" -> Color(0xFFFFA726).copy(alpha = 0.2f)
                    "Triste" -> Color(0xFF90A4AE).copy(alpha = 0.2f)
                    else -> MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                }
                else -> MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
            }
        ),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = when (label) {
                "Feliz" -> Color(0xFF4CAF50)
                "Neutro" -> Color(0xFFFFA726)
                "Triste" -> Color(0xFF90A4AE)
                else -> MaterialTheme.colorScheme.surface
            }.copy(alpha = if (isSelected) 1f else 0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = emoji,
                fontSize = 28.sp
            )
            Text(
                text = label,
                style = TextStyle(
                    fontSize = 13.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    letterSpacing = 0.2.sp,
                    color = when (label) {
                        "Feliz" -> Color(0xFF2E7D32)
                        "Neutro" -> Color(0xFFE65100)
                        "Triste" -> Color(0xFF455A64)
                        else -> DarkBlue
                    }
                )
            )
        }
    }
}

@Composable
fun LegendaItem(texto: String, cor: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Canvas(modifier = Modifier.size(12.dp)) {
            drawCircle(color = cor)
        }
        Text(
            text = texto,
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = FontFamily.SansSerif,
                color = DarkBlue.copy(alpha = 0.7f)
            )
        )
    }
}

@Composable
fun EstatisticaItem(titulo: String, valor: String, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = valor,
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.SemiBold,
                color = color
            )
        )
        Text(
            text = titulo,
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = FontFamily.SansSerif,
                color = color.copy(alpha = 0.7f)
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun HumorGraph(
    humores: List<Int>,
    modifier: Modifier = Modifier
) {
    val felizColor = Color(0xFF4CAF50)
    val neutroColor = Color(0xFFFFA726)
    val tristeColor = Color(0xFF90A4AE)

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val espacoEntrepontos = width / (humores.size - 1)
        val escalaY = height / 4

        val strokePathGrid = Path()
        for (i in 0..3) {
            val y = height - (i * escalaY)
            strokePathGrid.moveTo(0f, y)
            strokePathGrid.lineTo(width, y)
        }
        drawPath(
            strokePathGrid,
            color = Color.Gray.copy(alpha = 0.2f),
            style = Stroke(width = 1.dp.toPx())
        )

        val strokePath = Path()
        var firstPoint = true
        humores.forEachIndexed { index, humor ->
            val x = index * espacoEntrepontos
            val y = height - (humor * escalaY)
            if (firstPoint) {
                strokePath.moveTo(x, y)
                firstPoint = false
            } else {
                strokePath.lineTo(x, y)
            }

            val pointColor = when(humor) {
                3 -> felizColor
                2 -> neutroColor
                else -> tristeColor
            }
            drawCircle(
                color = pointColor,
                radius = 8.dp.toPx(),
                center = Offset(x, y)
            )
            drawCircle(
                color = Color.White,
                radius = 4.dp.toPx(),
                center = Offset(x, y)
            )
        }

        drawPath(
            strokePath,
            color = DarkBlue,
            style = Stroke(
                width = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewHome() {
    val navController = rememberNavController()
    Home(navController)
}
