package second.project.ui.veiculos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import second.project.viewmodel.VeiculoViewModel

@Composable
fun FormularioVeiculoScreen(viewModel: VeiculoViewModel, onSaved: () -> Unit) {
    Column(Modifier.fillMaxSize().background(Color.Black).padding(24.dp)) {
        Text("Gerenciar Veículo", color = Color.White, style = MaterialTheme.typography.h5)
        Spacer(Modifier.height(24.dp))

        OutlinedTextField(value = viewModel.placa, onValueChange = { viewModel.placa = it }, label = { Text("Placa") }, modifier = Modifier.fillMaxWidth(), colors = TextFieldDefaults.outlinedTextFieldColors(textColor = Color.White))
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = viewModel.modelo, onValueChange = { viewModel.modelo = it }, label = { Text("Modelo") }, modifier = Modifier.fillMaxWidth(), colors = TextFieldDefaults.outlinedTextFieldColors(textColor = Color.White))
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = viewModel.cor, onValueChange = { viewModel.cor = it }, label = { Text("Cor") }, modifier = Modifier.fillMaxWidth(), colors = TextFieldDefaults.outlinedTextFieldColors(textColor = Color.White))
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = viewModel.proprietario, onValueChange = { viewModel.proprietario = it }, label = { Text("Proprietário") }, modifier = Modifier.fillMaxWidth(), colors = TextFieldDefaults.outlinedTextFieldColors(textColor = Color.White))

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = { viewModel.gravar(); onSaved() },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF4A3B8B))
        ) {
            Text("SALVAR", color = Color.White)
        }

        Spacer(Modifier.height(12.dp))

        OutlinedButton(
            onClick = { viewModel.limparCampos() },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("LIMPAR CAMPOS", color = Color.White)
        }
    }
}