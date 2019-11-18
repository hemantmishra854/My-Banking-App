package com.example.bankingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPasswordActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    TextInputLayout emailInputLayout;
    TextInputEditText emailEditText;
    ProgressBar progressBar;
    String email;
    MaterialButton buttonResetPassword;
    boolean isValidCustomer=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        emailEditText = findViewById(R.id.email_edit_text);
        emailInputLayout = findViewById(R.id.email_text_input);
        buttonResetPassword = findViewById(R.id.resetPassword);

        buttonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailEditText.getText().toString();
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
                firebaseAuth.sendPasswordResetEmail(email)
                     .addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {
                             if (task.isSuccessful())
                             {
                                 Toast.makeText(ForgotPasswordActivity.this,
                                         "password reset link sent to your email",
                                         Toast.LENGTH_LONG).show();
                                 finish();
                                 Intent intent=new Intent(ForgotPasswordActivity.this,
                                         CustomerLoginActivity.class);
                                 startActivity(intent);
                             }
                             else {
                                 Toast.makeText(ForgotPasswordActivity.this,
                                         task.getException().getMessage(),
                                         Toast.LENGTH_LONG).show();

                             }
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
