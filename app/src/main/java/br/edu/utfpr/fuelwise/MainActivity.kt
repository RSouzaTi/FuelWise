package br.edu.utfpr.fuelwise

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView




class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var carAdapter: CarAdapter

    // Lista de carros de exemplo para inicialização
    private val carList = createCarList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Inicializar o RecyclerView
        recyclerView = findViewById(R.id.recycler_view_cars)
        // 2. Configurar o Adapter
        // Passamos a lista de carros e definimos o que acontece no clique (o lambda)
        carAdapter = CarAdapter(carList) { carModel ->
            // Chama a função de navegação quando um carro é clicado
            navigateToConsumption(carModel)
        }

        // 3. Configurar o LayoutManager
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = carAdapter
    }

    /**
     * Cria e retorna uma lista de CarModel (dados mockados para teste).
     */
    private fun createCarList(): List<CarModel> {
        return listOf(
            CarModel(1, "Fiat Argo 1.0", 14.2, 9.9),
            CarModel(2, "VW Polo TSI", 13.8, 9.6),
            CarModel(3, "Hyundai HB20 1.0", 12.8, 8.8),
            CarModel(4, "Chevrolet Onix Plus", 15.3, 10.9)
        )
    }

    /**
     * Inicia a ConsumptionActivity e passa o CarModel como Parcelable.
     * Esta é a função que resolveu o crash da última vez.
     */
    private fun navigateToConsumption(car: CarModel) {
        val intent = Intent(this, ConsumptionActivity::class.java)

        // CHAVE CRUCIAL: "CAR_MODEL" - DEVE CORRESPONDER AO QUE A CONSUMPTIONACTIVITY ESPERA
        intent.putExtra("CAR_MODEL", car)

        startActivity(intent)
    }
}