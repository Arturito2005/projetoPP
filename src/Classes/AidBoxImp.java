package Classes;

import com.estg.core.AidBox;
import com.estg.core.Container;
import com.estg.core.GeographicCoordinates;
import com.estg.core.ItemType;
import com.estg.core.exceptions.AidBoxException;
import com.estg.core.exceptions.ContainerException;
import java.util.Arrays;
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
 * AidBoxImp, classe que representa as aid boxes
 * 
 * @author Artur Gentil Silva Pinto
 * @version 1.0
 * @since 2024-05-28
 */

public class AidBoxImp implements AidBox{
    /**
     * Número máximo de containers que a aid box pode ter.
     */
    private static final int NUM_MAX_CONTAINER = 4;
    
    /**
    * Código da aid box.
    */
    private String code;
    
    /**
    * Contador para o número de containers guardados no Array de Containers.
    */
    private int cont_container;
    
    /**
    * Array de Containers.
    */
    private Container[] container;
    
    /**
    * Zona onde está a aid box.
    */
    private String zone;
    
    /**
    * Referência do Local onde está a aid box.
    */
    private String ref_local;
    
    /**
    * Coordenadas Geográficas da aid box.
    */
    private GeographicCoordinates geografic_coord;
    
    /**
    * Contador das distâncias.
    */
    private int cont_dist;
    
    /**
    * Array de Distâncias.
    */
    private Distance[] distance;

    /**
    * Método construtor de AidBoxImp
    * 
    * @param code código da aid box, único para cada uma;
    * @param zone Zona onde está a aid box
    * @param ref_local Local onde está a aid box
    * @param geografic_coord Coordenadas geográficas da aid box
    */   
    public AidBoxImp(String code, String zone, String ref_local, GeographicCoordinates geografic_coord) {
        this.code = code;
        this.cont_container = 0;
        this.container = new Container[NUM_MAX_CONTAINER];
        this.zone = zone;
        this.ref_local = ref_local;
        this.geografic_coord = geografic_coord;
        this.cont_dist = 0;
        this.distance = new Distance[NUM_MAX_CONTAINER];
    }
    
    /**
    * Método construtor de AidBoxImp, mais utilizado para ler dos ficheiros JSON
    * 
    * @param code código da aidBox, único para cada uma;
    * @param zone Zona onde está a aidBox
    * @param geografic_coord Coordenadas geográficas da aidBox
    */
    public AidBoxImp(String code, String zone, GeographicCoordinates geografic_coord) {
        this.code = code;
        this.cont_container = 0;
        this.container = new Container[NUM_MAX_CONTAINER];
        this.zone = zone;
        this.ref_local = null;
        this.geografic_coord = geografic_coord;
        this.cont_dist = 0;
        this.distance = new Distance[NUM_MAX_CONTAINER];
    }
    
    /**
    * Método getter para obter o código da aid box
    * 
    * @return o código da aid box
    */
    @Override
    public String getCode() {
        return this.code;
    }

    /**
    * Método getter para obter a zona da aid box
    * 
    * @return a zona da aid box
    */
    @Override
    public String getZone() {
        return this.zone;
    }

    /**
    * Método getter para obter uma descrição do local onde a aid box está instalada
    * 
    * @return a referência do local da aid box
    */
    @Override
    public String getRefLocal() {
        return this.ref_local;
    }
    
    /**
    * Método getter para obter as coordenadas geográficas da aid box
    * 
    * @return as coordenadas geográficas da aid box
    */
    @Override
    public GeographicCoordinates getCoordinates() {
        return this.geografic_coord;
    }
    
    /**
     * Método que verifica se uma aid box existe no array de Distances
     * 
     * @param code_Aid, código da aid box
     * @return i, posição da aid box, se esta for encontrada no Array de Distances
     * @return -1, caso não seja encontrada
     */
    private int findAidBox(String code_Aid) {
        for (int i = 0; i < this.cont_dist; i++) {
            if (this.distance[i].getAidDestino().equals(code_Aid)) {
                return i;
            }
        }
        
        return -1;
    }

    /**
    * Método que duplica o espaço do array distance sempre que necessário.
    */
    private void raise_distance() {
        Distance[] temp = new Distance[this.cont_dist + 1];
        
        for (int i = 0; i < this.cont_dist; i++) {
            temp[i] = this.distance[i];
        }
        
        this.distance = temp;
    }
    
    /**
     * Método que adiciona uma nova distância à aid box
     * 
     * @param distance nova distância a adicionar
     * @return true, se conseguiu adicionar
     */
    public boolean addDistance(Distance distance) {
        if (this.distance.length == this.cont_dist) {
            this.raise_distance();
        }
        
        this.distance[this.cont_dist++] = distance;
        return true;
    }
    
    /**
    * Método getter para a distância entre a aid box atual e a aidbox recebida
    * 
    * @param aidbox aid box alvo
    * @return a distância entre duas aid boxes
    * @throws AidBoxException, se a aid box alvo não existir
    */
    @Override
    public double getDistance(AidBox aidbox) throws AidBoxException {
        if (aidbox == null) {
            throw new AidBoxException("A aid box está nula!");
        }
        
        int pos = this.findAidBox(aidbox.getCode()); 
        
        if (pos == -1) {            
            System.out.println("Não existe nenhuma comparação entre estas duas aid boxes!");
            return 0;
        }
                
        return this.distance[pos].getDistance();
    }
       
