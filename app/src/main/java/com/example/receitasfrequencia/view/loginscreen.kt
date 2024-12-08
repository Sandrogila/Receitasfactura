package com.example.receitasfrequencia.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.receitasfrequencia.viewmodel.AuthState
import com.example.receitasfrequencia.viewmodel.AuthViewModel
import com.example.receitasfrequencia.viewmodel.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    recipeViewModel: RecipeViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // Observar o estado de autenticação
    val authState by authViewModel.authState.collectAsStateWithLifecycle()

    // Efeito para reagir às mudanças no estado de autenticação
    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthState.Authenticated -> {
                // Definir o usuário atual no RecipeViewModel
                recipeViewModel.setCurrentUser(state.user)

                // Navegar para a tela home
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            }
            is AuthState.Error -> {
                // Mostrar mensagem de erro
                errorMessage = state.message
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF6D96C9), Color(0xFF5790A1)))
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bem-vindo!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Campo de e-mail
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-mail") },
                placeholder = { Text("Digite seu e-mail") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White.copy(alpha = 0.5f),
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.White.copy(alpha = 0.5f)
                )
            )

            // Campo de senha
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Senha") },
                placeholder = { Text("Digite sua senha") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White.copy(alpha = 0.5f),
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.White.copy(alpha = 0.5f)
                ),
                visualTransformation = PasswordVisualTransformation()
            )

            // Mensagem de erro
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Yellow,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // Botão de login
            Button(
                onClick = {
                    authViewModel.login(email, password)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF0072FF)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Entrar", fontWeight = FontWeight.Bold, color = Color(0xFF0072FF))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Link para cadastro
            Text(
                text = "Não tem conta? Cadastre-se!",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp,
                modifier = Modifier.clickable {
                    // Navegar para tela de cadastro
                    navController.navigate("register")
                }
            )
        }
    }
}