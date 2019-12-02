package me.fahimabrar.transporttracker;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TrackerActivity extends Activity {
    private DatabaseReference databaseReference;
    String id = "";
    private static final int PERMISSIONS_REQUEST = 1;
    ImageButton trackButton, chatButton, alarmButton, logoutButton;
    TextView textView;
    Boolean choice = false;
    Animation animation;
    String driver;

    Button btnSend;
    EditText etMessage;
    RecyclerView chatItem;
    AdapterChat adapterChat;
    List<ModelChat> listChat = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);

        alarmButton = findViewById(R.id.alarmButton);
        btnSend = findViewById(R.id.btnSend);
        etMessage = findViewById(R.id.etMessage);
        chatItem = findViewById(R.id.chatItem);

        if(!isMyServiceRunning(TrackerService.class)){
            startTracking();
            //Toast.makeText(getApplicationContext(),"tracking",Toast.LENGTH_LONG).show();
        }

        animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(500); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in

        alarmButton.startAnimation(animation);
        alarmButton.clearAnimation();
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("mypref",Context.MODE_PRIVATE);
        driver = sharedPref.getString("driver","null");
        id = sharedPref.getString("id","5");

        Log.e("id",id);
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("alarm").child(id);
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                choice = dataSnapshot.getValue(Boolean.class);
                if(choice){
                    alarmButton.startAnimation(animation);
                    //alarmButton.setBackgroundResource(R.drawable.alarm_on_button);
                    //textView.setText("Alarm (On)");
                }else{
                    alarmButton.clearAnimation();
                    //alarmButton.setBackgroundResource(R.drawable.alarm_button);
                    //textView.setText("Alarm (Off)");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        adapterChat = new AdapterChat(listChat, "vehicle"+id);

        chatItem.setHasFixedSize(true);
        chatItem.setLayoutManager(new LinearLayoutManager(this));
        chatItem.setAdapter(adapterChat);



        etMessage.addTextChangedListener(new EditTextListener(btnSend));





        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = "vehicle"+id;
                String Message = etMessage.getText().toString();


                ModelChat chat = new ModelChat();
                chat.setUser(username);
                chat.setMessage(Message);

                ChatRequest.postMessage(chat,id);

                etMessage.setText("");

                InputMethodManager im = (InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
                if (im != null) {
                    im.hideSoftInputFromWindow(etMessage.getWindowToken() , InputMethodManager.RESULT_UNCHANGED_SHOWN);
                }
            }
        });

        ChatRequest.getChat(new ChatRequest.OnChatRequest() {
            @Override
            public void result(ModelChat chat) {
                listChat.add(chat);
                chatItem.scrollToPosition(listChat.size()-1);
                if (listChat.size() > 100) {
                    listChat.remove(0);
                }

                adapterChat.notifyDataSetChanged();
            }
        },id);

    }

    public void startTracking() {
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show();
            //finish();
        }
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            startService(new Intent(this, TrackerService.class));
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);
        }
        //finish();
    }

    public void startAlarm(View view){

        Log.e("choice",choice+"");
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("alarm");
        if(choice!=null&&choice){
            if(!id.equals("")){
                mDatabase.child(id).setValue(false);
            }else{
                Toast.makeText(getApplicationContext(),"Please Wait for a moment!", Toast.LENGTH_LONG).show();
            }
        }else{
            if(!id.equals("")){
                AlertDialog.Builder localBuilder = new AlertDialog.Builder(view.getContext());
                localBuilder.setTitle("Alert");
                localBuilder.setMessage("Do you want to send a Distress signal?");
                localBuilder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                        mDatabase.child(id).setValue(true);
                    }
                });
                localBuilder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                        paramAnonymousDialogInterface.dismiss();
                    }
                });
                AlertDialog alert = localBuilder.create();
                alert.show();
            }else{
                Toast.makeText(getApplicationContext(),"Please Wait for a moment!", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void startCount(View view){
        showFuelDialog();
        /*AlertDialog.Builder localBuilder = new AlertDialog.Builder(view.getContext());
        localBuilder.setTitle("Alert");
        localBuilder.setMessage("Do you want to start?");
        localBuilder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {

            }
        });
        localBuilder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                paramAnonymousDialogInterface.dismiss();
            }
        });
        AlertDialog alert = localBuilder.create();
        alert.show();*/
    }

    public void logout(View view){
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(view.getContext());
        localBuilder.setTitle("Alert");
        localBuilder.setMessage("Do you want to Logout?");
        localBuilder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("log").child(auth.getUid()).child(driver).child("logout");
                String key = mDatabase.push().getKey();
                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);
                SimpleDateFormat df = new SimpleDateFormat("dd/mm/yyyy - hh:mm:ss aa");
                String formattedDate = df.format(c);
                mDatabase.child(key).setValue(formattedDate);
                if(isMyServiceRunning(TrackerService.class))
                    stopService(new Intent(getApplicationContext(), TrackerService.class));
                auth.signOut();
                Intent intent = new Intent(TrackerActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        localBuilder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                paramAnonymousDialogInterface.dismiss();
            }
        });
        AlertDialog alert = localBuilder.create();
        alert.show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("mypref",Context.MODE_PRIVATE);
        driver = sharedPref.getString("driver","null");
        if(!isMyServiceRunning(TrackerService.class))
            startTracking();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Start the service when the permission is granted
            startService(new Intent(this, TrackerService.class));
        } else {
            finish();
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void showFuelDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editText = (EditText) dialogView.findViewById(R.id.fuelEditText);

        dialogBuilder.setTitle("Fuel Amount");
        //dialogBuilder.setMessage("Enter text below");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String fuelAmount = editText.getText().toString();
                //Toast.makeText(TrackerActivity.this,fuelAmount,Toast.LENGTH_LONG).show();
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();
                final String uid = user.getUid();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("distance/"+uid+"/dist");
                databaseReference.setValue(0);
                databaseReference = FirebaseDatabase.getInstance().getReference("distance/"+uid+"/fuel");
                String key = databaseReference.push().getKey();
                DatabaseReference newDR = databaseReference.child(key);
                newDR.child("amount").setValue(Integer.parseInt(fuelAmount));
                newDR.child("driver").setValue(driver);
                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);
                SimpleDateFormat df = new SimpleDateFormat("dd/mm/yyyy - hh:mm:ss aa");
                String time = df.format(c);
                newDR.child("time").setValue(time);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
               dialog.dismiss();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

}
