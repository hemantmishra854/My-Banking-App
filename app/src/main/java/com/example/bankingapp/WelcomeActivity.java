package com.example.bankingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class WelcomeActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    DrawerLayout dLayout;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    TextView textViewUser, textViewUserEmail;
    ImageView profileImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("customers");
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        dLayout=findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,dLayout,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        dLayout.addDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dLayout.openDrawer(Gravity.LEFT);
            }
        });
        setNavigationDrawer();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame,
                    new HomeFragment()).commit();
            NavigationView navigationView = findViewById(R.id.navigation_view);
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    private void setNavigationDrawer() {
        dLayout = findViewById(R.id.drawer_layout); // initiate a DrawerLayout
        NavigationView navView = findViewById(R.id.navigation_view);
        // initiate a Navigation View
        View header = navView.getHeaderView(0);
        textViewUser = header.findViewById(R.id.text_user);
        textViewUserEmail = header.findViewById(R.id.text_user_email);
        profileImg = header.findViewById(R.id.image_profile);
        if (currentUser != null) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot cid : dataSnapshot.getChildren()) {
                        String value = cid.child("Email")
                                .getValue(String.class);
                        if (currentUser.getEmail().equals(value)) {
                            String firstName = cid.child("FirstName").getValue(String.class);
                            String lastName = cid.child("LastName").getValue(String.class);
                            String name = firstName + " " + lastName;
                            textViewUser.setText(name);
                            textViewUserEmail.setText("signed as: " + value);
                            break;
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WelcomeActivity.this,
                        ProfileActivity.class));
            }
        });

        // implement setNavigationItemSelectedListener event on NavigationView
        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        Fragment frag = null; // create a Fragment Object
                        int itemId = menuItem.getItemId(); // get selected menu item's id
                        // check selected menu item's id and replace a Fragment Accordingly
                        if (itemId == R.id.nav_home) {
                            frag = new HomeFragment();
                        } else if (itemId == R.id.nav_contact_us) {
                            frag = new ContactUsFragment();
                        }
                        else if (itemId == R.id.nav_FAQs) {
                            frag = new FAQsFragment();
                        }
                        else if (itemId == R.id.nav_change_password) {
                            frag = new ChangePasswordFragment();
                        }
                        else if (itemId == R.id.nav_de_register) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(
                                    WelcomeActivity.this,
                                    R.style.ThemeOverlay_MaterialComponents_Dialog);
                            builder.setMessage("Are you sure to delete account?").
                                    setCancelable(false)
                                    .setPositiveButton("Yes",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface,
                                                                    int i) {
                                                    currentUser.delete().addOnCompleteListener(
                                                            new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Toast.makeText(WelcomeActivity.this,
                                                                                "Account deleted! ",
                                                                                Toast.LENGTH_LONG).show();
                                                                        finish();
                                                                        Intent intent = new Intent(WelcomeActivity.this,
                                                                                MainActivity.class);
                                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                        startActivity(intent);
                                                                    } else {
                                                                        Toast.makeText(WelcomeActivity.this,
                                                                                task.getException().getMessage(),
                                                                                Toast.LENGTH_LONG).show();
                                                                    }
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
                            AlertDialog alertDialog = builder.create();
                            alertDialog.setTitle("Banking App");
                            alertDialog.show();


                        } else if (itemId == R.id.nav_logout) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(
                                    WelcomeActivity.this,
                                    R.style.ThemeOverlay_MaterialComponents_Dialog);
                            builder.setMessage("Are you sure to logout?").
                                    setCancelable(false)
                                    .setPositiveButton("Yes",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface,
                                                                    int i) {

                                                    firebaseAuth.getInstance().signOut();
                                                    finish();
                                                    Intent intent = new Intent(WelcomeActivity.this,
                                                            MainActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);

                                                }
                                            })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.setTitle("Banking App");
                            alertDialog.show();

                        }
                        // display a toast message with menu item's title
                        Toast.makeText(getApplicationContext(),
                                menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                        if (frag != null) {
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.frame, frag); // replace a Fragment with Frame Layout
                            transaction.commit(); // commit the changes
                            dLayout.closeDrawers(); // close the all open Drawer Views
                            return true;
                        }
                        return false;
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if(dLayout.isDrawerOpen(GravityCompat.START))
        {
            dLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.action_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));

        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ic_search:
                Toast.makeText(this, "Search is selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ic_logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        WelcomeActivity.this,
                        R.style.ThemeOverlay_MaterialComponents_Dialog);
                builder.setMessage("Are you sure to logout?").
                        setCancelable(false)
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface,
                                                        int i) {

                                        firebaseAuth.getInstance().signOut();
                                        finish();
                                        Intent intent = new Intent(WelcomeActivity.this,
                                                MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);

                                    }
                                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.setTitle("Banking App");
                alertDialog.show();
                break;

        }
        return super.onOptionsItemSelected(item);

    }

}
