package com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageTaskScreen(
    uiState: State<ToDoItem>,
    onEvent: (ManageUiEvent) -> Unit,
) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val openDatePicker = remember { mutableStateOf(false) }
    val openTimePicker = remember { mutableStateOf(false) }

    val saveCreateState = remember {
        mutableStateOf(if (uiState.value.id == "-1") "Create" else "Save")
    }

    var openBottomSheet by rememberSaveable { mutableStateOf(false) }

    val bottomSheetState = rememberModalBottomSheetState(true)

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Toolbar(
                onCloseClick = { onEvent(ManageUiEvent.Close) },
                onSaveClick = {
                    if (saveCreateState.value == "Save") onEvent(ManageUiEvent.SaveTask)
                    else onEvent(ManageUiEvent.CreateTask)
                },
                scrollBehavior = scrollBehavior,
                saveCreateState = saveCreateState.value
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .verticalScroll(rememberScrollState())
            ) {
                EditTextField(
                    text = uiState.value.description,
                    onChanged = { newText -> onEvent(ManageUiEvent.ChangeDescription(newText)) })

                ImportancePanel(
                    selectedImportance = uiState.value.importance,
                    openBottomSheet = { openBottomSheet = true }
                )

                Divider()

                DeadlinePanel(
                    deadline = uiState.value.deadline,
                    clearDeadline = { onEvent(ManageUiEvent.ClearDeadline) },
                    showDatePicker = { openDatePicker.value = true })

                Divider()

                DeleteButton(
                    onClick = { onEvent(ManageUiEvent.DeleteTodo) },
                    uiState.value.id != "-1"
                )

                if (openBottomSheet) {
                    ModalBottomSheet(
                        sheetState = bottomSheetState,
                        onDismissRequest = { openBottomSheet = false },
                    ) {
                        ImportanceBottomSheet { importance ->
                            onEvent(ManageUiEvent.ChangeImportance(importance))
                            if (bottomSheetState.isVisible) {
                                openBottomSheet = false
                            }
                        }
                    }
                }

                if (openDatePicker.value) {
                    DatePickerDialogMaterial(
                        openDatePicker = openDatePicker,
                        openTimePicker = openTimePicker
                    ) { time ->
                        uiState.value.deadline = time
                    }
                }

                if (openTimePicker.value) {
                    TimePickerSwitchable(
                        showTimePicker = openTimePicker,
                        selectedDate = uiState.value.deadline
                    ) { time ->
                        onEvent(ManageUiEvent.ChangeDeadline(time))
                    }
                }
            }
        }
    )
}