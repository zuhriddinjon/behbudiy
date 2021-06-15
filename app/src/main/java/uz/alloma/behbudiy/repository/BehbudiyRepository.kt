package uz.alloma.behbudiy.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import uz.alloma.behbudiy.models.Kind
import uz.alloma.behbudiy.models.Plan


class BehbudiyRepository {

    fun getPlanList(id: String): MutableLiveData<List<Plan>> {
        val list = MutableLiveData<List<Plan>>()

        val tempList = ArrayList<Plan>()

            val instance = FirebaseFirestore.getInstance()
            val settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()
            instance.firestoreSettings = settings
            instance.collection("plans").whereEqualTo("titleId", id).get()
                .addOnSuccessListener { p0 ->
                    if (!p0!!.isEmpty) {
                        val documents = p0.documents
                        for (i in documents) {
                            val toObject = i.toObject(Plan::class.java)
                            tempList.add(toObject!!)
                            Log.d("getPlanList", "getPlanList: $toObject")
                        }
                        list.postValue(tempList)
                    }
                }.addOnFailureListener {
                    Log.d("getPlanList", "getPlanList: $it")
                }
            return list
    }

    fun getKindList(): MutableLiveData<List<Kind>> {
        val list = MutableLiveData<List<Kind>>()

        val tempList = ArrayList<Kind>()

        val instance = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        instance.firestoreSettings = settings
        instance.collection("kind").get()
            .addOnSuccessListener { p0 ->
            if (!p0!!.isEmpty) {
                val documents = p0.documents
                for (i in documents) {
                    val toObject = i.toObject(Kind::class.java)
                    tempList.add(toObject!!)
                    Log.d("getKindList", "getList: ${toObject.id}")
                }
                list.postValue(tempList)
            }
        }
            .addOnFailureListener {
            Log.d("getKindList", "getKindList: $it")
        }
        Log.d("getKindList", "getKindList: $tempList")

        return list
    }
}