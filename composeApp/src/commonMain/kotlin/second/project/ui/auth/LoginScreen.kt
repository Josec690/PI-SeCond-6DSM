package second.project.ui.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import second.composeapp.generated.resources.Logo_SeCond_Dark_1
import second.composeapp.generated.resources.Res
import second.project.getPlatform
import second.project.model.UserRole
import second.project.preferences.PreferencesManager
import second.project.ui.components.CrudDesign
import second.project.ui.components.crudOutlinedTextFieldColorsM3

@Composable
fun LoginScreen(onLogin: () -> Unit, onNavigateToCadastro: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var senhaVisivel by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf(UserRole.MORADOR) }
    var errorMessage by remember { mutableStateOf("") }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(CrudDesign.page)
    ) {
        val desktopLayout = maxWidth >= 900.dp

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CrudDesign.page)
        ) {
            DecorativeBackground()

            if (desktopLayout) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BrandPanel(
                        modifier = Modifier.weight(1f),
                        title = "Seu Portal para uma\nVida Elevada.",
                        subtitle = "Experimente uma gestão comunitária perfeita com o toque de um concierge digital."
                    )
                    LoginPanel(
                        modifier = Modifier.weight(1f),
                        email = email,
                        onEmailChange = { email = it },
                        senha = senha,
                        onSenhaChange = { senha = it },
                        senhaVisivel = senhaVisivel,
                        onToggleSenha = { senhaVisivel = !senhaVisivel },
                        selectedRole = selectedRole,
                        onSelectRole = { selectedRole = it },
                        errorMessage = errorMessage,
                        onLogin = {
                            val platformName = getPlatform().name.lowercase()
                            if (selectedRole == UserRole.ADMIN && platformName.contains("android")) {
                                errorMessage = "Contas de administrador só podem acessar via website/desktop."
                                return@LoginPanel
                            }
                            PreferencesManager.setUserRole(selectedRole.value)
                            errorMessage = ""
                            onLogin()
                        },
                        onNavigateToCadastro = onNavigateToCadastro
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    BrandPanel(
                        modifier = Modifier.fillMaxWidth(),
                        title = "SeCond",
                        subtitle = "Gestão Segura e Inteligente de Condomínios"
                    )

                    LoginPanel(
                        modifier = Modifier.fillMaxWidth(),
                        email = email,
                        onEmailChange = { email = it },
                        senha = senha,
                        onSenhaChange = { senha = it },
                        senhaVisivel = senhaVisivel,
                        onToggleSenha = { senhaVisivel = !senhaVisivel },
                        selectedRole = selectedRole,
                        onSelectRole = { selectedRole = it },
                        errorMessage = errorMessage,
                        onLogin = {
                            val platformName = getPlatform().name.lowercase()
                            if (selectedRole == UserRole.ADMIN && platformName.contains("android")) {
                                errorMessage = "Contas de administrador só podem acessar via website/desktop."
                                return@LoginPanel
                            }
                            PreferencesManager.setUserRole(selectedRole.value)
                            errorMessage = ""
                            onLogin()
                        },
                        onNavigateToCadastro = onNavigateToCadastro
                    )
                }
            }
        }
    }
}

@Composable
private fun BrandPanel(modifier: Modifier = Modifier, title: String, subtitle: String) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, CrudDesign.primary.copy(alpha = 0.18f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF4A3B8B), Color(0xFF251D4D), Color(0xFF0E0B18))
                    )
                )
                .padding(28.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.14f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.Logo_SeCond_Dark_1),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Text("SeCond", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Black)
                }

                Text(title, color = Color.White, fontSize = 34.sp, fontWeight = FontWeight.Black, lineHeight = 38.sp)
                Text(subtitle, color = Color(0xFFE0DBFF), fontSize = 14.sp, fontWeight = FontWeight.Medium)

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatChip(label = "Residentes", value = "2.5k+")
                    StatChip(label = "Propriedades", value = "48")
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(180.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.05f))
            )
        }
    }
}

@Composable
private fun StatChip(label: String, value: String) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.10f)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.12f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
            Text(value, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Black)
            Text(label, color = Color.White.copy(alpha = 0.65f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun LoginPanel(
    modifier: Modifier = Modifier,
    email: String,
    onEmailChange: (String) -> Unit,
    senha: String,
    onSenhaChange: (String) -> Unit,
    senhaVisivel: Boolean,
    onToggleSenha: () -> Unit,
    selectedRole: UserRole,
    onSelectRole: (UserRole) -> Unit,
    errorMessage: String,
    onLogin: () -> Unit,
    onNavigateToCadastro: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = CrudDesign.surface.copy(alpha = 0.95f)),
        border = BorderStroke(1.dp, CrudDesign.primary.copy(alpha = 0.16f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("Bem-vindo", color = CrudDesign.textPrimary, fontSize = 28.sp, fontWeight = FontWeight.Black)
            Text("Entre com sua conta para continuar", color = CrudDesign.textSecondary, fontSize = 13.sp)

            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                label = { Text("E-mail") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = CrudDesign.primary) },
                modifier = Modifier.fillMaxWidth().height(58.dp),
                shape = CrudDesign.fieldShape,
                colors = crudOutlinedTextFieldColorsM3(),
                singleLine = true
            )

            OutlinedTextField(
                value = senha,
                onValueChange = onSenhaChange,
                label = { Text("Senha") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = CrudDesign.primary) },
                trailingIcon = {
                    Icon(
                        if (senhaVisivel) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = CrudDesign.primary,
                        modifier = Modifier.clickable { onToggleSenha() }
                    )
                },
                visualTransformation = if (senhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().height(58.dp),
                shape = CrudDesign.fieldShape,
                colors = crudOutlinedTextFieldColorsM3(),
                singleLine = true
            )

            Text("Entrar como", color = CrudDesign.textSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                RoleChip("Morador", selectedRole == UserRole.MORADOR) { onSelectRole(UserRole.MORADOR) }
                RoleChip("Administrador", selectedRole == UserRole.ADMIN) { onSelectRole(UserRole.ADMIN) }
            }

            if (errorMessage.isNotBlank()) {
                Text(errorMessage, color = CrudDesign.danger, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }

            Button(
                onClick = onLogin,
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CrudDesign.primary)
            ) {
                Text("ENTRAR", color = CrudDesign.textPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Text("Não tem conta? ", color = CrudDesign.textSecondary, fontSize = 12.sp)
                Text(
                    "Cadastre-se",
                    color = CrudDesign.textPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToCadastro() }
                )
            }
        }
    }
}

@Composable
private fun RoleChip(text: String, selected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(999.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) CrudDesign.primary.copy(alpha = 0.22f) else CrudDesign.surfaceAlt
        ),
        border = BorderStroke(1.dp, if (selected) CrudDesign.primary else CrudDesign.primary.copy(alpha = 0.18f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Text(
            text = text,
            color = CrudDesign.textPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
        )
    }
}

@Composable
private fun DecorativeBackground() {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .size(260.dp)
                .align(Alignment.TopStart)
                .background(Color(0xFF4A3B8B).copy(alpha = 0.12f), CircleShape)
        )
        Box(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.CenterEnd)
                .background(Color(0xFF4A3B8B).copy(alpha = 0.08f), CircleShape)
        )
        Box(
            modifier = Modifier
                .size(360.dp)
                .align(Alignment.BottomCenter)
                .background(Color(0xFF4A3B8B).copy(alpha = 0.05f), CircleShape)
        )
    }
}

