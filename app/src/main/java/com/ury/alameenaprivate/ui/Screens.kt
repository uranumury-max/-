package com.ury.alameenaprivate.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val Gold = Color(0xFFE6C068)

@Composable
fun AppTheme(content: @Composable () -> Unit) = MaterialTheme(
    colorScheme = darkColorScheme(primary = Gold, background = Color(0xFF080B1E), surface = Color(0xFF121634), onPrimary = Color.Black),
    content = content
)

@Composable
fun NameScreen(onSave: (String) -> Unit) {
    var name by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().padding(32.dp), Arrangement.Center, Alignment.CenterHorizontally) {
        Text("أهلًا بك في عالمنا الخاص", style = MaterialTheme.typography.headlineSmall, color = Gold)
        Spacer(Modifier.height(24.dp))
        OutlinedTextField(name, { name = it }, label = { Text("اسم العرض") }, singleLine = true)
        Spacer(Modifier.height(16.dp))
        Button(onClick = { onSave(name.trim()) }, enabled = name.isNotBlank()) { Text("ابدأ") }
    }
}

@Composable
fun HomeScreen(name: String, onScene: (String) -> Unit) {
    Column(Modifier.fillMaxSize().padding(32.dp), Arrangement.Center, Alignment.CenterHorizontally) {
        Text("مرحبًا $name", style = MaterialTheme.typography.headlineMedium, color = Gold)
        Spacer(Modifier.height(32.dp))
        Button(onClick = { onScene("stars") }) { Text("سماء الكوكبات") }
        Spacer(Modifier.height(12.dp))
        Button(onClick = { onScene("particles") }) { Text("حقل الجسيمات") }
    }
}
