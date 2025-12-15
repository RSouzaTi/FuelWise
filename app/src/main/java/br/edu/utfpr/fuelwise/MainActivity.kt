package br.edu.utfpr.fuelwise

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import br.edu.utfpr.fuelwise.databinding.ActivityMainBinding
import br.edu.utfpr.fuelwise.MainViewModel
import br.edu.utfpr.fuelwise.CarModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    companion object {
        // Chave de Intent para comunicação entre as Activities
        const val EXTRA_CAR_MODEL = "com.example.fuelwise.CAR_MODEL"
    }

    // Contrato para lançar e receber o resultado da tela de seleção de carro
    private val consumptionResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val carModel = result.data?.getParcelableExtra<CarModel>(EXTRA_CAR_MODEL)

            // Preenche os campos de consumo no ViewModel com os dados do carro selecionado
            carModel?.let {
                viewModel.setConsumption(it.consumptionGasoline, it.consumptionEthanol)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        setupInputBindings()
        setupListeners()
        setupObservers()
    }

    private fun setupInputBindings() {
        // Liga os inputs ao LiveData
        binding.editGasolinePrice.doAfterTextChanged { viewModel.gasolinePrice.value = it.toString() }
        binding.editEthanolPrice.doAfterTextChanged { viewModel.ethanolPrice.value = it.toString() }
        binding.editGasolineConsumption.doAfterTextChanged { viewModel.gasolineConsumption.value = it.toString() }
        binding.editEthanolConsumption.doAfterTextChanged { viewModel.ethanolConsumption.value = it.toString() }

        // Observa o LiveData para atualizar o EditText (quando o consumo retorna da 2ª tela)
        viewModel.gasolineConsumption.observe(this) { value ->
            if (binding.editGasolineConsumption.text.toString() != value) {
                binding.editGasolineConsumption.setText(value)
            }
        }
        viewModel.ethanolConsumption.observe(this) { value ->
            if (binding.editEthanolConsumption.text.toString() != value) {
                binding.editEthanolConsumption.setText(value)
            }
        }
    }

    private fun setupListeners() {
        // Ação de cálculo
        binding.buttonCalculate.setOnClickListener {
            viewModel.calculateFuel()
        }

        // Ação de buscar carro (inicia a ConsumptionActivity)
        binding.buttonSearchConsumption.setOnClickListener {
            val intent = Intent(this, ConsumptionActivity::class.java)
            consumptionResultLauncher.launch(intent)
        }
    }

    private fun setupObservers() {
        // Observa a mensagem de resultado e a exibe
        viewModel.resultMessage.observe(this) { message ->
            binding.textResult.text = message
        }

        // Observa a cor do resultado
        viewModel.resultColor.observe(this) { colorInt ->
            // Converte o Int (HEX) para a cor que o TextView pode usar
            val colorHex = String.format("#%06X", (0xFFFFFF and colorInt))
            binding.textResult.setTextColor(Color.parseColor(colorHex))
        }
    }
}