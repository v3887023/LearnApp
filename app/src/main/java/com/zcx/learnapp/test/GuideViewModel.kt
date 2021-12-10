package com.zcx.learnapp.test

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GuideViewModel : ViewModel() {
    val liveData = MutableLiveData<View>()
}