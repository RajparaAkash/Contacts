package com.contacts.phonecontact.phonebook.dialer.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.contacts.phonecontact.phonebook.dialer.MyApplication;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Language;
import com.contacts.phonecontact.phonebook.dialer.databinding.ItemLanguageBinding;

import java.util.List;

public class AdapterLanguage extends RecyclerView.Adapter<AdapterLanguage.ImageViewHolder> {
    Activity activity;
    List<Language> languageList;

    String selected = "";

    public AdapterLanguage(Activity activity) {
        this.activity = activity;
        this.languageList = MyApplication.getInstance().getLanguageList();
        selected = MyApplication.getInstance().getLanguage();
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ImageViewHolder(ItemLanguageBinding.inflate(LayoutInflater.from(activity), viewGroup, false));
    }

    public void onBindViewHolder(ImageViewHolder viewHolder, int i) {
        Language language = languageList.get(i);

        try {
            viewHolder.binding.imgLanguage.setImageResource(language.getLanguageResourceId());
            viewHolder.binding.txtLanguageName.setText(language.getLanguageName());

            if (selected.equals(languageList.get(i).getLanguageCode())) {
                viewHolder.binding.ivSelected.setImageResource(R.drawable.lang_radio_sel);
                viewHolder.binding.llLanguage.setBackgroundResource(R.drawable.lang_bg_selected);
            } else {
                viewHolder.binding.ivSelected.setImageResource(R.drawable.lang_radio_unsel);
                viewHolder.binding.llLanguage.setBackgroundResource(R.drawable.lang_bg_unselected);
            }

            viewHolder.itemView.setOnClickListener(view -> {
                selected = languageList.get(i).getLanguageCode();
                notifyDataSetChanged();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getSelectedLang() {
        return selected;
    }

    @Override
    public int getItemCount() {
        if (languageList == null) {
            return 0;
        }
        return languageList.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ItemLanguageBinding binding;

        public ImageViewHolder(ItemLanguageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

}
