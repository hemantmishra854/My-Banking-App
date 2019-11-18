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

public class WelcomeRecyclerAdapter extends RecyclerView.Adapter<WelcomeRecyclerAdapter.RecyclerViewHolder> {
    private ArrayList<RecyclerGridItems> recyclerGridItems;
    private Context context;

    public WelcomeRecyclerAdapter(Context context, ArrayList<RecyclerGridItems> gridItems) {
        this.context = context;
        recyclerGridItems = gridItems;

    }

    @NonNull
    @Override
    public WelcomeRecyclerAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                        int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_items,
                parent, false);
        WelcomeRecyclerAdapter.RecyclerViewHolder recyclerViewHolder = new
                WelcomeRecyclerAdapter.RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WelcomeRecyclerAdapter.RecyclerViewHolder holder,
                                 int position) {
        final RecyclerGridItems currentItem = recyclerGridItems.get(position);
        holder.imageView.setImageResource(currentItem.getImageResource());
        holder.textView.setText(currentItem.getAction());
        holder.setIsRecyclable(false);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                switch (currentItem.getAction()) {
                    case "Account":
                        intent = new Intent(context, AccountActivity.class);
                        intent.putExtra("account", currentItem.getAction()); // put image data in Intent
                        context.startActivity(intent); // start Intent
                        break;
                    case "Loans":
                        intent = new Intent(context, LoanActivity.class);
                        intent.putExtra("loan", currentItem.getAction()); // put image data in Intent
                        context.startActivity(intent); // start Intent
                        break;
                    case "Deposits":
                        intent = new Intent(context, DepositActivity.class);
                        intent.putExtra("deposit", currentItem.getAction()); // put image data in Intent
                        context.startActivity(intent); // start Intent
                        break;
                    case "Cards":
                        intent = new Intent(context, CardServicesActivity.class);
                        intent.putExtra("cards", currentItem.getAction()); // put image data in Intent
                        context.startActivity(intent); // start Intent
                        break;
                    case "Feedback":
                        intent = new Intent(context, FeedbackActivity.class);
                        intent.putExtra("feedback", currentItem.getAction()); // put image data in Intent
                        context.startActivity(intent); // start Intent
                        break;
                    case "Fund Transfer":
                        intent = new Intent(context, FundTransferActivity.class);
                        intent.putExtra("fund", currentItem.getAction()); // put image data in Intent
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

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            textView = itemView.findViewById(R.id.action);

        }
    }
}
