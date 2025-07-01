package Classes;

import java.util.Objects;

/**
 * Nome: Artur Gentil Silva Pinto
 * Número: 8230138
 * Turma: LEIT1 
 * 
 * 
 * Nome: Francisco Miguel Pereira Oliveira
 * Número: 8230148
 * Turma: LEIT2
 *
*/

/**
 * Distance, classe para guardar a distância e a duração entre 2 aid boxes
 * 
 * @author Artur Gentil Silva Pinto
 * @version 1.0
 * @since 2024-05-25
 */
public class Distance {
    
    /**
     * Aid box destino.
     */
    private String aid_destino; 

    /**
     * Distância entre a aid box origem e a aid box destino.
     */
    private double distance;

    /**
     * Duração entre a aid box origem e a aid box destino.
     */
    private double duration; 

    /**
    * Método construtor de Distance
    * 
    * @param aid_destino aid box destino
    * @param distance valor da distância
    * @param duration valor da duração
    */ 
    public Distance(String aid_destino, double distance, double duration) {
        this.aid_destino = aid_destino;
        if (distance <= 0) {
            this.distance = 0;
        } else {
            this.distance = distance;
        }
        if (duration <= 0) {
            this.duration = 0;
        } else {
            this.duration = duration;
        }
    }

    /**
    * Método getter do código da aid box destino
    * 
    * @return o código da aid box destino
    */
    public String getAidDestino() {
        return this.aid_destino;
    }

    /**
    * Método getter da distância entre a aid box origem e a aid box destino
    * 
    * @return a distância entre a aid box origem e a aid box destino
    */
    public double getDistance() {
        return this.distance;
    }

    /**
    * Método getter da duração entre a aid box origem e a aid box destino
    * 
    * @return a duração entre a aid box origem e a aid box destino
    */
    public double getDuration() {
        return this.duration;
    }

    /** Método equals de Distance
     * 
     * Compara uma aid box com o objeto recebido
     * 
     * @param obj o objeto a ser comparado com uma aid box
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
        final Distance other = (Distance) obj;

        return Objects.equals(this.aid_destino, other.aid_destino);
    }

    /**
    * Método toString de Distance
    * 
    * @return o código da aid box destino, a distância e a duração
    */
    @Override
    public String toString() {
        return "Distance: {" + "aidbox destino = " + aid_destino + ", distância = " + distance + ", duração = " + duration + '}';
    }
}