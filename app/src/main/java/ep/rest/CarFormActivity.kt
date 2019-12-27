package ep.rest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText

import androidx.appcompat.app.AppCompatActivity

import java.io.IOException

import okhttp3.Headers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CarFormActivity : AppCompatActivity(), View.OnClickListener, Callback<Void> {

    private var marka: EditText? = null
    private var cena: EditText? = null
    private var slika: EditText? = null
    private var aktiven: EditText? = null
    private var button: Button? = null

    private var car: Car? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_form)

        marka = findViewById(R.id.etMarka)
        cena = findViewById(R.id.etCena)
        slika = findViewById(R.id.etSlika)
        aktiven = findViewById(R.id.etAktiven)
        button = findViewById(R.id.button)
        button!!.setOnClickListener(this)

        val intent = intent
        car = intent.getSerializableExtra("ep.rest.car") as Car
        if (car != null) {
            marka!!.setText(car!!.marka)
            slika!!.setText(car!!.slika)
            cena!!.setText(car!!.cena.toString())
            aktiven!!.setText(car!!.aktiven.toString())
        }
    }

    override fun onClick(view: View) {
        val carMarka = marka!!.text.toString().trim { it <= ' ' }
        val carSlika = slika!!.text.toString().trim { it <= ' ' }
        val carCena = Integer.parseInt(cena!!.text.toString().trim { it <= ' ' })
        val carAktiven = Integer.parseInt(aktiven!!.text.toString().trim { it <= ' ' })

        if (car == null) {
            CarService.instance.insert(carMarka, carSlika, carCena,
                    carAktiven).enqueue(this)
        } else {
            CarService.instance.update(car!!.id, carMarka, carSlika, carCena,
                    carAktiven).enqueue(this)
        }
    }

    override fun onResponse(call: Call<Void>, response: Response<Void>) {
        val headers = response.headers()

        if (response.isSuccessful) {
            val id: Int
            if (car == null) {
                Log.i(TAG, "Insertion completed.")
                // Preberemo Location iz zaglavja
                val parts = headers.get("Location")?.split("/".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()
                id = parts?.get(parts.size - 1)?.toInt()!!
            } else {
                Log.i(TAG, "Editing saved.")
                id = car!!.id
            }
            val intent = Intent(this, CarDetailActivity::class.java)
            intent.putExtra("ep.rest.id", id)
            startActivity(intent)
        } else {
            var errorMessage: String
            try {
                errorMessage = "An error occurred: " + response.errorBody()?.string()
            } catch (e: IOException) {
                errorMessage = "An error occurred: error while decoding the error message."
            }

            Log.e(TAG, errorMessage)
        }

    }

    override fun onFailure(call: Call<Void>, t: Throwable) {
        Log.w(TAG, "Error: " + t.message, t)
    }

    companion object {
        private val TAG = CarFormActivity::class.java.canonicalName
    }
}