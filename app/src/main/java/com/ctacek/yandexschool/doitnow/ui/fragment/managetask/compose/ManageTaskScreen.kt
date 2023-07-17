package com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ctacek.yandexschool.doitnow.domain.model.ToDoItem
import com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose.components.DatePickerDialogMaterial
import com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose.components.DeadlinePanel
import com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose.components.DeleteButton
import com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose.components.EditTextField
import com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose.components.ImportanceBottomSheet
import com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose.components.ImportancePanel
import com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose.components.TimePickerSwitchable
import com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose.components.Toolbar
import com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose.eventsholders.ManageUiEvent
import com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose.theme.YandexTodoTheme
import kotlinx.coroutines.launch
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ManageTaskScreen(
    uiState: State<ToDoItem>,
    onEvent: (ManageUiEvent) -> Unit,
) {

    val openDatePicker = remember { mutableStateOf(false) }
    val openTimePicker = remember { mutableStateOf(false) }

    val checkedDate = remember { mutableStateOf(uiState.value.deadline != null) }

    val saveCreateState = remember {
        mutableStateOf(if (uiState.value.id == "-1") "Create" else "Save")
    }

    val datePickerState = rememberDatePickerState()


    val scrollState = rememberScrollState()

    val coroutineScope = rememberCoroutineScope()

    val bottomSheetState =
        androidx.compose.material.rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)


    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            ImportanceBottomSheet { importance ->
                onEvent(ManageUiEvent.ChangeImportance(importance))
                coroutineScope.launch {
                    bottomSheetState.hide()
                }
            }
        }
    ) {
        Scaffold(
            modifier = Modifier
                .nestedScroll(TopAppBarDefaults.pinnedScrollBehavior().nestedScrollConnection),
            topBar = {
                Toolbar(
                    onEvent = onEvent,
                    scrollState = scrollState,
                    saveCreateState = saveCreateState.value
                )
            },
            content = {
                Column(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxHeight()
                        .verticalScroll(scrollState)
                        .background(YandexTodoTheme.colors.backPrimary)
                ) {
                    EditTextField(
                        text = uiState.value.description,
                        onChanged = { newText -> onEvent(ManageUiEvent.ChangeDescription(newText)) })

                    ImportancePanel(
                        selectedImportance = uiState.value.importance,
                        coroutineScope = coroutineScope,
                        bottomSheetState = bottomSheetState
                    )

                    Divider(color = YandexTodoTheme.colors.labelTertiary)

                    DeadlinePanel(
                        deadline = uiState.value.deadline,
                        clearDeadline = {
                            if (checkedDate.value) {
                                openDatePicker.value = true
                            } else {
                                onEvent(ManageUiEvent.ChangeDeadline(null))
                            }
                        },

                        showDatePicker = { openDatePicker.value = true })

                    Divider(color = YandexTodoTheme.colors.labelTertiary)

                    DeleteButton(
                        onClick = { onEvent(ManageUiEvent.DeleteTodo) },
                        uiState.value.id != "-1"
                    )

                    if (openDatePicker.value) {
                        DatePickerDialogMaterial(
                            openDatePicker = openDatePicker,
                            openTimePicker = openTimePicker,
                            datePickerState = datePickerState,
                            dateChanged = checkedDate
                        ) { time ->
                            uiState.value.deadline = time
                        }
                    }

                    if (openTimePicker.value) {
                        TimePickerSwitchable(
                            showTimePicker = openTimePicker,
                            selectedDate = uiState.value.deadline,
                            dateChanged = checkedDate
                        ) { time ->
                            onEvent(ManageUiEvent.ChangeDeadline(time))
                        }
                    }
                }
            }
        )
    }
}

@Composable
@Preview
private fun MainScreenPreviewLight() {
    YandexTodoTheme(
        darkTheme = false
    ) {
        ManageTaskScreen(
            uiState = remember { mutableStateOf(ToDoItem())},
            onEvent = {},
        )
    }
}

@Composable
@Preview
private fun MainScreenPreviewDark() {
    YandexTodoTheme(
        darkTheme = true
    ) {
        ManageTaskScreen(
            uiState = remember { mutableStateOf(ToDoItem().copy(id = "Preview", deadline = Date(1231213123123)))},
            // Эх, доллар по 30 :с
            onEvent = {},
        )
    }
}