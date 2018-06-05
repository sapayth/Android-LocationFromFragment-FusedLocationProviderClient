package com.sapayth.fusedlocationfromfragment;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment {

    private static final int LOCATION_REQUEST_CODE = 101;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private TextView mLatTV, mLonTV;
    private Button mFindLocationButton;

    public LocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mLatTV = view.findViewById(R.id.latitudeTextView);
        mLonTV = view.findViewById(R.id.longitudeTextView);
        mFindLocationButton = view.findViewById(R.id.findLocationButton);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        mFindLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findLocation();
            }
        });
    }

    private void findLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            showLocation();
        } else {
            // call requestPermissions
            // NOT ActivityCompate.requestPermissions
            // cause ActivityCompate.requestPermissions callbacks activity
            requestLocationPermission();
        }
    }

    private void requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= 23) {  // if version is greater then 23 show a Rational message
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(getContext(), "Location permission is needed to show your location", Toast.LENGTH_SHORT).show();
            }

            // ask the permission
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
            requestPermissions(permissions, LOCATION_REQUEST_CODE);
        } else {// if version is less then 23 ask permission directly
            // ask the permission
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
            // call requestPermissions
            // NOT ActivityCompate.requestPermissions
            // cause ActivityCompate.requestPermissions callbacks activity
            requestPermissions(permissions, LOCATION_REQUEST_CODE);
        }


    }

    private void showLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    mLatTV.setText("Latitude: " + location.getLatitude());
                    mLonTV.setText("Longitude: " + location.getLongitude() + "");
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the task you need to do.
                showLocation();
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
