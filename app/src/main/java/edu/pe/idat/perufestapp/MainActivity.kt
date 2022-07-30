package edu.pe.idat.perufestapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import edu.pe.idat.perufestapp.adapter.PostAdapter
import edu.pe.idat.perufestapp.databinding.ActivityMainBinding
import edu.pe.idat.perufestapp.databinding.ActivityPfregistroBinding
import edu.pe.idat.perufestapp.viewModel.MainViewModel

//pantalla sin logeo
class MainActivity : AppCompatActivity(), View.OnClickListener  {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PostAdapter
    private val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {

        Thread.sleep(2000)
        setTheme(R.style.Theme_PeruFestApp)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_main)
        binding.btndloginpg.setOnClickListener(this)
        binding.btnslreg.setOnClickListener(this)

        adapter= PostAdapter(this)
        binding.rwlistaeventos.layoutManager= LinearLayoutManager(this)
        binding.rwlistaeventos.adapter= adapter
        getPostData()
    }

    private fun getPostData() {
        viewModel.fetchUserData().observe(this,Observer{
            adapter.setListData(it)
            adapter.notifyDataSetChanged()
        }
        )
    }
    override fun onClick(v: View) {
        when(v.id) {
            R.id.btndloginpg-> DireLogin()
            R.id.btnslreg-> RegistoSlog()
        }
    }

    private fun RegistoSlog() {
        val intentActivity = Intent(this,PfregistroActivity ::class.java)
        startActivity(intentActivity)    }

    private fun DireLogin() {
        val intentActivity = Intent(this,IniciarActivity ::class.java)
        startActivity(intentActivity)
    }
}