    /**
    * Método getter para a duração entre a aid box atual e a aid box recebida
    * 
    * @param aidbox aid box alvo
    * @return o tempo (em segundos) que demora a chegar de uma aid box à outra
    * @throws AidBoxException, se a aid box alvo não existir
    */
    @Override
    public double getDuration(AidBox aidbox) throws AidBoxException {
        if (aidbox == null) {
            throw new AidBoxException("A aidbox está nula!");
        }
        
        int pos = this.findAidBox(aidbox.getCode()); 
        
        if (pos == -1) {
            System.out.println("Não existe nenhuma comparação entre estas duas aid boxes!");
            return 0;
        }
                
        return this.distance[pos].getDuration();
    }
        
    /**
    * Método para comparar se o container atual é igual ao container recebido
    * 
    * @param container container a comparar
    * @return true, se os containers forem iguais
    * false, se os containers forem diferentes
    */
    private boolean find_container(Container container) {
        for (int i = 0; i < this.cont_container; i++) {
            if (this.container[i].equals(container)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
    * Método para comparar se o container atual tem o mesmo tipo que o container recebido
    * 
    * @param container container a comparar
    * @return true, se os containers forem do mesmo tipo
    * false, se os containers não forem do mesmo tipo 
    */
    private boolean equalContainerType(Container container) {
        for (int i = 0; i < this.cont_container; i++) {
            if (this.container[i].getType() == container.getType()) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
    * Método para adicionar um novo container à aid box
    * 
    * @param cntnr container a adicionar
    * @return true, se o container foi inserido na lista de containers
    * @throws ContainerException, se o container é null ou 
    * se a aid box já tiver um container daquele tipo
    */
    @Override
    public boolean addContainer(Container cntnr) throws ContainerException {
        if (cntnr == null) {
            throw new ContainerException("O Container está nulo!");
        }

        if (this.find_container(cntnr)) {
            throw new ContainerException("O container que tentou introduzir, já está na lista de containers!");
        }

        if (this.equalContainerType(cntnr)) {
            throw new ContainerException("Já existe um container do tipo " + cntnr.getType() + " !");
        }

        if (this.container.length == this.cont_container) {
            throw new ContainerException("Número de containers máximo atingido!");
        }

        this.container[this.cont_container++] = cntnr;
        return true;
    }
    
    /**
    * Método para retirar um container da aid box
    * 
    * @param container container 
    * @return true, se o container foi removido com sucesso
    * false, se o container não existir 
    */
    public boolean deleteContainer(Container container) {
        if (this.cont_container == 0) {
            System.out.println("Não existem containers para eliminar!");
            return false;
        }
        
        for (int i = 0; i < this.cont_container; i++) {
            for (int j = i; j < this.cont_container - 1; j++) {
                this.container[j] = this.container[j + 1];
            }
            this.container[this.cont_container - 1] = null;
            this.cont_container--;
        }
        
        return true;
    }

    /**
    * Método que, considerando o tipo, retorna o container correspondente
    * 
    * @param it tipo do container
    * @return o container, caso exista um container do tipo recebido numa certa aid box
    * null, se o container daquele tipo não existir
    */
    @Override
    public Container getContainer(ItemType it) {
        for (int i = 0; i < this.cont_container; i++) {
            if (this.container[i].getType().equals(it)) {
                return this.container[i];
            }
        }
        
        System.out.println("Não existe nenhum container do tipo " + it + " !");
        return null;
    }
        
    /**
    * Método que devolve uma cópia dos containers existentes
    * @return uma cópia do conjunto dos containers existentes
    */
    @Override
    public Container[] getContainers() { 
        if (this.cont_container == this.container.length) {
            return this.container;
        }
                
        Container[] temp = new Container[this.cont_container];
            
        for (int i = 0; i < this.cont_container; i++) {
            temp[i] = this.container[i];
        }
        
        return temp;
    }
   
    /** Método equals de AidBoxImp
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
        
        final AidBoxImp other = (AidBoxImp) obj;
        
        if (Objects.equals(this.code, other.code) || (Arrays.deepEquals(this.container, other.container) && Objects.equals(this.geografic_coord, other.geografic_coord))) {
            return true;
        }
        
        return false;
    }

    /**
    * Método toString de AidBoxImp, que retorna a representação em string de uma aid box.
    *
    * @return uma string que representa uma aid box (contém o código da aid box, número total de containers,
    * zona, referência do local e coordenadas geográficas, etc).
    */
    @Override
    public String toString() {
        return "AidBoxImp: {" + "code = " + this.code + ", cont_container = " + this.cont_container + ", container = " + this.container + ", zone = " + this.zone + ", ref_local = " + this.ref_local + ", geografic_coord = " + this.geografic_coord + ", cont_dist = " + this.cont_dist + ", distance = " + this.distance + '}';
    }
    
}