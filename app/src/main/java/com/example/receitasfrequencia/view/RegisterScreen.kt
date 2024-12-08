package com.example.receitasfrequencia.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // Observar o estado de autenticação
    val authState by authViewModel.authState.collectAsStateWithLifecycle()

    // Efeito para reagir às mudanças no estado de autenticação
    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthState.Registered -> {
                // Navegar de volta para o login após cadastro
                navController.navigate("login") {
                    popUpTo("register") { inclusive = true }
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
            .background(brush =
            Brush.verticalGradient(
                colors = listOf(Color(0xFF6D96C9), Color(0xFF5790A1))))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Criar Conta",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Campo de nome de usuário
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Nome de Usuário") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = fieldColors()
            )

            // Campo de e-mail
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-mail") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = fieldColors()
            )

            // Campo de senha
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Senha") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                visualTransformation = PasswordVisualTransformation(),
                colors = fieldColors()
            )

            // Campo de confirmação de senha
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar Senha") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                visualTransformation = PasswordVisualTransformation(),
                colors = fieldColors()
            )

            // Mensagem de erro
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Yellow,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // Botão de cadastro
            Button(
                onClick = {
                    // Validações antes do cadastro
                    when {
                        username.isBlank() -> errorMessage = "Nome de usuário é obrigatório"
                        email.isBlank() -> errorMessage = "E-mail é obrigatório"
                        password.isBlank() -> errorMessage = "Senha é obrigatória"
                        password != confirmPassword -> errorMessage = "Senhas não conferem"
                        else -> {
                            errorMessage = ""
                            authViewModel.registerUser(username, email, password)
                        }
                    }
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
                Text(text = "Cadastrar", fontWeight = FontWeight.Bold, color = Color(0xFF0072FF))
            }
        }
    }
}

// Função auxiliar para cores dos campos de texto
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun fieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White.copy(alpha = 0.5f),
    focusedIndicatorColor = Color.White,
    unfocusedIndicatorColor = Color.White.copy(alpha = 0.5f)
)