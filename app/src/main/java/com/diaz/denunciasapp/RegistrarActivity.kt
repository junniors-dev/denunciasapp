package com.diaz.denunciasapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.diaz.denunciasapp.databinding.ActivityRegistrarBinding
import kotlinx.coroutines.launch

class RegistrarActivity : AppCompatActivity() {

    private lateinit var b: ActivityRegistrarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityRegistrarBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        b.txtTipo.setText("ROBO", false)
        b.btnGuardar.setOnClickListener { guardar() }
        b.btnCancelar.setOnClickListener { finish() }
    }

    // Flecha de la barra superior
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun guardar() {
        val dni = b.txtDni.text.toString().trim()
        val direccion = b.txtDireccion.text.toString().trim()
        val descripcion = b.txtDescripcion.text.toString().trim()

        b.tilDni.error = if (dni.length != 8) "El DNI tiene 8 dígitos" else null
        b.tilDireccion.error = if (direccion.isEmpty()) "Escribe la dirección" else null
        b.tilDescripcion.error = if (descripcion.isEmpty()) "Cuenta qué pasó" else null
        if (b.tilDni.error != null || b.tilDireccion.error != null || b.tilDescripcion.error != null) return

        b.btnGuardar.isEnabled = false
        b.btnGuardar.text = "Registrando…"
        lifecycleScope.launch {
            try {
                val nueva = ApiClient.api.registrar(
                    Denuncia(
                        dni = dni,
                        tipo = b.txtTipo.text.toString(),
                        direccion = direccion,
                        descripcion = descripcion
                    )
                )
                Toast.makeText(this@RegistrarActivity, "Denuncia #${nueva.id} registrada", Toast.LENGTH_SHORT).show()
                finish()
            } catch (e: Exception) {
                mostrarError(e)
                b.btnGuardar.isEnabled = true
                b.btnGuardar.text = "Registrar denuncia"
            }
        }
    }
}
