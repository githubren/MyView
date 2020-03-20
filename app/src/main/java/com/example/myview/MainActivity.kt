package com.example.myview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val myView = findViewById<MyView>(R.id.my_view)
//        val dataList = myView.dataList
//        dataList.add(1251600)
//        dataList.add(500000)
//        dataList.add(600000)
//        dataList.add(130200)
//        dataList.add(500000)
//
//
//
//        val hintList = myView.hintList
//        hintList.add("网店售价")
//        hintList.add("消保金")
//        hintList.add("技术年费")
//        hintList.add("居间费")
//        hintList.add("品牌授权押金")


        val progressView = findViewById<ProgressView>(R.id.progress_view)
        val btn = findViewById<Button>(R.id.btn)
        btn.setOnClickListener {
            progressView.startAnim()
        }
    }
}
