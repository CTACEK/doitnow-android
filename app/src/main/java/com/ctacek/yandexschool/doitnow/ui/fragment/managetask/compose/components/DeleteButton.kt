package com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ctacek.yandexschool.doitnow.R
import com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose.theme.YandexTodoTheme

@Composable
fun DeleteButton(onClick: () -> Unit, state: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .clickable(enabled = state) {
                onClick()
            }
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = stringResource(id = R.string.delete_button),
            tint = if (state) Color.Red else Color.Gray
        )
        Text(
            text = stringResource(id = R.string.delete_button),
            modifier = Modifier.padding(start = 4.dp),
            color = if (state) YandexTodoTheme.colors.colorRed else YandexTodoTheme.colors.labelTertiary
        )
    }
}

@Composable
@Preview
private fun DeleteButtonPreview(){
    YandexTodoTheme(
        darkTheme = false
    ){
        DeleteButton(onClick = { /*TODO*/ }, state = true)
    }
}
