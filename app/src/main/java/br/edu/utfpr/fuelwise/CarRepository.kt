package br.edu.utfpr.fuelwise

class CarRepository {
    fun getPopularCars(): List<CarModel> {
        // Dados de consumo (Gasolina / Etanol)
        return listOf(
            CarModel(1, "Fiat Argo 1.0", 14.2, 9.9),
            CarModel(2, "Chevrolet Onix 1.0", 13.5, 9.6),
            CarModel(3, "VW Gol 1.0", 12.9, 8.8),
            CarModel(4, "Hyundai HB20 1.0", 14.5, 10.1),
            CarModel(5, "Renault Kwid 1.0", 15.6, 10.8),
            CarModel(6, "Toyota Corolla 2.0", 11.9, 8.3),
            CarModel(7, "Honda Civic 2.0", 11.5, 8.0),
            CarModel(8, "Ford Ka 1.0", 13.8, 9.5),
            CarModel(9, "Jeep Renegade 1.8", 10.5, 7.3)
        )
    }
}