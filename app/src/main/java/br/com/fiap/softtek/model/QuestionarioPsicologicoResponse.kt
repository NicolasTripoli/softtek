package br.com.fiap.softtek.model

data class QuestionarioPsicologicoResponse(
    val id: String,
    val pergunta: String,
    val opcoes: List<String>
)