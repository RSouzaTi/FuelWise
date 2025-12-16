package br.edu.utfpr.fuelwise

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


data class CalculationResult(
    val totalCost: Double,
    val bestFuel: String,
    val gasCost: Double,
    val ethanolCost: Double
)

class ConsumptionViewModel(
    val carModel: CarModel
) : ViewModel() {

    // LiveData para expor o resultado do cálculo
    private val _calculationResult = MutableLiveData<CalculationResult>()
    val calculationResult: LiveData<CalculationResult> = _calculationResult

    /**
     * Realiza o cálculo de custo de viagem e compara a eficiência dos combustíveis.
     * @param distance Distância da viagem (km).
     * @param priceGas Preço da gasolina por litro.
     * @param priceEthanol Preço do etanol por litro.
     */
    fun calculateConsumption(
        distance: Double,
        priceGas: Double,
        priceEthanol: Double
    ) {
        // 1. CÁLCULO DO CUSTO TOTAL (Gasolina)
        // Consumo em litros para a distância: (Distância / Km/L Gasolina)
        val requiredGas = distance / carModel.consumptionGasoline
        // Custo total da viagem com Gasolina
        val gasTotalCost = requiredGas * priceGas

        // 2. CÁLCULO DO CUSTO TOTAL (Etanol)
        // Consumo em litros para a distância: (Distância / Km/L Etanol)
        val requiredEthanol = distance / carModel.consumptionEthanol
        // Custo total da viagem com Etanol
        val ethanolTotalCost = requiredEthanol * priceEthanol

        // 3. DETERMINANDO O MELHOR COMBUSTÍVEL
        val bestFuel = if (ethanolTotalCost < gasTotalCost) {
            "Etanol"
        } else {
            "Gasolina"
        }

        // 4. ATUALIZANDO O LIVE DATA
        _calculationResult.value = CalculationResult(
            totalCost = minOf(gasTotalCost, ethanolTotalCost),
            bestFuel = bestFuel,
            gasCost = gasTotalCost,
            ethanolCost = ethanolTotalCost
        )
    }

    // Função de Factory para instanciar o ViewModel com o argumento CarModel
    // Garante que o ViewModel possa receber o CarModel no seu construtor
    class Factory(private val carModel: CarModel) : androidx.lifecycle.ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ConsumptionViewModel::class.java)) {
                return ConsumptionViewModel(carModel) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
