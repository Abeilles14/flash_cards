package com.isabelle.flash.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.isabelle.flash.R;
import com.isabelle.flash.adapters.CategoryAdapter;

import android.support.design.widget.FloatingActionButton;
import android.widget.EditText;
import android.widget.Toast;

import com.isabelle.flash.cards.CategoryCard;
import com.isabelle.flash.database.DbHelper;
import com.isabelle.flash.models.Category;
import com.isabelle.flash.navDrawer.MainActivity;
import com.isabelle.flash.navDrawer.Utils;

import java.util.ArrayList;

public class CategoriesFragment extends Fragment implements View.OnClickListener {

    private static final String LOG_TAG = "Category Fragment";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter category_adapter;
    private RecyclerView.LayoutManager layoutManager;
    private View view;

    private FloatingActionButton buttonFab;
    private ArrayList<Category> categories;
    CategoryCard cardController = null;
    DbHelper dbHelper;

    //edit/add dialog
    private AlertDialog.Builder alertDialog;
    private EditText et_category;
    private boolean add = false;

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
        dbHelper = new DbHelper(getActivity());


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper((cardController));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                cardController.onDraw(c);
            }
        });

        //retrieve array of categories from database
        categories = new ArrayList<>(dbHelper.getAllCategories());

        //initialize adapter? (after categories array set
        category_adapter = new CategoryAdapter(this.getActivity(), categories);
        recyclerView.setAdapter(category_adapter);

        //initialize dialogue box and adding categories
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
                alertDialog.setTitle("Add Category");       //dialog box title
                et_category.setText("");        //initial text on edit bar
                alertDialog.show();
                break;
            //TODO
            //case category, on click category go to fragment
        }
    }

    //add new categort
    private void initDialog() {
        alertDialog = new AlertDialog.Builder(getActivity());
        view = getLayoutInflater().inflate(R.layout.fragment_new_category, null);
        alertDialog.setView(view);

        alertDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (add) {
                    add = false;
                    //add to category
                    //category_adapter.addItem(et_category.getText().toString());
                    //countries.set(edit_position, et_category.getText().toString());
                    //category_adapter.notifyDataSetChanged();    //refresh recyclerview

                    //new helper?
                    Category category = new Category();

                    //if textfield not empty, try catch
                    if (!et_category.getText().toString().isEmpty()) {
                        category.setTitle(et_category.getText().toString());    //set category title
                        try {
                            Utils.hideKeyboard(getActivity());
                            dbHelper.createCategory(category);  //db create new category title

                            //open fragment?? add new category? add new card?
                            categories.add(category);
                            category_adapter.notifyDataSetChanged();    //doesnt do anything?
                            System.out.println(categories.size());

                            //refresh?
                        } catch (Exception ex) {
                            Log.i(LOG_TAG, "Could not create category");
                        }
                    } else {        //if textfield empty, toast
                        Toast.makeText(getActivity(), "Category must have a name.", Toast.LENGTH_LONG).show();
                    }


                    dialog.dismiss();
                } else {      //not sure what this does
                    //countries.set(edit_position, et_category.getText().toString());
                    category_adapter.notifyDataSetChanged();    //refresh recyclerview
                    dialog.dismiss();
                }
            }
        });
        et_category = (EditText) view.findViewById(R.id.et_country);    //replaces dialog text by typed text
    }

    //close dialog box
    private void removeView() {
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }
}



