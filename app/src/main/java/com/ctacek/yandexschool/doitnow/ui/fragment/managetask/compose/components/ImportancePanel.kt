package com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ctacek.yandexschool.doitnow.R
import com.ctacek.yandexschool.doitnow.data.model.Importance
import com.ctacek.yandexschool.doitnow.data.model.toName
import com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose.theme.Typography
import com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose.theme.YandexTodoTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ImportancePanel(
    selectedImportance: Importance,
    coroutineScope: CoroutineScope,
    bottomSheetState: ModalBottomSheetState
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 16.dp)
            .clickable {
                coroutineScope.launch {
                    bottomSheetState.show()
                }
            }
    ) {
        Text(
            text = stringResource(id = R.string.importance_title_text),
            modifier = Modifier.padding(bottom = 2.dp),
            style = Typography.body1,
            color = YandexTodoTheme.colors.labelPrimary
        )
        Text(
            text = selectedImportance.toName(),
            style = Typography.body2,
            modifier = Modifier.alpha(0.9f),
            color = if (selectedImportance == Importance.IMPORTANT)
                YandexTodoTheme.colors.colorRed else YandexTodoTheme.colors.labelTertiary,
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview(backgroundColor = 0xFFFFFFFF)
private fun ImportancePanelPreviewLight() {
    YandexTodoTheme(
        darkTheme = false
    ) {
        ImportancePanel(
            Importance.BASIC,
            CoroutineScope(Dispatchers.Default),
            ModalBottomSheetState(ModalBottomSheetValue.Hidden)
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview(backgroundColor = 0xFFFFFFFF)
private fun ImportancePanelPreviewDark() {
    YandexTodoTheme(
        darkTheme = true
    ) {
        ImportancePanel(
            Importance.IMPORTANT,
            CoroutineScope(Dispatchers.Default),
            ModalBottomSheetState(ModalBottomSheetValue.Hidden)
        )
    }
}
