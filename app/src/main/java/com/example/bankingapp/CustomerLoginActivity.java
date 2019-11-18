package com.example.bankingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerLoginActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    TextInputLayout emailInputLayout, passwordInputLayout;
    TextInputEditText emailEditText, passwordEditText;
    ProgressBar progressBar;
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);

        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        emailInputLayout = findViewById(R.id.email_text_input);
        passwordInputLayout = findViewById(R.id.password_text_input);
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = firebaseAuth.getCurrentUser();
        if(currentUser!=null)
        {   finish();
            Intent intent=new Intent(this,WelcomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    public void validateCustomer(View view) {
        email=emailEditText.getText().toString();
        password=passwordEditText.getText().toString();

        if (email.isEmpty()) {
            emailInputLayout.setError(getString(R.string.error_email));
            emailInputLayout.requestFocus();
            return;
        }
        else {
            emailInputLayout.setError(null);
        }
        if (password.isEmpty()) {
            passwordInputLayout.setError(getString(R.string.error_password));
            passwordInputLayout.requestFocus();
            return;
        }
        else {
            passwordInputLayout.setError(null);
        }
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            currentUser = firebaseAuth.getCurrentUser();
                            finish();
                            Intent intent=new Intent(CustomerLoginActivity.this,
                                    WelcomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {
                            Toast.makeText(CustomerLoginActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }

    public void openSignUpActivity(View view) {
        startActivity(new Intent(this,SignUpActivity.class));
    }

    public void openForgotPasswordActivity(View view) {
        startActivity(new Intent(this,ForgotPasswordActivity.class));
    }
}
