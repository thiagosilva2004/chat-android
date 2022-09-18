package com.example.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MessagesActivity extends AppCompatActivity implements ContactsLastMessagesClick{

    private List<Contacts> listaContactsLastMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        verifyAuthentication();
        fetchContactsLastMessages();

        RecyclerView recyclerView = findViewById(R.id.recycler_contacts_last_messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AdapterContactsLastMessages adapterContactsLastMessages = new AdapterContactsLastMessages(listaContactsLastMessage, this);
        recyclerView.setAdapter(adapterContactsLastMessages);
    }

    private void fetchContactsLastMessages() {
        String uuid =  FirebaseAuth.getInstance().getUid();

        FirebaseFirestore.getInstance().collection("/last-messages")
                .document(uuid)
                .collection("contacts")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error == null){
                            return;
                        }

                        List<DocumentChange> documentChanges = value.getDocumentChanges();

                        if(documentChanges != null){
                            for (DocumentChange doc:
                                 documentChanges) {
                                if (doc.getType() == DocumentChange.Type.ADDED){
                                    Contacts contacts = doc.getDocument().toObject(Contacts.class);
                                    listaContactsLastMessage.add(contacts);
                                }
                            }
                        }
                    }
                });
    }

    private void verifyAuthentication() {
        if (FirebaseAuth.getInstance().getUid() == null){
            Intent intent = new Intent(MessagesActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.contacts:
                Intent intent = new Intent(MessagesActivity.this, ContactsActivity.class);
                startActivity(intent);
                break;
            case R.id.sair:
                FirebaseAuth.getInstance().signOut();
                verifyAuthentication();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnItemClicked(Contacts contacts) {
        Intent intent = new Intent(MessagesActivity.this, ChatActivity.class);
        User user = new User(contacts.getUuid(), contacts.getUsername(), contacts.getProfileURL());
        intent.putExtra("u", user);
        startActivity(intent);
    }
}