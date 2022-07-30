package edu.pe.idat.perufestapp

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

import com.google.firebase.ktx.Firebase
import edu.pe.idat.perufestapp.databinding.ActivityPfregistroBinding
//crear cuenta
enum class ProviderType{
    basicopf
}


class PfregistroActivity : AppCompatActivity() , View.OnClickListener  {

    private lateinit var binding: ActivityPfregistroBinding
    private val db = FirebaseFirestore.getInstance()
    lateinit var datePickerDialog: DatePickerDialog

    private val documentoid = ""
    private val valgenero = ""

    // private lateinit var bindinglg: ActivityIniciarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPfregistroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_pfregistro)
        binding.btncancelar.setOnClickListener(this)
        binding.btnregistrarnu.setOnClickListener(this)

        binding.etfechanaci.setOnClickListener {
            val c: Calendar = Calendar.getInstance()
            val mYear: Int = c.get(Calendar.YEAR)

            val mMonth: Int = c.get(Calendar.MONTH)

            val mDay: Int = c.get(Calendar.DAY_OF_MONTH)

            datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    binding.etfechanaci.setText(
                        dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
                    )
                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()

        }



        //val bundle = intent.extras val email = bundle?.getString("email")
        // val provider = bundle?.getString("provider") AccederActivity(email ?: "", provider ?:"")
    }
    override fun onClick(v: View) {
        when(v.id) {
            R.id.btncancelar-> CancelarRegistro()
            R.id.btnregistrarnu -> RegistrarPerufest( )
        }
    }

    private fun RegistrarPerufest() {

        fun validargenero(): Boolean{
            var respuesta = true
            if(binding.radioGroup2.checkedRadioButtonId == -1){
                respuesta= false
        }
        return respuesta
        }
        fun validardocumento(): Boolean{
            var respuesta = true
            if(binding.radioGroup.checkedRadioButtonId == -1){
                respuesta= false
            }
            return respuesta
        }
        fun obtenergenerose(): String{
            var genero =""
            when (binding.radioGroup2.checkedRadioButtonId){
                R.id.rbmasculino -> genero =binding.rbmasculino.text.toString()
                R.id.rbfemenino -> genero =binding.rbfemenino.text.toString()
            }
            return genero
        }
        fun obtenerTipodoc(): String{

            var tipodoc =""
            when (binding.radioGroup.checkedRadioButtonId ){
                R.id.rbdni -> tipodoc =binding.rbdni.text.toString()
                R.id.rbcarnet -> tipodoc =binding.rbcarnet.text.toString()
                R.id.rbpasaporte -> tipodoc =binding.rbpasaporte.text.toString()
            }
            return tipodoc
        }

        if (validardocumento().toString().isNotEmpty() && validargenero().toString().isNotEmpty() && obtenergenerose().isNotEmpty()  && obtenerTipodoc().isNotEmpty() &&
            binding.etnombre.text.toString().isNotEmpty() && binding.etapellido.text.toString().isNotEmpty()
            && binding.etnumerodocumento.text.toString().isNotEmpty() && binding.etcelular.text.toString().isNotEmpty()
            && binding.etemailreg.text.toString().isNotEmpty() && binding.etregpasw.text.toString().isNotEmpty()
            && binding.etfechanaci.text.toString().isNotEmpty()){
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(binding.etemailreg.text.toString(),
                binding.etregpasw.text.toString()).addOnCompleteListener(this) {
                    val email = binding.etemailreg.text.toString()
                var rol =  "Usuario"
                    if (it.isSuccessful) {
                        db.collection("users").document(email).set(hashMapOf("nombre" to binding.etnombre.text.toString(),
                            "apellido" to binding.etapellido.text.toString(),
                            "celular" to binding.etcelular.text.toString(),
                            "tipodocumento" to obtenerTipodoc(),
                            "fechaDeNacimiento" to binding.etfechanaci.text.toString(),
                            "documento" to  binding.etnumerodocumento.text.toString(),
                            "rol" to rol,
                            "genero" to obtenergenerose())
                        ).addOnSuccessListener{
                            ShowCrearEvento()
                        }.addOnFailureListener {
                                Toast.makeText(
                                    this,
                                    "Fallo al guardar la informacion",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    } else {
                        Toast.makeText(
                            this, "Autenticación Fallida.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            Toast.makeText(this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show()
        }

        }

    private fun ShowCrearEvento() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Cuenta Creada")
        builder.setMessage("Tu cuenta ha sido creada correctamente, Bienvenido a PeruFest")
        builder.setPositiveButton("Aceptar"){ _: DialogInterface, _: Int ->
            CancelarRegistro()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    //private fun infoUser(email: String) {
      //      val ceventoIntent = Intent(this, PerueventoActivity ::class.java).apply{
        //        putExtra("email", email)
    //} startActivity(ceventoIntent)  }

    private fun CancelarRegistro() {
        val intentActivity = Intent(this,IniciarActivity ::class.java)
        startActivity(intentActivity)
    }


}