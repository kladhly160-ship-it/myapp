package com.photo.gallery

import android.os.Bundle
import android.widget.GridView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        val g = GridView(this)
        g.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,
            List(30) { "IMG_${1000 + it}.jpg" })
        setContentView(g)
    }
}
