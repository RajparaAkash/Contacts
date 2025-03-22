//package com.contacts.phonecontact.phonebook.dialer.Viewmodels;
//
//import androidx.lifecycle.MutableLiveData;
//import androidx.lifecycle.ViewModel;
//
//import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.ContactSource;
//
//import java.util.ArrayList;
//
//public class MainActViewModel extends ViewModel {
//    private final MutableLiveData<ArrayList<ContactSource>> stateOfContactSources = new MutableLiveData<>();
//    private String contactFragmentTag = "";
//    private String favoriteFragmentTag = "";
//    private String historyFragmentTag = "";
//
//    public String getContactFragmentTag() {
//        return this.contactFragmentTag;
//    }
//
//    public void setContactFragmentTag(String str) {
//        this.contactFragmentTag = str;
//    }
//
//    public String getFavoriteFragmentTag() {
//        return this.favoriteFragmentTag;
//    }
//
//    public void setFavoriteFragmentTag(String str) {
//        this.favoriteFragmentTag = str;
//    }
//
//    public String getHistoryFragmentTag() {
//        return this.historyFragmentTag;
//    }
//
//    public void setHistoryFragmentTag(String str) {
//        this.historyFragmentTag = str;
//    }
//
//    public MutableLiveData<ArrayList<ContactSource>> getStateOfContactSources() {
//        return this.stateOfContactSources;
//    }
//}
