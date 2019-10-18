package edu.training.droidbountyhunterkotlin

import android.annotation.SuppressLint
import android.os. Bundle
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.support.v7.app. AppCompatActivity
import android.view.View
import android.view.View.GONE
import android.widget.Toast
import edu.training.droidbountyhunterkotlin.data.DatabaseBountyHunter
import edu.training.droidbountyhunterkotlin.models.Fugitivo
import edu.training.droidbountyhunterkotlin.network.NetworkServices
import kotlinx.android.synthetic.main.activity_detalle.*
import org.json.JSONObject

class DetalleActivity : AppCompatActivity(){

    var fugitivo: Fugitivo? = null
    var database: DatabaseBountyHunter? = null
    private var UDID: String? = ""

    override fun onCreate(savedInstanceState: Bundle ?) {

        @SuppressLint( "HardwareIds" )
        UDID = Settings.Secure.getString(contentResolver , Settings.Secure.ANDROID_ID)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle )
        fugitivo = intent.extras["fugitivo"] as Fugitivo
// Se obtiene el nombre del fugitivo del intent y se usa como título
        title = fugitivo!!.name //+ " - " + fugitivo!!.id
       // title = intent.extras ["titulo"] as CharSequence?
// Se identifica si es Fugitivo o capturado para el mensaje...
        if ( fugitivo!!.status == 0 ) {
            etiquetaMensaje.text = "El fugitivo sigue suelto..."
        } else {
            etiquetaMensaje.text = "Atrapado!!!"
            botonCapturar.visibility = GONE
        }
    }

    fun mensajeDeCerrado(mensaje: String ){
        val builder = AlertDialog.Builder( this )
        builder.create()
        builder.setTitle( "Alerta!!!" )
            .setMessage(mensaje)
            .setOnDismissListener {
                setResult( fugitivo!!.status )
                finish()
            }.show()
    }
    fun capturarFugitivoPresionado(view: View){
        database = DatabaseBountyHunter( this )
        fugitivo!!.status = 1
        database!!.actualizarFugitivo(fugitivo!!)


        val services = NetworkServices(object :
            NetworkServices.onTaskListener {
            override fun tareaCompletada(respuesta: String) {
                val obj = JSONObject(respuesta)
                val mensaje = obj.optString("mensaje", "")
                mensajeDeCerrado(mensaje)
            }

            override fun tareaConError(codigo: Int, mensaje: String, error: String) {
                Toast.makeText(
                    applicationContext,
                    "Ocurrio un problema en la comunicación con el WebService!!!",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
        services.execute("Atrapar" ,UDID )
        botonCapturar.visibility = GONE
        botonEliminar.visibility = GONE
        setResult(0)
    }

    fun eliminarFugitivoPresionado(view: View ){
        database = DatabaseBountyHunter(this)
        database!!.borrarFugitivo(fugitivo!!)
        setResult(0)
        finish()
    }
}
