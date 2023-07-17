package com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose.components

import androidx.compose.foundation.ScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ctacek.yandexschool.doitnow.R
import com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose.eventsholders.ManageUiEvent
import com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose.theme.YandexTodoTheme
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar(
    onEvent: (ManageUiEvent) -> Unit,
    scrollState: ScrollState,
    saveCreateState: String
) {
    TopAppBar(
        title = {},
        navigationIcon =
        {
            IconButton(onClick = {
                onEvent(ManageUiEvent.Close)
            }) {
                Icon(
                    Icons.Default.Close,
                    tint = YandexTodoTheme.colors.labelPrimary,
                    contentDescription = null,
                )
            }
        },
        actions =
        {
            TextButton(onClick = {
                onEvent(ManageUiEvent.SaveTask)
            }) {
                Text(
                    text = (saveCreateState).uppercase(Locale.ROOT),
                    style = TextStyle(
                        color = YandexTodoTheme.colors.colorBlue,
                        fontFamily = FontFamily(Font(R.font.roboto_medium)),
                        fontSize = 18.sp
                    )
                )
            }

        }, scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = YandexTodoTheme.colors.backPrimary
        ),
        modifier = Modifier
            .shadow(elevation = calculateElevation(scrollValue = scrollState.value.toFloat()))
    )
}

@Composable
@Preview
private fun ToolbarPreviewDark() {
    YandexTodoTheme(
        darkTheme = true
    ) {
        Toolbar(
            onEvent = {},
            scrollState = ScrollState(10),
            saveCreateState = "Save"
        )
    }
}

@Composable
@Preview
private fun ToolbarPreviewLight() {
    YandexTodoTheme(
        darkTheme = false
    ) {
        Toolbar(
            onEvent = {},
            scrollState = ScrollState(10),
            saveCreateState = "Save"
        )
    }
}

private fun calculateElevation(scrollValue: Float): Dp {
    return if (scrollValue > 0.dp.value) 4.dp else 0.dp
}
