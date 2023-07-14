package com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ctacek.yandexschool.doitnow.R

@Composable
fun DeleteButton(onClick: () -> Unit, state: Boolean) {
    Button(
        onClick = onClick,
        modifier = Modifier.clickable(enabled = state, onClick = onClick),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
        elevation = ButtonDefaults.elevation(0.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(id = R.string.delete_button),
                tint = if (state) Color.Red else Color.Gray
            )
            Text(
                text = stringResource(id = R.string.delete_button),
                modifier = Modifier.padding(start = 4.dp),
                color = if (state) Color.Red else Color.Gray
            )
        }
    }
}