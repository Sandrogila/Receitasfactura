package com.example.receitasfrequencia.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.receitasfrequencia.model.Recipe

@Dao
interface DaoRecipe {
    @Insert
    suspend fun insertRecipe(recipe: Recipe)

    // Atualizar receita
    @Update
    suspend fun updateRecipe(recipe: Recipe)

    // Deletar receita
    @Delete
    suspend fun deleteRecipe(recipe: Recipe)

    // Buscar todas as receitas
    @Query("SELECT * FROM recipes WHERE userId = :userId")
    suspend fun getRecipesByUser(userId: Long): List<Recipe>

    @Query("SELECT * FROM recipes WHERE id = :id LIMIT 1")
    suspend fun getRecipeById(id: Long): Recipe?

    // Buscar receitas por título ou categoria
    @Query("SELECT * FROM recipes WHERE title LIKE :query OR category LIKE :query")
    suspend fun searchRecipes(query: String): List<Recipe>
}