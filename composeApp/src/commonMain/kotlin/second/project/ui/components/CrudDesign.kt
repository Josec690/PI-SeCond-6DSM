package second.project.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextFieldDefaults as OutlinedTextFieldDefaultsM3
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object CrudDesign {
    var page by mutableStateOf(Color(0xFF0F0F14))
    var surface by mutableStateOf(Color(0xFF171723))
    var surfaceAlt by mutableStateOf(Color(0xFF1F1F2D))
    var primary by mutableStateOf(Color(0xFF4A3B8B))
    var textPrimary by mutableStateOf(Color(0xFFF5F2FF))
    var textSecondary by mutableStateOf(Color(0xFFB39DDB))
    var danger by mutableStateOf(Color(0xFFCF6679))

    val cardShape = RoundedCornerShape(16.dp)
    val fieldShape = RoundedCornerShape(12.dp)

    fun applyTheme(isDark: Boolean) {
        if (isDark) {
            page = Color(0xFF0F0F14)
            surface = Color(0xFF171723)
            surfaceAlt = Color(0xFF1F1F2D)
            primary = Color(0xFF4A3B8B)
            textPrimary = Color(0xFFF5F2FF)
            textSecondary = Color(0xFFB39DDB)
            danger = Color(0xFFCF6679)
        } else {
            page = Color(0xFFF5F5FA)
            surface = Color(0xFFFFFFFF)
            surfaceAlt = Color(0xFFE9E6F6)
            primary = Color(0xFF5A46A8)
            textPrimary = Color(0xFF1A1630)
            textSecondary = Color(0xFF5F548A)
            danger = Color(0xFFB00020)
        }
    }
}

@Composable
fun crudOutlinedTextFieldColors() = OutlinedTextFieldDefaultsM3.colors(
    focusedTextColor = CrudDesign.textPrimary,
    unfocusedTextColor = CrudDesign.textPrimary,
    disabledTextColor = CrudDesign.textPrimary.copy(alpha = 0.5f),
    cursorColor = CrudDesign.textPrimary,
    focusedLabelColor = CrudDesign.textSecondary,
    unfocusedLabelColor = CrudDesign.textSecondary.copy(alpha = 0.85f),
    focusedBorderColor = CrudDesign.primary,
    unfocusedBorderColor = CrudDesign.primary.copy(alpha = 0.35f),
    focusedContainerColor = CrudDesign.primary.copy(alpha = 0.08f),
    unfocusedContainerColor = CrudDesign.primary.copy(alpha = 0.08f)
)

@Composable
fun crudOutlinedTextFieldColorsM3() = crudOutlinedTextFieldColors()

