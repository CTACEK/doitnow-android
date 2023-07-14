package com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar(
    onCloseClick: () -> Unit,
    onSaveClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    saveCreateState : String
) {
    TopAppBar(
    title = {},
    navigationIcon =
    {
        IconButton(onClick = { onCloseClick() }) {
            Icon(Icons.Default.Close, contentDescription = null)
        }
    },
    actions =
    {
        TextButton(onClick = {
            onSaveClick()
        }) {
            Text(text = (saveCreateState).uppercase(Locale.ROOT))
        }

    }, scrollBehavior = scrollBehavior
    )
}