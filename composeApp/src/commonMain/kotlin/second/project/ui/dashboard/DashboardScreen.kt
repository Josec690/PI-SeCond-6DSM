package second.project.ui.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
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
    onNavReservas: () -> Unit,
    onNavPrestadores: () -> Unit,
    onNavDocumentos: () -> Unit,
    onNavConfiguracao: () -> Unit,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onLogout: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = androidx.compose.material3.DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    fun openDrawer() { scope.launch { drawerState.open() } }
    fun navigateAndClose(action: () -> Unit) { scope.launch { drawerState.close(); action() } }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = CrudDesign.surface.copy(alpha = 0.98f),
                drawerContentColor = CrudDesign.textPrimary
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("Menu", color = CrudDesign.textPrimary, fontSize = 24.sp, fontWeight = FontWeight.Black)
                    Text("Acesse os módulos e saia quando terminar.", color = CrudDesign.textSecondary, fontSize = 12.sp)
                    HorizontalDivider(color = CrudDesign.primary.copy(alpha = 0.18f))
                    Spacer(modifier = Modifier.height(4.dp))

                    DrawerItem("Veículos", Icons.Default.DirectionsCar) { navigateAndClose(onNavVeiculos) }
                    DrawerItem("Convidados", Icons.Default.Group) { navigateAndClose(onNavConvidados) }
                    DrawerItem("Encomendas", Icons.Default.LocalShipping) { navigateAndClose(onNavEncomendas) }
                    DrawerItem("Avisos", Icons.Default.Notifications) { navigateAndClose(onNavAvisos) }
                    DrawerItem("Reservas", Icons.Default.Event) { navigateAndClose(onNavReservas) }
                    DrawerItem("Prestadores", Icons.Default.Build) { navigateAndClose(onNavPrestadores) }
                    DrawerItem("Documentos", Icons.Default.Description) { navigateAndClose(onNavDocumentos) }
                    DrawerItem("Perfil/Configuração", Icons.Default.Settings) { navigateAndClose(onNavConfiguracao) }

                    Spacer(modifier = Modifier.height(8.dp))

                    androidx.compose.foundation.layout.Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Modo Escuro", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = CrudDesign.textPrimary)
                        Switch(
                            checked = isDarkTheme,
                            onCheckedChange = { navigateAndClose(onToggleTheme) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = CrudDesign.primary,
                                checkedTrackColor = CrudDesign.primary.copy(alpha = 0.5f),
                                uncheckedThumbColor = CrudDesign.textSecondary,
                                uncheckedTrackColor = CrudDesign.primary.copy(alpha = 0.18f)
                            )
                        )
                    }

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
                    title = { Text("SeCond", fontWeight = FontWeight.Black, fontSize = 24.sp, letterSpacing = 1.sp, color = CrudDesign.textPrimary) },
                    navigationIcon = {
                        IconButton(onClick = { openDrawer() }) {
                            Icon(Icons.Default.Menu, contentDescription = "Abrir menu", tint = CrudDesign.textPrimary, modifier = Modifier.size(28.dp))
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = CrudDesign.page.copy(alpha = 0.96f),
                        titleContentColor = CrudDesign.textPrimary
                    )
                )
            },
            containerColor = CrudDesign.page
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(CrudDesign.page)
            ) {
                Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                    Spacer(modifier = Modifier.height(12.dp))

                    HeroCard()

                    Spacer(modifier = Modifier.height(22.dp))
                    SectionTitle("Serviços e Gestão")
                    Spacer(modifier = Modifier.height(12.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.heightIn(min = 560.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            listOf(
                                QuickTile("Convidados", "Controle de Acesso", Icons.Default.Group, onNavConvidados),
                                QuickTile("Encomendas", "Entregas do dia", Icons.Default.LocalShipping, onNavEncomendas),
                                QuickTile("Prestadores", "Equipe de Serviço", Icons.Default.Build, onNavPrestadores),
                                QuickTile("Veículos", "Entrada Garagem", Icons.Default.DirectionsCar, onNavVeiculos),
                                QuickTile("Mural", "Comunicados", Icons.Default.Notifications, onNavAvisos),
                                QuickTile("Documentos", "Regimento e arquivos", Icons.Default.Description, onNavDocumentos),
                                QuickTile("Reservas", "Reservar espaços", Icons.Default.Event, onNavReservas),
                                QuickTile("Ajustes", "Configurações", Icons.Default.Settings, onNavConfiguracao)
                            )
                        ) { tile ->
                            GlassTile(tile)
                        }
                    }

                    Spacer(modifier = Modifier.height(22.dp))
                    SectionTitle("Destaques do Condomínio")
                    Spacer(modifier = Modifier.height(12.dp))

                    androidx.compose.foundation.layout.Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        FeaturedBanner(modifier = Modifier.weight(1.6f))
                        ProfilePanel(modifier = Modifier.weight(0.9f))
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
private fun DrawerItem(label: String, icon: ImageVector, onClick: () -> Unit) {
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
        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)
    )
}

@Composable
private fun HeroCard() {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(Color(0xFF4A3B8B), Color(0xFF251D4D))))
                .padding(22.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Bem-vindo ao SeCond", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Black)
                Text("Seu portal para uma vida elevada", color = Color(0xFFE0DBFF), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Gestão Segura e Inteligente de Condomínios", color = Color(0xFFC9BEFF), fontSize = 11.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.6.sp)
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    androidx.compose.foundation.layout.Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(width = 8.dp, height = 24.dp)
                .clip(RoundedCornerShape(999.dp))
                .background(CrudDesign.primary)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(title, color = CrudDesign.textPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

private data class QuickTile(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

@Composable
private fun GlassTile(tile: QuickTile) {
    Card(
        modifier = Modifier
            .height(160.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable { tile.onClick() },
        colors = CardDefaults.cardColors(containerColor = CrudDesign.surface.copy(alpha = 0.92f)),
        border = BorderStroke(1.dp, CrudDesign.primary.copy(alpha = 0.20f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(CrudDesign.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(tile.icon, contentDescription = tile.title, tint = Color.White, modifier = Modifier.size(30.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(tile.title, color = CrudDesign.textPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                tile.subtitle,
                color = CrudDesign.textSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.5.sp,
                textAlign = TextAlign.Center,
                maxLines = 2,
                lineHeight = 11.sp
            )
        }
    }
}

@Composable
private fun FeaturedBanner(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(180.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(listOf(Color(0xFF4A3B8B), Color(0xFF20143C), Color(0xFF0E0B18))))
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.align(Alignment.BottomStart),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Acesso à Piscina e Lounge", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                Text("Áreas comuns abertas até as 22h hoje.", color = Color(0xFFE0DBFF), fontSize = 13.sp)
                Box(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(Color.White.copy(alpha = 0.12f))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text("ÁREAS COMUNS", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp)
                }
            }
        }
    }
}

@Composable
private fun ProfilePanel(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(180.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CrudDesign.surface.copy(alpha = 0.95f)),
        border = BorderStroke(1.dp, CrudDesign.primary.copy(alpha = 0.18f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(18.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Meu Perfil", color = CrudDesign.textPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("Unidade 1204 - Torre A", color = CrudDesign.textSecondary, fontSize = 12.sp)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(999.dp))
                    .background(CrudDesign.primary)
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Editar Perfil", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

