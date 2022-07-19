package edu.pe.idat.perufestapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.pe.idat.perufestapp.post.Post
import edu.pe.idat.perufestapp.repository.Repo

class MainViewModel: ViewModel(){
    val repo = Repo()
    fun fetchUserData():LiveData<MutableList<Post>>{
        val mutableData =MutableLiveData<MutableList<Post>>()
        repo.getUserData().observeForever { userList->
            mutableData.value= userList
        }
        return mutableData
    }
    }