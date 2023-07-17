package com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose.components

import android.annotation.SuppressLint
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose.theme.Typography
import com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose.theme.YandexTodoTheme
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialogMaterial(
    openDatePicker: MutableState<Boolean>,
    openTimePicker: MutableState<Boolean>,
    dateChanged: MutableState<Boolean>,
    datePickerState: DatePickerState,
    onTimeSelected: (Date) -> Unit
) {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            surface = YandexTodoTheme.colors.backPrimary,
            secondary = YandexTodoTheme.colors.labelPrimary,
            primary = Color(0x4D007AFF),
            onPrimary = YandexTodoTheme.colors.colorBlue,
            surfaceTint = YandexTodoTheme.colors.labelPrimary,
            tertiary = YandexTodoTheme.colors.labelPrimary,
            onSecondary = YandexTodoTheme.colors.labelPrimary,
        )
    ) {
        DatePickerDialog(
            onDismissRequest = {
                openDatePicker.value = false
                dateChanged.value = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (datePickerState.selectedDateMillis == null) {
                            onTimeSelected(java.sql.Date(System.currentTimeMillis()))
                        } else {
                            onTimeSelected(java.sql.Date(datePickerState.selectedDateMillis!!))
                        }
                        openDatePicker.value = false
                        openTimePicker.value = true
                    },
                ) {
                    Text(
                        "OK",
                        style = Typography.body1,
                        color = YandexTodoTheme.colors.colorBlue
                    )

                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        dateChanged.value = false
                        openDatePicker.value = false
                    }
                ) {
                    Text(
                        "Cancel",
                        style = Typography.body1,
                        color = YandexTodoTheme.colors.labelTertiary
                    )
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    titleContentColor = YandexTodoTheme.colors.labelPrimary,
                    currentYearContentColor = YandexTodoTheme.colors.labelPrimary,
                    dayContentColor = YandexTodoTheme.colors.labelPrimary,
                    disabledDayContentColor = YandexTodoTheme.colors.labelPrimary,
                    headlineContentColor = YandexTodoTheme.colors.labelPrimary,
                    subheadContentColor = YandexTodoTheme.colors.labelPrimary,
                    selectedDayContentColor = YandexTodoTheme.colors.colorWhite,
                    selectedDayContainerColor = YandexTodoTheme.colors.colorBlue,
                    weekdayContentColor = YandexTodoTheme.colors.labelPrimary,
                    yearContentColor = YandexTodoTheme.colors.labelPrimary,
                    disabledSelectedDayContentColor = YandexTodoTheme.colors.labelTertiary,
                    dayInSelectionRangeContentColor = YandexTodoTheme.colors.labelPrimary,
                    selectedYearContentColor = YandexTodoTheme.colors.labelPrimary,
                    todayContentColor = YandexTodoTheme.colors.colorBlue,
                    containerColor = YandexTodoTheme.colors.backSecondary,
                    dayInSelectionRangeContainerColor = YandexTodoTheme.colors.labelPrimary,
                    disabledSelectedDayContainerColor = YandexTodoTheme.colors.labelPrimary
                )
            )
        }
    }
}


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
private fun DatePickerDialogPreviewLight(){
    YandexTodoTheme(
        darkTheme = false
    ){
        DatePickerDialogMaterial(
            openDatePicker = mutableStateOf(false),
            openTimePicker=  mutableStateOf(false),
            dateChanged=  mutableStateOf(false),
            datePickerState = rememberDatePickerState(),
            onTimeSelected=  {  }
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
private fun DatePickerDialogPreviewDark(){
    YandexTodoTheme(
        darkTheme = true
    ){
        DatePickerDialogMaterial(
            openDatePicker = mutableStateOf(false),
            openTimePicker=  mutableStateOf(false),
            dateChanged=  mutableStateOf(false),
            datePickerState = rememberDatePickerState(),
            onTimeSelected=  {  }
        )
    }
}
