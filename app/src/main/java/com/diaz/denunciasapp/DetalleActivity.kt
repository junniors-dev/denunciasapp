package com.diaz.denunciasapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.diaz.denunciasapp.databinding.ActivityDetalleBinding
import kotlinx.coroutines.launch

class DetalleActivity : AppCompatActivity() {

    private lateinit var b: ActivityDetalleBinding
    private val id by lazy { intent.getIntExtra("id", 0) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityDetalleBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.btnAtender.isEnabled = false
        b.btnAtender.setOnClickListener { atender() }

        lifecycleScope.launch {
            try {
                mostrar(ApiClient.api.obtener(id))
            } catch (e: Exception) {
                mostrarError(e)
            }
        }
    }

    private fun mostrar(d: Denuncia) {
        b.txtDetalle.text = """
            Código: ${d.id}
            DNI: ${d.dni}
            Fecha: ${fechaCorta(d.fecha)}
            Tipo: ${d.tipo}
            Dirección: ${d.direccion}
            Descripción: ${d.descripcion}
            Estado: ${estadoTexto(d.estado)}
        """.trimIndent()
        b.btnAtender.isEnabled = d.estado == 1
    }

    private fun atender() {
        b.btnAtender.isEnabled = false
        lifecycleScope.launch {
            try {
                mostrar(ApiClient.api.atender(id))
                Toast.makeText(this@DetalleActivity, "Denuncia atendida", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                mostrarError(e)
                b.btnAtender.isEnabled = true
            }
        }
    }
}
