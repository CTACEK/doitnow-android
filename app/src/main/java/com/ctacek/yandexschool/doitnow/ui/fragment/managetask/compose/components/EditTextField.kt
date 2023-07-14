package com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ctacek.yandexschool.doitnow.R

@Composable
fun EditTextField(
    text: String,
    onChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = text,
        onValueChange = onChanged,
        label = { Text(stringResource(id = R.string.enter_your_task_hint)) },
        singleLine = false,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 150.dp) // Adjust the maximum height as needed
            .padding(vertical = 8.dp, horizontal = 16.dp)
    )
}