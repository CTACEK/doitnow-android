package com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ctacek.yandexschool.doitnow.R
import com.ctacek.yandexschool.doitnow.data.model.Importance
import java.util.Date
import java.util.Locale

@Composable
fun ImportancePanel(
    selectedImportance: Importance,
    openBottomSheet: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 16.dp)
            .clickable { openBottomSheet() }
    ) {
        Text(
            text = stringResource(id = R.string.importance_title_text),
            modifier = Modifier.padding(bottom = 5.dp)
        )
        Text(
            text = selectedImportance.toString().lowercase().capitalize(Locale.ROOT),
            modifier = Modifier.alpha(0.5f),
            color = if (selectedImportance == Importance.IMPORTANT) Color.Red else Color.Gray,
        )
    }
}