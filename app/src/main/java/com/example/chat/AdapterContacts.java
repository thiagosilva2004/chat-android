package com.example.chat;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterContacts extends RecyclerView.Adapter<AdapterContacts.MyViewHolder> {

    public List<User> listaUser;
    public ContactsClick listenerContactsClick;

    public AdapterContacts(List<User> listaUser, ContactsClick contactsClick) {
        this.listaUser = listaUser;
        this.listenerContactsClick = contactsClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                                            .inflate(R.layout.list_user, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        position = holder.getAdapterPosition();
        User user = listaUser.get(position);

        holder.nomeContato.setText(user.getUsername());
        Picasso.get()
                .load(user.getProfileURL())
                .into(holder.imagemProfileContato);

        holder.nomeContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int positionItem = holder.getAdapterPosition();
                listenerContactsClick.OnItemClicked(listaUser.get(positionItem));
            }
        });

        holder.imagemProfileContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int positionItem = holder.getAdapterPosition();
                listenerContactsClick.OnItemClicked(listaUser.get(positionItem));
            }
        });

    }

    @Override
    public int getItemCount() {
        // tamanho da lista
        return listaUser.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nomeContato;
        ImageView imagemProfileContato;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nomeContato = itemView.findViewById(R.id.txtNomeContato);
            imagemProfileContato = itemView.findViewById(R.id.imageProfileUserContacts);
        }
    }

}
