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
import kotlinx.android.synthetic.main.activity_car_detail.*
import kotlinx.android.synthetic.main.content_car_detail.*

import java.io.IOException

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CarDetailActivity : AppCompatActivity(), Callback<Car> {

    private var car: Car? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_detail)
        setSupportActionBar(toolbar)



        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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
        Log.i(TAG, "Got result: $car")

        if (response.isSuccessful) {
            tv_car_detail.text = car?.opis
            toolbar_layout.title = car?.marka
        } else {
            val errorMessage = try {
                "An error occurred: ${response.errorBody()?.string()}"
            } catch (e: IOException) {
                "An error occurred: error while decoding the error message."
            }

            Log.e(TAG, errorMessage)
            tv_car_detail.text = errorMessage
        }
    }

    override fun onFailure(call: Call<Car>, t: Throwable) {
        Log.w(TAG, "Error: " + t.message, t)
    }

    companion object {
        private val TAG = CarDetailActivity::class.java.canonicalName
    }
}