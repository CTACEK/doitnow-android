package com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose.eventsholders

import androidx.compose.runtime.Immutable
import com.ctacek.yandexschool.doitnow.data.model.Importance
import java.util.Date
@Immutable
sealed interface ManageUiEvent {
    data class ChangeDescription(val text: String) : ManageUiEvent
    data class ChangeImportance(val importance: Importance) : ManageUiEvent
    data class ChangeDeadline(val deadline: Date?) : ManageUiEvent
    object ClearDeadline : ManageUiEvent
    object SaveTask : ManageUiEvent
    object DeleteTodo : ManageUiEvent
    object Close : ManageUiEvent
}
