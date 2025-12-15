package br.edu.utfpr.fuelwise

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import java.text.DecimalFormat

class MainViewModel(application: Application) : AndroidViewModel(application) {

    // Entradas
    val gasolinePrice = MutableLiveData("")
    val ethanolPrice = MutableLiveData("")
    val gasolineConsumption = MutableLiveData("")
    val ethanolConsumption = MutableLiveData("")

    // Saídas
    val resultMessage = MutableLiveData("")
    val resultColor = MutableLiveData(0xFF757575.toInt())

    private val context = application.applicationContext
    private val df = DecimalFormat("#.##")

    private fun getString(resId: Int, vararg formatArgs: Any): String {
        return context.getString(resId, *formatArgs)
    }

    fun calculateFuel() {
        val pGas = gasolinePrice.value?.replace(',', '.')?.toDoubleOrNull()
        val pEt = ethanolPrice.value?.replace(',', '.')?.toDoubleOrNull()
        val cGas = gasolineConsumption.value?.replace(',', '.')?.toDoubleOrNull()
        val cEt = ethanolConsumption.value?.replace(',', '.')?.toDoubleOrNull()

        // 1. Validação
        if (pGas == null || pEt == null || cGas == null || cEt == null ||
            pGas <= 0 || pEt <= 0 || cGas <= 0 || cEt <= 0) {

            resultMessage.value = getString(R.string.error_invalid_input)
            resultColor.value = 0xFFFF9800.toInt() // Laranja
            return
        }

        // 2. Lógica de Cálculo
        val consumptionFactor = cGas / cEt // Ex: 14.0 / 9.8 = 1.428... (Etanol deve custar até 70%)
        val priceRatio = pEt / pGas       // Ex: 3.50 / 5.00 = 0.7
        val equilibriumPrice = pGas * consumptionFactor

        // 3. Formatadores para o resultado
        val equilibriumPriceString = getString(R.string.currency_price, df.format(equilibriumPrice))
        val equilibriumPercentageString = getString(R.string.percentage, df.format(consumptionFactor * 100))

        val recommendedFuel: String
        val advantagePercentage: Double

        if (priceRatio < consumptionFactor) {
            // Etanol é vantajoso
            recommendedFuel = "Etanol (Álcool)"
            resultColor.value = 0xFF4CAF50.toInt() // Verde
            advantagePercentage = ((consumptionFactor - priceRatio) / consumptionFactor) * 100

            val percentageString = getString(R.string.percentage, df.format(advantagePercentage))

            resultMessage.value = getString(
                R.string.result_ethanol_better,
                recommendedFuel, // %1$s
                percentageString // %2$s
            )

        } else {
            // Gasolina é vantajosa
            recommendedFuel = "Gasolina"
            resultColor.value = 0xFFF44336.toInt() // Vermelho
            advantagePercentage = ((priceRatio - consumptionFactor) / consumptionFactor) * 100

            val percentageString = getString(R.string.percentage, df.format(advantagePercentage))

            resultMessage.value = getString(
                R.string.result_gasoline_better,
                recommendedFuel,        // %1$s
                equilibriumPriceString, // %2$s
                percentageString        // %3$s
            )
        }

        // Adiciona a nota do Ponto de Equilíbrio
        resultMessage.value += getString(R.string.result_equilibrium_point, equilibriumPercentageString)
    }

    fun setConsumption(gasoline: Double, ethanol: Double) {
        // Atualiza os campos de consumo após a seleção do carro
        gasolineConsumption.value = gasoline.toString().replace('.', ',')
        ethanolConsumption.value = ethanol.toString().replace('.', ',')
        resultMessage.value = ""
        resultColor.value = 0xFF757575.toInt()
    }
}