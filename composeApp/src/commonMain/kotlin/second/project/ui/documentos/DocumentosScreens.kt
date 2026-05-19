package second.project.ui.documentos

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import second.project.ui.components.CrudDesign
import second.project.ui.components.ScreenHeader
import second.project.ui.components.crudOutlinedTextFieldColors
import second.project.viewmodel.DocumentoViewModel

@Composable
fun ListaDocumentosScreen(viewModel: DocumentoViewModel, onAddClick: () -> Unit, onBack: () -> Unit) {
    LaunchedEffect(Unit) { viewModel.carregar() }

    val total = viewModel.listaDocumentos.size

    Scaffold(
        containerColor = CrudDesign.page,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.limparCampos()
                    onAddClick()
                },
                containerColor = CrudDesign.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = CrudDesign.textPrimary)
            }
        }
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            ScreenHeader(
                title = "Documentos",
                subtitle = "Acesso seguro e organizado aos documentos essenciais e registros legais da sua residência.",
                onBack = onBack
            )

            Spacer(Modifier.height(14.dp))

            DocumentsHero()

            Spacer(Modifier.height(14.dp))

            Text("Categorias", color = CrudDesign.textPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("Explore as pastas do condomínio e seus arquivos recentes.", color = CrudDesign.textSecondary, fontSize = 12.sp)

            Spacer(Modifier.height(12.dp))

            SummaryBadge("Total", total.toString())

            Spacer(Modifier.height(14.dp))

            LazyColumn(
                contentPadding = PaddingValues(bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(viewModel.listaDocumentos, key = { it.id }) { documento ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = CrudDesign.surface.copy(alpha = 0.95f)),
                        shape = RoundedCornerShape(24.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CrudDesign.primary.copy(alpha = 0.18f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Description, contentDescription = null, tint = CrudDesign.primary)
                                Spacer(Modifier.size(10.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(documento.titulo, color = CrudDesign.textPrimary, fontWeight = FontWeight.Bold)
                                    Text(documento.tipo, color = CrudDesign.textSecondary)
                                }
                            }

                            Text(documento.descricao, color = CrudDesign.textSecondary)
                            if (documento.dataCadastro.isNotBlank()) {
                                Text("Cadastro: ${documento.dataCadastro}", color = CrudDesign.textSecondary)
                            }
                            if (documento.arquivoUrl.isNotBlank()) {
                                Text("Arquivo: ${documento.arquivoUrl}", color = CrudDesign.textSecondary)
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Spacer(Modifier.weight(1f))
                                IconButton(onClick = { viewModel.editar(documento); onAddClick() }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Editar documento", tint = CrudDesign.textPrimary)
                                }
                                IconButton(onClick = { viewModel.apagar(documento.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Apagar documento", tint = CrudDesign.danger)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FormularioDocumentoScreen(viewModel: DocumentoViewModel, onSaved: () -> Unit, onBack: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .background(CrudDesign.page)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        ScreenHeader(
            title = "Gestão de Documentos",
            subtitle = "Cadastre ou atualize documentos do condomínio.",
            onBack = onBack
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CrudDesign.surface.copy(alpha = 0.95f)),
            shape = RoundedCornerShape(28.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CrudDesign.primary.copy(alpha = 0.16f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(value = viewModel.titulo, onValueChange = { viewModel.titulo = it }, label = { Text("Título") }, modifier = Modifier.fillMaxWidth(), colors = crudOutlinedTextFieldColors(), singleLine = true)
                OutlinedTextField(value = viewModel.tipo, onValueChange = { viewModel.tipo = it }, label = { Text("Tipo") }, modifier = Modifier.fillMaxWidth(), colors = crudOutlinedTextFieldColors(), singleLine = true)
                OutlinedTextField(value = viewModel.descricao, onValueChange = { viewModel.descricao = it }, label = { Text("Descrição") }, modifier = Modifier.fillMaxWidth(), colors = crudOutlinedTextFieldColors(), minLines = 3)
                OutlinedTextField(value = viewModel.dataCadastro, onValueChange = { viewModel.dataCadastro = it }, label = { Text("Data de cadastro") }, modifier = Modifier.fillMaxWidth(), colors = crudOutlinedTextFieldColors(), singleLine = true)
                OutlinedTextField(value = viewModel.arquivoUrl, onValueChange = { viewModel.arquivoUrl = it }, label = { Text("URL do arquivo") }, modifier = Modifier.fillMaxWidth(), colors = crudOutlinedTextFieldColors(), singleLine = true)

                Button(
                    onClick = {
                        viewModel.gravar()
                        onSaved()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = CrudDesign.primary)
                ) {
                    Text("GRAVAR")
                }

                OutlinedButton(
                    onClick = viewModel::limparCampos,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("LIMPAR CAMPOS")
                }
            }
        }
    }
}

@Composable
private fun DocumentsHero() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(Color(0xFF4A3B8B), Color(0xFF321B84), Color(0xFF0A0A0A))))
                .padding(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Cofre Central", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text("Armazenamento de Documentos", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Black)
                Text("Acesso seguro e organizado aos documentos essenciais e registros legais.", color = Color(0xFFE0DBFF), fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun SummaryBadge(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = CrudDesign.surfaceAlt),
        shape = CrudDesign.cardShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
            Text(label, color = CrudDesign.textSecondary, style = MaterialTheme.typography.bodySmall)
            Text(value, color = CrudDesign.textPrimary, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}





