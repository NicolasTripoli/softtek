package br.com.fiap.softtek.model

data class SentimentalResponse(
    val id: String,
    val cpfUsuario: String,
    val sentimento: String,
    val dataCheckIn: String,
)
