package Classes;

import com.estg.core.Container;
import com.estg.core.ItemType;
import com.estg.core.Measurement;
import com.estg.core.exceptions.MeasurementException;
import java.time.LocalDate;
import java.util.Arrays;
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
 * ContainerImp, classe que regista um contentor
 * 
 * @author Francisco Miguel Pereira Oliveira
 * @version 1.0
 * @since 2024-05-25
 */
public class ContainerImp implements Container {
    /**
     * Número inicial de Medições.
     */
    private static final int INICIAL_MEASUREMENTS = 5;
    
    /**
     * Código identificador do contentor.
     */
    private String code; 
    
    /**
     * Tipo de suplemento que o contentor carrega.
     */
    private ItemType type; 
    
    /**
     * Número atual de medições do contentor.
     */
    private int numMeasurements;
    
    /**
     * Coleção de medições do contentor.  
     */
    private Measurement[] measurements;
    
    /**
     * Capacidade máxima (em Kg) do contentor.
     */
    private double capacity;
    
    /**
    * Método construtor de ContainerImp
    * 
    * @param code código do contentor
    * @param type tipo de suplemento que o contentor carrega
    * @param capacity valor da capacidade máxima do contentor
    */ 
    public ContainerImp(String code, ItemType type, double capacity) {
        this.code = code;
        this.type = type;
        this.measurements = new Measurement[INICIAL_MEASUREMENTS];
        this.capacity = capacity;
        this.numMeasurements = 0;
    }   
    
    /**
    * Método getter do código identificador do contentor
    * 
    * @return o código identificador do contentor
    */
    @Override
    public String getCode() {
        return this.code;
    }
    
    /**
    * Método getter da capacidade máxima do contentor
    * 
    * @return o valor da capacidade máxima do contentor
    */
    @Override
    public double getCapacity() {
        return this.capacity;
    }
    
    /**
    * Método getter do tipo de suplemento que o contentor carrega
    * 
    * @return o tipo de suplemento que o contentor carrega
    */
    @Override
    public ItemType getType() {
        return this.type;
    }
    
    /**
    * Método getter do número de medições do contentor
    * 
    * @return o valor do número de medições do contentor
    */
    public int getNumMeasurements() {
        return this.numMeasurements;
    }
    
    /**
    * Método getter do conjunto de medições do contentor
    * 
    * @return o conjunto de medições do contentor
    */
    @Override
    public Measurement[] getMeasurements() {
        if (this.measurements.length == this.numMeasurements) {
            return this.measurements;
        }

        Measurement[] tmp = new Measurement[this.numMeasurements];

        for (int i = 0; i < this.numMeasurements; i++) {
            tmp[i] = this.measurements[i];
        }

        return tmp;
    }
    
    /**
    * Método que retorna o conjunto de medições do contentor que tiverem a data passada recebida
    * 
    * @param ld data da medição do contentor
    * @return o conjunto de medições do contentor
    */
    @Override
    public Measurement[] getMeasurements(LocalDate ld) {
        int count_tmp = 0;
        
        if (this.measurements.length == this.numMeasurements) {
            return this.measurements;
        }
        
        Measurement[] tmp = new Measurement[this.numMeasurements];
        
        for (int i = 0; i < this.numMeasurements; i++) {
            if (this.measurements[i].getDate().toLocalDate().equals(ld)) {
                tmp[count_tmp++] = this.measurements[i];
            }
        }
        
        return tmp;
    }
    
    /**
    * Método que verifica se a medição já foi adicionada ao contentor
    * 
    * @param measurement medição do contentor
    * @return true, se o contentor tiver uma medição igual à que foi passada por parâmetro
    * false, se a medição não foi encontrada
    */
    private boolean findMeasurement(Measurement measurement) {
        for (int i = 0; i < this.numMeasurements; i++) {
            if (this.measurements[i].equals(measurement)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
    * Método que duplica o espaço do array measurements sempre que necessário.
    */
    private void raiseMeasurements() {
        Measurement[] temp = new Measurement[this.numMeasurements + INICIAL_MEASUREMENTS];
        
        for (int i = 0; i < this.numMeasurements; i++) {
            temp[i] = this.measurements[i];
        }
        
        this.measurements = temp;
    }
    
    /**
    * Método que adiciona uma medição ao contentor
    * 
    * @param msrmnt medição do contentor
    * @return true, se a medição foi adicionada com sucesso
    * false, se não foi possível adicionar a medição
    * @throws MeasurementException, se a medição for nula, se o valor da medição for negativo,
    * se a data da medição for mais antiga do que a data da última medição introduzida,
    * ou se a medição já existe e tem um valor diferente da medição já existente no contentor
    */
    @Override
    public boolean addMeasurement(Measurement msrmnt) throws MeasurementException {
        if (msrmnt == null) {
            throw new MeasurementException("A medição é nula!");
        }
        
        if (msrmnt.getValue() < 0) {
            throw new MeasurementException("O valor da medição é negativo!");
        }
        
        
        if (this.capacity < msrmnt.getValue()) {
            throw new MeasurementException("O valor da medição é maior que a capacidade!");
        }
        
        
        if (this.numMeasurements == this.measurements.length) {
            this.raiseMeasurements();
        }

        if (this.numMeasurements > 0 && msrmnt.getDate().isBefore(this.measurements[(this.numMeasurements - 1)].getDate())) {
            throw new MeasurementException("A medição tem uma data anterior à data da última medição introduzida no contentor!");
        }
        
        for (Measurement measurement : this.getMeasurements()) {
            if (measurement.equals(msrmnt) && measurement.getValue() != msrmnt.getValue()) {
                throw new MeasurementException("A medição já existe e tem um valor diferente da medição já existente no contentor!");
            }
        }
        
        if (this.findMeasurement(msrmnt)) {
            return false;
        }
        
        this.measurements[this.numMeasurements++] = msrmnt;
        return true;
    }

    /**
    * Método toString de ContainerImp
    * 
    * @return os valores de type, measurements, code, capacity e numMeasurements
    */
    @Override
    public String toString() {
        return "ContainerImp: {" + "tipo = " + this.type + ", medições = " + Arrays.toString(this.measurements) + ", código = " + this.code + ", capacidade = " + this.capacity + ", número de medições = " + this.numMeasurements + '}';
    }

    /** Método equals de ContainerImp
     * 
     * Compara um contentor com o objeto recebido
     * 
     * @param obj o objeto a ser comparado com um contentor
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
        
        final ContainerImp other = (ContainerImp) obj;
        
        if (Objects.equals(other.code, this.code) || (Objects.equals(other.measurements, this.measurements) && Objects.equals(this.type, other.type))) {
            return true;
        }
           
        return false;
    }
}