package uz.alloma.behbudiy.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Plan(
    var id: String = "",
    var link: String = "",
    var name: String = "",
    var titleId: String = "",
    var text: List<String> = ArrayList()
) : Parcelable