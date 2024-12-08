package com.example.receitasfrequencia.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.receitasfrequencia.model.Recipe
import com.example.receitasfrequencia.model.User
import com.example.receitasfrequencia.repository.DaoRecipe
import com.example.receitasfrequencia.repository.Daouser

@Database(
    entities = [User::class, Recipe::class],  // Entidades do banco de dados
    version = 1,                             // Versão do banco de dados
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    // DAO para acessar receitas
    abstract val recipeDao: DaoRecipe

    // DAO para acessar usuários
    abstract val userDao: Daouser

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "recipes_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}