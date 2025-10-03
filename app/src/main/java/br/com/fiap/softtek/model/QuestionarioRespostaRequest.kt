package br.com.fiap.softtek.model

data class QuestionarioRespostaRequest(
    val cpf: String,
    val resposta: String,
    val questionarioID: String
)