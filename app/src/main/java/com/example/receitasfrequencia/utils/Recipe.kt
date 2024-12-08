package com.example.receitasfrequencia.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.receitasfrequencia.model.Recipe

@Composable
fun RecipeCard(recipe: Recipe, onClick: () -> Unit, onDelete: () -> Unit,onUpedate:()-> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Título da receita
            Text(
                text = recipe.title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF00C6FF),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Descrição curta
            Text(
                text = recipe.description,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Linha separadora suave
            Divider(color = Color.Gray.copy(alpha = 0.3f), thickness = 1.dp, modifier = Modifier.padding(bottom = 16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Botão de editar
                Button(
                    onClick = onUpedate,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0072FF)), // Azul para o botão de editar
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Edit", color = Color.White)
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Botão de deletar
                Button(
                    onClick = onDelete,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Delete", color = Color.White)
                }
            }
        }
    }
}