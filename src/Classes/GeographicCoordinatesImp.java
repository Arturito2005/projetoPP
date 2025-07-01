package Classes;

import com.estg.core.GeographicCoordinates;
import java.util.Objects;

/**
* Nome: Francisco Miguel Pereira Oliveira
* Número: 8230148
* Turma: LEIT2
*
* Nome: Artur Gentil Silva Pinto
* Número: 8230138
* Turma: LEIT1
*/

/**
 * GeographicCoordinatesImp, classe que regista a latitude e a longitude de uma caixa de suprimentos
 * 
 * @author Francisco Miguel Pereira Oliveira
 * @version 1.0
 * @since 2024-05-25
 */
public class GeographicCoordinatesImp implements GeographicCoordinates {
   /**
    * Atributo que controla o código identificador de modo a que seja único e sequencial.    
    */
    private static int nextId = 0;
    
    /**
     * Código identificador para um conjunto de coordenadas.
     */
    private final int id;
    
    /**
     * Medida (em graus) de um ponto da superfície terrestre até à Linha do Equador.
     */
    private double latitude;
    
    /**
     * Medida (em graus) de um ponto da superfície terrestre até ao Meridiano de Greenwich.
     */
    private double longitude; 
    
    /**
    * Método construtor de GeographicCoordinatesImp
    * 
    * @param latitude valor da latitude
    * @param longitude valor da longitude
    */
    public GeographicCoordinatesImp(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = nextId++;
    }
    
    /**
    * Método getter da latitude
    * 
    * @return o valor da latitude
    */
    @Override
    public double getLatitude() {
        return this.latitude;
    }
    
    /**
    * Método getter da longitude
    * 
    * @return o valor da longitude
    */
    @Override
    public double getLongitude() {
        return this.longitude;
    }

    /**
    * Método toString de GeographicCoordinatesImp
    * 
    * @return os valores de latitude e longitude
    */
    @Override    
    public String toString() {
        return "GeographicCoordinatesImp: {" + "id = " + this.id + ", latitude = " + this.latitude + ", longitude = " + this.longitude + '}';
    }

    /** Método equals de GeographicCoordinatesImp
     * 
     * Compara um conjunto de coordenadas com o objeto recebido
     * 
     * @param obj o objeto a ser comparado com um conjunto de coordenadas
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
        
        final GeographicCoordinatesImp other = (GeographicCoordinatesImp) obj;
        
        if (Objects.equals(this.id, other.id) || (Objects.equals(this.latitude, other.latitude) && Objects.equals(this.longitude, other.longitude))) {
            return true;
        }

        return false;
    }
}