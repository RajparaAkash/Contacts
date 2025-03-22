package com.contacts.phonecontact.phonebook.dialer.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ModelQuickResponse;
import com.contacts.phonecontact.phonebook.dialer.databinding.ItemQuickResponseBinding;

import java.util.ArrayList;

public class AdapterQuickResponse extends RecyclerView.Adapter<AdapterQuickResponse.MyViewHolder> {
    Activity activity;
    ArrayList<ModelQuickResponse> responseArrayList = new ArrayList();
    OnItemClickListener listener;

    public interface OnItemClickListener {
        void onClicked(ModelQuickResponse modelQuickResponse);
    }

    public AdapterQuickResponse(Activity activity, OnItemClickListener listener) {
        this.activity = activity;
        this.listener = listener;
    }

    public void setDataList(ArrayList<ModelQuickResponse> arrayList) {
        this.responseArrayList = arrayList;
        notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ItemQuickResponseBinding inflate = ItemQuickResponseBinding.inflate(LayoutInflater.from(viewGroup.getContext()));
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {

        viewHolder.binding.tvQuickResponse.setText(responseArrayList.get(i).getMessage());

        viewHolder.itemView.setOnClickListener(view -> {
            listener.onClicked(responseArrayList.get(i));
        });

    }

    @Override
    public int getItemCount() {
        return responseArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ItemQuickResponseBinding binding;

        public MyViewHolder(ItemQuickResponseBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

}
