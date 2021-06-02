package com.example.recyclerviewpet

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.data_list_item.*
import kotlinx.android.synthetic.main.data_list_item.view.*

class DataAdapter(
    val items : ArrayList<String>,
    val context: Context
) : RecyclerView.Adapter<ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(
            R.layout.data_list_item,
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.tvDataType.text = items[position]
    }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
//    val tvDataType = view.tv_data_type!!
}

class ExtensionViewHolder(override val containerView: View)
    : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(title: String) {
//            tv_data_type.text = "Hello Kotlin!"
        }
    }