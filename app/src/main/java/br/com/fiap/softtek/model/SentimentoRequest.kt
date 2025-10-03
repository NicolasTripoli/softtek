package br.com.fiap.softtek.model

data class SentimentoRequest(
    val cpf: String,
    val sentimento: String
)