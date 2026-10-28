package com.diaz.denunciasapp

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
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
        fila.b.txtInfo.text = "DNI ${d.dni} · ${fechaCorta(d.fecha)}"
        fila.b.txtEstado.pintarEstado(d.estado)
        fila.itemView.setOnClickListener { alTocar(d) }
    }
}

fun fechaCorta(fecha: String?) = fecha?.replace('T', ' ')?.take(16) ?: ""

// Píldora naranja para pendiente y verde para atendida
fun TextView.pintarEstado(estado: Int?) {
    val pendiente = estado == 1
    text = if (pendiente) "Pendiente" else "Atendida"
    setTextColor(ContextCompat.getColor(context, if (pendiente) R.color.pendiente_texto else R.color.atendida_texto))
    backgroundTintList = ColorStateList.valueOf(
        ContextCompat.getColor(context, if (pendiente) R.color.pendiente_fondo else R.color.atendida_fondo)
    )
}
