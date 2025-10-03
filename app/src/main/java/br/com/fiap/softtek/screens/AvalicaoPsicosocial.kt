package br.com.fiap.softtek.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.softtek.R
import br.com.fiap.softtek.components.MenuHeader
import br.com.fiap.softtek.factory.RetrofitFactory
import br.com.fiap.softtek.model.QuestionarioPsicologicoResponse
import br.com.fiap.softtek.model.QuestionarioRespostaRequest
import br.com.fiap.softtek.model.QuestionarioRespostaResponse
import br.com.fiap.softtek.ui.theme.DarkBlue
import br.com.fiap.softtek.ui.theme.LightGrey
import br.com.fiap.softtek.utils.UserDataManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun AvaliacaoPsicosocial(navController: NavController) {
    val context = LocalContext.current.applicationContext

    var perguntas by remember { mutableStateOf<List<QuestionarioPsicologicoResponse>>(emptyList()) }
    var perguntaIndex by remember { mutableStateOf(0) }
    var selectedOption by remember { mutableStateOf<String?>(null) }
    val respostas = remember { mutableStateMapOf<String, String>() } // chave = id da pergunta

    val perguntaAtual = perguntas.getOrNull(perguntaIndex)

    fun carregarQuestionario(
        context: Context, onResult: (List<QuestionarioPsicologicoResponse>?) -> Unit
    ) {
        val service = RetrofitFactory().getSofttekMapService(context)

        service.getQuestionarioPsicologico()
            .enqueue(object : Callback<List<QuestionarioPsicologicoResponse>> {
                override fun onResponse(
                    call: Call<List<QuestionarioPsicologicoResponse>>,
                    response: Response<List<QuestionarioPsicologicoResponse>>
                ) {
                    if (response.isSuccessful) {
                        onResult(response.body())
                    } else {
                        Log.e("Questionario", "Erro: ${response.code()} - ${response.message()}")
                        onResult(null)
                    }
                }

                override fun onFailure(
                    call: Call<List<QuestionarioPsicologicoResponse>>, t: Throwable
                ) {
                    Log.e("Questionario", "Falha: ${t.message}")
                    onResult(null)
                }
            })
    }

    fun enviarResposta(
        context: Context,
        resposta: String,
        questionarioID: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        val cpfUsuario = UserDataManager.getCpf(context) ?: ""
        val service = RetrofitFactory().getSofttekMapService(context)
        val request = QuestionarioRespostaRequest(
            cpf = cpfUsuario,
            resposta = resposta,
            questionarioID = questionarioID
        )

        service.enviarRespostaQuestionario(request)
            .enqueue(object : Callback<QuestionarioRespostaResponse> { // Response model novo
                override fun onResponse(
                    call: Call<QuestionarioRespostaResponse>,
                    response: Response<QuestionarioRespostaResponse>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        onResult(true, data?.resposta)
                    } else {
                        Log.e(
                            "RespostaQuestionario",
                            "Erro: ${response.code()} - ${response.message()}"
                        )
                        onResult(false, null)
                    }
                }

                override fun onFailure(call: Call<QuestionarioRespostaResponse>, t: Throwable) {
                    Log.e("RespostaQuestionario", "Falha: ${t.message}")
                    onResult(false, null)
                }
            })
    }


    LaunchedEffect(Unit) {
        carregarQuestionario(
            context, onResult = { result ->
                result?.let { perguntas = it }
            })
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
            pageTitle = "Avaliação Psicosocial",
            currentRoute = "avaliacaoPsicosocial",
            navController = navController,
            showBackButton = true
        ) {
            if (perguntaAtual == null) {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Carregando questionário...")
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = perguntaAtual.pergunta,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 24.dp),
                        textAlign = TextAlign.Center
                    )

                    perguntaAtual.opcoes.forEach { opcao ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                                .clickable { selectedOption = opcao }
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White)
                                .border(
                                    width = if (selectedOption == opcao) 2.dp else 1.dp,
                                    color = if (selectedOption == opcao) MaterialTheme.colorScheme.primary else Color.Gray,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = selectedOption == opcao,
                                onClick = { selectedOption = opcao })
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = opcao)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            selectedOption?.let { opcaoEscolhida ->
                                val pergunta = perguntas[perguntaIndex]
                                enviarResposta(
                                    context,
                                    resposta = opcaoEscolhida,
                                    questionarioID = pergunta.id
                                ) { sucesso, respostaRetornada ->
                                    if (sucesso) {
                                        respostas[pergunta.id] = respostaRetornada ?: opcaoEscolhida

                                        if (perguntaIndex < perguntas.size - 1) {
                                            perguntaIndex++
                                            selectedOption = respostas[perguntas[perguntaIndex].id]
                                        } else {
                                            println("Todas respostas enviadas: $respostas")
                                            navController.navigate("home")
                                        }
                                    } else {
                                        Log.e("RespostaQuestionario", "Falha ao enviar resposta")
                                    }
                                }
                            }
                        },
                        enabled = selectedOption != null
                    ) {
                        Text(
                            if (perguntaIndex < perguntas.size - 1) "Próximo" else "Finalizar"
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewAvaliacaoPsicosocial() {
    val navController = rememberNavController()
    AvaliacaoPsicosocial(navController)
}