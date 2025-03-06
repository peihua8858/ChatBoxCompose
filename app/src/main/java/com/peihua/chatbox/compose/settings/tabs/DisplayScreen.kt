package com.peihua.chatbox.compose.settings.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.intl.LocaleList

@Composable
fun DisplayScreen(modifier: Modifier = Modifier) {
    val localeList = LocaleList.current
    Column {
        DropdownMenu(expanded = true, onDismissRequest = {

        }) {
//            for (locale in localeList) {
//                DropdownMenuItem(text = {
//                    Text(text = locale.displayName)
//                }, onClick = {
//                })
//            }
        }
    }
}

data class LangDisplayOption(val title: String, val subtitle: String, val locale: Locale) {
    override fun toString(): String {
        return title
    }
}

val Locale.displayName: String
    get() = platformLocale.displayName