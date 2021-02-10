package uz.myapps.alishernavoiy.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import uz.myapps.alishernavoiy.models.Kind
import uz.myapps.alishernavoiy.models.Plan


class JavaRepository {

    fun getPlanList(id: String): MutableLiveData<List<Plan>> {
        var list = MutableLiveData<List<Plan>>()
        var tempList = ArrayList<Plan>()

        val instance = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        instance.firestoreSettings = settings
        instance.collection("plans").whereEqualTo("titleId",id).get().addOnSuccessListener {p0 ->
            if (!p0!!.isEmpty){
                val documents = p0.documents
                for (i in documents){
                    val toObject = i.toObject(Plan::class.java)
                    tempList.add(toObject!!)
                }
                list.value=tempList
            }
        }.addOnFailureListener {
            Log.d("getPlanList", "getPlanList: $it")
        }
        return list
    }
    fun getKindList(): MutableLiveData<List<Kind>> {
        var list = MutableLiveData<List<Kind>>()
        var tempList = ArrayList<Kind>()

        val instance = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        instance.firestoreSettings = settings
        instance.collection("kind").get().addOnSuccessListener {p0 ->
            if (!p0!!.isEmpty){
                val documents = p0.documents
                for (i in documents){
                    val toObject = i.toObject(Kind::class.java)
                    tempList.add(toObject!!)
                }
                list.value=tempList
            }
        }.addOnFailureListener {
            Log.d("getKindList", "getKindList: $it")
        }
        return list
    }
}