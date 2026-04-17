package second.project.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import second.project.ui.components.CrudDesign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavVeiculos: () -> Unit,
    onNavConvidados: () -> Unit,
    onNavEncomendas: () -> Unit,
    onNavAvisos: () -> Unit,
    onLogout: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = androidx.compose.material3.DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    fun openDrawer() {
        scope.launch { drawerState.open() }
    }

    fun navigateAndClose(action: () -> Unit) {
        scope.launch {
            drawerState.close()
            action()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = CrudDesign.surface,
                drawerContentColor = CrudDesign.textPrimary
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Menu",
                        color = CrudDesign.textPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Text(
                        text = "Acesse os módulos e saia quando terminar.",
                        color = CrudDesign.textSecondary,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    HorizontalDivider(color = CrudDesign.primary.copy(alpha = 0.18f))

                    Spacer(modifier = Modifier.height(6.dp))

                    DrawerItem("Veículos", Icons.Default.DirectionsCar) { navigateAndClose(onNavVeiculos) }
                    DrawerItem("Convidados", Icons.Default.Group) { navigateAndClose(onNavConvidados) }
                    DrawerItem("Encomendas", Icons.Default.LocalShipping) { navigateAndClose(onNavEncomendas) }
                    DrawerItem("Avisos", Icons.Default.Notifications) { navigateAndClose(onNavAvisos) }

                    Spacer(modifier = Modifier.weight(1f))

                    HorizontalDivider(color = CrudDesign.primary.copy(alpha = 0.18f))

                    DrawerItem("Sair", Icons.AutoMirrored.Filled.Logout) { navigateAndClose(onLogout) }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("SeCond", fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, letterSpacing = 1.sp, color = CrudDesign.textPrimary)
                    },
                    navigationIcon = {
                        IconButton(onClick = { openDrawer() }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Abrir menu",
                                tint = CrudDesign.textPrimary,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = CrudDesign.surfaceAlt,
                        titleContentColor = CrudDesign.textPrimary
                    )
                )
            },
            containerColor = CrudDesign.page
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
                    colors = CardDefaults.cardColors(containerColor = CrudDesign.primary),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
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

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ServiceCard(
                        titulo = "Encomendas",
                        icone = Icons.Default.LocalShipping,
                        onClick = onNavEncomendas,
                        modifier = Modifier.weight(1f)
                    )
                    ServiceCard(
                        titulo = "Avisos",
                        icone = Icons.Default.Notifications,
                        onClick = onNavAvisos,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun DrawerItem(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = { Text(label, fontSize = 15.sp, fontWeight = FontWeight.SemiBold) },
        selected = false,
        onClick = onClick,
        icon = { Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(28.dp)) },
        colors = NavigationDrawerItemDefaults.colors(
            unselectedContainerColor = CrudDesign.surface,
            unselectedTextColor = CrudDesign.textPrimary,
            unselectedIconColor = CrudDesign.primary,
            selectedContainerColor = CrudDesign.primary.copy(alpha = 0.18f),
            selectedTextColor = CrudDesign.textPrimary,
            selectedIconColor = CrudDesign.primary
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
    )
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
        colors = CardDefaults.cardColors(containerColor = CrudDesign.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
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
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = titulo,
                color = CrudDesign.textPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
}