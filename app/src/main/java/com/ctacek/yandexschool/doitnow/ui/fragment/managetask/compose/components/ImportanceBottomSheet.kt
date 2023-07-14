package com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ctacek.yandexschool.doitnow.data.model.Importance
import java.util.Locale

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ImportanceModalBottomSheet(
//    bottomSheetState: SheetState,
//    onDismissRequest: () -> Unit,
//) {
//
//    fun ModalBottomSheet(
//        sheetState = bottomSheetState,
//        onDismissRequest = { onDismissRequest() },
//    ) {
//        ImportanceBottomSheet { importance ->
//            selectedImportance.value = importance
//            onPriorityItemClick(Importance.valueOf(importance.toUpperCase()))
//            if (bottomSheetState.isVisible) {
//                openBottomSheet = false
//            }
//        }
//    }
//}

@Composable
fun ImportanceBottomSheet(
    onImportanceSelected: (Importance) -> Unit
) {
    val importanceValues = Importance.values()

    Column(modifier = Modifier.padding(16.dp, bottom = 30.dp)) {
        importanceValues.forEach { importance ->
            Text(
                text = importance.toString().lowercase().capitalize(Locale.ROOT),
                color = if (importance == Importance.IMPORTANT) Color.Red else Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onImportanceSelected(importance) }
                    .padding(vertical = 8.dp)
            )
        }
    }
}