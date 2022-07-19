package edu.pe.idat.perufestapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth

import edu.pe.idat.perufestapp.databinding.ActivityIniciarBinding
//logeo de App
class IniciarActivity : AppCompatActivity() , View.OnClickListener {
    private lateinit var binding: ActivityIniciarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIniciarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnloginP.setOnClickListener(this)
        binding.btnreglog.setOnClickListener(this)

        val analytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("messaje", "Integración de Firebase completa")
        analytics.logEvent("InitScreen", bundle)
    }
    override fun onClick(v: View) {
        when(v.id) {
            R.id.btnreglog-> UsuarioregActivity()
            R.id.btnloginP-> AccederActivity()
        }
    }
    private fun AccederActivity() {
        if (binding.etusuarioPf.text.isNotEmpty() &&
            binding.etpasswordPf.text.isNotEmpty()){
            FirebaseAuth.getInstance().signInWithEmailAndPassword(binding.etusuarioPf.text.toString(),
                binding.etpasswordPf.text.toString()).addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(this, "Sesión iniciada", Toast.LENGTH_SHORT).show()
                    ShowHome(it.result?.user?.email ?: "" , ProviderpeType.basicope)
                }else{
                    ShowAlert()
                }           }
        }
    }
    private fun UsuarioregActivity() {
        val RegidocActivity = Intent(this,PfregistroActivity ::class.java)
        startActivity(RegidocActivity)
    }
    private fun ShowAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun ShowHome(email: String, provider: ProviderpeType) {

        val eventoInten = Intent(this, PerueventoActivity ::class.java).apply{
            putExtra("email", email)
            putExtra("provider", provider.name)

        }
        startActivity(eventoInten)
    }

}