package com.example.receitasfrequencia.view

import androidx.compose.material3.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.receitasfrequencia.model.Recipe
import com.example.receitasfrequencia.viewmodel.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeFormScreen(
    navController: NavController,
    viewModel: RecipeViewModel,
    recipeId: Long? = null
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }

    // Carregar os dados da receita caso seja edição
    LaunchedEffect(recipeId) {
        if (recipeId != null) {
            val recipe = viewModel.recipes.value.find { it.id == recipeId }
            recipe?.let {
                title = it.title
                description = it.description
                ingredients = it.ingredients
                category = it.category
                instructions = it.instructions
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (recipeId == null) "Adicionar receitas" else "Editar receitas",
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },

                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFF004A99)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (title.isNotBlank() && description.isNotBlank()) {
                        if (recipeId == null) {
                            // Adicionar nova receita
                            viewModel.addRecipe(
                                title, description, ingredients, category, instructions
                            )
                        } else {
                            // Atualizar receita existente
                            viewModel.updateRecipe(
                                Recipe(
                                    id = recipeId,
                                    title = title,
                                    description = description,
                                    ingredients = ingredients,
                                    category = category,
                                    instructions = instructions,
                                    userId = 1 // Substituir pelo ID real do usuário logado
                                )
                            )
                        }
                        navController.navigate("home")
                    }
                },
                containerColor = Color(0xFF004A99)
            ) {
                Icon(Icons.Default.Check, contentDescription = "Save Recipe", tint = Color.White)
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title", color = Color.White.copy(alpha = 0.8f)) },
                    colors = textFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description", color = Color.White.copy(alpha = 0.8f)) },
                    colors = textFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = ingredients,
                    onValueChange = { ingredients = it },
                    label = { Text("Ingredients", color = Color.White.copy(alpha = 0.8f)) },
                    colors = textFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category", color = Color.White.copy(alpha = 0.8f)) },
                    colors = textFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = instructions,
                    onValueChange = { instructions = it },
                    label = { Text("Instructions", color = Color.White.copy(alpha = 0.8f)) },
                    colors = textFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

// Função auxiliar para estilizar os campos de texto
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun textFieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White.copy(alpha = 0.8f),
    focusedIndicatorColor = Color.White,
    unfocusedIndicatorColor = Color.White.copy(alpha = 0.5f),
    cursorColor = Color.White
)
