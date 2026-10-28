package com.diaz.denunciasapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
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
        b.lista.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        b.lista.adapter = adapter

        b.btnMas.setOnClickListener { cargarPagina() }
        b.btnBuscar.setOnClickListener { buscar() }
        b.btnNueva.setOnClickListener {
            startActivity(Intent(this, RegistrarActivity::class.java))
        }
    }

    // Al volver de registrar o atender se recarga la lista desde el inicio
    override fun onResume() {
        super.onResume()
        b.txtDni.setText("")
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
        }
    }

    private fun buscar() {
        val dni = b.txtDni.text.toString().trim()
        if (dni.isEmpty()) {
            onResume()
            return
        }
        lifecycleScope.launch {
            try {
                val lista = ApiClient.api.buscarPorDni(dni)
                adapter.reemplazar(lista)
                b.btnMas.isEnabled = false
                if (lista.isEmpty()) {
                    Toast.makeText(this@MainActivity, "Sin denuncias para ese DNI", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                mostrarError(e)
            }
        }
    }
}
