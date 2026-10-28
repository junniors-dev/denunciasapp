package com.diaz.denunciasapp

import android.os.Bundle
import android.widget.ArrayAdapter
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

        b.spTipo.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item,
            listOf("ROBO", "ASALTO", "VANDALISMO", "ACOSO", "OTRO")
        )
        b.btnGuardar.setOnClickListener { guardar() }
    }

    private fun guardar() {
        val dni = b.txtDni.text.toString().trim()
        val direccion = b.txtDireccion.text.toString().trim()
        val descripcion = b.txtDescripcion.text.toString().trim()
        if (dni.length != 8 || direccion.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(this, "Completa todo (el DNI tiene 8 dígitos)", Toast.LENGTH_SHORT).show()
            return
        }

        b.btnGuardar.isEnabled = false
        lifecycleScope.launch {
            try {
                val nueva = ApiClient.api.registrar(
                    Denuncia(
                        dni = dni,
                        tipo = b.spTipo.selectedItem.toString(),
                        direccion = direccion,
                        descripcion = descripcion
                    )
                )
                Toast.makeText(this@RegistrarActivity, "Denuncia #${nueva.id} registrada", Toast.LENGTH_SHORT).show()
                finish()
            } catch (e: Exception) {
                mostrarError(e)
                b.btnGuardar.isEnabled = true
            }
        }
    }
}
