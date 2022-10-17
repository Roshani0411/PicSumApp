package com.techflitter.myapplicationgit.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.techflitter.myapplicationgit.api.ApiService
import com.techflitter.myapplicationgit.model.PicSumModel
import org.json.JSONObject


class RepositoryData(var service: ApiService) {

    private var picMutableLiveData = MutableLiveData<PicSumModel>()
    var picData: LiveData<PicSumModel>? = picMutableLiveData
    suspend fun getAPI(page: Int, limit: Int) {
        try {
            val result = service.getData(page,limit)
            Log.d("RepositoryData", "result:-> $result")
            Log.d("RepositoryData", "result.body():-> ${result.body()}")
            Log.d("RepositoryData", "result.errorBody():-> ${result.errorBody()}")

            if (result.isSuccessful) {
                picMutableLiveData.postValue(result.body())
            } else {
                val jsObj = result.errorBody()?.toString()
                Log.d(
                    "RepositoryData",
                    "checkUserNameAvailability userNameAvailable-2 jsObj:-> ${jsObj}<="
                )
                showErrorMsg(jsObj, "" + result.code())
            }
        } catch (ex: Exception) {
            Log.d("RepositoryData", "getRegistrationAPI - Exception=${ex.localizedMessage} ")
            ex.printStackTrace()
        }
    }


    var msgBody = SingleLiveEvent<String>()
    val _msgBody: SingleLiveEvent<String> get() = msgBody

    private fun showErrorMsg(jsObj: String?, errorCode: String): SingleLiveEvent<String> {
        msgBody.postValue(errorCode)
        try {
            val jSons = jsObj?.let { JSONObject(it) }
            if (jSons?.has("message") == true) {
                msgBody.postValue(jSons.getString("message"))
            }
        } catch (ex: Exception) {
            Log.d(
                "RepositoryData",
                "checkUserNameAvailability userNameAvailable-0000000 Exception -> ${ex.localizedMessage}"
            )
        }
        return msgBody
    }

}