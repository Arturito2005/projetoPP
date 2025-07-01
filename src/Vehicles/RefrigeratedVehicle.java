package Vehicles;

import com.estg.core.ItemType;

/**
* Nome: Artur Gentil Silva Pinto
* Número: 8230138
* Turma: LEIT1
*
* Nome: Francisco Miguel Pereira Oliveira
* Número: 8230148
* Turma: LEIT2
*
*/

/**
 * RefrigeratedVehicle, classe que regista um veículo refrigerado (transporta alimentos perecíveis)
 * 
 * @author Artur Gentil Silva Pinto
 * @version 1.0
 * @since 2024-05-25
 */
public class RefrigeratedVehicle extends VehicleImp {
    
    /**
    * Número máximo de quilómetros que o veículo pode transportar com carga
    */
    public final double max_km;
    
    /**
    * Método construtor de RefrigeratedVehicle
    * 
    * @param matricula matrícula do veículo, única para cada veículo;
    * @param max_capacity capacidade máxima que o veículo pode transportar (em Kg)
    * @param max_km número de quilómetros que o veículo pode transportar com carga
    */  
    public RefrigeratedVehicle(String matricula, double max_capacity,
                               double max_km) {
        
        super(matricula, max_capacity, ItemType.PERISHABLE_FOOD);
        if (max_km > 0) {
            this.max_km = max_km;
        } else {
            this.max_km = 0;
        }
    }
    
    /**
    * Método getter para o número máximo de quilómetros que o veículo pode transportar com carga
    * 
    * @return o número máximo de quilómetros que o veículo pode transportar com carga
    */
    public double getMaxKm() {
        return this.max_km;
    }

    /**
    * Método toString, que retorna a representação em string de um veículo refrigerado
    *
    * @return uma string que representa um veículo refrigerado (contém o número máximo de quilómetros que este pode percorrer com carga)
    */
    @Override
    public String toString() {
        return super.toString() + " Refrigerated Vehicle: {" + "limite de quilómetros = " + max_km + '}';
    }
 
}