package edu.training.droidbountyhunterkotlin

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
//import android.support.design.widget.TabLayout
import android.view.Menu
import android.view.MenuItem
import edu.training.droidbountyhunterkotlin.ui.main.SectionsPagerAdapter
import edu.training.droidbountyhunterkotlin.utils.PictureTools
import kotlinx.android.synthetic.main.activity_detalle.*
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setSupportActionBar(toolbar)

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        view_pager.adapter = mSectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)

        fab.setOnClickListener { view ->
            val intent = Intent(this, AgregarActivity::class.java)
            startActivityForResult(intent, 0)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.menu_agregar) {
            val intent = Intent(this, AgregarActivity::class.java)
            startActivityForResult(intent, 0)
        }

        return super.onOptionsItemSelected(item)
    }

    fun actualizarListas(index: Int){
        view_pager.adapter = mSectionsPagerAdapter
        view_pager.currentItem = index
    }
}
