package com.example.bankingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    ImageSliderAdapter imageSliderAdapter;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar=findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        viewPager=findViewById(R.id.viewPager);
        imageSliderAdapter=new ImageSliderAdapter(this);
        viewPager.setAdapter(imageSliderAdapter);

        firebaseAuth = FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressBar);
        ArrayList<RecyclerGridItems> arrayList=new ArrayList<>();
        RecyclerGridItems recyclerGridItems1=new RecyclerGridItems(
                R.drawable.ic_account_balance, "Open Account");
        RecyclerGridItems recyclerGridItems2=new RecyclerGridItems(
                R.drawable.ic_description, "Loans");
        RecyclerGridItems recyclerGridItems3=new RecyclerGridItems(
                R.drawable.ic_view_list, "Deposits");
        RecyclerGridItems recyclerGridItems4=new RecyclerGridItems(
                R.drawable.ic_credit_card, "Cards");
        RecyclerGridItems recyclerGridItems5=new RecyclerGridItems(
                R.drawable.ic_local_offer, "Offers");
        RecyclerGridItems recyclerGridItems6=new RecyclerGridItems(
                R.drawable.ic_phone, "Contact Us");
        arrayList.add(recyclerGridItems1);
        arrayList.add(recyclerGridItems2);
        arrayList.add(recyclerGridItems3);
        arrayList.add(recyclerGridItems4);
        arrayList.add(recyclerGridItems5);
        arrayList.add(recyclerGridItems6);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,
                3, GridLayoutManager.VERTICAL, false));
        adapter = new MainRecyclerViewAdapter(this,arrayList);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = firebaseAuth.getCurrentUser();
        if(currentUser!=null)
        {   finish();
            startActivity(new Intent(this,WelcomeActivity.class));

        }
    }


    public void openCustomerLoginActivity(View view) {
        startActivity(new Intent(this,CustomerLoginActivity.class));
    }
    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(
                MainActivity.this,
                R.style.ThemeOverlay_MaterialComponents_Dialog);
        builder.setMessage("Are you sure to exit?").
                setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface,
                                                int i) {
                                MainActivity.super.onBackPressed();
                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle("Banking App");
        alertDialog.show();
    }


}
