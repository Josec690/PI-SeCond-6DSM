package second.project.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SimpleCalendarDialog(
    onDismiss: () -> Unit,
    onDateSelected: (String) -> Unit
) {
    var mes by remember { mutableStateOf("01") }
    var ano by remember { mutableStateOf("2026") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Selecionar data") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = mes,
                        onValueChange = { mes = it.filter(Char::isDigit).take(2) },
                        label = { Text("Mes") },
                        modifier = Modifier.weight(1f),
                        colors = crudOutlinedTextFieldColors(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = ano,
                        onValueChange = { ano = it.filter(Char::isDigit).take(4) },
                        label = { Text("Ano") },
                        modifier = Modifier.weight(1f),
                        colors = crudOutlinedTextFieldColors(),
                        singleLine = true
                    )
                }

                (1..31).chunked(7).forEach { semana ->
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        semana.forEach { dia ->
                            Button(
                                onClick = {
                                    val diaFormatado = dia.toString().padStart(2, '0')
                                    val mesFormatado = mes.padStart(2, '0').takeLast(2)
                                    val anoFormatado = ano.padStart(4, '0').takeLast(4)
                                    onDateSelected("$diaFormatado/$mesFormatado/$anoFormatado")
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = CrudDesign.surfaceAlt)
                            ) {
                                Text(dia.toString(), color = CrudDesign.textPrimary)
                            }
                        }
                        repeat(7 - semana.size) {
                            Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("FECHAR")
            }
        }
    )
}
