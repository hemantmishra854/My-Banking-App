package com.example.bankingapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    ProgressBar progressBar;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    TextView textViewUser, textViewUserEmail;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container,
                false);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("customers");
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        textViewUserEmail=view.findViewById(R.id.text_user_email);
        textViewUser =view.findViewById(R.id.text_user);

        ArrayList<RecyclerGridItems> arrayList=new ArrayList<>();
        RecyclerGridItems recyclerGridItems1=new RecyclerGridItems(
                R.drawable.ic_account_balance, "Account");
        RecyclerGridItems recyclerGridItems2=new RecyclerGridItems(
                R.drawable.ic_description, "Loans");
        RecyclerGridItems recyclerGridItems3=new RecyclerGridItems(
                R.drawable.ic_view_list, "Deposits");
        RecyclerGridItems recyclerGridItems4=new RecyclerGridItems(
                R.drawable.ic_credit_card, "Cards");
        RecyclerGridItems recyclerGridItems5=new RecyclerGridItems(
                R.drawable.ic_feedback, "Feedback");
        RecyclerGridItems recyclerGridItems6=new RecyclerGridItems(
                R.drawable.ic_attach_money, "Fund Transfer");
        arrayList.add(recyclerGridItems1);
        arrayList.add(recyclerGridItems2);
        arrayList.add(recyclerGridItems3);
        arrayList.add(recyclerGridItems4);
        arrayList.add(recyclerGridItems5);
        arrayList.add(recyclerGridItems6);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),
                3, GridLayoutManager.VERTICAL, false));
        adapter = new WelcomeRecyclerAdapter(getContext(),arrayList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot cid : dataSnapshot.getChildren()) {
                        String value = cid.child("Email")
                                .getValue(String.class);
                        if (currentUser.getEmail().equals(value)) {
                            try {
                                String firstName = cid.child("FirstName").getValue(String.class);
                                String lastName = cid.child("LastName").getValue(String.class);
                                String name = firstName + " " + lastName;
                                textViewUser.setText(name);
                                textViewUserEmail.setText("signed as: "+value);
                            }
                            catch (NullPointerException ex)
                            {
                                Toast.makeText(getContext(),
                                        ex.getMessage(),
                                        Toast.LENGTH_LONG).show();

                            }
                            catch (Exception ex)
                            {
                                Toast.makeText(getContext(),
                                        ex.getMessage(),
                                        Toast.LENGTH_LONG).show();

                            }
                            break;
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(getContext(),databaseError.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        }

    }

}
