package com.example.receitasfrequencia.view

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.receitasfrequencia.viewmodel.AuthViewModel
import com.example.receitasfrequencia.viewmodel.RecipeViewModel



import com.example.receitasfrequencia.database.AppDatabase
import com.example.receitasfrequencia.viewmodel.AuthViewModelFactory
import com.example.receitasfrequencia.viewmodel.RecipeViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.receitasfrequencia.utils.RecipeDetailScreen

@Composable
fun AppNavigation(database: AppDatabase) {
    // Criar o NavController
    val navController = rememberNavController()

    // Criar os ViewModels usando as factories
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(database)
    )

    val recipeViewModel: RecipeViewModel = viewModel(
        factory = RecipeViewModelFactory(database)
    )

    NavHost( navController = navController,  startDestination = "splash"
    ) {
        // Splash Screen
        composable("splash") {
            SplashScreen(navController = navController)
        }

        composable("login") {
            LoginScreen(
                navController = navController,
                authViewModel = authViewModel,
                recipeViewModel = recipeViewModel
            )
        }

        // Tela de Cadastro
        composable("register") {
            RegisterScreen(navController = navController, authViewModel = authViewModel)
        }

        composable("home") {
            RecipeListScreen(viewModel = recipeViewModel, navController = navController)
        }

        // Tela para adicionar uma nova receita
        composable("add_recipe") {
            RecipeFormScreen(viewModel = recipeViewModel, navController = navController)
        }


        composable("recipe_edite/{recipeId}") { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId")?.toLongOrNull()
            RecipeFormScreen(viewModel = recipeViewModel,
                navController = navController,
                recipeId = recipeId)
        }


        // Tela de detalhes da receita
        composable("recipe_detail/{recipeId}") { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId")?.toLongOrNull()
            recipeId?.let {
                RecipeDetailScreen(recipeId = it, viewModel = recipeViewModel, navController = navController)
            }
        }


    }
}