package com.example.studyplanner.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MenuLateral(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    ModalDrawerSheet {
        Text("Menu", modifier = Modifier.padding(16.dp))
        NavigationDrawerItem(
            label = { Text("Disciplinas") },
            icon = { Icon(Icons.Default.MenuBook, contentDescription = null) },
            selected = currentRoute == "disciplinas",
            onClick = { onNavigate("disciplinas") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text(("Resumo do Estudo")) },
            icon = { Icon(Icons.Default.BarChart, contentDescription = null) },
            selected = currentRoute == "resumo_estudo",
            onClick = { onNavigate("resumo_estudo") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}