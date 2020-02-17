package com.deepmodi.shareden.ViewModel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FirebaseCheckUserViewModel extends ViewModel {

    FirebaseDatabase database;
    DatabaseReference reference;
    MutableLiveData<String> phoneNumber;
    Context context;
    Query query;

    public MutableLiveData<String> getCurrentPhoneNumber()
    {
        if(phoneNumber==null)
        {
            phoneNumber = new MutableLiveData<String>();
        }

        return phoneNumber;
    }

    public FirebaseCheckUserViewModel(Context context, DatabaseReference reference,FirebaseDatabase database)
    {
        this.context = context;
        this.reference = reference;
        this.database = database;
    }

    private void loadDatabase()
    {
        Query query = reference;
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
