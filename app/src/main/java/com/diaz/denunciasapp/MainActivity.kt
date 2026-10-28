package com.diaz.denunciasapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.diaz.denunciasapp.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var b: ActivityMainBinding
    private var offset = 0
    private val adapter = DenunciaAdapter { d ->
        startActivity(Intent(this, DetalleActivity::class.java).putExtra("id", d.id))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.lista.layoutManager = LinearLayoutManager(this)
        b.lista.adapter = adapter

        b.btnMas.setOnClickListener { cargarPagina() }
        b.btnBuscar.setOnClickListener { buscar() }
        b.txtDni.setOnEditorActionListener { _, accion, _ ->
            if (accion == EditorInfo.IME_ACTION_SEARCH) buscar()
            true
        }
        b.btnNueva.setOnClickListener {
            startActivity(Intent(this, RegistrarActivity::class.java))
        }
    }

    // Al volver de registrar o atender se recarga la lista desde el inicio
    override fun onResume() {
        super.onResume()
        b.txtDni.setText("")
        recargar()
    }

    private fun recargar() {
        offset = 0
        adapter.reemplazar(emptyList())
        cargarPagina()
    }

    private fun cargarPagina() {
        b.btnMas.isEnabled = false
        b.btnMas.text = "Cargando…"
        lifecycleScope.launch {
            try {
                val pagina = ApiClient.api.listar(offset)
                adapter.agregar(pagina)
                offset += pagina.size
                // si llegaron menos de 5 ya no hay más
                b.btnMas.isEnabled = pagina.size == 5
            } catch (e: Exception) {
                mostrarError(e)
                b.btnMas.isEnabled = true
            }
            b.btnMas.text = "Cargar 5 más"
            mostrarVacio("No hay denuncias")
        }
    }

    private fun buscar() {
        val dni = b.txtDni.text.toString().trim()
        if (dni.isEmpty()) {
            recargar()
            return
        }
        lifecycleScope.launch {
            try {
                adapter.reemplazar(ApiClient.api.buscarPorDni(dni))
                b.btnMas.isEnabled = false
                mostrarVacio("Sin denuncias para el DNI $dni")
            } catch (e: Exception) {
                mostrarError(e)
            }
        }
    }

    private fun mostrarVacio(mensaje: String) {
        b.txtVacio.text = mensaje
        b.txtVacio.visibility = if (adapter.itemCount == 0) View.VISIBLE else View.GONE
    }
}
