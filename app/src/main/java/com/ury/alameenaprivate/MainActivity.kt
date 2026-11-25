package com.ury.alameenaprivate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.ury.alameenaprivate.data.AppDatabase
import com.ury.alameenaprivate.data.UserProfile
import com.ury.alameenaprivate.scenes.ParticleFieldScene
import com.ury.alameenaprivate.scenes.StarSkyScene
import com.ury.alameenaprivate.ui.*
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dao = AppDatabase.get(this).profileDao()
        setContent {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                AppTheme {
                    var loaded by remember { mutableStateOf(false) }
                    var name by remember { mutableStateOf<String?>(null) }
                    var scene by rememberSaveable { mutableStateOf<String?>(null) }
                    val scope = rememberCoroutineScope()
                    LaunchedEffect(Unit) { name = dao.get()?.displayName; loaded = true }
                    BackHandler(enabled = scene != null) { scene = null }
                    if (loaded) {
                        val n = name
                        when {
                            n == null -> NameScreen { v -> scope.launch { dao.save(UserProfile(displayName = v)); name = v } }
                            scene == "stars" -> StarSkyScene()
                            scene == "particles" -> ParticleFieldScene()
                            else -> HomeScreen(n) { scene = it }
                        }
                    }
                }
            }
        }
    }
}
