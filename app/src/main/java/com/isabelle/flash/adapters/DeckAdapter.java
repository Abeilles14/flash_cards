package com.isabelle.flash.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.isabelle.flash.R;
import com.isabelle.flash.models.Deck;

import java.util.ArrayList;

public class DeckAdapter extends RecyclerView.Adapter<DeckAdapter.ViewHolder> {

    private ArrayList<Deck> decks;

    public DeckAdapter(Context context, ArrayList<Deck> list){
        decks = list;
    }

    //single row/card in fragment_category
    //if item in list gets clicked, will activate this class
    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView deck_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //retrieve views from xml
            //extracts individual itemView from ViewHolder, text
            deck_name = itemView.findViewById(R.id.deck_name);

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
    public DeckAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //referring to fragment_categories
        //connected to horizontal layout/row
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_category,viewGroup,false);
        return new ViewHolder(view);   //returns to ViewHolder with the layout of the category row
    }

    //set text on view
    //run for amount of viewHolders/categories in array
    @Override
    public void onBindViewHolder(@NonNull DeckAdapter.ViewHolder viewHolder, int i) {
        //get specific list item tag
        viewHolder.itemView.setTag(decks.get(i));  //use tag in onClick to check which clicked

        //TODO fix null object bug
        viewHolder.deck_name.setText(decks.get(i).getTitle());
    }

    //get number of categories in array list
    @Override
    public int getItemCount() {
        return decks.size();
    }
}
