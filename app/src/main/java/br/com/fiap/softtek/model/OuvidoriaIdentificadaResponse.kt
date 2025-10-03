package br.com.fiap.softtek.model

data class OuvidoriaIdentificadaResponse(
    val id: String,
    val cpf: String,
    val mensagem: String,
    val isAnonimo: Boolean,
    val enviadoEm: String
)