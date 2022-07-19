package edu.pe.idat.perufestapp

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter

import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import edu.pe.idat.perufestapp.databinding.ActivityEventoBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

//crear evento
class EventoActivity : AppCompatActivity(),
    AdapterView.OnItemSelectedListener {

    private lateinit var binding: ActivityEventoBinding
    private val dbeven = FirebaseFirestore.getInstance()
    private lateinit var ImageUri : Uri


    lateinit var datePickerDialog2: DatePickerDialog
    private var listacategotias = ""
    private var listatipoentrada = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setContentView(R.layout.activity_evento)
        val bundle = intent.extras
        val email = bundle?.getString("email")
        CrearEvento(email ?: "")
        binding.etdfecha.setOnClickListener {
            val c: Calendar = Calendar.getInstance()
            val mYear: Int = c.get(Calendar.YEAR)
            val mMonth: Int = c.get(Calendar.MONTH)
            val mDay: Int = c.get(Calendar.DAY_OF_MONTH)
            datePickerDialog2 = DatePickerDialog(
                this,
                { _, year, monthOfYear, dayOfMonth ->
                    binding.etdfecha.setText(
                        dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
                    )
                }, mYear, mMonth, mDay
            )
            datePickerDialog2.show()

        }

        ArrayAdapter.createFromResource(
            this,
            R.array.listacategoria,
            android.R.layout.simple_spinner_item
        ).also {adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spcategoria.adapter = adapter
        }
        ArrayAdapter.createFromResource(
            this,
            R.array.listotipoent,
            android.R.layout.simple_spinner_item
        ).also {adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.sptipent.adapter = adapter
        }
        binding.btnimg.setOnClickListener {
            selectImag()
        }

        binding.sptipent.onItemSelectedListener = this
        binding.spcategoria.onItemSelectedListener = this

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            ImageUri = data?.data!!
            binding.ivImagEvento.setImageURI(ImageUri)

        }
    }
        private fun selectImag() {
            val intent = Intent()
            intent.type = "eventos/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, 100)

            }

    private fun setearFormulario(){
        binding.spcategoria.setSelection(0)
        binding.sptipent.setSelection(0)
        binding.etnomeven.setText("")
        binding.etdeseven.setText("")
        binding.etdfecha.setText("")
        binding.etdireceven.setText("")
    }

    private fun CrearEvento(email: String) {



        fun validardatos(): Boolean {
        var repuesta = true
            if(binding.etnomeven.text.toString().trim().isEmpty()){
                binding.etnomeven.isFocusableInTouchMode= true
                binding.etnomeven.requestFocus()
                repuesta= false
            }else if(binding.etdeseven.text.toString().trim().isEmpty()){
                binding.etdeseven.isFocusableInTouchMode= true
                binding.etdeseven.requestFocus()
                repuesta= false
            }else if(binding.etdireceven.text.toString().trim().isEmpty()){
                binding.etdireceven.isFocusableInTouchMode= true
                binding.etdireceven.requestFocus()
                repuesta= false
            }
            return  repuesta
        }
        fun validarcategorai(): Boolean {
            var respuesta = true
            if(listacategotias == ""){
                respuesta = false
            }
            return respuesta
        }
        fun validarentrada(): Boolean {
            var respuesta = true
            if(listatipoentrada == ""){
                respuesta = false
            }
            return respuesta
        }
        dbeven.collection("users").document(email).get().addOnSuccessListener {
            binding.etnombreuser.setText( it.get("nombre") as String?+ " " + it.get("apellido") as String?)
        }
        binding.btnregisteven.setOnClickListener {

               // storageReference.downloadUrl.addOnSuccessListener{uri->
                 //   val hashMap=
                   //     HashMap<String, String>()
                    //hashMap["imagurl"] = java.lang.String.valueOf(uri)
                    //dbeven.collection("eventos").add(
                   //hashMap )
               // }

            if (validardatos() && validarcategorai() && validarentrada()
                && binding.etdeseven.text.toString().isNotEmpty()
                && binding.etnomeven.text.toString().isNotEmpty()
                && binding.etdfecha.text.toString().isNotEmpty()
                && binding.etdireceven.text.toString().isNotEmpty()
            ) {
                val storageReference = FirebaseStorage.getInstance().getReference("eventos/")
                storageReference.putFile(ImageUri).addOnSuccessListener {
                    binding.ivImagEvento.setImageURI(null)
                    storageReference.downloadUrl.addOnSuccessListener { uri ->
                        val url = uri.toString();

                        dbeven.collection("eventos").add(
                            hashMapOf(
                                "descripcion" to binding.etdeseven.text.toString(),
                                "fechaevento" to binding.etdfecha.text.toString(),
                                "nombrevento" to binding.etnomeven.text.toString(),
                                "direccionevento" to binding.etdireceven.text.toString(),
                                "eventointeres" to listacategotias,
                                "tipoentrada" to listatipoentrada,
                                "imagurl" to url,
                                "nombreusuario" to binding.etnombreuser.text.toString()
                            )
                        ).addOnSuccessListener {
                            val builder = AlertDialog.Builder(this)
                            builder.setTitle("PeruFest")
                            builder.setMessage("El Evento ha sido creado correctamente")
                            builder.setPositiveButton("Aceptar") { _: DialogInterface, _: Int ->
                                setearFormulario()
                            }
                            val dialog: AlertDialog = builder.create()
                            dialog.show()
                        }.addOnFailureListener {
                            Toast.makeText(
                                this,
                                "Fallo al guardar la informacion",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnevencanc.setOnClickListener {
            fun MenuPeru() {
                val intentActivity = Intent(this, PerueventoActivity::class.java).apply {
                    putExtra("email", email)
                }
                startActivity(intentActivity)
            }
            MenuPeru()
        }
    }
    override fun onItemSelected(adapter: AdapterView<*>?, view: View?, position: Int, id: Long) {
        listacategotias = if(position > 0){
            adapter!!.getItemAtPosition(position).toString()
        }else{
            ""
        }
        listatipoentrada =  if (position > 0){
            adapter!!.getItemAtPosition(position).toString()
        }else{
            ""
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}