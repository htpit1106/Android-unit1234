package com.example.bai2sqliteth4

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookcare.PreferrentHelper

class MainActivity : ComponentActivity() {
    private lateinit var databaseHelper: PreferrentHelper
    private lateinit var edtid: EditText
    private lateinit var edtten: EditText
    private lateinit var edtsdt: EditText
    private lateinit var btnadd: Button
    private lateinit var btnupdate: Button
    private lateinit var btndelete: Button
    private lateinit var btnshow: Button
    private lateinit var adapter: adapterCV
    private lateinit var listaccount: ArrayList<account>
    private lateinit var rcv: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

        anhxa()

        btnadd.setOnClickListener {
            val name = edtten.text.toString().trim()
            val phone = edtsdt.text.toString().trim()

            if (name.isNotEmpty() && phone.isNotEmpty()) {
                val result = databaseHelper.insertData(account(0, name, phone))
                if (result != -1L) {
                    Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show()
                    refreshList()  // Cập nhật danh sách sau khi thêm
                } else {
                    Toast.makeText(this, "Thêm thất bại!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
            }
        }

        btnupdate.setOnClickListener {
            val id = edtid.text.toString().toIntOrNull()
            val name = edtten.text.toString()
            val phone = edtsdt.text.toString()

            if (id != null && name.isNotEmpty() && phone.isNotEmpty()) {
                val acc = account(id, name, phone)
                if (databaseHelper.updateData(id, acc)) {
                    refreshList()
                }
            } else {
                Toast.makeText(this, "Thông tin không hợp lệ!", Toast.LENGTH_SHORT).show()
            }
        }

        btndelete.setOnClickListener {
            val id = edtid.text.toString().toIntOrNull()
            if (id != null) {
                if (databaseHelper.deleteData(id)) {
                    refreshList()
                } else {
                    Toast.makeText(this, "Không tìm thấy tài khoản với ID $id", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnshow.setOnClickListener {
            refreshList()
        }
    }

    private fun anhxa() {
        databaseHelper = PreferrentHelper(this)

        edtid = findViewById(R.id.edtid)
        edtten = findViewById(R.id.edtten)
        edtsdt = findViewById(R.id.edtsdt)
        btnadd = findViewById(R.id.btnadd)
        btnupdate = findViewById(R.id.btnupdate)
        btndelete = findViewById(R.id.btndelete)
        btnshow = findViewById(R.id.btneshow)

        listaccount = ArrayList()
        rcv = findViewById(R.id.rcv)
        rcv.layoutManager = LinearLayoutManager(this)

        adapter = adapterCV(listaccount)
        rcv.adapter = adapter
    }

    private fun refreshList() {
        listaccount.clear()
        listaccount.addAll(databaseHelper.getAllData())
        adapter.notifyDataSetChanged()
    }
}
