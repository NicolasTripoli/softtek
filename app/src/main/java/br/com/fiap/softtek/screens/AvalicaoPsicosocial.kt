package br.com.fiap.softtek.screens

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.softtek.R
import br.com.fiap.softtek.components.MenuHeader
import br.com.fiap.softtek.ui.theme.DarkBlue
import br.com.fiap.softtek.ui.theme.LightGrey

@Composable
fun AvaliacaoPsicosocial(navController: NavController) {
    val perguntas = listOf(
        Pergunta(
            texto = "Antes de iniciar um novo dia de trabalho, como você se sente?",
            opcoes = listOf(
                "Me sinto bem, pronto para contribuir",
                "Me sinto normal, mais um dia",
                "Me sinto ansioso e preocupado"
            )
        ),
        Pergunta(
            texto = "Com que frequência você faz hora extras:",
            opcoes = listOf("Nunca", "Raramente", "Às vezes", "Frequentemente", "Sempre")
        ),
        Pergunta(
            texto = "Com que frequência você sente que seu trabalho atrapalha seu humor negativamente:",
            opcoes = listOf("Nunca", "Raramente", "Às vezes", "Frequentemente", "Sempre")
        ),
        Pergunta(
            texto = "De 1 a 5 como você avalia o clima da empresa, e da sua equipe.",
            opcoes = listOf("1", "2", "3", "4", "5")
        ),
        Pergunta(
            texto = "De 1 a 5 como você avalia a comunicação com seus pares no dia a dia.",
            opcoes = listOf("1", "2", "3", "4", "5")
        ),
        Pergunta(
            texto = "De 1 a 5 como você avalia sua relação com seu gestor.",
            opcoes = listOf("1", "2", "3", "4", "5")
        )
    )

    var perguntaIndex by remember { mutableStateOf(0) }
    var selectedOption by remember { mutableStateOf<String?>(null) }

    val respostas = remember { mutableStateMapOf<Int, String>() }

    val perguntaAtual = perguntas[perguntaIndex]
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
            navController = navController
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = perguntaAtual.texto,
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
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedOption == opcao,
                            onClick = { selectedOption = opcao }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = opcao)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        selectedOption?.let {
                            respostas[perguntaIndex] = it
                        }

                        if (perguntaIndex < perguntas.size - 1) {
                            perguntaIndex++
                            selectedOption = respostas[perguntaIndex]
                        } else {
                            println("Respostas: $respostas")
                            navController.navigate("home")
                        }
                    },
                    enabled = selectedOption != null
                ) {
                    Text(
                        if (perguntaIndex < perguntas.size - 1)
                            "Próximo"
                        else
                            "Finalizar"
                    )
                }
            }
        }
    }
}

data class Pergunta(
    val texto: String,
    val opcoes: List<String>
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewAvaliacaoPsicosocial() {
    val navController = rememberNavController()
    AvaliacaoPsicosocial(navController)
}