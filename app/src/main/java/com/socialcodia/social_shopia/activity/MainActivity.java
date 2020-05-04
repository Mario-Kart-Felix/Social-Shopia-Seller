package com.socialcodia.social_shopia.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.socialcodia.social_shopia.R;
import com.socialcodia.social_shopia.storage.Constants;

public class MainActivity extends AppCompatActivity {

    //Firebase
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    FirebaseStorage mStorage;
    StorageReference mStorageRef;
    FirebaseUser mUser;

    String userId;

    private ImageButton btnEditShop,btnAddProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Ui init

        btnEditShop = findViewById(R.id.btnEditShop);
        btnAddProduct = findViewById(R.id.btnAddProduct);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference();
        mUser = mAuth.getCurrentUser();

        if (mUser!=null)
        {
            userId = mUser.getUid();
        }

        btnEditShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToEditShop();
            }
        });

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToAddProduct();
            }
        });

        checkLoginState();
    }

    private void sendToAddProduct()
    {
        Intent intent = new Intent(getApplicationContext(),AddProductActivity.class);
        startActivity(intent);
    }


    private void checkLoginState()
    {
        if (mAuth.getCurrentUser()!=null)
        {
            mRef.child(Constants.SHOPS).child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String userType = dataSnapshot.child(Constants.USER_TYPE).getValue(String.class);
                    if (userType.equals("seller"))
                    {
                        String name = dataSnapshot.child(Constants.USER_NAME).getValue(String.class);
                        String shopName = dataSnapshot.child(Constants.SHOP_NAME).getValue(String.class);
                        if (name.isEmpty() || shopName.isEmpty())
                        {
                            sendToCreateShop();
                        }
                        else
                        {

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void sendToCreateShop()
    {
        Intent intent = new Intent(getApplicationContext(),CreateShopActivity.class);
        startActivity(intent);
        finish();
    }

    private void sendToEditShop()
    {
        Intent intent = new Intent(getApplicationContext(),EditShopActivity.class);
        startActivity(intent);
    }
}
