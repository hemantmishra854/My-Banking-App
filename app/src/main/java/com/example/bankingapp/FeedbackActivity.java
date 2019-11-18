package com.example.bankingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FeedbackActivity extends AppCompatActivity {

  FirebaseAuth firebaseAuth;
  FirebaseDatabase database;
  DatabaseReference databaseReference;
  FirebaseUser currentUser;
  TextView textViewUser, textViewAccountNo,textViewBalance;
  RadioGroup radioGroup;
  MaterialRadioButton radioButton;
  TextInputEditText feedbackEditText;
  TextInputLayout feedbackInputLayout;
  String feedback,rating;
  MaterialButton submitButton;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_feedback);
    database = FirebaseDatabase.getInstance();
    databaseReference = database.getReference("feedback_requests");
    firebaseAuth = FirebaseAuth.getInstance();
    currentUser = firebaseAuth.getCurrentUser();
    Toolbar toolbar = findViewById(R.id.app_bar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    submitButton =findViewById(R.id.button_submit);
    feedbackInputLayout =findViewById(R.id.feedback_text_input);
    feedbackEditText =findViewById(R.id.feedback_edit_text);
    radioGroup =findViewById(R.id.radioGroup);
    radioGroup =findViewById(R.id.radioGroup);
    int checkedRadioButton=radioGroup.getCheckedRadioButtonId();
    radioButton=findViewById(checkedRadioButton);
    submitButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        feedback=feedbackEditText.getText().toString();
        rating=radioButton.getText().toString();
        if (feedback.isEmpty()) {
          feedbackInputLayout.setError(getString(R.string.error_feedback));
          feedbackInputLayout.requestFocus();
          return;
        }
        else {
          feedbackInputLayout.setError(null);
        }
        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
          String applicationId=databaseReference.push().getKey();
          FeedbackDetails details=new FeedbackDetails(rating,feedback,
                  currentUser.getEmail());
          databaseReference.child(applicationId).setValue(details)
                  .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
              finish();
              Toast.makeText(FeedbackActivity.this,
                      "Thanks for your feedback",
                      Toast.LENGTH_LONG).show();
              startActivity(new Intent(FeedbackActivity.this,
                      WelcomeActivity.class));

            }
          })
          .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
              Toast.makeText(FeedbackActivity.this,
                      e.getMessage(),
                      Toast.LENGTH_LONG).show();
            }
          });

        }

      }
    });


  }
}
