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
import com.isabelle.flash.adapters.DeckAdapter;
import com.isabelle.flash.cards.RecyclerCard;
import com.isabelle.flash.controllers.SwipeControllerActions;
import com.isabelle.flash.database.DbHelper;
import com.isabelle.flash.models.Deck;
import com.isabelle.flash.navDrawer.Utils;
import java.util.ArrayList;

public class DecksFragment extends Fragment implements View.OnClickListener {

    private static final String LOG_TAG = "Deck Fragment";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter deck_adapter;
    private RecyclerView.LayoutManager layoutManager;
    private View view;

    private FloatingActionButton buttonFab;
    private ArrayList<Deck> decks;
    private RecyclerCard cardController = null;
    private DbHelper dbHelper;

    private AlertDialog.Builder alertDialog;
    private EditText et_deck;
    private Toast toast;
    private boolean add = false;
    private int edit_position;

    private String category_title;
    private long category_id;

    public DecksFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_decks, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //initialize FloatingActionButton and set colors
        buttonFab = (FloatingActionButton) view.findViewById(R.id.addNewDeck);
        buttonFab.setOnClickListener(this);

        //initialize recycler view from fragment_deck
        recyclerView = view.findViewById(R.id.list_cards);
        recyclerView.setHasFixedSize(true);

        //initialize layout manager
        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);

        dbHelper = new DbHelper(getActivity()); //supposed to be declared after bundles?
        Bundle b = getArguments();
        category_title = b.getString(dbHelper.TITLE);
        category_id = b.getLong(dbHelper._ID);
        getActivity().setTitle(category_title);

        //initialize deckCard for swipe controller
        //implement Swipe Buttons
        cardController = new RecyclerCard(new SwipeControllerActions() {
            //edit
            @Override
            public void onLeftClicked(int position) {
                //TODO edit deck
                removeView();
                edit_position = position;
                alertDialog.setTitle("Edit deck");
                et_deck.setText(decks.get(position).getTitle());
                alertDialog.show();
            }

            //delete
            @Override
            public void onRightClicked(int position) {
                //delete from database using position of card
                dbHelper.deleteItem(decks.get(position).getId(), DbHelper.DECKS_TABLE);
                decks.remove(position);
                //refresh
                deck_adapter.notifyItemRemoved(position);
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
        decks = new ArrayList<Deck>(dbHelper.getAllDecksByCategoryId(category_id));

        //initialize adapter? (after decks array set
        deck_adapter = new DeckAdapter(this.getActivity(), decks);
        recyclerView.setAdapter(deck_adapter);
        //initialize dialogue box and adding decks
        initDialog();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addNewDeck:
                removeView();
                add = true;
                alertDialog.setTitle("Add deck");       //dialog box title
                et_deck.setText("");        //initial text on edit bar
                alertDialog.show();
                break;
            //TODO
            //case id deck card, on click deck go to fragment
        }
    }

    //add new deck
    private void initDialog() {
        alertDialog = new AlertDialog.Builder(getActivity());
        view = getLayoutInflater().inflate(R.layout.fragment_new_deck, null);
        alertDialog.setView(view);

        alertDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //adding deck
                if (add) {
                    add = false;
                    Deck deck = new Deck();
                    //if textfield not empty, try catch
                    if (!et_deck.getText().toString().isEmpty()) {
                        deck.setTitle(et_deck.getText().toString());    //set deck title
                        try {
                            Utils.hideKeyboard(getActivity());
                            dbHelper.createDeck(deck,category_id);  //db create new deck title
                            decks = new ArrayList<Deck>(dbHelper.getAllDecksByCategoryId(category_id));

                            //refresh
                            deck_adapter.notifyItemInserted(decks.size());

                        } catch (Exception ex) {
                            Log.i(LOG_TAG, "Could not create deck");
                        }
                    } else {        //if textfield empty, toast
                        showToast(R.string.deck_name_missing);
                    }

                    //editing deck
                } else {
                    //if textfield not empty, try catch
                    if (!et_deck.getText().toString().isEmpty()) {
                        try {
                            Utils.hideKeyboard(getActivity());
                            decks.get(edit_position).setTitle(et_deck.getText().toString());
                            dbHelper.updateDeck(decks.get(edit_position));

                            //refresh
                            deck_adapter.notifyItemChanged(edit_position);

                        } catch (Exception ex) {
                            Log.i(LOG_TAG, "Could not create deck");
                        }
                    } else {        //if textfield empty, toast
                        showToast(R.string.deck_name_missing);
                    }
                }
                dialog.dismiss();
            }
        });
        et_deck = (EditText) view.findViewById(R.id.et_deck);    //replaces dialog text by typed text
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

