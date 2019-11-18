package com.example.bankingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    TextInputLayout emailInputLayout, passwordInputLayout, confirmPasswordInputLayout;
    TextInputEditText emailEditText, passwordEditText, confirmPasswordEditText;
    ProgressBar progressBar;
    String email, password, confirmPassword;
    boolean isValidCustomer=false;

    @Override
    protected void onStart() {
        super.onStart();
        currentUser=firebaseAuth.getCurrentUser();
        if(currentUser!=null)
        {
            finish();
            Intent intent=new Intent(SignUpActivity.this,
                WelcomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("customers");
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        confirmPasswordEditText = findViewById(R.id.confirm_password_edit_text);
        emailInputLayout = findViewById(R.id.email_text_input);
        passwordInputLayout = findViewById(R.id.password_text_input);
        confirmPasswordInputLayout = findViewById(R.id.confirm_password_text_input);
    }

    public void addCustomer(View view) {
        email = emailEditText.getText().toString();
        password = passwordEditText.getText().toString();
        confirmPassword = confirmPasswordEditText.getText().toString();
        if (email.isEmpty()) {
            emailInputLayout.setError(getString(R.string.error_email));
            emailInputLayout.requestFocus();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInputLayout.setError(getString(R.string.error_invalid_email));
            emailInputLayout.requestFocus();
            return;
        } else {
            emailInputLayout.setError(null);
        }

        if (password.isEmpty()) {
            passwordInputLayout.setError(getString(R.string.error_password));
            passwordInputLayout.requestFocus();
            return;
        } else if (password.length() < 6) {
            passwordInputLayout.setError(getString(R.string.error_invalid_password));
            passwordInputLayout.requestFocus();
            return;
        } else {
            passwordInputLayout.setError(null);
        }

        if (confirmPassword.isEmpty()) {
            confirmPasswordInputLayout.setError(getString(R.string.error_password));
            confirmPasswordInputLayout.requestFocus();
            return;
        } else if (confirmPassword.length() < 6) {
            confirmPasswordInputLayout.setError(getString(R.string.error_confirm_password));
            confirmPasswordInputLayout.requestFocus();
            return;
        } else {
            confirmPasswordInputLayout.setError(null);
        }

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot cid : dataSnapshot.getChildren()) {
                    String value = cid.child("Email")
                            .getValue(String.class);
                    if (email.equals(value)) {
                        isValidCustomer=true;
                        firebaseAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            currentUser= firebaseAuth.getCurrentUser();
                                            finish();
                                            Toast.makeText(SignUpActivity.this, "Email verification link sent!",
                                                    Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(SignUpActivity.this,
                                                    CustomerLoginActivity.class);
                                            startActivity(intent);
                                        }
                                        else {

                                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(),
                                                    Toast.LENGTH_SHORT).show();

                                        }

                                    }
                                });
                        break;
                    }

                }
                if(!isValidCustomer)
                {
                    Toast.makeText(SignUpActivity.this, "Use the email registered with bank",
                            Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void openCustomerLoginActivity(View view) {
        startActivity(new Intent(this, CustomerLoginActivity.class));
    }
}
