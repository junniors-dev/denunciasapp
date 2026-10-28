package com.diaz.denunciasapp

import android.content.Context
import android.widget.Toast
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

data class Denuncia(
    val id: Int? = null,
    val dni: String,
    val fecha: String? = null,
    val tipo: String,
    val direccion: String,
    val descripcion: String,
    val estado: Int? = null
)

interface DenunciaApi {
    @POST("denuncias/")
    suspend fun registrar(@Body denuncia: Denuncia): Denuncia

    @GET("denuncias/")
    suspend fun listar(@Query("offset") offset: Int, @Query("limit") limit: Int = 5): List<Denuncia>

    @GET("denuncias/{id}")
    suspend fun obtener(@Path("id") id: Int): Denuncia

    @GET("denuncias/ciudadano/{dni}")
    suspend fun buscarPorDni(@Path("dni") dni: String): List<Denuncia>

    @PUT("denuncias/{id}")
    suspend fun atender(@Path("id") id: Int): Denuncia
}

object ApiClient {
    // Para la API local desde el emulador: "http://10.0.2.2:8080/"
    private const val BASE_URL = "https://denunciaservice-diaz.onrender.com/"

    // Render gratis tarda hasta un minuto en despertar
    private val http = OkHttpClient.Builder()
        .connectTimeout(90, TimeUnit.SECONDS)
        .readTimeout(90, TimeUnit.SECONDS)
        .build()

    val api: DenunciaApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(http)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(DenunciaApi::class.java)
}

fun Context.mostrarError(e: Exception) {
    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
}
