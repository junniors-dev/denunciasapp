package com.diaz.denunciasapp

import android.os.Bundle
import android.view.View
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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        b.btnAtender.setOnClickListener { atender() }
        b.btnVolver.setOnClickListener { finish() }

        lifecycleScope.launch {
            try {
                mostrar(ApiClient.api.obtener(id))
            } catch (e: Exception) {
                mostrarError(e)
            }
        }
    }

    // Flecha de la barra superior
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun mostrar(d: Denuncia) {
        b.txtTitulo.text = "#${d.id} · ${d.tipo}"
        b.txtEstado.visibility = View.VISIBLE
        b.txtEstado.pintarEstado(d.estado)
        b.txtDni.text = d.dni
        b.txtFecha.text = fechaCorta(d.fecha)
        b.txtDireccion.text = d.direccion
        b.txtDescripcion.text = d.descripcion

        val pendiente = d.estado == 1
        b.btnAtender.isEnabled = pendiente
        b.btnAtender.text = if (pendiente) "Atender denuncia" else "Denuncia ya atendida"
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
