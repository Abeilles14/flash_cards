package com.isabelle.flash.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.isabelle.flash.R;
import com.isabelle.flash.models.FlashCard;

import java.util.ArrayList;

public class FlashCardAdapter extends RecyclerView.Adapter<FlashCardAdapter.ViewHolder> {

    private ArrayList<FlashCard> flashcards;
    private OnItemClickListener mListener;

    //click interface
    public interface OnItemClickListener {
        void onItemClick(int position);     //pass item clicked position
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public FlashCardAdapter(Context context, ArrayList<FlashCard> list) {
        flashcards = list;
    }

    //single row/card in fragment_category
    //if item in list gets clicked, will activate this class
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView flashcard_name;

        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            //retrieve views from xml
            //extracts individual itemView from ViewHolder, text
            flashcard_name = itemView.findViewById(R.id.flashcard_name);

            //on click
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {  //check if valid
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    //gets layout of the category/row
    @NonNull
    @Override
    public FlashCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //referring to fragment_categories
        //connected to horizontal layout/row
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_flashcard, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view, mListener);
        return viewHolder;   //returns to ViewHolder with the layout of the category row
    }

    //set text on view
    //run for amount of viewHolders/categories in array
    @Override
    public void onBindViewHolder(@NonNull FlashCardAdapter.ViewHolder viewHolder, int i) {
        //get specific list item tag
        viewHolder.itemView.setTag(flashcards.get(i));  //use tag in onClick to check which clicked
        viewHolder.flashcard_name.setText(flashcards.get(i).getTitle());
    }

    //get number of categories in array list
    @Override
    public int getItemCount() {
        return flashcards.size();
    }
}
