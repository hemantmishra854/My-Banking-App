package com.example.bankingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

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

public class FundTransferActivity extends AppCompatActivity {

    Spinner spinner;
    String payee,account_no,IFSC,remarks,amount,bank_to, debited_from_account;
    TextInputLayout payeeInputLayout,accountNoInputLayout,iFSCInputLayout,
            remarksInputLayout, amountInputLayout,fromInputLayout;
    TextInputEditText payeeEditText,accountNoEditText,iFSCEditText,amountEditText,
            remarksEditText,fromEditText;
    MaterialButton applyButton;
    ArrayAdapter<CharSequence> adapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,customerDatabaseReference;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    DataSnapshot child_id;
    Long currentAmount,updatedAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_transfer);
        Toolbar toolbar=findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spinner=findViewById(R.id.spinner_select_bank);
        adapter= ArrayAdapter.createFromResource(this,
                R.array.bank_name,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("fund_transfer");
        customerDatabaseReference=firebaseDatabase.getReference("customers");        customerDatabaseReference=firebaseDatabase.getReference("customers");
        firebaseAuth= FirebaseAuth.getInstance();
        payeeEditText=findViewById(R.id.payee_edit_text);
        accountNoEditText=findViewById(R.id.account_no_edit_text);
        iFSCEditText=findViewById(R.id.IFSC_edit_text);
        amountEditText=findViewById(R.id.amount_edit_text);
        remarksEditText =findViewById(R.id.remarks_edit_text);
        fromEditText=findViewById(R.id.from_edit_text);

        progressBar=findViewById(R.id.progressBar);

        payeeInputLayout=findViewById(R.id.payee_text_input);
        accountNoInputLayout=findViewById(R.id.account_no_text_input);
        iFSCInputLayout=findViewById(R.id.IFSC_text_input);
        amountInputLayout=findViewById(R.id.amount_text_input);
        remarksInputLayout =findViewById(R.id.remarks_text_input);
        fromInputLayout=findViewById(R.id.from_text_input);
        applyButton=findViewById(R.id.button_fund_transfer);

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bank_to=spinner.getSelectedItem().toString();
                payee=payeeEditText.getText().toString();
                account_no=accountNoEditText.getText().toString();
                IFSC=iFSCEditText.getText().toString();
                amount=amountEditText.getText().toString();
                remarks= remarksEditText.getText().toString();
                debited_from_account =fromEditText.getText().toString();
                if(spinner.getSelectedItem().toString().equals("Select Bank"))
                {
                    Toast.makeText(FundTransferActivity.this,"Select Bank",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if(payee.isEmpty())
                {
                    payeeInputLayout.setError(getString(
                            R.string.error_payee));
                    payeeInputLayout.requestFocus();
                    return;
                }
                else {
                    payeeInputLayout.setError(null);
                }
                if(account_no.isEmpty())
                {
                    accountNoInputLayout.setError(getString(
                            R.string.error_account_no));
                    accountNoInputLayout.requestFocus();
                    return;
                }
                else {
                    accountNoInputLayout.setError(null);
                }
                if(IFSC.isEmpty())
                {
                    iFSCInputLayout.setError(getString(R.string.error_IFSC));
                    iFSCInputLayout.requestFocus();
                    return;
                }

                else {
                    iFSCInputLayout.setError(null);
                }
                if(amount.isEmpty())
                {
                    amountInputLayout.setError(getString(R.string.error_amount));
                    amountInputLayout.requestFocus();
                    return;
                }

                else {
                    amountInputLayout.setError(null);
                }

                if(debited_from_account.isEmpty())
                {
                    fromInputLayout.setError(getString(R.string.error_bank_from));
                    fromInputLayout.requestFocus();
                    return;
                }
                else {
                    fromInputLayout.setError(null);
                }
                if(Long.parseLong(amount)>currentAmount)
                {
                    amountInputLayout.setError(getString(R.string.error_invalid_amount));
                    amountInputLayout.requestFocus();
                    return;
                }
                else {
                    amountInputLayout.setError(null);
                }

                AlertDialog.Builder builder=new AlertDialog.Builder(
                        FundTransferActivity.this,R.style.ThemeOverlay_MaterialComponents_Dialog);
                builder.setMessage("Are you sure to send money?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                String applicationId=databaseReference.push().getKey();
                                FundTransferDetails fundTransferDetails=new FundTransferDetails(
                                        bank_to, payee,account_no,amount,IFSC,remarks, debited_from_account);
                                progressBar.setVisibility(View.VISIBLE);
                                databaseReference.child(applicationId).setValue(fundTransferDetails)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                progressBar.setVisibility(View.GONE);
                                                finish();
                                                try
                                                {
                                                    updatedAmount=currentAmount-
                                                            Long.parseLong(amount);
                                                }
                                                catch (NullPointerException ex)
                                                {
                                                    Toast.makeText(FundTransferActivity.this,
                                                            ex.getMessage(),
                                                            Toast.LENGTH_LONG).show();

                                                }
                                                catch (Exception ex)
                                                {
                                                    Toast.makeText(FundTransferActivity.this,
                                                            ex.getMessage(),
                                                            Toast.LENGTH_LONG).show();

                                                }

                                                customerDatabaseReference.child(child_id.getKey())
                                                        .child("Balance")
                                                        .setValue(updatedAmount);
                                                Toast.makeText(FundTransferActivity.this,
                                                        "fund has been transferred successfully",
                                                        Toast.LENGTH_LONG).show();
                                                Intent intent=new Intent(FundTransferActivity.this,
                                                        WelcomeActivity.class);
                                                startActivity(intent);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(FundTransferActivity.this,
                                                        e.getMessage(),
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alertDialog=builder.create();
                alertDialog.setTitle("Banking App");
                alertDialog.show();

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
    @Override
    protected void onStart() {
        super.onStart();
        currentUser=firebaseAuth.getCurrentUser();
        if(currentUser!=null) {
            customerDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot cid : dataSnapshot.getChildren()) {
                        child_id=cid;
                        String value = cid.child("Email")
                                .getValue(String.class);
                        if (currentUser.getEmail().equals(value)) {
                            fromEditText.setText(cid.child("AccountNo")
                                    .getValue(String.class));
                            currentAmount=cid.child("Balance")
                                    .getValue(Long.class);
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
