package com.example.receitasfrequencia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.room.Room
import com.example.receitasfrequencia.database.AppDatabase
import com.example.receitasfrequencia.view.AppNavigation
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "recita_database"
        ).fallbackToDestructiveMigration().build()

        setContent {
            MaterialTheme {
                AppNavigation(database)
            }
        }
    }
}

