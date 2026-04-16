package second.project.ui.convidados

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import second.project.viewmodel.ConvidadoViewModel

@Composable
fun FormularioConvidadoScreen(viewModel: ConvidadoViewModel, onSaved: () -> Unit) {
    Column(Modifier.fillMaxSize().background(Color.Black).padding(24.dp)) {
        Text("Dados do Convidado", color = Color.White, style = MaterialTheme.typography.h5)
        Spacer(Modifier.height(24.dp))

        OutlinedTextField(value = viewModel.nome, onValueChange = { viewModel.nome = it }, label = { Text("Nome") }, modifier = Modifier.fillMaxWidth(), colors = TextFieldDefaults.outlinedTextFieldColors(textColor = Color.White))
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = viewModel.telefone, onValueChange = { viewModel.telefone = it }, label = { Text("Telefone") }, modifier = Modifier.fillMaxWidth(), colors = TextFieldDefaults.outlinedTextFieldColors(textColor = Color.White))
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = viewModel.email, onValueChange = { viewModel.email = it }, label = { Text("E-mail") }, modifier = Modifier.fillMaxWidth(), colors = TextFieldDefaults.outlinedTextFieldColors(textColor = Color.White))
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = viewModel.localAlugado, onValueChange = { viewModel.localAlugado = it }, label = { Text("Local Alugado") }, modifier = Modifier.fillMaxWidth(), colors = TextFieldDefaults.outlinedTextFieldColors(textColor = Color.White))

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = { viewModel.gravar(); onSaved() },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF4A3B8B))
        ) {
            Text("SALVAR CONVIDADO", color = Color.White)
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