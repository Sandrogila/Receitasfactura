package com.example.receitasfrequencia.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.receitasfrequencia.model.Recipe
import com.example.receitasfrequencia.viewmodel.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    navController: NavController,
    viewModel: RecipeViewModel,
    recipeId: Long
) {
    // Busca a receita específica
    val recipe = remember(recipeId) {
        viewModel.recipes.value.find { it.id == recipeId }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Receitas",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFF004A99)// Azul para combinar com o Home e Login
                )
            )
        }
    ) { padding ->
        recipe?.let { selectedRecipe ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF0072FF), Color(0xFF00C6FF)) // Gradiente azul similar às outras telas
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp)
                ) {
                    RecipeHeader(recipe = selectedRecipe)

                    Spacer(modifier = Modifier.height(24.dp))

                    RecipeSection(
                        title = "Description",
                        content = selectedRecipe.description
                    )

                    Divider(
                        color = Color.White.copy(alpha = 0.5f),
                        modifier = Modifier.padding(vertical = 12.dp)
                    )

                    RecipeSection(
                        title = "Category",
                        content = selectedRecipe.category.ifBlank { "Not specified" }
                    )

                    Divider(
                        color = Color.White.copy(alpha = 0.5f),
                        modifier = Modifier.padding(vertical = 12.dp)
                    )

                    RecipeSection(
                        title = "Ingredients",
                        content = selectedRecipe.ingredients.ifBlank { "No ingredients listed" }
                    )

                    Divider(
                        color = Color.White.copy(alpha = 0.5f),
                        modifier = Modifier.padding(vertical = 12.dp)
                    )

                    RecipeSection(
                        title = "Instructions",
                        content = selectedRecipe.instructions.ifBlank { "No instructions provided" }
                    )
                }
            }
        } ?: run {
            // Caso em que a receita não é encontrada
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF507BAF), Color(0xFF4689A1)) // Gradiente azul similar às outras telas
                        )
                    )
            ) {
                Text(
                    text = "Recipe not found",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun RecipeHeader(recipe: Recipe) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF004A99)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = recipe.title,
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF00C6FF),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "A melhor receita para ti ${recipe.category.ifBlank { "everyone" }}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun RecipeSection(title: String, content: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF00C6FF),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.9f),
            lineHeight = 24.sp
        )
    }
}
