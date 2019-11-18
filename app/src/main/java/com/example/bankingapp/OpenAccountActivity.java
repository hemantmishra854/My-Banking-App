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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OpenAccountActivity extends AppCompatActivity {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_account);
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spinner = findViewById(R.id.spinner_select_account_type);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.account_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("account_requests");
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
        applyButton = findViewById(R.id.button_apply_for_account);

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = spinner.getSelectedItem().toString();
                name = nameEditText.getText().toString();
                email = emailEditText.getText().toString();
                mobile = mobileEditText.getText().toString();
                city = cityEditText.getText().toString();
                if (spinner.getSelectedItem().toString().equals("Select Account")) {
                    Toast.makeText(OpenAccountActivity.this, "Select Account",
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
                                Toast.makeText(OpenAccountActivity.this,
                                        "Your account request submitted successfully",
                                        Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(OpenAccountActivity.this,
                                        WelcomeActivity.class);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(OpenAccountActivity.this,
                                        e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
