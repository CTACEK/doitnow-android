package com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialogMaterial(
    openDatePicker: MutableState<Boolean>,
    openTimePicker: MutableState<Boolean>,
    onTimeSelected: (Date) -> Unit
) {
    val datePickerState = rememberDatePickerState()
    DatePickerDialog(
        onDismissRequest = {
            openDatePicker.value = false
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onTimeSelected(Date(datePickerState.selectedDateMillis!!))
                    openDatePicker.value = false
                    openTimePicker.value = true
                },
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    openDatePicker.value = false
                }
            ) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}
