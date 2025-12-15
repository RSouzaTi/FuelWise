package br.edu.utfpr.fuelwise

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CarAdapter(
    private val cars: List<CarModel>,
    // Lambda function para tratar o clique
    private val onCarClick: (CarModel) -> Unit
) : RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_car, parent, false)
        return CarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val car = cars[position]
        holder.bind(car)

        holder.itemView.setOnClickListener {
            onCarClick(car)
        }
    }

    override fun getItemCount(): Int = cars.size

    class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val modelText: TextView = itemView.findViewById(R.id.text_model)
        private val gasConsumptionText: TextView = itemView.findViewById(R.id.text_consumption_gas)
        private val ethanolConsumptionText: TextView = itemView.findViewById(R.id.text_consumption_ethanol)

        fun bind(car: CarModel) {
            modelText.text = car.model

            // Usa strings de recurso para formatar o texto (i18n)
            gasConsumptionText.text = itemView.context.getString(
                R.string.consumption_gasoline_format,
                car.consumptionGasoline.toString()
            )

            ethanolConsumptionText.text = itemView.context.getString(
                R.string.consumption_ethanol_format,
                car.consumptionEthanol.toString()
            )
        }
    }
}

