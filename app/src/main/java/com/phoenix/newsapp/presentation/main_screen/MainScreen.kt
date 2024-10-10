package com.phoenix.newsapp.presentation.main_screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun MainScreen() {
    Scaffold { innerPadding ->
        Row(modifier = Modifier.padding(innerPadding)) {
            Text("Test", modifier = Modifier.align(Alignment.CenterVertically))
        }
    }
}