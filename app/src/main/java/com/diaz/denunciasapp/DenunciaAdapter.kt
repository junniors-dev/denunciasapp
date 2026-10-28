package com.diaz.denunciasapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.diaz.denunciasapp.databinding.ItemDenunciaBinding

class DenunciaAdapter(private val alTocar: (Denuncia) -> Unit) :
    RecyclerView.Adapter<DenunciaAdapter.Fila>() {

    private val items = mutableListOf<Denuncia>()

    class Fila(val b: ItemDenunciaBinding) : RecyclerView.ViewHolder(b.root)

    fun reemplazar(lista: List<Denuncia>) {
        items.clear()
        items.addAll(lista)
        notifyDataSetChanged()
    }

    fun agregar(lista: List<Denuncia>) {
        val inicio = items.size
        items.addAll(lista)
        notifyItemRangeInserted(inicio, lista.size)
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Fila(ItemDenunciaBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(fila: Fila, position: Int) {
        val d = items[position]
        fila.b.txtTitulo.text = "#${d.id} · ${d.tipo}"
        fila.b.txtDireccion.text = d.direccion
        fila.b.txtInfo.text = "DNI ${d.dni} · ${fechaCorta(d.fecha)} · ${estadoTexto(d.estado)}"
        fila.itemView.setOnClickListener { alTocar(d) }
    }
}

fun fechaCorta(fecha: String?) = fecha?.replace('T', ' ')?.take(16) ?: ""

fun estadoTexto(estado: Int?) = if (estado == 1) "Pendiente" else "Atendida"
