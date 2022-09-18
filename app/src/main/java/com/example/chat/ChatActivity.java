package com.example.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private List<Messages> listaMessages;
    private EditText editChat;
    private User user;
    private User me;
    private AdapterMessages adapterMessages;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        user = getIntent().getExtras().getParcelable("u");
        getSupportActionBar().setTitle(user.getUsername());

        adapterMessages = new AdapterMessages(listaMessages);

        recyclerView = findViewById(R.id.recycler_chat);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterMessages);

        FirebaseFirestore.getInstance().collection("/users")
                .document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        me = documentSnapshot.toObject(User.class);
                        fetchMessages();
                    }
                });

        editChat = findViewById(R.id.editMensagem);
        Button btnChat = findViewById(R.id.btnEnviaMensagem);
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }

    private void fetchMessages() {
        if (me == null){
            return;
        }

        String fromId = me.getUuid();
        String toId = user.getUuid();

        FirebaseFirestore.getInstance().collection("/conversation")
                .document(fromId)
                .collection(toId)
                .orderBy("time", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        List<DocumentChange> documentChanges = value.getDocumentChanges();

                        if(documentChanges == null){
                            return;
                        }

                        for (DocumentChange doc:
                             documentChanges) {
                           if(doc.getType() == DocumentChange.Type.ADDED){
                               Messages messages = doc.getDocument().toObject(Messages.class);
                                listaMessages.add(messages);
                                adapterMessages.setListaMessages(listaMessages);
                                recyclerView.setAdapter(adapterMessages);
                           }
                        }
                    }

                });
    }

    private void sendMessage() {
        String mensagem = editChat.getText().toString();

        editChat.setText("");

        if (mensagem.isEmpty() || mensagem.trim().equals("") ){
            return;
        }

        String fromId = FirebaseAuth.getInstance().getUid();
        String toId = user.getUuid();
        long time = System.currentTimeMillis();
        Messages messages = new Messages(mensagem, time, fromId, toId);

        FirebaseFirestore.getInstance().collection("/conversations")
                .document(fromId)
                .collection(toId)
                .add(mensagem)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Contacts contacts = new Contacts(toId, user.getUsername(), messages.getMessage(),
                                                messages.getTime(), user.getProfileURL());

                        FirebaseFirestore.getInstance().collection("/last-messages")
                                .document(fromId)
                                .collection("contacts")
                                .document(toId)
                                .set(contacts);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        FirebaseFirestore.getInstance().collection("/conversations")
                .document(toId)
                .collection(fromId)
                .add(mensagem)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Contacts contacts = new Contacts(toId, user.getUsername(), messages.getMessage(),
                                messages.getTime(), user.getProfileURL());

                        FirebaseFirestore.getInstance().collection("/last-messages")
                                .document(toId)
                                .collection("contacts")
                                .document(fromId)
                                .set(contacts);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }


}