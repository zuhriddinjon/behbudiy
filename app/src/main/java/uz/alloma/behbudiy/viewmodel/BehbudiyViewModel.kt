package uz.alloma.behbudiy.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uz.alloma.behbudiy.models.Kind
import uz.alloma.behbudiy.models.Plan
import uz.alloma.behbudiy.repository.BehbudiyRepository

class BehbudiyViewModel(app: Application) : AndroidViewModel(app) {
    var repository = BehbudiyRepository()
    private var _livePlan = MutableLiveData<List<Plan>>()
    private var _liveKind = MutableLiveData<List<Kind>>()
    val livePlan: LiveData<List<Plan>>
        get() = _livePlan
    val liveKind: LiveData<List<Kind>>
        get() = _liveKind

    fun getPlanListData(id: String) {
        viewModelScope.launch {
            try {
                _livePlan = repository.getPlanList(id)
            } catch (e: Exception) {
                Log.d("TAG_Plan_Error", "getPlanListData: ${e.message}")
            }
        }
    }

    fun getKindListData() {
        viewModelScope.launch {
            try {
                _liveKind = repository.getKindList()
            } catch (e: Exception) {
                Log.d("TAG_Kind_Error", "getKindListData: ${e.message}")
            }
        }
    }
}