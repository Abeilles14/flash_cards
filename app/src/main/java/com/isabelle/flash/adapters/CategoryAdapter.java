package com.isabelle.flash.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.isabelle.flash.R;
import com.isabelle.flash.models.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private ArrayList<Category> categories;


    //constructor
    //context = activity
    public CategoryAdapter(Context context, ArrayList<Category> list){
        categories = list;
    }

    //single row/card in fragment_category
    //if item in list gets clicked, will activate this class
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView category_name, category_description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //retrieve views from xml
            //extracts individual itemView from ViewHolder, text
            category_name = itemView.findViewById(R.id.category_name);
            category_description = itemView.findViewById(R.id.category_description);

            //on click
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    //stuff here later
                }
            });
        }
    }

    //gets layout of the category/row
    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //referring to fragment_categories
        //connected to horizontal layout/row
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_row_layout,viewGroup,false);
        return new ViewHolder(view);   //returns to ViewHolder with the layout of the category row
    }

    //set text on view
    //run for amount of viewHolders/categories in array
    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder viewHolder, int i) {
        //get specific list item tag
        viewHolder.itemView.setTag(categories.get(i));  //use tag in onClick to check which clicked

        viewHolder.category_name.setText(categories.get(i).getName());
        viewHolder.category_description.setText(categories.get(i).getDescription());
    }

    //get number of categories in array list
    @Override
    public int getItemCount() {
        return categories.size();
    }
}
