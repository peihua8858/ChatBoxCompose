package com.peihua.chatbox.compose.message

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun MessageScreen(menuId: String, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .padding(37.dp)
    ) {
        Text(
            text = "menuId =$menuId",
            fontSize = 20.sp,
            color = androidx.compose.ui.graphics.Color.Black,
            textAlign = TextAlign.Center
        )
    }
}