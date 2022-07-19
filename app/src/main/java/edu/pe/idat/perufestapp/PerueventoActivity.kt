package edu.pe.idat.perufestapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.pe.idat.perufestapp.adapter.PostAdapter
import edu.pe.idat.perufestapp.databinding.ActivityPerueventoBinding
import edu.pe.idat.perufestapp.viewModel.MainViewModel

enum class ProviderpeType{
    basicope
}
class PerueventoActivity : AppCompatActivity() , View.OnClickListener {
    private lateinit var binding: ActivityPerueventoBinding
    private lateinit var adapter: PostAdapter
    private val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerueventoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_peruevento)
        adapter= PostAdapter(this)
        binding.rwlistaeventos.layoutManager= LinearLayoutManager(this)
        binding.rwlistaeventos.adapter= adapter
        getPostData()
        binding.btncesess.setOnClickListener(this)
        val bundle = intent.extras
        val email = bundle?.getString("email")
        BienvenidoActivity(email ?: "")
    }
    private fun getPostData() {
        viewModel.fetchUserData().observe(this,Observer{
            adapter.setListData(it)
            adapter.notifyDataSetChanged()
        })
    }


    private fun BienvenidoActivity(email: String) {
        db.collection("users").document(email).get().addOnSuccessListener {
            binding.etbienusu.setText("Bienvenido "+ it.get("nombre")as String?)
        }
        binding.btnedper.setOnClickListener{
            fun EditarPertfil() {
                val intentActivity = Intent(this,PerfilusuActivity::class.java).apply{
                    putExtra("email", email)
                }
                startActivity(intentActivity)
            }
            EditarPertfil()
        }
        binding.btnCreaEven.setOnClickListener {
             fun CrearEventopfact() {
                val intentActivity = Intent(this, EventoActivity::class.java).apply {
                    putExtra("email", email)
                }
                startActivity(intentActivity)
            }
            CrearEventopfact()
        }
    }
    override fun onClick(v: View) {
        when(v.id) {
            R.id.btncesess-> CerraLogin()
        }
    }
    private fun CerraLogin() {
        FirebaseAuth.getInstance().signOut()
        val intentActivity = Intent(this,IniciarActivity::class.java)
        startActivity(intentActivity)
    }

}