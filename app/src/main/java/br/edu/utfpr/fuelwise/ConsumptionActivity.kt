package br.edu.utfpr.fuelwise // Verifique se o pacote está correto

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class ConsumptionActivity : AppCompatActivity() {

    private lateinit var viewModel: ConsumptionViewModel
    private lateinit var carModel: CarModel

    // 1. DECLARAÇÃO DAS VIEWS (Necessário para initializeViews)
    private lateinit var distanceInput: EditText
    private lateinit var gasPriceInput: EditText
    private lateinit var ethanolPriceInput: EditText
    private lateinit var calculateButton: Button
    private lateinit var resultTextView: TextView
    private lateinit var carModelTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consumption)

        // RECUPERAÇÃO DO ARGUMENTO E INICIALIZAÇÃO DO VIEWMODEL
        carModel = intent.getParcelableExtra("CAR_MODEL")
            ?: throw IllegalStateException("CarModel is required.")

        val factory = ConsumptionViewModel.Factory(carModel)
        viewModel = ViewModelProvider(this, factory)[ConsumptionViewModel::class.java]

        // CHAMADA DOS AUXILIARES
        initializeViews()
        setupUI() // Resolve o erro de referência ao exibir carModel.model
        setupListeners()
        setupObserver()
    }

    private fun initializeViews() {
        distanceInput = findViewById(R.id.input_distance)
        gasPriceInput = findViewById(R.id.input_gas_price)
        ethanolPriceInput = findViewById(R.id.input_ethanol_price)
        calculateButton = findViewById(R.id.button_calculate)
        resultTextView = findViewById(R.id.text_result)
        carModelTextView = findViewById(R.id.text_car_model)
    }

    private fun setupUI() {
        carModelTextView.text = getString(
            R.string.consumption_car_details_format,
            carModel.model, // <-- USAMOS A VARIÁVEL carModel, QUE JÁ ESTÁ NO ESCOPO
            carModel.consumptionGasoline,
            carModel.consumptionEthanol
        )
        // Valores padrão para facilitar testes
        gasPriceInput.setText("5.50")
        ethanolPriceInput.setText("4.00")
        distanceInput.setText("100.0")
    }

    private fun setupListeners() {
        calculateButton.setOnClickListener {
            // Conversão de Strings para Double com tratamento de erro
            val distance = distanceInput.text.toString().toDoubleOrNull() ?: 0.0
            val priceGas = gasPriceInput.text.toString().toDoubleOrNull() ?: 0.0
            val priceEthanol = ethanolPriceInput.text.toString().toDoubleOrNull() ?: 0.0

            if (distance > 0.0 && priceGas > 0.0 && priceEthanol > 0.0) {
                viewModel.calculateConsumption(distance, priceGas, priceEthanol)
            } else {
                resultTextView.text = getString(R.string.consumption_error_input)
            }
        }
    }

    private fun setupObserver() {
        viewModel.calculationResult.observe(this) { result ->
            val resultText = getString(
                R.string.calculation_result_format,
                result.bestFuel,
                result.totalCost,
                result.gasCost,
                result.ethanolCost
            )
            resultTextView.text = resultText
        }
    }
}