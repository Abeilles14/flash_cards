package com.isabelle.flash.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.isabelle.flash.R;
import com.isabelle.flash.adapters.FlashCardAdapter;
import com.isabelle.flash.cards.RecyclerCard;
import com.isabelle.flash.controllers.SwipeControllerActions;
import com.isabelle.flash.database.DbHelper;
import com.isabelle.flash.models.FlashCard;
import com.isabelle.flash.navDrawer.Utils;

import java.util.ArrayList;

public class FlashCardsFragment extends Fragment implements View.OnClickListener {

    private static final String LOG_TAG = "FlashCard Fragment";

    private RecyclerView recyclerView;
    private FlashCardAdapter flashcard_adapter;
    private RecyclerView.LayoutManager layoutManager;
    private View view;

    private FloatingActionButton buttonFab;
    private ArrayList<FlashCard> flashcards;
    private RecyclerCard cardController = null;
    private DbHelper dbHelper;

    private AlertDialog.Builder alertDialog;
    private EditText et_flashcard;
    private Toast toast;
    private boolean add = false;
    private int edit_position;

    private String deck_title;
    private long deck_id;

    public FlashCardsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_flashcards, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //initialize FloatingActionButton and set colors
        buttonFab = (FloatingActionButton) view.findViewById(R.id.addNewFlashCard);
        buttonFab.setOnClickListener(this);

        //initialize recycler view from fragment_deck
        recyclerView = view.findViewById(R.id.flashcards_list_cards);
        recyclerView.setHasFixedSize(true);

        recyclerView.setClickable(true);
        recyclerView.setOnClickListener(this);

        //initialize layout manager
        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);

        dbHelper = new DbHelper(getActivity()); //supposed to be declared after bundles?
        //retrieve information sent to new fragment
        Bundle b = getArguments();
        if (b != null) {
            deck_id = b.getLong("deck_id");
            deck_title = b.getString("deck_title");
        }
        getActivity().setTitle(deck_title);

        //initialize deckCard for swipe controller
        //implement Swipe Buttons
        cardController = new RecyclerCard(new SwipeControllerActions() {
            //TODO fix edit
            //edit
            @Override
            public void onLeftClicked(int position) {
                removeView();
                edit_position = position;
                alertDialog.setTitle("Edit flashcard");
                et_flashcard.setText(flashcards.get(position).getTitle());
                alertDialog.show();
            }

            //delete
            @Override
            public void onRightClicked(int position) {
                //delete from database using position of card
                dbHelper.deleteItem(flashcards.get(position).getId(), DbHelper.FLASHCARDS_TABLE);
                flashcards.remove(position);
                //refresh
                flashcard_adapter.notifyItemRemoved(position);
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper((cardController));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                cardController.onDraw(c);
            }
        });

        //retrieve array of decks from database
        flashcards = new ArrayList<>(dbHelper.getAllFlashCardsByDeckId(deck_id));

        //initialize adapter after decks array set
        flashcard_adapter = new FlashCardAdapter(this.getActivity(), flashcards);
        recyclerView.setAdapter(flashcard_adapter);

        //click on catogory item
        flashcard_adapter.setOnItemClickListener(new FlashCardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                flashcards.get(position);
                //save all to bundle
                Bundle bundle = new Bundle();
            }
        });

        //initialize dialogue box and adding decks
        initDialog();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addNewFlashCard:
                removeView();
                add = true;
                alertDialog.setTitle("Add Flashcard");       //dialog box title
                et_flashcard.setText("");        //initial text on edit bar
                alertDialog.show();
                break;
        }
    }

    //add new deck
    private void initDialog() {
        alertDialog = new AlertDialog.Builder(getActivity());
        view = getLayoutInflater().inflate(R.layout.fragment_new_flashcard, null);
        alertDialog.setView(view);

        alertDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //adding deck
                if (add) {
                    add = false;
                    FlashCard flashcard = new FlashCard();
                    //if textfield not empty, try catch
                    if (!et_flashcard.getText().toString().isEmpty()) {
                        flashcard.setTitle(et_flashcard.getText().toString());    //set deck title
                        try {
                            Utils.hideKeyboard(getActivity());
                            dbHelper.createFlashCard(flashcard, deck_id);  //db create new deck title
                            //TODO add content and answer
                            flashcards.add(flashcard);
                            //decks = new ArrayList<>(dbHelper.getAllDecksByCategoryId(category_id));

                            //refresh
                            flashcard_adapter.notifyItemInserted(flashcards.size());

                        } catch (Exception ex) {
                            Log.i(LOG_TAG, "Could not create flashcard");
                        }
                    } else {        //if textfield empty, toast
                        showToast(R.string.flashcard_name_missing);
                    }

                    //editing flashcard
                } else {
                    //if textfield not empty, try catch
                    if (!et_flashcard.getText().toString().isEmpty()) {
                        try {
                            Utils.hideKeyboard(getActivity());
                            flashcards.get(edit_position).setTitle(et_flashcard.getText().toString());
                            dbHelper.updateFlashCard(flashcards.get(edit_position));

                            //refresh
                            flashcard_adapter.notifyItemChanged(edit_position);

                        } catch (Exception ex) {
                            Log.i(LOG_TAG, "Could not create flashcard");
                        }
                    } else {        //if textfield empty, toast
                        showToast(R.string.flashcard_name_missing);
                    }
                }
                dialog.dismiss();
            }
        });
        //TODO fix bug et_flashcard_content
        et_flashcard = (EditText) view.findViewById(R.id.et_flashcard_content);    //replaces dialog text by typed text
    }

    //close dialog box
    private void removeView() {
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    private void showToast(int textId) {
        toast = Toast.makeText(getActivity(), textId, Toast.LENGTH_LONG);
        View toastView = toast.getView();
        toastView.getBackground().setColorFilter(getResources().getColor(R.color.toast), PorterDuff.Mode.SRC_IN);
        toast.show();
    }
}

