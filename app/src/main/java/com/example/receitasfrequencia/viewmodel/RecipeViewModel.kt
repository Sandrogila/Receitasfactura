package com.example.receitasfrequencia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.receitasfrequencia.database.AppDatabase
import com.example.receitasfrequencia.model.Recipe
import com.example.receitasfrequencia.model.User
import com.example.receitasfrequencia.repository.DaoRecipe
import com.example.receitasfrequencia.repository.Daouser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch




class RecipeViewModel(
    private val recipeDao: DaoRecipe,
    private val userDao: Daouser
) : ViewModel() {



    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes.asStateFlow()



    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    // Método para definir o usuário atual
    fun setCurrentUser(user: User) {
        _currentUser.value = user
        loadUserRecipes()
    }

    private fun loadUserRecipes() {
        viewModelScope.launch {
            try {
                val user = _currentUser.value
                if (user != null) {
                    _recipes.value = recipeDao.getRecipesByUser(user.id)
                } else {
                    _recipes.value = emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _recipes.value = emptyList()
            }
        }
    }

    // Modificar o método addRecipe
    fun addRecipe(
        title: String,
        description: String,
        ingredients: String,
        category: String,
        instructions: String
    ) {
        viewModelScope.launch {
            try {
                val user = _currentUser.value ?: throw IllegalStateException("Usuário não autenticado")

                val recipe = Recipe(
                    title = title,
                    description = description,
                    ingredients = ingredients,
                    category = category,
                    instructions = instructions,
                    userId = user.id
                )

                recipeDao.insertRecipe(recipe)
                loadUserRecipes()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }



    // Atualizar receita existente
    fun updateRecipe(recipe: Recipe) {
        viewModelScope.launch {
            try {
                recipeDao.updateRecipe(recipe)
                loadUserRecipes()
            } catch (e: Exception) {

            }
        }
    }

    // Remover receita
    fun removeRecipe(recipe: Recipe) {
        viewModelScope.launch {
            try {
                recipeDao.deleteRecipe(recipe)
                loadUserRecipes() // Recarregar receitas após remoção
            } catch (e: Exception) {

            }
        }
    }

    // Buscar receitas por título ou categoria
    fun searchRecipes(query: String) {
        viewModelScope.launch {
            try {
                _recipes.value = recipeDao.searchRecipes(query)
            } catch (e: Exception) {

            }
        }
    }
}

// Factory para criar ViewModel com dependências
class RecipeViewModelFactory(
    private val database: AppDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipeViewModel(
                recipeDao = database.recipeDao,
                userDao = database.userDao
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
