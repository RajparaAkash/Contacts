package com.contacts.phonecontact.phonebook.dialer.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.contacts.phonecontact.phonebook.dialer.AllModels.BlockContact;
import com.contacts.phonecontact.phonebook.dialer.databinding.ItemBlockNumberBinding;
import com.contacts.phonecontact.phonebook.dialer.interfaces.OnUnblockClick;

import java.util.List;

import kotlin.collections.CollectionsKt;

public class AdapterBlockNumber extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<BlockContact> blockNumberList = CollectionsKt.emptyList();
    private OnUnblockClick mOnUnblockClick;

    public void setOnUnblockClick(OnUnblockClick onUnblockClick) {
        this.mOnUnblockClick = onUnblockClick;
    }

    public void setBlockList(List<BlockContact> list) {
        this.blockNumberList = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new BlockNumberHolder(ItemBlockNumberBinding.inflate(LayoutInflater.from(viewGroup.getContext())));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        BlockNumberHolder blockNumberHolder = (BlockNumberHolder) viewHolder;
        blockNumberHolder.binding.tvBlockNumber.setText(blockNumberList.get(blockNumberHolder.getAdapterPosition()).getValue());
        blockNumberHolder.binding.blockName.setText(blockNumberList.get(blockNumberHolder.getAdapterPosition()).getContactName());
        blockNumberHolder.binding.btnUnblock.setOnClickListener(view -> {
            if (mOnUnblockClick != null) {
                mOnUnblockClick.onClick(blockNumberList.get(blockNumberHolder.getAdapterPosition()).getValue());
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.blockNumberList.size();
    }

    public static class BlockNumberHolder extends RecyclerView.ViewHolder {
        ItemBlockNumberBinding binding;

        public BlockNumberHolder(ItemBlockNumberBinding itemBlockNumberBinding) {
            super(itemBlockNumberBinding.getRoot());
            this.binding = itemBlockNumberBinding;
        }

    }

}
