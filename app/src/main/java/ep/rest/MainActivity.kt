package ep.rest

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.AdapterView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class MainActivity : AppCompatActivity(), Callback<List<Car>> {

    private var adapter: CarAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = CarAdapter(this)
        items.adapter = adapter
        items.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
            val car = adapter?.getItem(i)
            if (car != null) {
                val intent = Intent(this, CarDetailActivity::class.java)
                intent.putExtra("ep.rest.id", car.id)
                startActivity(intent)
            }
        }

        container.setOnRefreshListener { CarService.instance.getAll().enqueue(this) }

        addButton.setOnClickListener {
            val intent = Intent(this, CarFormActivity::class.java)
            startActivity(intent)
        }

        CarService.instance.getAll().enqueue(this)
    }

    override fun onResponse(call: Call<List<Car>>, response: Response<List<Car>>) {
        val hits = response.body() ?: emptyList()

        if (response.isSuccessful) {
            Log.i(TAG, "Hits: " + hits.size)
            adapter?.clear()
            adapter?.addAll(hits)
        } else {
            val errorMessage = try {
                "An error occurred: ${response.errorBody()?.string()}"
            } catch (e: IOException) {
                "An error occurred: error while decoding the error message."
            }

            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            Log.e(TAG, errorMessage)
        }
        container.isRefreshing = false
    }

    override fun onFailure(call: Call<List<Car>>, t: Throwable) {
        Log.w(TAG, "Error: ${t.message}", t)
        container.isRefreshing = false
    }

    companion object {
        private val TAG = MainActivity::class.java.canonicalName
    }
}