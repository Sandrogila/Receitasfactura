package com.example.receitasfrequencia.viewmodel


import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.receitasfrequencia.database.AppDatabase
import com.example.receitasfrequencia.model.User
import com.example.receitasfrequencia.repository.Daouser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.regex.Pattern

class AuthViewModel(
    private val userDao: Daouser

): ViewModel(){

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState = _authState.asStateFlow()

    // Validação de e-mail
    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Validação de força de senha
    private fun isStrongPassword(password: String): Boolean {
        val passwordRegex = Pattern.compile(
            "^" +
                    "(?=.*[0-9])" +         // Pelo menos um número
                    "(?=.*[a-z])" +         // Pelo menos uma letra minúscula
                    "(?=.*[A-Z])" +         // Pelo menos uma letra maiúscula
                    "(?=.*[!@#$%^&*()_+])" + // Pelo menos um caractere especial
                    "(?=\\S+$)" +           // Sem espaços em branco
                    ".{8,}" +               // Pelo menos 8 caracteres
                    "$"
        )
        return passwordRegex.matcher(password).matches()
    }

    // Registrar usuário com validações mais robustas
    fun registerUser(username: String, email: String, password: String) {
        viewModelScope.launch {
            when {
                username.isBlank() -> {
                    _authState.value = AuthState.Error("Nome de usuário é obrigatório")
                    return@launch
                }
                !isValidEmail(email) -> {
                    _authState.value = AuthState.Error("E-mail inválido")
                    return@launch
                }
                !isStrongPassword(password) -> {
                    _authState.value = AuthState.Error(
                        "Senha fraca. Deve conter: " +
                                "8+ caracteres, números, letras maiúsculas e minúsculas, caractere especial"
                    )
                    return@launch
                }
            }

            val existingUser = userDao.getUserByEmail(email)
            if (existingUser != null) {
                _authState.value = AuthState.Error("Usuário já cadastrado")
                return@launch
            }

            val hashedPassword = hashPassword(password)
            val newUser = User(
                username = username,
                email = email,
                password = hashedPassword
            )

            try {
                userDao.insertUser(newUser)
                _authState.value = AuthState.Registered
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Erro ao cadastrar usuário: ${e.localizedMessage}")
            }
        }
    }
    fun logout() {
        viewModelScope.launch {
            _authState.value = AuthState.Unauthenticated
        }
    }

    // Login com validações
    fun login(email: String, password: String) {
        viewModelScope.launch {
            when {
                email.isBlank() -> {
                    _authState.value = AuthState.Error("E-mail é obrigatório")
                    return@launch
                }
                !isValidEmail(email) -> {
                    _authState.value = AuthState.Error("E-mail inválido")
                    return@launch
                }
                password.isBlank() -> {
                    _authState.value = AuthState.Error("Senha é obrigatória")
                    return@launch
                }
            }

            val user = userDao.getUserByEmail(email)
            if (user == null) {
                _authState.value = AuthState.Error("Usuário não encontrado")
                return@launch
            }

            val hashedPassword = hashPassword(password)
            if (user.password == hashedPassword) {
                _authState.value = AuthState.Authenticated(user)
            } else {
                _authState.value = AuthState.Error("Senha incorreta")
            }
        }
    }

    // Recuperação de senha (lógica básica)
    fun resetPassword(email: String) {
        viewModelScope.launch {
            if (!isValidEmail(email)) {
                _authState.value = AuthState.Error("E-mail inválido")
                return@launch
            }

            val user = userDao.getUserByEmail(email)
            if (user == null) {
                _authState.value = AuthState.Error("Usuário não encontrado")
                return@launch
            }

            // Aqui você poderia implementar a lógica de envio de e-mail de recuperação de senha
            // Por exemplo, gerar um token de redefinição de senha, enviar por e-mail, etc.
            _authState.value = AuthState.PasswordResetRequested
        }
    }

    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(password.toByteArray(Charsets.UTF_8))
        return hashBytes.joinToString("") { "%02x".format(it) }
    }
}

// Atualizar estados de autenticação
sealed class AuthState {
    object Unauthenticated : AuthState()
    data class Authenticated(val user: User) : AuthState()
    object Registered : AuthState()
    object PasswordResetRequested : AuthState()
    data class Error(val message: String) : AuthState()
}


// Corrigir factory method
class AuthViewModelFactory(
    private val database: AppDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(userDao = database.userDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
