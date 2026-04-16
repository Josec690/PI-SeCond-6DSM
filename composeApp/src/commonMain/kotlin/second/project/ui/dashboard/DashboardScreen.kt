package second.project.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Group
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
                    Text("SeCond", fontWeight = FontWeight.Bold, fontSize = 22.sp)
                },
                backgroundColor = Color(0xFF121212),
                contentColor = Color.White,
                elevation = 10.dp
            )
        },
        backgroundColor = Color.Black
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Bem-vindo ao SeCond",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            LazyVerticalGrid(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // CARD DE VEÍCULOS
                item {
                    MenuCard(
                        titulo = "Veículos",
                        icone = Icons.Default.DirectionsCar,
                        onClick = onNavVeiculos
                    )
                }

                // CARD DE CONVIDADOS
                item {
                    MenuCard(
                        titulo = "Convidados",
                        icone = Icons.Default.Group,
                        onClick = onNavConvidados
                    )
                }
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ir para Login",
                    color = Color(0xFFB39DDB),
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { onNavLogin() }
                )
                Text(
                    text = "Ir para Cadastro",
                    color = Color(0xFFB39DDB),
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { onNavCadastro() }
                )
            }
        }
    }
}

@Composable
fun MenuCard(titulo: String, icone: ImageVector, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable { onClick() },
        backgroundColor = Color(0xFF1E1E1E),
        shape = MaterialTheme.shapes.medium,
        elevation = 6.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = icone,
                contentDescription = titulo,
                tint = Color(0xFF4A3B8B), // O Roxo da sua marca
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = titulo,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
        }
    }
}