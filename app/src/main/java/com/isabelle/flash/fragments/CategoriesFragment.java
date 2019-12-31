package com.isabelle.flash.fragments;

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

import com.isabelle.flash.cards.CategoryCard;
import com.isabelle.flash.models.Category;

import java.util.ArrayList;

public class CategoriesFragment extends Fragment implements View.OnClickListener{

    private RecyclerView recyclerView;
    private RecyclerView.Adapter category_adapter;
    private RecyclerView.LayoutManager layoutManager;
    private View view;

    private FloatingActionButton buttonFab;
    private ArrayList<Category> categories;
    CategoryCard cardController = null;

    public CategoriesFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_categories,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //initialize FloatingActionButton and set colors
        buttonFab = (FloatingActionButton) view.findViewById(R.id.addNewCategory);
        buttonFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //action - add category
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        //initialize recycler view from fragment_category
        recyclerView = view.findViewById(R.id.list_cards);
        recyclerView.setHasFixedSize(true);

        //initialize layout manager
        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //initialize CategoryCard for swipe controller
        //implement Swipe Buttons
        cardController = new CategoryCard(getContext());

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper( (cardController));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration(){
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                cardController.onDraw(c);
            }
        });

        //creating instances of categories, adding to array list
//        for (int i = 0; i < categories.size(); i++) {
//            CategoryCard card = new CategoryCard(getActivity());
//            card.setId(String.valueOf(categories.get(i).getId()));
//            card.setCategory(categories.get(i));
//            mCardsList.add(card);
//        }
        categories = new ArrayList<Category>();
            for (int i = 1; i < 9; i++) {
                //new CategoryCard card, setId, setCategory, add to array
                categories.add(new Category("Category " + i));
            }

        //initialize adapter
        category_adapter = new CategoryAdapter(this.getActivity(),categories);
        recyclerView.setAdapter(category_adapter);
    }

    //TODO
    //on click
    @Override
    public void onClick(View v) {
//        if(v.getId() == buttonFab.getId()) {
//            ((MainActivity) getActivity()).displayView(MainActivity.NEW_CATEGORY_FRAG, null);
//        }
    }
}
