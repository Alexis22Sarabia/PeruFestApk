package edu.pe.idat.perufestapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import edu.pe.idat.perufestapp.post.Post
import com.google.firebase.firestore.FirebaseFirestore


class Repo {
    fun getUserData(): LiveData<MutableList<Post>>{
        val mutableData =MutableLiveData<MutableList<Post>>()
        FirebaseFirestore.getInstance().collection("eventos").get().addOnSuccessListener {     result->
            val listData = mutableListOf<Post>()
            for (document in result){
                val imagurl = document.getString("imagurl")
                val descripcion= document.getString("descripcion")
                val fechaevento= document.getString("fechaevento")
                val nombrevento= document.getString("nombrevento")
                val direccionevento= document.getString("direccionevento")
                val eventointeres= document.getString("eventointeres")
                val tipoentrada = document.getString("tipoentrada")
                val nombreusuario= document.getString("nombreusuario")
                val post = Post(imagurl!!,descripcion!!,fechaevento!!,nombrevento!!,
                    direccionevento!!,eventointeres!!,tipoentrada!!,nombreusuario!!)
                listData.add(post)
            }
            mutableData.value = listData
        }
        return mutableData
    }
}