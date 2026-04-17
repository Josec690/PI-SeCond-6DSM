package second.project.ui.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import second.composeapp.generated.resources.Logo_SeCond_Dark_1
import second.composeapp.generated.resources.Res
import second.project.ui.components.CrudDesign
import second.project.ui.components.crudOutlinedTextFieldColorsM3

@Composable
fun CadastroScreen(onRegisterSuccess: () -> Unit, onBackToLogin: () -> Unit) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmarSenha by remember { mutableStateOf("") }
    var senhaVisivel by remember { mutableStateOf(false) }
    var confirmarVisivel by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .background(CrudDesign.page)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo
        Image(
            painter = painterResource(Res.drawable.Logo_SeCond_Dark_1),
            contentDescription = "Logo SeCond",
            modifier = Modifier
                .fillMaxWidth(0.72f)
                .height(96.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Criar Conta",
            color = CrudDesign.textSecondary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(Modifier.height(32.dp))

        // Card de Cadastro
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = CrudDesign.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Nome
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome Completo") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = CrudDesign.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = CrudDesign.fieldShape,
                    colors = crudOutlinedTextFieldColorsM3(),
                    singleLine = true
                )
                Spacer(Modifier.height(12.dp))

                // Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("E-mail") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Email,
                            contentDescription = null,
                            tint = CrudDesign.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = CrudDesign.fieldShape,
                    colors = crudOutlinedTextFieldColorsM3(),
                    singleLine = true
                )
                Spacer(Modifier.height(12.dp))

                // Senha
                OutlinedTextField(
                    value = senha,
                    onValueChange = { senha = it },
                    label = { Text("Senha") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null,
                            tint = CrudDesign.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        Icon(
                            if (senhaVisivel) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = CrudDesign.primary,
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { senhaVisivel = !senhaVisivel }
                        )
                    },
                    visualTransformation = if (senhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = CrudDesign.fieldShape,
                    colors = crudOutlinedTextFieldColorsM3(),
                    singleLine = true
                )
                Spacer(Modifier.height(12.dp))

                // Confirmar Senha
                OutlinedTextField(
                    value = confirmarSenha,
                    onValueChange = { confirmarSenha = it },
                    label = { Text("Confirmar Senha") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null,
                            tint = CrudDesign.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        Icon(
                            if (confirmarVisivel) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = CrudDesign.primary,
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { confirmarVisivel = !confirmarVisivel }
                        )
                    },
                    visualTransformation = if (confirmarVisivel) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = CrudDesign.fieldShape,
                    colors = crudOutlinedTextFieldColorsM3(),
                    singleLine = true
                )

                Spacer(Modifier.height(24.dp))

                // Botão Cadastrar
                Button(
                    onClick = {
                        if (senha.isNotBlank() && senha == confirmarSenha) {
                            onRegisterSuccess()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(CrudDesign.fieldShape),
                    colors = ButtonDefaults.buttonColors(containerColor = CrudDesign.primary)
                ) {
                    Text(
                        "CADASTRAR",
                        color = CrudDesign.textPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(Modifier.height(12.dp))

                // Botão Voltar
                OutlinedButton(
                    onClick = onBackToLogin,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(CrudDesign.fieldShape),
                    border = BorderStroke(1.5.dp, CrudDesign.primary)
                ) {
                    Text(
                        "VOLTAR PARA LOGIN",
                        color = CrudDesign.textSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}