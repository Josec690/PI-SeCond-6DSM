package second.project.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(onLogin: () -> Unit, onNavigateToCadastro: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    Column(
        Modifier.fillMaxSize().background(Color.Black).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("SeCond", color = Color(0xFF4A3B8B), style = MaterialTheme.typography.h3, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text("Gestão Inteligente", color = Color.Gray, style = MaterialTheme.typography.subtitle1)

        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = email, onValueChange = { email = it },
            label = { Text("E-mail") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(textColor = Color.White, focusedBorderColor = Color(0xFF4A3B8B))
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = senha, onValueChange = { senha = it },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(textColor = Color.White, focusedBorderColor = Color(0xFF4A3B8B))
        )

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = onLogin,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF4A3B8B))
        ) {
            Text("ENTRAR", color = Color.White)
        }

        TextButton(onClick = onNavigateToCadastro) {
            Text("Não tem conta? Cadastre-se", color = Color.Gray)
        }
    }
}