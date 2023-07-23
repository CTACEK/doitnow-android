package com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose.components

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ctacek.yandexschool.doitnow.R
import com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose.theme.Typography
import com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose.theme.YandexTodoTheme
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun DeadlinePanel(
    deadline: Date?,
    clearDeadline: () -> Unit,
    showDatePicker: () -> Unit,
) {

    @SuppressLint("SimpleDateFormat")
    val dataFormat = SimpleDateFormat("E, dd MMM yyyy HH:mm")

    Column(
        modifier = Modifier
            .padding(vertical = 20.dp, horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.do_before_title),
                style = Typography.body1,
                color = YandexTodoTheme.colors.labelPrimary,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start)
                    .wrapContentHeight(Alignment.CenterVertically)
            )
            Switch(
                checked = deadline != null,
                onCheckedChange = {
                    if (it)
                        showDatePicker()
                    else
                        clearDeadline()
                },
                modifier = Modifier.padding(end = 16.dp),
                colors = SwitchDefaults.colors(
                    checkedThumbColor = YandexTodoTheme.colors.colorBlue,
                    checkedTrackColor = Color(0x4D007AFF),
                    uncheckedThumbColor = YandexTodoTheme.colors.backElevated,
                    uncheckedTrackColor = YandexTodoTheme.colors.supportOverlay
                )
            )
        }
        if (deadline != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        showDatePicker()
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painterResource(id = R.drawable.calendar_24),
                    contentDescription = null,
                    tint = YandexTodoTheme.colors.colorBlue,
                )
                Text(
                    text = dataFormat.format(deadline),
                    style = Typography.body2,
                    color = YandexTodoTheme.colors.colorBlue,
                )
            }
        }
    }
}

@Composable
@Preview
private fun DeadlinePanelPreviewLight(){
    YandexTodoTheme(
        darkTheme = false
    ){
        DeadlinePanel(
            Date(),
            {},
            {}
        )
    }
}

@Composable
@Preview
private fun DeadlinePanelPreviewDark(){
    YandexTodoTheme(
        darkTheme = true
    ){
        DeadlinePanel(
            null,
            {},
            {}
        )
    }
}
