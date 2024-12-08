package com.example.receitasfrequencia.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "recipes",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)


data class Recipe(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val ingredients: String, // Ingredientes armazenados como texto (separados por vírgula)
    val category: String, // Exemplo: "Sobremesas", "Prato Principal"
    val instructions: String,
    val userId: Long // Relacionamento com o usuário (quem criou a receita)
)

