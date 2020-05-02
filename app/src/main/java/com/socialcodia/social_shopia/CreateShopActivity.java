package com.socialcodia.social_shopia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.socialcodia.social_shopia.storage.Constants;

import java.util.HashMap;

public class CreateShopActivity extends AppCompatActivity {


    Toolbar toolbar;
    private ImageButton btnBack, btnLocation;
    private ImageView storeImageIcon;
    private EditText inputName, inputMobileNumber, inputCity, inputState, inputCountry, inputAddress, inputShopName,inputDeliveryFees;
    private Button btnCreateShop;


    //Firebase
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    FirebaseStorage mStorage;
    StorageReference mStorageRef;
    FirebaseUser mUser;

    Uri filePath;
    private final  static int STORAGE_PERMISSION_CODE = 100;

    String storagePermission[];

    String userId,name,mobile,state,city,country,address,shopName,deliveryFees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shop);

        btnLocation = findViewById(R.id.btnLocation);
        storeImageIcon = findViewById(R.id.storeImageIcon);
        inputName = findViewById(R.id.inputName);
        inputMobileNumber = findViewById(R.id.inputMobileNumber);
        inputCity = findViewById(R.id.inputCity);
        inputState = findViewById(R.id.inputState);
        inputCountry = findViewById(R.id.inputCountry);
        inputAddress = findViewById(R.id.inputAddress);
        inputShopName = findViewById(R.id.inputShopName);
        btnCreateShop = findViewById(R.id.btnCreateShop);
        inputDeliveryFees = findViewById(R.id.inputDeliveryFees);

        //Firebase Init

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

        storagePermission = new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};

        btnCreateShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });


        storeImageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkStoragePermission())
                {
                    chooseImage();
                }
                else
                {
                    requestStoragePermission();
                }
            }
        });

    }

    private void chooseImage()
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==100 && resultCode==RESULT_OK && data!=null)
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                storeImageIcon.setImageBitmap(bitmap);
            }
            catch (Exception e)
            {
                Toast.makeText(this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    private boolean checkStoragePermission()
    {
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission()
    {
        ActivityCompat.requestPermissions(this,storagePermission,STORAGE_PERMISSION_CODE);
    }

    private void validateData()
    {
        name = inputName.getText().toString().trim();
        mobile = inputMobileNumber.getText().toString().trim();
        city = inputCity.getText().toString().trim();
        state = inputState.getText().toString().trim();
        country = inputCountry.getText().toString().trim();
        address = inputAddress.getText().toString().trim();
        shopName = inputShopName.getText().toString().trim();
        deliveryFees = inputDeliveryFees.getText().toString().trim();

        if (name.isEmpty())
        {
            inputName.setError("Enter Name");
            inputName.requestFocus();
        }
        else if (name.length()<3)
        {
            inputName.setError("Name should be greater than 3 character");
            inputName.requestFocus();
        }
        else if (mobile.isEmpty())
        {
            inputMobileNumber.setError("Enter Mobile Number");
            inputMobileNumber.requestFocus();
        }
        else if (mobile.length()<10 || mobile.length()>13)
        {
            inputMobileNumber.setError("Enter Valid Mobile Number");
            inputMobileNumber.requestFocus();
        }
        else if (city.isEmpty())
        {
            inputCity.setError("Enter City");
            inputCity.requestFocus();
        }
        else if (city.length()<2 || city.length()>15)
        {
            inputCity.setError("Enter Valid City Name");
            inputCity.requestFocus();
        }
        else if (state.isEmpty())
        {
            inputState.setError("Enter State");
            inputState.requestFocus();
        }
        else if (state.length()<2 || state.length()>20)
        {
            inputState.setError("Enter Valid State");
            inputState.requestFocus();
        }
        else if (country.isEmpty())
        {
            inputCountry.setError("Enter Country");
            inputCountry.requestFocus();
        }
        else if (country.length()<2 || country.length()>20)
        {
            inputCountry.setError("Enter Valid Country Name");
            inputCountry.requestFocus();
        }
        else if (address.isEmpty())
        {
            inputAddress.setError("Enter Shop Address");
            inputAddress.requestFocus();
        }
        else if (address.length()<3 || address.length()>40)
        {
            inputAddress.setError("Enter A Valid Address");
            inputAddress.requestFocus();
        }
        else if (shopName.isEmpty())
        {
            inputShopName.setError("Enter Shop Name");
            inputShopName.requestFocus();
        }
        else if (shopName.length()<2 || shopName.length()>30)
        {
            inputShopName.setError("Enter Valid Shop Name");
            inputShopName.requestFocus();
        }
        else if (deliveryFees.isEmpty())
        {
            inputDeliveryFees.setError("Enter Delivery Fees");
            inputDeliveryFees.requestFocus();
        }
        else if (deliveryFees.length()>3 || deliveryFees.length()<=1)
        {
            inputDeliveryFees.setError("Enter Delivery Fees Between 10 to 999");
            inputDeliveryFees.requestFocus();
        }
        else if (filePath==null)
        {
            Toast.makeText(this, "Please Select A Shop Image", Toast.LENGTH_SHORT).show();
        }
        else
        {
            uploadImage();
        }

    }

    private void uploadImage()
    {
        btnCreateShop.setEnabled(false);
        mStorageRef.child("shopImageIcon").child(userId).putFile(filePath)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful())
                        {
                            task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageDownloadUrl = uri.toString();
                                    updateProfile(imageDownloadUrl);
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(CreateShopActivity.this, "Oops! Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateProfile(String imageDownloadUrl)
    {
        HashMap<String,Object> map = new HashMap<>();
        map.put(Constants.USER_NAME,name);
        map.put(Constants.SHOP_NAME,shopName);
        map.put(Constants.DELIVERY_FEES,deliveryFees);
        map.put(Constants.USER_MOBILE,mobile);
        map.put(Constants.CITY,city);
        map.put(Constants.STATE,state);
        map.put(Constants.COUNTRY,country);
        map.put(Constants.ADDRESS,address);
        map.put(Constants.SHOP_IMAGE,imageDownloadUrl);
        mRef.child(Constants.SHOPS).child(userId).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                btnCreateShop.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Shop Created", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                btnCreateShop.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }



}
