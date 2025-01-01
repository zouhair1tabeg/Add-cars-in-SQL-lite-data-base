package com.example.sqllite_voiture

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update

@Entity(tableName = "voiture_table")
data class Voiture(
    @PrimaryKey(autoGenerate = true)
    val id : Int=0,
    val name : String,
    val price : Double,
    val isfulloptions :Boolean ,
    val image : String
)


@Dao
interface VoitureDao {
    @Insert
    fun insertVoiture(voiture: Voiture)

    @Query("SELECT * FROM voiture_table")
    fun getVoiture(): List<Voiture>

    @Update
    fun updateVoiture(voiture: Voiture)

    @Query("DELETE FROM voiture_table WHERE id = :id")
    fun deleteVoitureById(id: Int)

    @Query("SELECT * FROM voiture_table WHERE name LIKE '%' || :query || '%'")
    fun searchVoitureByName(query: String): List<Voiture>
}


@Database(entities = [Voiture::class], version = 1, exportSchema = false)
abstract class voitureDatabase : RoomDatabase(){
    abstract fun VoitureDao(): VoitureDao

    companion object{
        private var INSTANCE: voitureDatabase? = null

        fun getDataBase(context: Context):voitureDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    voitureDatabase::class.java,
                    "Voiture_database"
                ).allowMainThreadQueries()
                    .build()
                INSTANCE=instance
                instance
            }
        }
    }
}