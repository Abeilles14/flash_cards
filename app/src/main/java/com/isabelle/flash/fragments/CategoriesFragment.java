package com.isabelle.flash.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.isabelle.flash.ApplicationClass;
import com.isabelle.flash.R;
import com.isabelle.flash.adapters.CategoryAdapter;
import com.isabelle.flash.fab.FloatingActionButton;
import com.isabelle.flash.models.Category;

import java.util.ArrayList;

public class CategoriesFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter category_adapter;
    private RecyclerView.LayoutManager layoutManager;
    private View view;

    private FloatingActionButton buttonFab;

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
//        buttonFab = (FloatingActionButton) view.findViewById(R.id.addNewCategory);
//        buttonFab.setColor((getResources().getColor(R.color.action_bar_color)));
//        buttonFab.setTextColor(getResources().getColor(R.color.action_bar_text_color));

        //initialize recycler view from fragment_category
        recyclerView = view.findViewById(R.id.list_cards);
        recyclerView.setHasFixedSize(true);

        //initialize layout manager
        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //TO DELETE
        //new instances of example lists
//        categories = new ArrayList<Category>();
//        categories.add(new Category("Kanji", "Memorize new Japanese Kanji Characters"));
//        categories.add(new Category("Vocabulary", "Rehearse Japanese words"));

        //creating instances of categories, adding to array list
//        for (int i = 0; i < 10; i++) {
//            Category category = new Category("Category " + i,"Category "+ i + " Description");
//            categories.add(category);
//        }

        category_adapter = new CategoryAdapter(this.getActivity(),ApplicationClass.categories);
        recyclerView.setAdapter(category_adapter);
    }
}
