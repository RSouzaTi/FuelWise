package br.edu.utfpr.fuelwise
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.utfpr.fuelwise.databinding.ActivityConsumptionBinding

class ConsumptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConsumptionBinding
    private val repository = CarRepository() // Usa o repositório de dados

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityConsumptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Buscar Consumo"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val carList = repository.getPopularCars()

        val adapter = CarAdapter(carList) { carSelected ->
            returnResultToMainActivity(carSelected)
        }

        binding.recyclerCars.layoutManager = LinearLayoutManager(this)
        binding.recyclerCars.adapter = adapter
    }

    private fun returnResultToMainActivity(car: CarModel) {
        val resultIntent = Intent().apply {
            // Empacota o CarModel e o associa à chave de Intent da MainActivity
            putExtra(MainActivity.EXTRA_CAR_MODEL, car)
        }

        setResult(Activity.RESULT_OK, resultIntent)
        finish() // Fecha a Activity
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}