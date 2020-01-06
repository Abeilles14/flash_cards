package com.isabelle.flash.cards;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.isabelle.flash.R;
import com.isabelle.flash.controllers.SwipeControllerActions;

import static android.support.v7.widget.helper.ItemTouchHelper.*;

//button states
enum ButtonsState {
    GONE,
    LEFT_VISIBLE,
    RIGHT_VISIBLE
}

//tells helper what kind of actions recycler should handle (L/R)
//what to do on given actions
public class RecyclerCard extends Callback {

    private boolean swipeBack = false;
    private ButtonsState buttonShowedState = ButtonsState.GONE;
    private RectF buttonInstance = null;
    private RecyclerView.ViewHolder currentItemViewHolder = null;
    private static final float buttonWidth = 300;

    private SwipeControllerActions buttonsActions;

    public RecyclerCard(SwipeControllerActions buttonsActions) {
        this.buttonsActions = buttonsActions;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, LEFT | RIGHT);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //do nothing
    }

    //blocks swipe
    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        if (swipeBack) {
            swipeBack = buttonShowedState != ButtonsState.GONE;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ACTION_STATE_SWIPE) {
            if (buttonShowedState != ButtonsState.GONE) {
                if (buttonShowedState == ButtonsState.LEFT_VISIBLE) {
                    dX = Math.max(dX, buttonWidth);
                }
                if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
                    dX = Math.min(dX, -buttonWidth);
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            } else {
                //set touch listener
                setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }
        if (buttonShowedState == ButtonsState.GONE) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
        currentItemViewHolder = viewHolder;
        //drawButtons(c, viewHolder);
    }

    //check how much to left/right swiped item, change state to show buttons
    private void setTouchListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;
                //whether or not to show buttons, if swiped far enough
                if (swipeBack) {
                    if (dX < -buttonWidth) {
                        buttonShowedState = ButtonsState.RIGHT_VISIBLE;
                    } else if (dX > buttonWidth) {
                        buttonShowedState = ButtonsState.LEFT_VISIBLE;
                    }

                    if (buttonShowedState != ButtonsState.GONE) {
                        setTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                        setItemsClickable(recyclerView, false);
                    }
                }
                return false;
            }
        });
    }

    //if buttonShowedState != GONE, overwrite listener and simulate click
    //may already have onclick listener on items, need to disable it
    private void setTouchDownListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    setTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
                return false;
            }
        });
    }

    private void setTouchUpListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    RecyclerCard.super.onChildDraw(c, recyclerView, viewHolder, 0F, dY, actionState, isCurrentlyActive);
                    recyclerView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return false;
                        }
                    });
                    setItemsClickable(recyclerView, true);
                    swipeBack = false;

                    //button actions
                    //removed buttonsActions != null && from if??
                    if (buttonsActions != null && buttonInstance != null && buttonInstance.contains(event.getX(), event.getY())) {
                        if (buttonInstance != null && buttonInstance.contains(event.getX(), event.getY())) {
                            if (buttonShowedState == ButtonsState.LEFT_VISIBLE) {
                                //on left button clicked, edit
                                //buttonsActions.onLeftClicked(viewHolder.getAdapterPosition());
                                buttonsActions.onLeftClicked(viewHolder.getAdapterPosition());
                            } else if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
                                //on right button clicked, delete
                                //buttonsActions.onRightClicked(viewHolder.getAdapterPosition());
                                buttonsActions.onRightClicked(viewHolder.getAdapterPosition());

                            }
                        }
                    }
                    buttonShowedState = ButtonsState.GONE;
                    currentItemViewHolder = null;
                }
                return false;
            }
        });
    }

    //after user clicks on recyclerview, just reset state of swipe controller
    private void setItemsClickable(RecyclerView recyclerView, boolean isClickable) {
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            recyclerView.getChildAt(i).setClickable(isClickable);
        }
    }

    //draw the buttons
    private void drawButtons(Canvas c, RecyclerView.ViewHolder viewHolder) {
        View itemView = viewHolder.itemView;

        float buttonPadding = 2;
        float buttonMarginHorizontal = viewHolder.itemView.getResources().getDimension(R.dimen.button_horizontal_margin);
        float buttonMarginVertical = viewHolder.itemView.getResources().getDimension(R.dimen.button_vertical_margin) + buttonPadding;

        Drawable ic_edit = viewHolder.itemView.getResources().getDrawable(R.drawable.ic_edit);
        Drawable ic_delete = viewHolder.itemView.getResources().getDrawable(R.drawable.ic_delete);

        float corners = 16;
        Paint p = new Paint();

        //edit button
        RectF leftButton = new RectF(itemView.getLeft() + buttonMarginHorizontal, itemView.getTop() + buttonMarginVertical, itemView.getLeft() + buttonWidth, itemView.getBottom() - buttonMarginVertical);
        p.setColor(viewHolder.itemView.getResources().getColor(R.color.edit_card));
        c.drawRoundRect(leftButton, corners, corners, p);
        drawIcon(ic_edit, c, leftButton);

        //delete button
        RectF rightButton = new RectF(itemView.getRight() - buttonWidth, itemView.getTop() + buttonMarginVertical, itemView.getRight() - buttonMarginHorizontal, itemView.getBottom() - buttonMarginVertical);
        p.setColor(viewHolder.itemView.getResources().getColor(R.color.delete_card));
        c.drawRoundRect(rightButton, corners, corners, p);
        drawIcon(ic_delete, c, rightButton);

        buttonInstance = null;
        if (buttonShowedState == ButtonsState.LEFT_VISIBLE) {
            buttonInstance = leftButton;
        } else if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
            buttonInstance = rightButton;
        }
    }

    //draw icon in middle of button
    private void drawIcon(Drawable icon, Canvas c, RectF button) {
        //icon size
        int ic_width = icon.getMinimumWidth() + 30;
        int ic_height = icon.getMinimumHeight() + 30;
        //icon top left corner position
        int left = (int) button.centerX() - (ic_width / 2);
        int top = (int) button.centerY() - (ic_height / 2);

        icon.setBounds(left, top, left + ic_width, top + ic_height);
        icon.draw(c);
    }

    public void onDraw(Canvas c) {
        if (currentItemViewHolder != null) {
            drawButtons(c, currentItemViewHolder);
        }
    }

}
