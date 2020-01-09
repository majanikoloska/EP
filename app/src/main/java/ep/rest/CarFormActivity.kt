package ep.rest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText

import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_car_form.*

import java.io.IOException

import okhttp3.Headers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CarFormActivity : AppCompatActivity(), Callback<Void> {

    private var car: Car? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_form)

        button.setOnClickListener {
            val marka = etMarka.text.toString().trim()
            val aktiven = etAktiven.text.toString().trim().toInt()
            val description = etOpis.text.toString().trim()
            val price = etCena.text.toString().trim().toInt()

            if (car == null) { // dodajanje
                CarService.instance.insert(marka, description, price,
                        aktiven).enqueue(this)
            } else { // urejanje
                CarService.instance.update(car!!.id_avto, marka, description, price,
                        aktiven).enqueue(this)
            }
        }

        val car = intent?.getSerializableExtra("ep.rest.book") as Car?
        if (car != null) {
            etMarka.setText(car.marka)
            etOpis.setText(car.opis)
            etCena.setText(car.cena.toString())
            etAktiven.setText(car.aktiven.toString())
            this.car = car
        }
    }

    override fun onResponse(call: Call<Void>, response: Response<Void>) {
        val headers = response.headers()

        if (response.isSuccessful) {
            val id = if (car == null) {
                // Preberemo Location iz zaglavja
                Log.i(TAG, "Insertion completed.")
                val parts = headers.get("Location")?.split("/".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()
                // spremenljivka id dobi vrednost, ki jo vrne zadnji izraz v bloku
                parts?.get(parts.size - 1)?.toInt()
            } else {
                Log.i(TAG, "Editing saved.")
                // spremenljivka id dobi vrednost, ki jo vrne zadnji izraz v bloku
                car!!.id_avto
            }

            val intent = Intent(this, CarDetailActivity::class.java)
            intent.putExtra("ep.rest.id", id)
            startActivity(intent)
        } else {
            val errorMessage = try {
                "An error occurred: ${response.errorBody()?.string()}"
            } catch (e: IOException) {
                "An error occurred: error while decoding the error message."
            }

            Log.e(TAG, errorMessage)
        }
    }

    override fun onFailure(call: Call<Void>, t: Throwable) {
        Log.w(TAG, "Error: ${t.message}", t)
    }

    companion object {
        private val TAG = CarFormActivity::class.java.canonicalName
    }
}