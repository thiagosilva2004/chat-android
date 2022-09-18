package com.example.chat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.Document;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterMessages extends RecyclerView.Adapter<AdapterMessages.MyViewHolder>{

    public List<Messages> getListaMessages() {
        return listaMessages;
    }

    public void setListaMessages(List<Messages> listaMessages) {
        this.listaMessages = listaMessages;
    }

    private List<Messages> listaMessages;
    private User user;

    public AdapterMessages(List<Messages> listaMessages) {
        this.listaMessages = listaMessages;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int indice =  getItemViewType(viewType);
        Messages messages = listaMessages.get(indice);

        if(messages.getFromID() == FirebaseAuth.getInstance().getUid()){
            View itemLista = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_to_message, parent, false);
            return new MyViewHolder(itemLista);
        }else{
            View itemLista = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_from_message, parent, false);
            return new MyViewHolder(itemLista);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Messages messages = listaMessages.get(position);

         FirebaseFirestore.getInstance().collection("/users")
                 .whereEqualTo("uuid", messages.getFromID())
                         .addSnapshotListener(new EventListener<QuerySnapshot>() {
                             @Override
                             public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error != null){
                                    Log.e("erro", error.getMessage());
                                    return;
                                }
                                if (value.isEmpty()){
                                    Log.e("erro_mensagem_vazia","nao encontrado dados desse usuario com esse id");
                                    return;
                                }
                                List<DocumentSnapshot> docs = value.getDocuments();
                                for (DocumentSnapshot doc: docs){
                                    user =  doc.toObject(User.class);
                                }

                             }
                         });

        holder.txtMessage.setText(messages.getMessage());
        Picasso.get()
                .load(user.getProfileURL())
                .into(holder.imagemProfileContato);
    }

    @Override
    public int getItemCount() {
        return listaMessages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtMessage;
        ImageView imagemProfileContato;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtMessage = itemView.findViewById(R.id.txtMessage);
            imagemProfileContato = itemView.findViewById(R.id.imageProfileUserContacts);

        }
    }
}