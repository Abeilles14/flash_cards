package com.isabelle.flash.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.isabelle.flash.R;
import com.isabelle.flash.adapters.CategoryAdapter;

import android.support.design.widget.FloatingActionButton;
import android.widget.EditText;

import com.isabelle.flash.cards.CategoryCard;
import com.isabelle.flash.models.Category;

import java.util.ArrayList;

public class CategoriesFragment extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter category_adapter;
    private RecyclerView.LayoutManager layoutManager;
    private View view;

    private FloatingActionButton buttonFab;
    private ArrayList<Category> categories;
    CategoryCard cardController = null;

    //edit/add dialog
    private AlertDialog.Builder alertDialog;
    private EditText et_category;
    private boolean add = false;
    private int edit_position;

    public CategoriesFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //initialize FloatingActionButton and set colors
        buttonFab = (FloatingActionButton) view.findViewById(R.id.addNewCategory);
        buttonFab.setOnClickListener(this);

        //initialize recycler view from fragment_category
        recyclerView = view.findViewById(R.id.list_cards);
        recyclerView.setHasFixedSize(true);

        //initialize layout manager
        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //initialize CategoryCard for swipe controller
        //implement Swipe Buttons

        cardController = new CategoryCard(getContext());

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper((cardController));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                cardController.onDraw(c);
            }
        });

        //categories = new ArrayList<Category>();

        //creating instances of categories, adding to array list
//        for (int i = 0; i < categories.size(); i++) {
//
//            cardController = new CategoryCard(getActivity(), categories.get(i));
//
//            cardController.setId(categories.get(i).getId());
//            cardController.setCategory(categories.get(i));
//            categories.add(category);
//        }

        categories = new ArrayList<Category>();
        for (int i = 1; i < 9; i++) {
            //new CategoryCard card, setId, setCategory, add to array
            categories.add(new Category("Category " + i));
        }

        //initialize adapter
        category_adapter = new CategoryAdapter(this.getActivity(), categories);
        recyclerView.setAdapter(category_adapter);

        initDialog();
    }


    //TODO
    //on add buttonFab click
    @Override
    public void onClick(View v) {
        //action - add category
        switch (v.getId()) {
            case R.id.addNewCategory:
                removeView();
                add = true;
                alertDialog.setTitle("Add Category");
                et_category.setText("");
                alertDialog.show();
                break;
        }
    }

    private void initDialog() {
        alertDialog = new AlertDialog.Builder(getActivity());
        view = getLayoutInflater().inflate(R.layout.fragment_new_category, null);
        alertDialog.setView(view);
        alertDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (add) {
                    add = false;
                    //add
                    //adapter.addItem(et_category.getText().toString());
                    dialog.dismiss();
                } else {
                    //edit
                    //set categories array
                    //countries.set(edit_position, et_category.getText().toString());
                    //notify adapter, refresh
                    category_adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }

            }
        });
        et_category = (EditText) view.findViewById(R.id.et_country);
    }

    //close dialog box
    private void removeView() {
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }


//    @Override
//    public void onClick(View v) {
//        if(v.getId() == buttonFab.getId()) {
//            ((MainActivity) getActivity()).displayView(MainActivity.NEW_CATEGORY_FRAG, null);
//        }
}



