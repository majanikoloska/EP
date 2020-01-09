package ep.rest

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_car_detail.*
import kotlinx.android.synthetic.main.content_car_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class CarDetailActivity : AppCompatActivity(), Callback<Car> {

    private var car: Car? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_detail)
        setSupportActionBar(toolbar)

        fabEdit.setOnClickListener {
            val intent = Intent(this, CarFormActivity::class.java)
            intent.putExtra("ep.rest.book", car)
            startActivity(intent)
        }

        fabDelete.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Confirm deletion")
            dialog.setMessage("Are you sure?")
            dialog.setPositiveButton("Yes") { _, _ -> deleteCar() }
            dialog.setNegativeButton("Cancel", null)
            dialog.create().show()
        }


        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.getIntExtra("ep.rest.id", 0)


        if (id > 0) {
            CarService.instance.get(id).enqueue(this)

            val opis = intent.getStringExtra("ep.rest.opis")

            val tvCarDetail: TextView = findViewById(R.id.tvCarDetail) as TextView

            tvCarDetail.text = opis


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
            tvCarDetail.text = car?.opis
            toolbarLayout.title = car?.marka
        } else {
            val errorMessage = try {
                "An error occurred: ${response.errorBody()?.string()}"
            } catch (e: IOException) {
                "An error occurred: error while decoding the error message."
            }

            Log.e(TAG, errorMessage)
            tvCarDetail.text = errorMessage
        }
    }

    override fun onFailure(call: Call<Car>, t: Throwable) {
        Log.w(TAG, "Error: ${t.message}", t)
    }

    companion object {
        private val TAG = CarDetailActivity::class.java.canonicalName
    }
}