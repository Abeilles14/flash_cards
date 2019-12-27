package com.isabelle.flash.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.isabelle.flash.R;
import com.isabelle.flash.adapters.CategoryAdapter;
import com.isabelle.flash.models.Category;

import java.util.ArrayList;

public class CategoriesFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.Adapter category_adapter;
    RecyclerView.LayoutManager layoutManager;
    View view;
    private ArrayList<Category> categories;

    public CategoriesFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_categories,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = view.findViewById(R.id.list_cards);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //TO DELETE
        //new instances of example lists
        categories = new ArrayList<Category>();
        categories.add(new Category("Kanji", "Memorize new Japanese Kanji Characters"));
        categories.add(new Category("Vocabulary", "Rehearse Japanese words"));

//        for (int i = 0; i < 10; i++) {
//            Category category = new Category(getActivity());
//            category.setName(i);
//            category.setDescription(i);
//            categories.add(new Category);
//        }

        category_adapter = new CategoryAdapter(this.getActivity(),categories);
        recyclerView.setAdapter(category_adapter);
    }
}
