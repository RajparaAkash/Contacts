package com.contacts.phonecontact.phonebook.dialer.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdapterViewPager extends FragmentStateAdapter {

    public List<Fragment> listOfPages = new ArrayList<>();

    public AdapterViewPager(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    public void setPages(@NonNull List<Fragment> list) {
        this.listOfPages.clear();
        this.listOfPages.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return listOfPages.get(position);
    }

    @Override
    public int getItemCount() {
        return listOfPages.size();
    }
}