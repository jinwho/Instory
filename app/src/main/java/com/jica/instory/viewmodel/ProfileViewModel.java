package com.jica.instory.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.jica.instory.database.entity.ProfileMinimal;

import java.util.List;

public class ProfileViewModel extends ViewModel {
    private MutableLiveData<List<ProfileMinimal>> profiles = new MutableLiveData<>();

    public ProfileViewModel(){
    }
    public MutableLiveData<List<ProfileMinimal>> getProfiles() {
        return profiles;
    }
}
