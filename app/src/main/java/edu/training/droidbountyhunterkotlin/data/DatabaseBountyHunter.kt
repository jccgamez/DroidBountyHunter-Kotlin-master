package edu.training.droidbountyhunterkotlin.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.system.Os.close
import android.util.Log
import edu.training.droidbountyhunterkotlin.models.Fugitivo
import java.security.AccessControlContext

const val DATABASE_NAME = "DroidBountyHunterDatabase"

const val VERSION = 2

const val TABLE_NAME_FUGITIVOS = "fujitivos"
const val COLUMN_NAME_ID = "id"
const val COLUMN_NAME_NAME = "name"
const val COLUMN_NAME_STATUS = "status"


class DatabaseBountyHunter(val context : Context){

    private val TAG: String = DatabaseBountyHunter::class.java.simpleName

    // Declaracion de mis tablas
    private val TableFugitivos = "CREATE TABLE " + TABLE_NAME_FUGITIVOS + " (" +
            COLUMN_NAME_ID + " INTEGER PRIMARY KEY NOT NULL, " +
            COLUMN_NAME_NAME + " TEXT NOT NULL, " +
            COLUMN_NAME_STATUS + " INTEGER, " +
            "UNIQUE (" + COLUMN_NAME_NAME + ") ON CONFLICT REPLACE);"

    // Variables y helpers
    private var helper : DBHelper ? = null
    private var database : SQLiteDatabase ? = null

    fun openDatabase(): DatabaseBountyHunter {
        helper = DBHelper( context )
        database = helper!!.writableDatabase
        return this
    }
    fun closeDatabase(){
        helper!!.close()
        database!!.close()
    }

    fun querySQL(sql: String , selectionArgs: Array <String>): Cursor {
        openDatabase()
        val retorno = database!!.rawQuery(sql, selectionArgs)
        return retorno
    }

    fun borrarFugitivo(fugitivo: Fugitivo){
        openDatabase()
        database!!.delete(TABLE_NAME_FUGITIVOS , COLUMN_NAME_ID + "=?" ,
            arrayOf(fugitivo.id.toString()))
        closeDatabase()
    }

    fun actualizarFugitivo(fugitivo: Fugitivo){
        val values = ContentValues()
        values.put(COLUMN_NAME_NAME , fugitivo.name)
        values.put(COLUMN_NAME_STATUS , fugitivo.status)
        openDatabase()
        database!!.update(TABLE_NAME_FUGITIVOS ,values, COLUMN_NAME_ID + "=?",
            arrayOf (fugitivo.id.toString()))
        closeDatabase()
    }

    fun insertarFugitivo(fugitivo: Fugitivo){
        val values = ContentValues()
        values.put(COLUMN_NAME_NAME , fugitivo. name)
        values.put(COLUMN_NAME_STATUS , fugitivo. status)
        openDatabase()
        database!!.insert(TABLE_NAME_FUGITIVOS , null ,values)
        closeDatabase()
    }

    fun obtenerFugitivos(status: Int ) : Array <Fugitivo> {
        var fugitivos: Array <Fugitivo> = arrayOf()
        val dataCursor = querySQL( "SELECT * FROM " + TABLE_NAME_FUGITIVOS +
                " WHERE " + COLUMN_NAME_STATUS + "= ? ORDER BY " + COLUMN_NAME_NAME,
            arrayOf(status.toString()))
        if (dataCursor.count > 0) {
            fugitivos = generateSequence {
                if (dataCursor.moveToNext()) dataCursor else null
            }.map {
                val name = it.getString(it.getColumnIndex(COLUMN_NAME_NAME))
                val statusFugitivo = it.getInt(it.getColumnIndex(COLUMN_NAME_STATUS))
                val id = it.getInt(it.getColumnIndex(COLUMN_NAME_ID))
                return@map Fugitivo(id,name,statusFugitivo)
            }.toList().toTypedArray()
        }
        return fugitivos
    }


    inner class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION){
        override fun onCreate(db: SQLiteDatabase?) {
            Log.d(TAG, "Creación de la base de datos")
            db!!.execSQL( TableFugitivos )
            // TODO tantas CREATES como tablas que necesitemos
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            Log.w(TAG, "Actualización de la BDD de la versión " + oldVersion + "a la " +
                    newVersion + ", de la que se destruirá la información anterior" )
// Destruir BDD anterior y crearla nuevamente las tablas actualizadas
            db!!.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME_FUGITIVOS )
// Re-creando nuevamente la BDD actualizada
            // TODO la BDD
            onCreate(db)
        }
    }
}
