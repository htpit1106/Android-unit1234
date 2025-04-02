package com.example.bai2sqliteth4

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class adapterCV(private val arrayList: ArrayList<account>) : RecyclerView.Adapter<adapterCV.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtName: TextView = itemView.findViewById(R.id.txtten)
        private val txtEmail: TextView = itemView.findViewById(R.id.txtsodienthoai)

        fun bind(account: account) {
            txtName.text = account.ten
            txtEmail.text = account.sdt}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = arrayList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(arrayList[position])
    }
}
