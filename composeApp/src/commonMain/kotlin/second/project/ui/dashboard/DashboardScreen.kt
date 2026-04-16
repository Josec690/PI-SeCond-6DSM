package second.project.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Group
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import second.project.ui.components.CrudDesign

@Composable
fun DashboardScreen(
    onNavVeiculos: () -> Unit,
    onNavConvidados: () -> Unit,
    onNavLogin: () -> Unit,
    onNavCadastro: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("SeCond", fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, letterSpacing = 1.sp, color = CrudDesign.textPrimary)
                },
                backgroundColor = CrudDesign.surfaceAlt,
                contentColor = CrudDesign.textPrimary,
                elevation = 8.dp
            )
        },
        backgroundColor = CrudDesign.page
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            // Greeting Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .clip(RoundedCornerShape(16.dp)),
                backgroundColor = CrudDesign.primary,
                elevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        "Bem-vindo ao SeCond",
                        color = CrudDesign.textPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Seu portal para uma vida elevada",
                        color = CrudDesign.textSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.alpha(0.9f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Serviços Grid
            Text(
                "Serviços Disponíveis",
                color = CrudDesign.textPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp,
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ServiceCard(
                    titulo = "Veículos",
                    icone = Icons.Default.DirectionsCar,
                    onClick = onNavVeiculos,
                    modifier = Modifier.weight(1f)
                )
                ServiceCard(
                    titulo = "Convidados",
                    icone = Icons.Default.Group,
                    onClick = onNavConvidados,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Bottom Links
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 50.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ir para Login",
                    color = CrudDesign.textSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { onNavLogin() }
                )
                Text(
                    text = "Ir para Cadastro",
                    color = CrudDesign.textSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { onNavCadastro() }
                )
            }
        }
    }
}

@Composable
fun ServiceCard(
    titulo: String,
    icone: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(120.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        backgroundColor = CrudDesign.surface,
        elevation = 6.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
                .background(CrudDesign.surface)
        ) {
            Icon(
                imageVector = icone,
                contentDescription = titulo,
                tint = CrudDesign.primary,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = titulo,
                color = CrudDesign.textPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}