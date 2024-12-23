package com.example.sqllite_voiture

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Switch
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val nameEd = findViewById<EditText>(R.id.nom)
        val priceEd = findViewById<EditText>(R.id.prix)
        val imageEd = findViewById<EditText>(R.id.img)
        val fullSwitch = findViewById<Switch>(R.id.full)
        val AddBtn = findViewById<Button>(R.id.Addbtn)
        val listview = findViewById<ListView>(R.id.lv)

        AddBtn.setOnClickListener{
            val nom = nameEd.text.toString()
            val price = priceEd.text.toString().toDoubleOrNull()
            val image = imageEd.text.toString()
            val switchOption = fullSwitch.isChecked

            if (nom.isNotBlank() && price!= null && image.isNotBlank()){
                val voiture = Voiture(name = nom , price = price , image = image , isfulloptions = switchOption)
                voitureDatabase.getDataBase(applicationContext).VoitureDao().insertVoiture(voiture)

                Toast.makeText(this, "Car added successfully!", Toast.LENGTH_SHORT).show()
                fetchData()
            }else{
                Toast.makeText(this, "Please complete all fields!", Toast.LENGTH_SHORT).show()
            }
        }

        listview.setOnItemClickListener { _, _, position, _ ->
            val cars = voitureDatabase.getDataBase(applicationContext).VoitureDao().getVoiture()
            val selectedVoiture = cars[position]

            val intent = Intent(this, Up_And_Dell::class.java).apply {
                putExtra("id", selectedVoiture.id)
                putExtra("name", selectedVoiture.name)
                putExtra("price", selectedVoiture.price)
                putExtra("image", selectedVoiture.image)
                putExtra("fullOption", selectedVoiture.isfulloptions)
            }
            startActivity(intent)
        }
    }

    private fun fetchData(){
        val listViewVoiture = findViewById<ListView>(R.id.lv)
        val voitureList = mutableListOf<String>()
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, voitureList)
        listViewVoiture.adapter = arrayAdapter

        val vehicule = voitureDatabase.getDataBase(applicationContext).VoitureDao().getVoiture()
        voitureList.clear()

        for (car in vehicule){
            voitureList.add("${car.name} - ${car.price} Dh")
        }

        arrayAdapter.notifyDataSetChanged()


//        Recherche

        val searchED = findViewById<EditText>(R.id.searchEd)
        searchED.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchQuery = s.toString().trim()
                if (searchQuery.isNotEmpty()) {
                    val Cars = voitureDatabase.getDataBase(applicationContext)
                        .VoitureDao()
                        .searchVoitureByName(searchQuery)

                    val filteredResults = Cars.map { "${it.name} - ${it.price} Dh" }


                    val adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, filteredResults)
                    listViewVoiture.adapter = adapter

                    if (filteredResults.isEmpty()) {
                        Toast.makeText(this@MainActivity, "Aucun produit trouvé", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    fetchData()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }


    override fun onResume() {
        super.onResume()
        fetchData()
    }
}