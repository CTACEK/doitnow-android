package com.ctacek.yandexschool.doitnow.ui.adapter

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.ctacek.yandexschool.doitnow.R
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class SwipeCallback(
    private val swipeCallback: SwipeCallbackInterface,
    private val context: Context
) : ItemTouchHelper.SimpleCallback(
    0,
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (direction == ItemTouchHelper.LEFT) {
            (viewHolder as? TaskViewHolder)?.todoItem?.let { swipeCallback.onDelete(it) }
        } else if (direction == ItemTouchHelper.RIGHT) {
            (viewHolder as? TaskViewHolder)?.todoItem?.let { swipeCallback.onChangeDone(it) }
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        RecyclerViewSwipeDecorator.Builder(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
            .addSwipeLeftBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.color_light_red
                )
            )
            .addSwipeLeftActionIcon(R.drawable.delete)
            .addSwipeRightBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.color_light_green
                )
            )
            .addSwipeRightActionIcon(R.drawable.baseline_check)
            .setActionIconTint(
                ContextCompat.getColor(
                    recyclerView.context,
                    android.R.color.white
                )
            )
            .create()
            .decorate()
        super.onChildDraw(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
    }



}