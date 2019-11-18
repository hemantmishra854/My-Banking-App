package com.example.bankingapp;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseUser currentUser;
    TextView textViewUser, textViewAccountNo,textViewBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("customers");
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewAccountNo =findViewById(R.id.text_account_no);
        textViewBalance=findViewById(R.id.text_available_balance);
        textViewUser =findViewById(R.id.text_user);

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
                                String acc_no = cid.child("AccountNo").getValue(String.class);
                                String bal = String.valueOf(cid.child("Balance").getValue(Long.class));
                                textViewUser.setText(name);
                                textViewAccountNo.setText(acc_no);
                                textViewBalance.setText(bal);
                            }
                            catch (NullPointerException ex)
                            {
                                Toast.makeText(AccountActivity.this,
                                        ex.getMessage(),
                                        Toast.LENGTH_LONG).show();

                            }
                            catch (Exception ex)
                            {
                                Toast.makeText(AccountActivity.this,
                                        ex.getMessage(),
                                        Toast.LENGTH_LONG).show();

                            }
                            break;
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(AccountActivity.this,databaseError.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        }

    }
}
