package second.project.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object CrudDesign {
    val page = Color(0xFF0F0F14)
    val surface = Color(0xFF171723)
    val surfaceAlt = Color(0xFF1F1F2D)
    val primary = Color(0xFF4A3B8B)
    val textPrimary = Color(0xFFF5F2FF)
    val textSecondary = Color(0xFFB39DDB)
    val danger = Color(0xFFCF6679)

    val cardShape = RoundedCornerShape(16.dp)
    val fieldShape = RoundedCornerShape(12.dp)
}

@Composable
fun crudOutlinedTextFieldColors() = TextFieldDefaults.outlinedTextFieldColors(
    textColor = CrudDesign.textPrimary,
    cursorColor = CrudDesign.textPrimary,
    focusedLabelColor = CrudDesign.textSecondary,
    unfocusedLabelColor = CrudDesign.textSecondary.copy(alpha = 0.85f),
    focusedBorderColor = CrudDesign.primary,
    unfocusedBorderColor = CrudDesign.primary.copy(alpha = 0.35f),
    backgroundColor = CrudDesign.primary.copy(alpha = 0.08f)
)

