package edu.pe.idat.perufestapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.pe.idat.perufestapp.databinding.ActivityPerfilusuBinding

class PerfilusuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPerfilusuBinding
    private val dba = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilusuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bundle = intent.extras
        val email = bundle?.getString("email")
        ActualizarActivity(email ?: "")
    }
    private fun ActualizarActivity(email: String) {
        binding.etveriemai.text = email
        dba.collection("users").document(email).get().addOnSuccessListener {
            binding.ptacrnombre.setText(it.get("nombre") as String?)
            binding.plactuapelllido.setText(it.get("apellido") as String?)
            binding.plaaccelul.setText(it.get("celular") as String?)
            binding.txtperdoc.setText(it.get("tipodocumento") as String?)
            binding.txtpernumdoc.setText(it.get("documento") as String?)
                binding.txtpergenero.setText(it.get("genero") as String?)
            binding.etperfechanaci.setText(it.get("fechaDeNacimiento") as String?)
        }
        binding.btnactual.setOnClickListener {
            dba.collection("users").document(email).set(
                hashMapOf("nombre" to binding.ptacrnombre.text.toString(),
                    "apellido" to binding.plactuapelllido.text.toString(),
                    "celular" to binding.plaaccelul.text.toString(),
                            "tipodocumento" to binding.txtperdoc.text.toString(),
                    "documento" to  binding.txtpernumdoc.text.toString(),
                    "fechaDeNacimiento" to binding.etperfechanaci.text.toString(),
                            "genero" to  binding.txtpergenero.text.toString())
            )
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirmacion")
            builder.setMessage("Se ha actualizado correctamente los datos")
            builder.setPositiveButton("Aceptar", null)
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
        binding.btnmen.setOnClickListener {
            fun MenuPeru() {
                val intentActivity = Intent(this, PerueventoActivity::class.java).apply {
                    putExtra("email", email)
                }
                startActivity(intentActivity)
            }
            MenuPeru()
        }
    }

}