package br.com.fiap.softtek.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.softtek.factory.RetrofitFactory
import br.com.fiap.softtek.model.OuvidoriaIdentificadaRequest
import br.com.fiap.softtek.model.OuvidoriaIdentificadaResponse
import br.com.fiap.softtek.ui.theme.DarkBlue
import br.com.fiap.softtek.ui.theme.LightGrey
import br.com.fiap.softtek.utils.UserDataManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RelatoEmail(navController: NavController) {
    val context = LocalContext.current.applicationContext

    var textoRelato by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun enviarOuvidoriaIdentificada(
        context: Context,
        mensagem: String,
        onResult: (OuvidoriaIdentificadaResponse?) -> Unit
    ) {
        val service = RetrofitFactory().getSofttekMapService(context)
        val cpfUsuario = UserDataManager.getCpf(context)

        val request = OuvidoriaIdentificadaRequest(cpfUsuario.toString(), mensagem)

        service.enviarOuvidoriaIdentificada(request)
            .enqueue(object : Callback<OuvidoriaIdentificadaResponse> {
                override fun onResponse(
                    call: Call<OuvidoriaIdentificadaResponse>,
                    response: Response<OuvidoriaIdentificadaResponse>
                ) {
                    if (response.isSuccessful) {
                        onResult(response.body())
                    } else {
                        Log.e("Ouvidoria", "Erro: ${response.code()} - ${response.message()}")
                        onResult(null)
                    }
                }

                override fun onFailure(call: Call<OuvidoriaIdentificadaResponse>, t: Throwable) {
                    Log.e("Ouvidoria", "Falha: ${t.message}")
                    onResult(null)
                }
            })
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Espaço Seguro",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(DarkBlue, LightGrey, DarkBlue),
                    )
                )
                .padding(24.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.92f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Escreva seu relato para a ouvidoria:",
                            fontSize = 18.sp,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                        OutlinedTextField(
                            value = "ouvidoria@empresa.com.br",
                            onValueChange = {},
                            label = { Text("E-mail da Ouvidoria") },
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = textoRelato,
                            onValueChange = { textoRelato = it },
                            label = { Text("Relato") },
                            placeholder = { Text("Digite aqui...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                        )
                        Button(
                            onClick = {
                                if (textoRelato.isNotBlank()) {
                                    scope.launch {
                                        isLoading = true
                                        enviarOuvidoriaIdentificada(
                                            context, textoRelato,
                                            onResult = { result ->
                                                isLoading = false
                                                textoRelato = ""

                                                if (result != null) {
                                                    scope.launch {
                                                        snackbarHostState.showSnackbar("Relato enviado com sucesso!")
                                                    }
                                                } else {
                                                    scope.launch {
                                                        snackbarHostState.showSnackbar("Erro ao enviar relato")
                                                    }
                                                }
                                            }
                                        )
                                    }
                                } else {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Por favor, digite algo antes de enviar")
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DarkBlue,
                                contentColor = Color.White
                            ),
                            enabled = !isLoading
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.height(24.dp)
                                )
                            } else {
                                Text("Enviar", fontWeight = FontWeight.Bold)
                            }
                        }
                        Text(
                            text = "Este relato é identificáveis.",
                            fontSize = 12.sp,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

