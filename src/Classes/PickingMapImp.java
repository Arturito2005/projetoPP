package Classes;

import com.estg.pickingManagement.PickingMap;
import com.estg.pickingManagement.Route;
import java.time.LocalDateTime;
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
 * PickingMapImp, classe que representa os mapas de rotas
 * 
 * @author Artur Gentil Silva Pinto
 * @version 1.0
 * @since 2024-05-28
 */
public class PickingMapImp implements PickingMap { 
    
    /**
    * Atributo que vai auto incrementar o valor do id, ou seja, vai atribuir um valor a cada id.
    */
    private static int NextId = 0;
    
    /**
    * Identificador do picking map, unico para cada picking map.
    */
    private final int id;
    
    /**
    * Data em que o picking map foi criado.
    */
    private LocalDateTime date;
    
    /**
    * Array que guarda as rotas do mapa!.
    */
    private Route[] route;
    
    /**
    * Método construtor de PickingMapImp
    * 
    * @param route conjunto de rotas pertencentes ao picking map
    */   
    public PickingMapImp(Route[] route) {
        this.id = NextId++;
        this.date = LocalDateTime.now();
        this.route = route;
    }
    
    /**
    * Getter para a data do picking map
    * 
    * @return a data do picking map
    */
    @Override
    public LocalDateTime getDate() {
        return this.date;
    }

    /**
    * Getter para as rotas do picking map
    * 
    * @return o conjunto de rotas do picking map
    */
    @Override
    public Route[] getRoutes() {
        return this.route;
    }

    /** Método equals de PickingMapImp
     * 
     * Compara um picking map com o objeto recebido
     * 
     * @param obj o objeto a ser comparado com um picking map
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
        
        final PickingMapImp other = (PickingMapImp) obj;
        
        if (Objects.equals(this.id, other.id) || (Objects.equals(this.date, other.date) && Arrays.deepEquals(this.route, other.route))) {
            return true;
        }

        return false;
    } 

    /**
    * Função toString de PickingMapCla, que retorna a representação em string de um picking map.
    *
    * @return uma string que representa um picking map.
    */
    @Override
    public String toString() {
        return "PickingMapImp: {" + " id = " + this.id + ", data = " + this.date + ", rotas = " + Arrays.toString(this.route) + '}';
    }
}