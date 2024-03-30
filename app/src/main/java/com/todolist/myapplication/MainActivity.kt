package com.todolist.myapplication

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.todolist.myapplication.databinding.ActivityMainBinding
import com.todolist.myapplication.utilities.FileHelper

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    var itemList = ArrayList<String>()
    var fileHelper = FileHelper()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        itemList = fileHelper.readData(this)


        var arrayListAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            android.R.id.text1,
            itemList
        )
        mBinding.listView.adapter = arrayListAdapter

        mBinding.btnAdd.setOnClickListener {

            if (mBinding.editText.text.isNotEmpty()) {
                var itemName: String = mBinding.editText.text.toString()
                itemList.add(itemName)
                Log.e("ITEM_NAME", "Item name --> $itemName")
                mBinding.editText.setText("")

                fileHelper.writeData(itemList, this)
                arrayListAdapter.notifyDataSetChanged()

            }else{
                Toast.makeText(this, "Your item is empty", Toast.LENGTH_SHORT).show()
            }


        }

        mBinding.listView.setOnItemClickListener { adapterView, view, position, l ->
            var alert = AlertDialog.Builder(this)
            alert.setTitle("Delete")
            alert.setMessage("Do you want to delete the item from the list?")
            alert.setCancelable(false)
            alert.setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i ->

                dialogInterface.cancel()
            })
            alert.setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->
                itemList.removeAt(position)
                arrayListAdapter.notifyDataSetChanged()
                fileHelper.writeData(itemList, this)
            })

            alert.create().show()
        }
    }
}