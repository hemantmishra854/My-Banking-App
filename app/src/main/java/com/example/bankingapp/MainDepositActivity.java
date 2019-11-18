package com.example.bankingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainDepositActivity extends AppCompatActivity {

    Spinner spinner;
    String name, email, mobile, type, city;
    TextInputLayout nameInputLayout, emailInputLayout, mobileInputLayout,
            cityInputLayout;
    TextInputEditText nameEditText, mobileEditText, emailEditText, cityEditText;
    MaterialButton applyButton;
    ArrayAdapter<CharSequence> adapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, customerDatabaseReference;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_deposit);
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spinner = findViewById(R.id.spinner_select_deposit);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.deposit_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("deposit_requests");
        customerDatabaseReference = firebaseDatabase.getReference("customers");
        firebaseAuth = FirebaseAuth.getInstance();
        nameEditText = findViewById(R.id.name_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        mobileEditText = findViewById(R.id.mobile_edit_text);
        cityEditText = findViewById(R.id.city_edit_text);
        progressBar = findViewById(R.id.progressBar);

        nameInputLayout = findViewById(R.id.name_text_input);
        emailInputLayout = findViewById(R.id.email_text_input);
        mobileInputLayout = findViewById(R.id.mobile_text_input);
        cityInputLayout = findViewById(R.id.city_text_input);
        applyButton = findViewById(R.id.button_apply_for_deposit);

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = spinner.getSelectedItem().toString();
                name = nameEditText.getText().toString();
                email = emailEditText.getText().toString();
                mobile = mobileEditText.getText().toString();
                city = cityEditText.getText().toString();
                if (spinner.getSelectedItem().toString().equals("Select Deposit")) {
                    Toast.makeText(MainDepositActivity.this, "Select Deposit",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (name.isEmpty()) {
                    nameInputLayout.setError(getString(
                            R.string.error_name));
                    nameInputLayout.requestFocus();
                    return;
                } else {
                    nameInputLayout.setError(null);
                }
                if (email.isEmpty()) {
                    emailInputLayout.setError(getString(
                            R.string.error_email));
                    emailInputLayout.requestFocus();
                    return;
                } else {
                    emailInputLayout.setError(null);
                }
                if (mobile.isEmpty()) {
                    mobileInputLayout.setError(getString(R.string.error_mobile_no));
                    mobileInputLayout.requestFocus();
                    return;
                } else {
                    mobileInputLayout.setError(null);
                }
                if (city.isEmpty()) {
                    cityInputLayout.setError(getString(R.string.error_ciy));
                    cityInputLayout.requestFocus();
                    return;
                } else {
                    cityInputLayout.setError(null);
                }
                String applicationId = databaseReference.push().getKey();
                CustomerDetails fundTransferDetails = new CustomerDetails(
                        type, name, email, mobile, city);
                progressBar.setVisibility(View.VISIBLE);
                databaseReference.child(applicationId).setValue(fundTransferDetails)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressBar.setVisibility(View.GONE);
                                finish();
                                Toast.makeText(MainDepositActivity.this,
                                        "Your deposit request submitted successfully",
                                        Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MainDepositActivity.this,
                                        WelcomeActivity.class);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainDepositActivity.this,
                                        e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            customerDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot cid : dataSnapshot.getChildren()) {
                        String value = cid.child("Email")
                                .getValue(String.class);
                        if (currentUser.getEmail().equals(value)) {

                            String firstName = cid.child("FirstName").getValue(String.class);
                            String lastName = cid.child("LastName").getValue(String.class);
                            String name = firstName + " " + lastName;
                            nameEditText.setText(name);
                            emailEditText.setText(cid.child("Email")
                                    .getValue(String.class));
                            mobileEditText.setText(String.valueOf(cid.child("Mobile")
                                    .getValue(Long.class)));
                            cityEditText.setText(cid.child("City")
                                    .getValue(String.class));
                            break;
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }
}
