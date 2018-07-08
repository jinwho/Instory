package com.jica.instory.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.jica.instory.database.ProfileMinimal;

import java.util.List;

public class ProfileViewModel extends ViewModel {
    private LiveData<List<ProfileMinimal>> profiles;


    public LiveData<List<ProfileMinimal>> getProfiles() {
        return profiles;
    }
}
