package Classes;

import com.estg.core.Measurement;
import java.time.LocalDateTime;

/*
* Nome: Francisco Miguel Pereira Oliveira
* Número: 8230148
* Turma: LEIT2
*
* Nome: Artur Gentil Silva Pinto
* Número: 8230138
* Turma: LEIT1
*/

/**
 * MeasurementImp, classe que regista a data de uma medição e o valor (em kg) de um contentor
 * 
 * @author Francisco Miguel Pereira Oliveira
 * @version 1.0
 * @since 2024-05-25
 */
public class MeasurementImp implements Measurement {
    private static int nextId = 0; // atributo que controla o identificador de modo a que seja único e sequencial
    
    private final int id; // identificador para uma dada medição
    
    private LocalDateTime date; // data de uma dada medição
    
    private double value; // valor (em Kg) de uma dada medição
    
    /**
    * Método construtor de MeasurementImp
    * 
    * @param date data da medição
    * @param value valor da medição (em Kg)
    */
    public MeasurementImp(LocalDateTime date, double value) {
        this.date = date;
        if (value < 0) {
            this.value = 0;
        } else {
            this.value = value;
        }
        this.id = nextId++;
    }
    
    /**
    * Método getter da data da medição
    * 
    * @return o valor da data da medição
    */
    @Override
    public LocalDateTime getDate() {
        return this.date;
    }
    
    /**
    * Método getter do valor da medição
    * 
    * @return o valor da medição
    */
    @Override
    public double getValue() { 
        return this.value;
    }

    /** Método equals de MeasurementImp
     * 
     * Compara uma medição com o objeto recebido
     * 
     * @param obj o objeto a ser comparado com uma medição
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
        
        final MeasurementImp other = (MeasurementImp) obj;
        
        if (this.id != other.id) {
            return false;
        }
        if (this.value != other.value) {
            return false;
        }
        return this.date.equals(other.date);
    }

    /**
    * Método toString de MeasurementImp
    * 
    * @return os valores de date e value
    */
    @Override
    public String toString() {
        return "MeasurementImp: {" + "id = " + this.id + ", date = " + this.date + ", value = " + this.value + '}';
    }
    
}