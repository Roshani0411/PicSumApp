package com.techflitter.myapplicationgit.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.techflitter.myapplicationgit.model.PicSumModel
import com.techflitter.myapplicationgit.repository.RepositoryData
import com.techflitter.myapplicationgit.repository.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel

class PicSumViewModel(var repositoryData: RepositoryData) : ViewModel() {


    var getAPIResponse: LiveData<PicSumModel> = repositoryData.picData!!
    var  getResponseError: SingleLiveEvent<String> = repositoryData._msgBody

    fun getPicSumAPI(page: Int, limit : Int) {
        viewModelScope.async(Dispatchers.IO) {
            repositoryData.getAPI(page,limit)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.coroutineContext.cancel(null)
    }
}
class Factory : ViewModelProvider.NewInstanceFactory {
    var repositoryData: RepositoryData? = null

    constructor(repositoryData: RepositoryData) {
        this.repositoryData = repositoryData
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PicSumViewModel(repositoryData!!) as T
    }
}
