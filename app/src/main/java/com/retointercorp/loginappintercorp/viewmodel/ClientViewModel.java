package com.retointercorp.loginappintercorp.viewmodel;

import com.retointercorp.loginappintercorp.model.Client;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ClientViewModel {

    private DatabaseReference databaseReference;
    public ClientViewModel(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Client.class.getSimpleName());
    }

    public Task<Void> add(Client client){
        return databaseReference.push().setValue(client);
    }
}
