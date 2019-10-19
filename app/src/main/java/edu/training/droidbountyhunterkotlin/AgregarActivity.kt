package edu.training.droidbountyhunterkotlin

import android.app.Activity.RESULT_OK
import android.os. Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app. AppCompatActivity
import android.view.View
import edu.training.droidbountyhunterkotlin.R
import edu.training.droidbountyhunterkotlin.data.DatabaseBountyHunter
import edu.training.droidbountyhunterkotlin.models.Fugitivo
import kotlinx.android.synthetic.main.activity_agregar.*
import kotlinx.android.synthetic.main.activity_detalle.*

class AgregarActivity:AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle ?) {
        super .onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar)
    }

    fun guardarFugitivoPresionado(view: View ){
        var nombre = nombreFugitivoTextView.text.toString()
        if (nombre.isNotEmpty()){
            val database = DatabaseBountyHunter( this )
            database.insertarFugitivo(Fugitivo( 0 ,nombre, 0 ))
            setResult( 0 )
            finish()
        } else {
                AlertDialog.Builder(this)
                .setTitle( "Alerta" )
                .setMessage( "Favor de capturar el nombre del fugitivo." )
                .show()
        }
    }
}




