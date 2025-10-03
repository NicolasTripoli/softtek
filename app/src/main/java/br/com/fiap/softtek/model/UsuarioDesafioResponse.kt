package br.com.fiap.softtek.model

data class UsuarioDesafioResponse(
    val id: String,
    val cpfUsuario: String,
    val desafioDiario: DesafioResponse,
    val dataFinalizado: String?
)