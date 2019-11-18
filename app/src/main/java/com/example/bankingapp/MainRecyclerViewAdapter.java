package com.example.bankingapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainRecyclerViewAdapter extends
        RecyclerView.Adapter<MainRecyclerViewAdapter.RecyclerViewHolder>{
    private ArrayList<RecyclerGridItems> recyclerGridItems;
    private Context context;

    public MainRecyclerViewAdapter(Context context,ArrayList<RecyclerGridItems> gridItems)
    {
        this.context=context;
        recyclerGridItems=gridItems;

    }
    @NonNull
    @Override
    public MainRecyclerViewAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                         int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_items,
                parent,false);
        MainRecyclerViewAdapter.RecyclerViewHolder recyclerViewHolder=new
                MainRecyclerViewAdapter.RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainRecyclerViewAdapter.RecyclerViewHolder holder,
                                 int position)
    {
        final RecyclerGridItems currentItem=recyclerGridItems.get(position);
        holder.imageView.setImageResource(currentItem.getImageResource());
        holder.textView.setText(currentItem.getAction());
        holder.setIsRecyclable(false);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                switch (currentItem.getAction())
                {
                    case "Open Account":
                        intent= new Intent(context, OpenAccountActivity.class);
                        intent.putExtra("account", currentItem.getAction()); // put image data in Intent
                        context.startActivity(intent); // start Intent
                        break;
                    case "Loans":
                        intent= new Intent(context, MainLoanActivity.class);
                        intent.putExtra("loan", currentItem.getAction()); // put image data in Intent
                        context.startActivity(intent); // start Intent
                        break;
                    case "Deposits":
                        intent= new Intent(context, MainDepositActivity.class);
                        intent.putExtra("deposit", currentItem.getAction()); // put image data in Intent
                        context.startActivity(intent); // start Intent
                        break;
                    case "Cards":
                        intent= new Intent(context, MainCardsActivity.class);
                        intent.putExtra("cards", currentItem.getAction()); // put image data in Intent
                        context.startActivity(intent); // start Intent
                        break;
                    case "Contact Us":
                        intent= new Intent(context, ContactUsActivity.class);
                        intent.putExtra("contact", currentItem.getAction()); // put image data in Intent
                        context.startActivity(intent); // start Intent
                        break;
                    case "Offers":
                        intent= new Intent(context, OfferActivity.class);
                        intent.putExtra("offers", currentItem.getAction()); // put image data in Intent
                        context.startActivity(intent); // start Intent
                        break;

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return recyclerGridItems.size();
    }

    public  static class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView imageView;
        public TextView textView;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image);
            textView=itemView.findViewById(R.id.action);

        }
    }
}
