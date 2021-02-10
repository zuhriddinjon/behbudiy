package uz.myapps.alishernavoiy.models

import java.io.Serializable

public class Plan(
    var id: String = "",
    var link: String = "",
    var name: String = "",
    var titleId: String = "",
    var mp3: String = "",
    var text: List<String> = ArrayList()
) : Serializable