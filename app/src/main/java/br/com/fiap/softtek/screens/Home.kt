package br.com.fiap.softtek.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
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
import br.com.fiap.softtek.components.MenuHeader
import br.com.fiap.softtek.factory.RetrofitFactory
import br.com.fiap.softtek.model.DesafioResponse
import br.com.fiap.softtek.model.FraseMotivacionalResponse
import br.com.fiap.softtek.model.SentimentalResponse
import br.com.fiap.softtek.model.SentimentoRequest
import br.com.fiap.softtek.model.UsuarioDesafioRequest
import br.com.fiap.softtek.model.UsuarioDesafioResponse
import br.com.fiap.softtek.ui.theme.DarkBlue
import br.com.fiap.softtek.ui.theme.LightGrey
import br.com.fiap.softtek.utils.UserDataManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun Home(navController: NavController) {
    val context = LocalContext.current.applicationContext

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

    val fraseAleatoria = remember { mutableStateOf("") }
    var desafioDoDia by remember { mutableStateOf<DesafioResponse?>(null) }
    var humorSelecionado by remember { mutableStateOf("") }
    var desafioConcluido by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    var humoresUltimaSemana by remember { mutableStateOf<List<Int>>(emptyList()) }

    fun buscarSentimentos(cpf: String, context: Context) {
        val service = RetrofitFactory().getSofttekMapService(context)
        service.getSentimentosCheck(cpf).enqueue(object : Callback<List<SentimentalResponse>> {
            override fun onResponse(
                call: Call<List<SentimentalResponse>>,
                response: Response<List<SentimentalResponse>>
            ) {
                if (response.isSuccessful) {
                    val listaSentimentos = response.body() ?: emptyList()
                    humoresUltimaSemana = listaSentimentos.map {
                        when (it.sentimento.lowercase()) {
                            "triste" -> 1
                            "neutro" -> 2
                            "feliz" -> 3
                            else -> 2
                        }
                    }
                }
            }

            override fun onFailure(
                call: Call<List<SentimentalResponse>?>,
                t: Throwable
            ) {
                Log.e("LoginError", "Falha na rede: ${t.message}")
            }
        })
    }

    fun buscarDesafioDiario(context: Context, onResult: (DesafioResponse?) -> Unit) {
        val service = RetrofitFactory().getSofttekMapService(context)
        service.getDesafioDiario().enqueue(object : Callback<DesafioResponse> {
            override fun onResponse(
                call: Call<DesafioResponse>,
                response: Response<DesafioResponse>
            ) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    Log.e("DesafioDiario", "Erro ao buscar: ${response.code()}")
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<DesafioResponse>, t: Throwable) {
                Log.e("DesafioDiario", "Falha na rede: ${t.message}")
                onResult(null)
            }
        })
    }

    fun carregarFraseMotivacional(
        context: Context,
        onResult: (FraseMotivacionalResponse?) -> Unit
    ) {
        val service = RetrofitFactory().getSofttekMapService(context)

        service.getFraseMotivacionalAleatoria()
            .enqueue(object : Callback<FraseMotivacionalResponse> {
                override fun onResponse(
                    call: Call<FraseMotivacionalResponse>,
                    response: Response<FraseMotivacionalResponse>
                ) {
                    if (response.isSuccessful) {
                        onResult(response.body())
                        Log.d("FraseMotivacional", "Frase carregada com sucesso!")
                    } else {
                        Log.e("FraseMotivacional", "Erro ao carregar: ${response.code()}")
                        onResult(null)
                    }
                }

                override fun onFailure(call: Call<FraseMotivacionalResponse>, t: Throwable) {
                    Log.e("FraseMotivacional", "Falha na rede: ${t.message}")
                    onResult(null)
                }
            })
    }

    fun enviarSentimento(sentimento: String, context: Context) {
        val service = RetrofitFactory().getSofttekMapService(context)
        val cpfUsuario = UserDataManager.getCpf(context)
        val request = SentimentoRequest(cpfUsuario.toString(), sentimento)

        service.postSentimento(request).enqueue(object : Callback<SentimentalResponse> {
            override fun onResponse(
                call: Call<SentimentalResponse>,
                response: Response<SentimentalResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("Sentimento", "Sentimento enviado com sucesso!")
                } else {
                    Log.e("Sentimento", "Erro ao enviar: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<SentimentalResponse>, t: Throwable) {
                Log.e("Sentimento", "Falha na rede: ${t.message}")
            }
        })
    }

    fun enviarUsuarioDesafio(context: Context) {
        if (desafioConcluido) return
        val service = RetrofitFactory().getSofttekMapService(context)
        val cpfUsuario = UserDataManager.getCpf(context)

        val request = UsuarioDesafioRequest(cpfUsuario.toString(), desafioDoDia?.id ?: "")

        service.postUsuarioDesafio(request).enqueue(object : Callback<UsuarioDesafioResponse> {
            override fun onResponse(
                call: Call<UsuarioDesafioResponse>,
                response: Response<UsuarioDesafioResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("UsuarioDesafio", "Desafio enviado com sucesso!")
                    desafioConcluido = true
                } else {
                    Log.e("UsuarioDesafio", "Erro ao enviar: ${response.code()}")
                    desafioConcluido = false
                }
            }

            override fun onFailure(call: Call<UsuarioDesafioResponse>, t: Throwable) {
                Log.e("UsuarioDesafio", "Falha na rede: ${t.message}")
                desafioConcluido = false
            }
        })
    }

    LaunchedEffect(Unit) {
        val cpfUsuario = UserDataManager.getCpf(context)

        carregarFraseMotivacional(context) { resultado ->
            resultado?.let {
                fraseAleatoria.value = it.frase
            }
        }
        buscarSentimentos(cpfUsuario.toString(), context)
        buscarDesafioDiario(context) { resultado ->
            resultado?.let {
                desafioDoDia = it
            }
        }
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
                            text = fraseAleatoria.value,
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
                                HumorButton("😄", "Feliz", humorSelecionado == "Feliz") { selected ->
                                    humorSelecionado = selected
                                    if (humorSelecionado.isEmpty()) {
                                        enviarSentimento(selected, context)
                                    }
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                HumorButton(
                                    "😐",
                                    "Neutro",
                                    humorSelecionado == "Neutro"
                                ) { selected ->
                                    humorSelecionado = selected
                                    if (humorSelecionado.isEmpty()) {
                                        enviarSentimento(selected, context)
                                    }
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                HumorButton(
                                    "😞",
                                    "Triste",
                                    humorSelecionado == "Triste"
                                ) { selected ->
                                    humorSelecionado = selected
                                    if (humorSelecionado.isEmpty()) {
                                        enviarSentimento(selected, context)
                                    }
                                }
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
                            val humorMaisFrequente = when (humoresUltimaSemana.groupBy { it }
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
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = desafioDoDia?.descricao ?: "Carregando...",
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
                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { enviarUsuarioDesafio(context) },
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

            val pointColor = when (humor) {
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
