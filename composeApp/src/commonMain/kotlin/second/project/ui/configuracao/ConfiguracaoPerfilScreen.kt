package second.project.ui.configuracao

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import second.project.model.UserRole
import second.project.model.UsuarioPerfil
import second.project.model.Veiculo
import second.project.ui.components.CrudDesign
import second.project.ui.components.ScreenHeader
import second.project.ui.components.crudOutlinedTextFieldColors
import second.project.viewmodel.PerfilViewModel

@Composable
fun ConfiguracaoPerfilScreen(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    userRole: UserRole,
    perfilViewModel: PerfilViewModel,
    onBack: () -> Unit
) {
    var senhaAtual by remember { mutableStateOf("") }
    var novaSenha by remember { mutableStateOf("") }
    var confirmarSenha by remember { mutableStateOf("") }
    var mensagemSenha by remember { mutableStateOf("") }

    LaunchedEffect(userRole) {
        if (userRole == UserRole.ADMIN) {
            perfilViewModel.carregarResumoCondominio()
        } else {
            perfilViewModel.carregar()
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(CrudDesign.page)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ScreenHeader(
            title = "Ajuste e Perfil",
            subtitle = if (userRole == UserRole.ADMIN) "Ajuste preferencias administrativas." else "Consulte seus dados e altere sua senha.",
            onBack = onBack
        )

        if (userRole == UserRole.ADMIN) {
            CondominioSettingsCard(
                totalApartamentos = perfilViewModel.totalApartamentos,
                onTotalApartamentosChange = { value ->
                    perfilViewModel.totalApartamentos = value.filter { it.isDigit() }
                },
                totalMoradores = perfilViewModel.totalMoradores,
                carregando = perfilViewModel.carregando,
                mensagem = perfilViewModel.mensagemConfiguracao.ifBlank { perfilViewModel.mensagemErro },
                onSalvar = perfilViewModel::salvarTotalApartamentos,
                modifier = Modifier.fillMaxWidth()
            )

            PasswordAndPreferencesCard(
                title = "Perfil do Administrador",
                description = "Responsavel por cadastros, documentos, encomendas e aprovacoes.",
                senhaAtual = senhaAtual,
                onSenhaAtualChange = { senhaAtual = it },
                novaSenha = novaSenha,
                onNovaSenhaChange = { novaSenha = it },
                confirmarSenha = confirmarSenha,
                onConfirmarSenhaChange = { confirmarSenha = it },
                mensagemSenha = mensagemSenha,
                onAlterarSenha = {
                    mensagemSenha = validarSenha(senhaAtual, novaSenha, confirmarSenha) {
                        senhaAtual = ""
                        novaSenha = ""
                        confirmarSenha = ""
                    }
                },
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
                onBack = onBack,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            BoxWithConstraints {
                val wide = maxWidth >= 760.dp
                if (wide) {
                    Row(horizontalArrangement = Arrangement.spacedBy(14.dp), modifier = Modifier.fillMaxWidth()) {
                        MoradorProfileCard(
                            perfil = perfilViewModel.perfil,
                            veiculo = perfilViewModel.veiculo,
                            carregando = perfilViewModel.carregando,
                            mensagemErro = perfilViewModel.mensagemErro,
                            modifier = Modifier.weight(1f)
                        )
                        PasswordAndPreferencesCard(
                            title = "Troca de Senha",
                            description = "Altere a senha padrao recebida pela administracao.",
                            senhaAtual = senhaAtual,
                            onSenhaAtualChange = { senhaAtual = it },
                            novaSenha = novaSenha,
                            onNovaSenhaChange = { novaSenha = it },
                            confirmarSenha = confirmarSenha,
                            onConfirmarSenhaChange = { confirmarSenha = it },
                            mensagemSenha = mensagemSenha,
                            onAlterarSenha = {
                                mensagemSenha = validarSenha(senhaAtual, novaSenha, confirmarSenha) {
                                    senhaAtual = ""
                                    novaSenha = ""
                                    confirmarSenha = ""
                                }
                            },
                            isDarkTheme = isDarkTheme,
                            onToggleTheme = onToggleTheme,
                            onBack = onBack,
                            modifier = Modifier.weight(1f)
                        )
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp), modifier = Modifier.fillMaxWidth()) {
                        MoradorProfileCard(
                            perfil = perfilViewModel.perfil,
                            veiculo = perfilViewModel.veiculo,
                            carregando = perfilViewModel.carregando,
                            mensagemErro = perfilViewModel.mensagemErro,
                            modifier = Modifier.fillMaxWidth()
                        )
                        PasswordAndPreferencesCard(
                            title = "Troca de Senha",
                            description = "Altere a senha padrao recebida pela administracao.",
                            senhaAtual = senhaAtual,
                            onSenhaAtualChange = { senhaAtual = it },
                            novaSenha = novaSenha,
                            onNovaSenhaChange = { novaSenha = it },
                            confirmarSenha = confirmarSenha,
                            onConfirmarSenhaChange = { confirmarSenha = it },
                            mensagemSenha = mensagemSenha,
                            onAlterarSenha = {
                                mensagemSenha = validarSenha(senhaAtual, novaSenha, confirmarSenha) {
                                    senhaAtual = ""
                                    novaSenha = ""
                                    confirmarSenha = ""
                                }
                            },
                            isDarkTheme = isDarkTheme,
                            onToggleTheme = onToggleTheme,
                            onBack = onBack,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CondominioSettingsCard(
    totalApartamentos: String,
    onTotalApartamentosChange: (String) -> Unit,
    totalMoradores: Int,
    carregando: Boolean,
    mensagem: String,
    onSalvar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = CrudDesign.surface),
        shape = CrudDesign.cardShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Dados do Condominio", color = CrudDesign.textPrimary, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(
                "Defina a quantidade total de apartamentos exibida na tela inicial. O total de residentes e calculado pelos moradores cadastrados.",
                color = CrudDesign.textSecondary,
                style = MaterialTheme.typography.bodySmall
            )

            OutlinedTextField(
                value = totalApartamentos,
                onValueChange = onTotalApartamentosChange,
                label = { Text("Quantidade de apartamentos") },
                modifier = Modifier.fillMaxWidth(),
                colors = crudOutlinedTextFieldColors(),
                singleLine = true
            )

            ProfileLine("Residentes cadastrados", totalMoradores.toString())

            Button(
                onClick = onSalvar,
                enabled = !carregando,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = CrudDesign.primary)
            ) {
                Text(if (carregando) "SALVANDO..." else "SALVAR APARTAMENTOS")
            }

            if (mensagem.isNotBlank()) {
                Text(mensagem, color = CrudDesign.textSecondary, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

private fun validarSenha(
    senhaAtual: String,
    novaSenha: String,
    confirmarSenha: String,
    onSuccess: () -> Unit
): String {
    return when {
        senhaAtual.isBlank() || novaSenha.isBlank() -> "Preencha a senha atual e a nova senha."
        novaSenha != confirmarSenha -> "A confirmacao nao confere."
        else -> {
            onSuccess()
            "Senha alterada neste dispositivo."
        }
    }
}

@Composable
private fun MoradorProfileCard(
    perfil: UsuarioPerfil?,
    veiculo: Veiculo?,
    carregando: Boolean,
    mensagemErro: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = CrudDesign.surface),
        shape = CrudDesign.cardShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Dados do Morador", color = CrudDesign.textPrimary, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            if (carregando) {
                Text("Carregando dados do Firebase...", color = CrudDesign.textSecondary, style = MaterialTheme.typography.bodySmall)
            }
            if (mensagemErro.isNotBlank()) {
                Text(mensagemErro, color = CrudDesign.danger, style = MaterialTheme.typography.bodySmall)
            }

            ProfileLine("Nome", perfil?.nome.orEmpty().fallback("Nao informado"))
            ProfileLine("Bloco", perfil?.bloco.orEmpty().fallback("Nao informado"))
            ProfileLine("Apartamento", perfil?.apartamento.orEmpty().fallback("Nao informado"))
            ProfileLine("E-mail", perfil?.email.orEmpty().fallback("Nao informado"))
            ProfileLine("Telefone", perfil?.telefone.orEmpty().fallback("Nao informado"))

            Spacer(Modifier.height(6.dp))
            Text("Veiculo", color = CrudDesign.textPrimary, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            ProfileLine("Modelo", veiculo?.modelo.orEmpty().fallback("Nenhum veiculo vinculado"))
            ProfileLine("Placa", veiculo?.placa.orEmpty().fallback("Nao informado"))
            ProfileLine("Cor", veiculo?.cor.orEmpty().fallback("Nao informado"))
        }
    }
}

private fun String.fallback(value: String): String {
    return if (isBlank()) value else this
}

@Composable
private fun ProfileLine(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(label, color = CrudDesign.textSecondary, style = MaterialTheme.typography.bodySmall)
        Text(value, color = CrudDesign.textPrimary, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun PasswordAndPreferencesCard(
    title: String,
    description: String,
    senhaAtual: String,
    onSenhaAtualChange: (String) -> Unit,
    novaSenha: String,
    onNovaSenhaChange: (String) -> Unit,
    confirmarSenha: String,
    onConfirmarSenhaChange: (String) -> Unit,
    mensagemSenha: String,
    onAlterarSenha: () -> Unit,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = CrudDesign.surface),
        shape = CrudDesign.cardShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text(title, color = CrudDesign.textPrimary, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(description, color = CrudDesign.textSecondary, style = MaterialTheme.typography.bodySmall)

            OutlinedTextField(
                value = senhaAtual,
                onValueChange = onSenhaAtualChange,
                label = { Text("Senha atual") },
                modifier = Modifier.fillMaxWidth(),
                colors = crudOutlinedTextFieldColors(),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )

            OutlinedTextField(
                value = novaSenha,
                onValueChange = onNovaSenhaChange,
                label = { Text("Nova senha") },
                modifier = Modifier.fillMaxWidth(),
                colors = crudOutlinedTextFieldColors(),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )

            OutlinedTextField(
                value = confirmarSenha,
                onValueChange = onConfirmarSenhaChange,
                label = { Text("Confirmar nova senha") },
                modifier = Modifier.fillMaxWidth(),
                colors = crudOutlinedTextFieldColors(),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )

            Button(
                onClick = onAlterarSenha,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = CrudDesign.primary)
            ) {
                Text("ALTERAR SENHA")
            }

            if (mensagemSenha.isNotBlank()) {
                Text(mensagemSenha, color = CrudDesign.textSecondary, style = MaterialTheme.typography.bodySmall)
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Modo escuro", color = CrudDesign.textPrimary)
                Switch(checked = isDarkTheme, onCheckedChange = { onToggleTheme() })
            }

            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = CrudDesign.primary)
            ) {
                Text("VOLTAR")
            }
        }
    }
}
