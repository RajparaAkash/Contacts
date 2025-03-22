package com.contacts.phonecontact.phonebook.dialer.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.contacts.phonecontact.phonebook.dialer.databinding.ItemRingtoneBinding;

import java.io.File;
import java.util.ArrayList;

public class AdapterSelectRingtone extends RecyclerView.Adapter<AdapterSelectRingtone.MyContentHolder> {
    Context context;
    ArrayList<String> ringtoneList = new ArrayList();
    OnRingtoneSelectListener listener;

    public AdapterSelectRingtone(Context context, OnRingtoneSelectListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void updateList(ArrayList<String> arrayList) {
        this.ringtoneList = arrayList;
        notifyDataSetChanged();
    }

    @Override
    public MyContentHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyContentHolder(ItemRingtoneBinding.inflate(LayoutInflater.from(viewGroup.getContext())));
    }

    @Override
    public void onBindViewHolder(MyContentHolder viewHolder, int i) {

        File file = new File(ringtoneList.get(i));

        viewHolder.binding.tvSongName.setText(file.getName());
        viewHolder.itemView.setOnClickListener(view -> {
            listener.onSelected(ringtoneList.get(i));
        });

    }

    @Override
    public int getItemCount() {
        return ringtoneList.size();
    }

    public interface OnRingtoneSelectListener {
        void onSelected(String ringtone);
    }

    public static class MyContentHolder extends RecyclerView.ViewHolder {
        ItemRingtoneBinding binding;

        public MyContentHolder(ItemRingtoneBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

}
