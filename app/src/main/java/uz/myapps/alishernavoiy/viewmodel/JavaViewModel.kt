package uz.myapps.alishernavoiy.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.myapps.alishernavoiy.models.Kind
import uz.myapps.alishernavoiy.models.Plan
import uz.myapps.alishernavoiy.repository.JavaRepository

class JavaViewModel: ViewModel() {
    var repository = JavaRepository()
    fun getPlanListData(id: String): MutableLiveData<List<Plan>> {
        return repository.getPlanList(id)
    }
    fun getKindListData(): MutableLiveData<List<Kind>> {
        return repository.getKindList()
    }
}