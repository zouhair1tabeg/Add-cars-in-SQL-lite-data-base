package com.example.sqllite_voiture

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Up_And_Dell : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_up_and_dell)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val editName = findViewById<EditText>(R.id.editName)
        val editPrice = findViewById<EditText>(R.id.editPrice)
        val editImage = findViewById<EditText>(R.id.editImage)
        val switch_option = findViewById<Switch>(R.id.option)
        val updateBtn = findViewById<Button>(R.id.updateBtn)
        val deleteBtn = findViewById<Button>(R.id.deleteBtn)


        val id = intent.getIntExtra("id", -1)
        val name = intent.getStringExtra("name")
        val price = intent.getDoubleExtra("price", 0.0)
        val image = intent.getStringExtra("image")
        val option = intent.getBooleanExtra("fullOption" , false)

        editName.setText(name)
        editPrice.setText(price.toString())
        editImage.setText(image)
        switch_option.isChecked = option

        val VoitureneDao = voitureDatabase.getDataBase(applicationContext).VoitureDao()

        updateBtn.setOnClickListener {
            val updatedName = editName.text.toString()
            val updatedPrice = editPrice.text.toString().toDoubleOrNull()
            val updatedImage = editImage.text.toString()
            val updateSwitch = switch_option.isChecked

            if (updatedName.isNotBlank() && updatedPrice != null && updatedImage.isNotBlank()) {
                val updatedVoiture = Voiture(id = id, name = updatedName, price = updatedPrice, image = updatedImage , isfulloptions = updateSwitch)
                VoitureneDao.updateVoiture(updatedVoiture)
                Toast.makeText(this, "Car updated successfully!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Please fill out all fields correctly.", Toast.LENGTH_SHORT).show()
            }
        }

        deleteBtn.setOnClickListener {
            VoitureneDao.deleteVoitureById(id)
            Toast.makeText(this, "Car deleted successfully!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

}