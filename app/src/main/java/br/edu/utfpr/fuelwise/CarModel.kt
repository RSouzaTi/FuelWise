package br.edu.utfpr.fuelwise

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CarModel(
    val id: Int,
    val model: String,
    val consumptionGasoline: Double,
    val consumptionEthanol: Double
) : Parcelable