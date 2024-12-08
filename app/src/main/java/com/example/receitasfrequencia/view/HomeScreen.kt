package com.example.receitasfrequencia.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.receitasfrequencia.utils.RecipeCard
import com.example.receitasfrequencia.viewmodel.AuthState
import com.example.receitasfrequencia.viewmodel.AuthViewModel
import com.example.receitasfrequencia.viewmodel.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListScreen(viewModel: RecipeViewModel,
                     navController: NavController,
                     authViewModel: AuthViewModel
) {
    val recipes by viewModel.recipes.collectAsState()
    var isLoading by remember { mutableStateOf(false) }

    val userName by authViewModel.authState.collectAsState(initial = AuthState.Unauthenticated)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (userName) {
                            is AuthState.Authenticated -> "Seja bem-vindo  , ${(userName as AuthState.Authenticated).user.username}!"
                            else -> "Welcome!"
                        },
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                actions = {
                    IconButton(onClick = {
                        authViewModel.logout() // Chama o logout no ViewModel
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true } // Remove a tela Home da pilha de navegação
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Logout",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF004A99))
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_recipe") },
                containerColor = Color(0xFF004A99)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Recipe", tint = Color.White)
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF0072FF), Color(0xFF00C6FF))
                    )
                )
        ) {
            if (recipes.isEmpty()) {
                Text(
                    text = "No recipes available.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    items(recipes) { recipe ->
                        RecipeCard(
                            recipe = recipe,
                            onClick = { navController.navigate("recipe_detail/${recipe.id}") },
                            onDelete = {
                                isLoading = true
                                viewModel.removeRecipe(recipe)
                                isLoading = false
                            },
                            onUpedate = {
                                navController.navigate("recipe_edite/${recipe.id}")
                            }
                        )
                    }
                }
            }

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )
            }
        }
    }
}
