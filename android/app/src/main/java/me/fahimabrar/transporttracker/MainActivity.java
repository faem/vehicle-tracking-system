package me.fahimabrar.transporttracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private EditText inputEmail, inputPassword, inputDriverId;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, TrackerActivity.class));
            finish();
        }

        // set the view now
        setContentView(R.layout.activity_main);

        inputEmail = findViewById(R.id.email);
        inputPassword =  findViewById(R.id.password);
        inputDriverId = findViewById(R.id.driverId);
        progressBar = findViewById(R.id.progressBar);
        btnLogin =  findViewById(R.id.btn_login);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();
                final String driverId = inputDriverId.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                final String driver = driverId;
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        inputPassword.setError("Password too short, enter minimum 6 characters!");
                                    } else {
                                        Toast.makeText(MainActivity.this, "Authentication failed!" , Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    final FirebaseAuth auth = FirebaseAuth.getInstance();
                                    final FirebaseUser user = auth.getCurrentUser();
                                    final String uid = user.getUid();
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String id = dataSnapshot.child("users").child(uid).child("id").getValue().toString();
                                            if(id!=null)
                                                Log.e("Data Firebase",id);
                                            else
                                                Log.e("Data Firebase","null");
                                            SharedPreferences sharedPref = context.getSharedPreferences("mypref", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPref.edit();
                                            editor.putString("id", id);
                                            editor.putString("driver",driver);
                                            editor.apply();

                                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("log").child(auth.getUid()).child(driver).child("login");
                                            String key = mDatabase.push().getKey();
                                            Date c = Calendar.getInstance().getTime();
                                            System.out.println("Current time => " + c);
                                            SimpleDateFormat df = new SimpleDateFormat("dd/mm/yyyy - hh:mm:ss aa");
                                            String formattedDate = df.format(c);
                                            mDatabase.child(key).setValue(formattedDate);

                                            Log.e("shrdpref",sharedPref.getString("id","5"));
                                            Intent intent = new Intent(MainActivity.this, TrackerActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Log.e("FB error",databaseError.getMessage());
                                        }
                                    });

                                }
                            }
                        });



            }
        });
    }
}
