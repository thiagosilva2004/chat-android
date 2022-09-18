package com.example.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterContactsLastMessages extends RecyclerView.Adapter<AdapterContactsLastMessages.MyViewHolder>{

    private List<Contacts> listaContactsLastMessage;
    public ContactsLastMessagesClick listenerContactsLastMessagesClick;

    public AdapterContactsLastMessages(List<Contacts> listaContactsLastMessage, ContactsLastMessagesClick contactsLastMessagesClick) {
        this.listaContactsLastMessage = listaContactsLastMessage;
        this.listenerContactsLastMessagesClick = contactsLastMessagesClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contacts_last_message, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       Contacts contacts = listaContactsLastMessage.get(position);

       holder.txtNameContact.setText(contacts.getUsername());
       holder.txtLastMessage.setText(contacts.getLastMessage());

        Picasso.get()
                .load(contacts.getProfileURL())
                .into(holder.imagemProfileContato);

        holder.imagemProfileContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int positionItem = holder.getAdapterPosition();
                listenerContactsLastMessagesClick.OnItemClicked(listaContactsLastMessage.get(positionItem));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaContactsLastMessage.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtNameContact;
        TextView txtLastMessage;
        ImageView imagemProfileContato;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNameContact = itemView.findViewById(R.id.txtNomeContato);
            txtLastMessage = itemView.findViewById(R.id.txtContactLastMessage);
            imagemProfileContato = itemView.findViewById(R.id.imageProfileUserContacts);

        }
    }
}
