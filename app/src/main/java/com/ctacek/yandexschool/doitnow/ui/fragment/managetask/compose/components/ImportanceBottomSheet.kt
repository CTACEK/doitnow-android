package com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ctacek.yandexschool.doitnow.data.model.Importance
import com.ctacek.yandexschool.doitnow.data.model.toName
import com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose.theme.Typography
import com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose.theme.YandexTodoTheme

@Composable
fun ImportanceBottomSheet(
    onImportanceSelected: (Importance) -> Unit
) {
    val importanceValues = Importance.values()

    Surface(
        shape = RoundedCornerShape(12.dp, 12.dp, 0.dp, 0.dp),
        color = YandexTodoTheme.colors.backPrimary
    ) {
        Column(
            modifier =
            Modifier.padding(bottom = 10.dp)
        ) {
            importanceValues.forEach { importance ->
                Text(
                    text = importance.toName(),
                    color = if (importance == Importance.IMPORTANT)
                        YandexTodoTheme.colors.colorRed else YandexTodoTheme.colors.labelPrimary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onImportanceSelected(importance) }
                        .padding(horizontal = 20.dp, vertical = 15.dp),
                    style = Typography.body1
                )
            }
        }
    }
}

@Composable
@Preview
private fun ImportancePreviewLight(){
    YandexTodoTheme(
        darkTheme = false
    ){
        ImportanceBottomSheet(
            {}
        )
    }
}

@Composable
@Preview
private fun ImportancePreviewDark(){
    YandexTodoTheme(
        darkTheme = true
    ){
        ImportanceBottomSheet(
            {}
        )
    }
}
