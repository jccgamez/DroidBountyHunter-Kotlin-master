package edu.training.droidbountyhunterkotlin.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import edu.training.droidbountyhunterkotlin.DetalleActivity
import edu.training.droidbountyhunterkotlin.R
import edu.training.droidbountyhunterkotlin.data.DatabaseBountyHunter
import edu.training.droidbountyhunterkotlin.models.Fugitivo
import edu.training.droidbountyhunterkotlin.network.JSONUtils
import edu.training.droidbountyhunterkotlin.network.NetworkServices
import kotlinx.android.synthetic.main.fragment_list.*

const val SECTION_NUMBER : String = "section_number"

class ListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Se hace referencia al Fragment generado por XML en los Layouts y
        // se instancia en una View...
        return inflater.inflate(R.layout.fragment_list, container,false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val modo = arguments!![SECTION_NUMBER] as Int
        actualizarDatos(listaFugitivosCapturados, modo)

        listaFugitivosCapturados.setOnItemClickListener {adapterView, view, position, id ->
            val intent = Intent(context, DetalleActivity::class.java)
            val fugitivos = listaFugitivosCapturados.tag as Array<Fugitivo>
            intent.putExtra("fugitivo" , fugitivos[position])
            startActivityForResult(intent, 0)
        }
    }

    private fun actualizarDatos(listView: ListView?, modo: Int) {
        val database = DatabaseBountyHunter(context!!)
        val fugitivos = database.obtenerFugitivos(modo)
        if (fugitivos.isNotEmpty()){
            val values = ArrayList<String>()
            fugitivos.mapTo(values){it.name}
            val adaptador = ArrayAdapter<String>(context ,
                R.layout.item_fugitivo_list, values)
            listView!!.adapter = adaptador
            listView.tag = fugitivos
        }else { // La base de datos se encuentra vacía
            if ( modo == 0 ){
                val services = NetworkServices( object : NetworkServices.onTaskListener {
                    override fun tareaCompletada(respuesta: String ) {
                        JSONUtils.parsearFugitivos(respuesta, context!!)
                        actualizarDatos( listView , modo )
                    }
                    override fun tareaConError(codigo: Int , mensaje: String , error: String ) {
                        Toast.makeText(
                            context,
                            "Ocurrio un problema con el WebService!!! --- Código de error: $codigo \n Mensaje: $mensaje " ,
                        Toast.LENGTH_LONG ).show()
                    }
                })
                services.execute( "Fugitivos" )
            }
        }
    }
}