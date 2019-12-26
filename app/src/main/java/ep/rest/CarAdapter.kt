package ep.rest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

import java.util.ArrayList
import java.util.Locale

class CarAdapter(context: Context) : ArrayAdapter<Car>(context, 0, ArrayList()) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val car = getItem(position)

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.carlist_element, parent, false)
        }

        val tvMarka = convertView!!.findViewById<TextView>(R.id.tv_marka)
        //        final TextView tvSlika = convertView.findViewById(R.id.tv_slika);
        val tvPrice = convertView.findViewById<TextView>(R.id.tv_price)

        tvMarka.setText(car!!.marka)
        //        tvSlika.setText(car.slika);
        tvPrice.text = String.format(Locale.ENGLISH, "%.2f EUR", car!!.cena)

        return convertView
    }
}