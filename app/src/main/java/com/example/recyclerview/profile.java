package com.example.recyclerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.recyclerview.MainActivity.EXTRA_ADDRESS;
import static com.example.recyclerview.MainActivity.EXTRA_AGE;
import static com.example.recyclerview.MainActivity.EXTRA_GENDER;
import static com.example.recyclerview.MainActivity.EXTRA_LAT;
import static com.example.recyclerview.MainActivity.EXTRA_LONG;
import static com.example.recyclerview.MainActivity.EXTRA_Name;
import static com.example.recyclerview.MainActivity.EXTRA_URL;

public class profile extends AppCompatActivity {
    CircleImageView img;
    TextView Tname;
    TextView TAge;
    TextView Taddress;
    ImageView gengerImg;
    CardView cardView;

    CircleImageView proImage;
    TextView Name;

    ImageView logout;
    LinearLayout linearlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        Intent i= getIntent();
        String imageurl=i.getStringExtra(EXTRA_URL);
        String Name=i.getStringExtra(EXTRA_Name);
        String Age=i.getStringExtra(EXTRA_AGE);
        String Address=i.getStringExtra(EXTRA_ADDRESS);
        String Gender=i.getStringExtra(EXTRA_GENDER);

        final String Long= i.getStringExtra(EXTRA_LONG);
        final String Lat= i.getStringExtra("latitude");


        fetch();

        //System.out.println(Lat+"***************************************** "+Long);
        setvalues();

        logout=findViewById(R.id.logout);


        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(this.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        final boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected){
                logout();}
                else{
                    Snackbar snackbar = Snackbar
                            .make(linearlayout, "No internet Connection..!!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });


        Picasso.get().load(imageurl).fit().centerInside().into(img);
        Tname.setText(Name);
        TAge.setText(Age);
        if(Gender.equals("male")){
            gengerImg.setImageResource(R.drawable.male);
        }else
            gengerImg.setImageResource(R.drawable.female);
        Taddress.setText(Address);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(profile.this,MapsActivity.class);
                i.putExtra("latitude",Lat);
                i.putExtra("longitude",Long);
                startActivity(i);
            }
        });
    }


    void setvalues(){
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();

            Uri personPhoto = acct.getPhotoUrl();

            Name = findViewById(R.id.username);
            proImage = findViewById(R.id.profilepic);
            Name.setText(personName);

            Picasso.get().load(personPhoto).fit().centerInside().into(proImage);
        }
    }

    public void logout() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mGoogleSignInClient.signOut()
                .addOnCompleteListener(profile.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(profile.this, "Logout Sucessfull", Toast.LENGTH_SHORT).show();
                        Intent i= new Intent(profile.this,Login.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
// ...
                    }
                });
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });


    }


    void fetch(){
        img= findViewById(R.id.profpic);
        Tname= findViewById(R.id.proname);
        TAge= findViewById(R.id.proAge);
        Taddress=findViewById(R.id.address);
        gengerImg=findViewById(R.id.gender);
        cardView=findViewById(R.id.mapcard);
        linearlayout=findViewById(R.id.base2);
    }
}
