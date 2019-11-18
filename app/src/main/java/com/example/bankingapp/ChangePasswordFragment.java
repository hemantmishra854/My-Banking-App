package com.example.bankingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    TextInputLayout passwordInputLayout, confirmPasswordInputLayout;
    TextInputEditText passwordEditText, confirmPasswordEditText;
    ProgressBar progressBar;
    String password, confirmPassword;
    MaterialButton buttonChangePassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_change_password,
                container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = view.findViewById(R.id.progressBar);
        passwordEditText = view.findViewById(R.id.password_edit_text);
        passwordInputLayout = view.findViewById(R.id.password_text_input);
        confirmPasswordEditText = view.findViewById(R.id.confirm_password_edit_text);
        confirmPasswordInputLayout = view.findViewById(R.id.confirm_password_text_input);
        buttonChangePassword = view.findViewById(R.id.changePassword);
        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password = passwordEditText.getText().toString();
                confirmPassword = confirmPasswordEditText.getText().toString();
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
                currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    currentUser.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(),
                                        "password has been changed successfully"
                                        , Toast.LENGTH_LONG).show();
                                firebaseAuth.signOut();
                                Intent intent = new Intent(
                                        getContext(),
                                        CustomerLoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            } else {
                                Toast.makeText(getContext(),
                                        task.getException().getMessage()
                                        , Toast.LENGTH_LONG).show();

                            }

                        }
                    });
                }

            }
        });
        return view;
    }

}
