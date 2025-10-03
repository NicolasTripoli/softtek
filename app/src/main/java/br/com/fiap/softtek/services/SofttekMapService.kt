package br.com.fiap.softtek.services

import br.com.fiap.softtek.model.DesafioResponse
import br.com.fiap.softtek.model.FraseMotivacionalResponse
import br.com.fiap.softtek.model.LoginRequest
import br.com.fiap.softtek.model.LoginResponse
import br.com.fiap.softtek.model.OuvidoriaIdentificadaRequest
import br.com.fiap.softtek.model.OuvidoriaIdentificadaResponse
import br.com.fiap.softtek.model.OuvidoriaRequest
import br.com.fiap.softtek.model.OuvidoriaResponse
import br.com.fiap.softtek.model.QuestionarioPsicologicoResponse
import br.com.fiap.softtek.model.QuestionarioRespostaRequest
import br.com.fiap.softtek.model.QuestionarioRespostaResponse
import br.com.fiap.softtek.model.SentimentalResponse
import br.com.fiap.softtek.model.SentimentoRequest
import br.com.fiap.softtek.model.UsuarioDesafioRequest
import br.com.fiap.softtek.model.UsuarioDesafioResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SofttekMapService {
    @POST("api/v1/auth/login")
    fun postLogin(
        @Body request: LoginRequest
    ): Call<LoginResponse>

    @GET("/api/v1/sentimentos-checkin/{cpf}")
    fun getSentimentosCheck(
        @Path("cpf") cpf: String
    ): Call<List<SentimentalResponse>>

    @POST("/api/v1/sentimentos-checkin")
    fun postSentimento(
        @Body request: SentimentoRequest
    ): Call<SentimentalResponse>

    @GET("/api/v1/desafio-diario/aleatorio")
    fun getDesafioDiario(): Call<DesafioResponse>

    @POST("/api/v1/desafio-diario/usuario-desafio")
    fun postUsuarioDesafio(
        @Body request: UsuarioDesafioRequest
    ): Call<UsuarioDesafioResponse>

    @GET("/api/v1/frase-motivacional/aleatoria")
    fun getFraseMotivacionalAleatoria(): Call<FraseMotivacionalResponse>

    @POST("/api/v1/ouvidoria/anonima")
    fun enviarOuvidoriaAnonima(
        @Body request: OuvidoriaRequest
    ): Call<OuvidoriaResponse>

    @POST("/api/v1/ouvidoria")
    fun enviarOuvidoriaIdentificada(
        @Body request: OuvidoriaIdentificadaRequest
    ): Call<OuvidoriaIdentificadaResponse>

    @GET("/api/v1/questionario-psicologico")
    fun getQuestionarioPsicologico(): Call<List<QuestionarioPsicologicoResponse>>

    @POST("/api/v1/questionario-psicologico/usuario-resposta")
    fun enviarRespostaQuestionario(
        @Body request: QuestionarioRespostaRequest
    ): Call<QuestionarioRespostaResponse>

    @GET("/api/v1/desafio-diario/usuario-desafio/todos/{cpf}")
    fun getTodosDesafiosUsuario(
        @Path("cpf") cpf: String
    ): Call<List<UsuarioDesafioResponse>>
}