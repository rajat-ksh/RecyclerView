package com.example.recyclerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements useradapter.OnItemClickListener {

    public static final String EXTRA_URL="imageUrl";
    public static final String EXTRA_Name="name";
    public static final String EXTRA_AGE="Age";
    public static final String EXTRA_GENDER="gender";
    public static final String EXTRA_ADDRESS="Address";
    public static final String EXTRA_LAT="0";
    public static final String EXTRA_LONG="0";

    CircleImageView proImage;
    TextView Name;
    RelativeLayout linearLayout;



    private RecyclerView mRecyclerView;
    private useradapter mUseradapter;
    private ArrayList<handledata> Handledatalist;
    private RequestQueue mQueue;

    private ShimmerFrameLayout mShimmerFrameLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout=findViewById(R.id.base);

        Snackbar snackbar = Snackbar
                .make(linearLayout, "Sign in Sucessfull.!!", Snackbar.LENGTH_LONG);
        snackbar.show();

        Setvalues();


        mShimmerFrameLayout=findViewById(R.id.shimmer_view_container);



        mQueue= Volley.newRequestQueue(this);
        mRecyclerView=findViewById(R.id.View);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Handledatalist = new ArrayList<>();


        parseJSON();

        ImageView btn=findViewById(R.id.logout);



        final GoogleSignInOptions gso = new GoogleSignInOptions.
                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                build();





        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(this.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        final boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected){
                logout();}
                else
                {
                    Snackbar snackbar = Snackbar
                            .make(linearLayout, "No internet Connection..!!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });

    }




    public void Setvalues(){
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

    //logout
    public void logout() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(MainActivity.this, gso);

        mGoogleSignInClient.signOut()
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainActivity.this, "Logout Sucessfull", Toast.LENGTH_SHORT).show();
                        Intent i= new Intent(MainActivity.this,Login.class);
                        startActivity(i);
                        finish();
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



    //fetching data
    private void parseJSON(){
        String url="https://randomuser.me/api/?page=1&results=20&seed=randoms";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray=response.getJSONArray("results");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject results=jsonArray.getJSONObject(i);
                                JSONObject dob=results.getJSONObject("dob");
                                JSONObject Locat=results.getJSONObject("name");
                                JSONObject Loc=results.getJSONObject("location");
                                JSONObject Photo=results.getJSONObject("picture");

                                //getting gender
                                String gender=results.getString("gender");


                                //getting age
                                String Age=dob.getString("age");

                               //getting address and location
                                JSONObject street=Loc.getJSONObject("street");
                                JSONObject LatLong=Loc.getJSONObject("coordinates");

                                //LatLong
                                String Lat=LatLong.getString("latitude");
                                String Long=LatLong.getString("longitude");

                                //Street Values
                                String Street=street.getString("number")+" "+
                                        street.getString("name");

                                //Bindinig Address
                                String add=Street+" "+
                                        Loc.getString("city")+"\nState: "+
                                        Loc.getString("state")+" \nPost Code: "+
                                        Loc.getString("postcode");

                                //getting name
                                String name=Locat.getString("title")+" "+
                                        Locat.getString("first")+" "+
                                        Locat.getString("last");



                                String url=Photo.getString("large");

                                Handledatalist.add(new handledata(url,name,Age,add,gender,Lat,Long));

                            }

                            mUseradapter=new useradapter(MainActivity.this,Handledatalist);
                            mRecyclerView.setAdapter(mUseradapter);
                            mUseradapter.setOnItemClickListener(MainActivity.this);


                            mShimmerFrameLayout.stopShimmer();
                            mShimmerFrameLayout.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }


    @Override
    public void onItemClick(int position) {
        Intent detailIntent=new Intent(this,profile.class);
        handledata clickedItem=Handledatalist.get(position);
        detailIntent.putExtra(EXTRA_URL,clickedItem.getImageURL());
        detailIntent.putExtra(EXTRA_Name,clickedItem.getNamme());
        detailIntent.putExtra(EXTRA_AGE,clickedItem.getmAge());
        detailIntent.putExtra(EXTRA_ADDRESS,clickedItem.getAddress());
        detailIntent.putExtra(EXTRA_GENDER,clickedItem.getmGender());
        detailIntent.putExtra("latitude",clickedItem.getmLat());
        detailIntent.putExtra(EXTRA_LONG,clickedItem.getmLong());

        startActivity(detailIntent);



    }

    @Override
    protected void onResume() {
        super.onResume();
        mShimmerFrameLayout.startShimmer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mShimmerFrameLayout.stopShimmer();

    }
}
