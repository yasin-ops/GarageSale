package com.muhammadyaseen.classifiedapp;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostFragment extends Fragment {

    NavController navController;
    private static final int CHOOSE_IMAGE = 1;
    private Button upload_Post_btn;
    private TextView txtUploadImage;
    //CropImageView cropImageView;
    //private ImageView post_image;
    ImageView post_image;
    private EditText post_tittle, post_price, post_description, post_city, post_country, post_number;
    private ProgressBar uploadProgress;
    TextView PostFragmentToHome, PostFragmentToMyPOST, PostFragmentToInfo, PostFragmentToLogout;
    private Uri imgUrl;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;


//.....................................Data Base Code Value...................................................


    //.................................Validation Code....................................
    private static final Pattern NAME_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                    //"(?=\\S+$)" +           //no white spaces
                    ".{3,50}" +               //at least 4 characters
                    "$");


    private static final Pattern DESCRIPTION_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                    //"(?=\\S+$)" +           //no white spaces
                    ".{3,500}" +               //at least 4 characters
                    "$");

    private static final Pattern NUMBER_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    //"(?=.*[a-zA-Z])" +      //any letter
                    //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                    // "(?=\\S+$)" +           //no white spaces
                    ".{11,11}" +               //at least 4 characters
                    "$");


    private static final Pattern PRICE_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    //"(?=.*[a-zA-Z])" +      //any letter
                    //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                    // "(?=\\S+$)" +           //no white spaces
                    ".{1,10000}" +               //at least 4 characters
                    "$");


    private boolean validate_Tittle() {

        String usernameInput = post_tittle.getText().toString().trim();
        if (usernameInput.isEmpty()) {
            post_tittle.setError("Field can't be empty");
            return false;


        } else if (NAME_PATTERN.matcher(usernameInput).matches()) {
            post_tittle.setError(null);
            return true;
        } else {
            post_tittle.setError("Valid name character should b atleast  3 maximum 50 character");
            return false;
        }
    }

    private boolean validate_Description() {

        String postDescription = post_description.getText().toString().trim();
        if (postDescription.isEmpty()) {
            post_description.setError("Field can't be empty");
            return false;


        } else if (DESCRIPTION_PATTERN.matcher(postDescription).matches()) {
            post_description.setError(null);
            return true;
        } else {
            post_description.setError(" Minimum 3 character and maximum 500 character ");
            return false;
        }
    }


    private boolean validate_City() {

        String cityInput = post_city.getText().toString().trim();
        if (cityInput.isEmpty()) {
            post_city.setError("Field can't be empty");
            return false;
        } else if (NAME_PATTERN.matcher(cityInput).matches()) {
            post_city.setError(null);
            return true;
        } else {
            post_city.setError("Valid City");
            return false;
        }

    }


    private boolean validate_phonenumber() {


        String numberInput = post_number.getText().toString().trim();
        if (numberInput.isEmpty()) {
            post_number.setError("Field can't be empty");
            return false;
        } else if (NUMBER_PATTERN.matcher(numberInput).matches()) {
            post_number.setError(null);
            return true;
        } else {
            post_number.setError("Valid Number");
            return false;
        }

    }

    private boolean validate_price() {

        String priceInput = post_price.getText().toString().trim();
        if (priceInput.isEmpty()) {
            post_price.setError("Field can't be empty");
            return false;
        } else if (PRICE_PATTERN.matcher(priceInput).matches()) {
            post_price.setError(null);
            return true;
        } else {
            post_price.setError("valid Price");
            return false;
        }

    }


    private boolean validate_Country() {

        String countryInput = post_country.getText().toString().trim();
        if (countryInput.isEmpty()) {
            post_country.setError("Field can't be empty");
            return false;
        } else if (NAME_PATTERN.matcher(countryInput).matches()) {
            post_country.setError(null);
            return true;
        } else {
            post_country.setError("Valid Country");
            return false;
        }

    }


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostFragment newInstance(String param1, String param2) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);


        post_image = view.findViewById(R.id.post_image);
        post_tittle = view.findViewById(R.id.post_tittle);
        post_description = view.findViewById(R.id.post_description);
        post_price = view.findViewById(R.id.post_price);
        post_country = view.findViewById(R.id.post_country);
        post_city = view.findViewById(R.id.post_city);
        post_number = view.findViewById(R.id.post_number);
        upload_Post_btn = view.findViewById(R.id.upload_Post_btn);
        txtUploadImage = view.findViewById(R.id.txtUploadImage);
        uploadProgress = view.findViewById(R.id.uploadProgress);
        PostFragmentToHome = view.findViewById(R.id.PostFragmentToHome);

        PostFragmentToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_postFragment_to_garageSaleHomeFragment);
            }
        });
        PostFragmentToMyPOST = view.findViewById(R.id.PostFragmentToMyPOST);
        PostFragmentToMyPOST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_postFragment_to_myPostFragment);
            }
        });
        PostFragmentToInfo = view.findViewById(R.id.PostFragmentToInfo);
        PostFragmentToInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_postFragment_to_aboutGarageFragment);
            }
        });
        PostFragmentToLogout = view.findViewById(R.id.PostFragmentToLogout);
        PostFragmentToLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getActivity().getSharedPreferences("checkbox", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("remember", "false");
                editor.apply();
                getActivity().finish();

            }
        });


        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");


        txtUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showFileChoose();

            }
        });

        upload_Post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(getActivity(), "Upload in progress", Toast.LENGTH_LONG).show();
                } else {
                    uploadImage();
                }

            }
        });


    }


    private void showFileChoose() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, CHOOSE_IMAGE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUrl = data.getData();

            Picasso.get().load(imgUrl)
                    .fit()
                    .centerInside()
                    .into(post_image);

        }

    }


    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        if (imgUrl != null) {
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(imgUrl));

            mUploadTask = fileReference.putFile(imgUrl)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    uploadProgress.setProgress(0);
                                }
                            }, 500);
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    Upload upload = new Upload(
                                            post_tittle.getText().toString().trim(),
                                            uri.toString(),
                                            post_price.getText().toString() + "Rs",
                                            post_description.getText().toString(),
                                            post_city.getText().toString(),
                                            post_country.getText().toString(),
                                            post_number.getText().toString());

                                    String uploadID = mDatabaseRef.push().getKey();
                                    mDatabaseRef.child(uploadID).setValue(upload);

                                    Toast.makeText(getActivity(), "UP load Successfully", Toast.LENGTH_SHORT).show();


                                }
                            });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            uploadProgress.setProgress((int) progress);



                        }
                    });
        } else {
            Toast.makeText(getActivity(), "No file selected", Toast.LENGTH_SHORT).show();


        }

    }
}



