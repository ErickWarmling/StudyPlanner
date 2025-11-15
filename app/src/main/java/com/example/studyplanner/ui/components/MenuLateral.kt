package com.example.studyplanner.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.studyplanner.R
import com.example.studyplanner.ui.theme.BlueHover
import com.example.studyplanner.ui.theme.BlueLight
import com.example.studyplanner.ui.theme.BluePrimary

@Composable
fun MenuLateral(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = BlueLight
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = BluePrimary)
                .height(180.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_studyplanner___65ddm),
                contentDescription = "Logo StudyPlanner",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        NavigationDrawerItem(
            label = { Text("Disciplinas") },
            icon = { Icon(Icons.Default.MenuBook, contentDescription = null) },
            selected = currentRoute == "disciplinas",
            onClick = { onNavigate("disciplinas") },
            colors = NavigationDrawerItemDefaults.colors(
                selectedContainerColor = BlueHover
            ),
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text(("Resumo do Estudo")) },
            icon = { Icon(Icons.Default.BarChart, contentDescription = null) },
            selected = currentRoute == "resumo_estudo",
            onClick = { onNavigate("resumo_estudo") },
            colors = NavigationDrawerItemDefaults.colors(
                selectedContainerColor = BlueHover
            ),
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}