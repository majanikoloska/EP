package ep.rest

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton

import java.io.IOException

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CarDetailActivity : AppCompatActivity(), Callback<Car> {

    private var car: Car? = null
    private var tvCarDetail: TextView? = null
    private var toolbarLayout: CollapsingToolbarLayout? = null
    private var fabEdit: FloatingActionButton? = null
    private var fabDelete: FloatingActionButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_detail)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        toolbarLayout = findViewById(R.id.toolbar_layout)

        tvCarDetail = findViewById(R.id.tv_car_detail)

        fabEdit = findViewById(R.id.fab_edit)
        fabEdit!!.setOnClickListener {
            val intent = Intent(this@CarDetailActivity, CarDetailActivity::class.java)
            intent.putExtra("ep.rest.car", car)
            startActivity(intent)
        }
        fabDelete = findViewById(R.id.fab_delete)
        fabDelete!!.setOnClickListener {
            val dialog = AlertDialog.Builder(this@CarDetailActivity)
            dialog.setTitle("Confirm deletion")
            dialog.setMessage("Are you sure?")
            dialog.setPositiveButton("Yes") { dialog, which -> deleteCar() }
            dialog.setNegativeButton("Cancel", null)
            dialog.create().show()
        }


        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val id = intent.getIntExtra("ep.rest.id", 0)
        if (id > 0) {
            CarService.instance.get(id).enqueue(this)
        }
    }

    private fun deleteCar() {
        val id = intent.getIntExtra("ep.rest.id", 0)
        CarService.instance.delete(id).enqueue(object : Callback<Void?> {
            override fun onFailure(call: Call<Void?>?, t: Throwable?) {
                Log.w(TAG, "An error occurred while deleting: ${t?.message}")
            }

            override fun onResponse(call: Call<Void?>?, response: Response<Void?>?) {
                startActivity(Intent(this@CarDetailActivity, MainActivity::class.java))
            }
        })
    }

    override fun onResponse(call: Call<Car>, response: Response<Car>) {
        car = response.body()
        Log.i(TAG, "Got result: " + car!!)
        if (response.isSuccessful) {
            tvCarDetail!!.text = car!!.marka
            toolbarLayout!!.title = car!!.marka
        } else {
            var errorMessage: String
            try {
                errorMessage = "An error occurred: " + response.errorBody()?.string()
            } catch (e: IOException) {
                errorMessage = "An error occurred: error while decoding the error message."
            }

            Log.e(TAG, errorMessage)
            tvCarDetail!!.text = errorMessage
        }
    }

    override fun onFailure(call: Call<Car>, t: Throwable) {
        Log.w(TAG, "Error: " + t.message, t)
    }

    companion object {
        private val TAG = CarDetailActivity::class.java.canonicalName
    }
}