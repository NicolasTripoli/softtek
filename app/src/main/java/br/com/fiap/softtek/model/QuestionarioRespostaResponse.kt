package br.com.fiap.softtek.model

data class QuestionarioRespostaResponse(
    val id: String,
    val cpfUsuario: String,
    val questionarioPsicologico: QuestionarioPsicologicoResponse,
    val resposta: String,
    val dataResposta: String
)