package Vehicles;

import com.estg.core.ItemType;
import com.estg.pickingManagement.Vehicle;
import java.util.Objects;

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
 * VehicleImp, classe que representa os veículos para transporte dos contentores
 * 
 * @author Artur Gentil Silva Pinto
 * @version 1.0
 * @since 2024-05-25
 */
public class VehicleImp implements Vehicle {

    /**
    * A matrícula do veículo
    */
    private String matricula;

    /**
    * A capacidade máxima (em Kg) que o veículo pode transportar
    */
    private double max_capacity;
    
    /*
    * Tipo de mercadoria que o veículo pode transportar
    */
    private ItemType supplyType;
    
    /*
    * Capacidade atual (em Kg) do veículo
    */
    private double atual_capacity;
    
    /**
    * Método construtor de VechicleCla
    * 
    * @param matricula matrícula do veículo, única para cada veículo;
    * @param max_capacity Capacidade máxima que o veículo pode transportar (em kg)
    * @param supplyType Tipo de mercadoria que o veículo pode transportar
    */   
    public VehicleImp(String matricula, double max_capacity, ItemType supplyType) {
        this.matricula = matricula;
        if(max_capacity <= 0) {
            this.max_capacity = 0;
        } else {
            this.max_capacity = max_capacity;
        }
        
        this.supplyType = supplyType;
        this.atual_capacity = 0;
    }

    /*
    * Método getter para a matrícula do veículo
    * 
    * @return a matrícula do veículo
    */
    public String getMatricula() {
        return this.matricula;
    }
    
    /*
    * Método getter para o tipo de mercadoria que o veículo pode transportar
    * 
    * @return o tipo de mercadoria que o veículo pode transportar
    */
    @Override
    public ItemType getSupplyType() {
        return this.supplyType;
    }

    /*
    * Método getter para a capacidade máxima (Kg) que o veículo pode transportar
    * 
    * @return a capacidade máxima (Kg) que o veículo pode transportar
    */
    @Override
    public double getMaxCapacity() {
        return this.max_capacity;
    }
    
    /*
    * Método getter para a capacidade atual (Kg) do veículo
    * 
    * @return a capacidade atual (Kg) do veículo
    */
    public double getAtualCapacity() {
        return this.atual_capacity;
    }
    
    /*
    * Método para atualizar a capacidade atual (Kg) do veículo 
    */
    public void updateCapacity(double capacity) {       
        this.atual_capacity = this.atual_capacity + capacity;
    }
    
    /*
    * Método para reiniciar a capacidade atual do veículo para 0 Kg (para quando o veículo tiver de descarregar na base)
    *
    */
    public void resetAtualCapacity() {
        this.atual_capacity = 0;
    }

    /** Método equals de VehicleImp
     * 
     * Compara um veículo com o objeto recebido
     * 
     * @param obj o objeto a ser comparado com um veículo
     * @return true, se os objetos forem iguais,
     * false, caso contrário
     * 
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final VehicleImp other = (VehicleImp) obj;

        return Objects.equals(this.matricula, other.matricula) || (Objects.equals(other.max_capacity, this.max_capacity) && Objects.equals(other.supplyType, obj));
    }

    /**
     * Método toString de VehicleImp, que retorna a representação em string de um veículo
     *
     * @return uma string que representa um veículo (contém a matrícula, a capacidade máxima e o tipo de suprimento que carrega).
     */
    @Override
    public String toString() {
        return "VehicleImp: {" + "matrícula = " + matricula + ", capacidade máxima = " + max_capacity + ", tipo de item = " + supplyType + ", capacidade atual = " + atual_capacity + '}';
    }

}