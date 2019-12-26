package ep.rest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import java.io.IOException
import java.util.ArrayList

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), Callback<List<Car>> {

    private val retrofit = Retrofit.Builder()
            .baseUrl("http://10.10.10.221/netbeans/mvc-rest/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val service = retrofit.create(CarService::class.java)

    private var container: SwipeRefreshLayout? = null
    private var button: Button? = null
    private var list: ListView? = null
    private val cars = ArrayList<Car>()
    private var adapter: CarAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list = findViewById(R.id.items)

        adapter = CarAdapter(this)
        list!!.adapter = adapter
        list!!.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val car = adapter!!.getItem(i)
            if (car != null) {
                val intent = Intent(this@MainActivity, CarDetailActivity::class.java)
                intent.putExtra("ep.rest.id", car.id)
                startActivity(intent)
            }
        }

        container = findViewById(R.id.container)
        container!!.setOnRefreshListener { CarService.instance.all.enqueue(this@MainActivity) }

        button = findViewById(R.id.add_button)
        button!!.setOnClickListener {
            val intent = Intent(this@MainActivity, CarFormActivity::class.java)
            startActivity(intent)
        }

        CarService.instance.all.enqueue(this@MainActivity)
    }

    override fun onResponse(call: Call<List<Car>>, response: Response<List<Car>>) {
        val hits = response.body() ?: emptyList()
        if (response.isSuccessful) {
            Log.i(TAG, "Hits: " + hits.size)
            adapter!!.clear()
            adapter!!.addAll(hits)
        } else {
            var errorMessage: String
            try {
                errorMessage = "An error occurred: " + response.errorBody()?.string()
            } catch (e: IOException) {
                errorMessage = "An error occurred: error while decoding the error message."
            }

            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            Log.e(TAG, errorMessage)
        }
        container!!.isRefreshing = false
    }

    override fun onFailure(call: Call<List<Car>>, t: Throwable) {
        Log.w(TAG, "Error: " + t.message, t)
        container!!.isRefreshing = false
    }

    companion object {
        private val TAG = MainActivity::class.java.canonicalName
    }
}