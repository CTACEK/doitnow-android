package com.ctacek.yandexschool.doitnow.ui.adapter.swipe

import android.content.Context
import androidx.recyclerview.widget.ItemTouchHelper

class SwipeHelper(
    swipeCallback: SwipeCallbackInterface,
    context: Context
) : ItemTouchHelper(SwipeCallback(swipeCallback, context))
