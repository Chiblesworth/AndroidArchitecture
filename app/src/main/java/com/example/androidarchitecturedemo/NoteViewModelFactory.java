package com.example.androidarchitecturedemo;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

// Used with ViewModelProvider to create a view model for the activity

public class NoteViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;

    public NoteViewModelFactory(Application application) {
        mApplication = application;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new NoteViewModel(mApplication);
    }
}
