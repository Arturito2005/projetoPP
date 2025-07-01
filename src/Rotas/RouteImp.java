package Rotas;

import com.estg.core.AidBox;
import com.estg.core.Container;
import com.estg.core.exceptions.AidBoxException;
import com.estg.pickingManagement.Route;
import com.estg.pickingManagement.Vehicle;
import com.estg.pickingManagement.exceptions.RouteException;
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
 * RouteImp, classe que representa as rotas
 * 
 * @author Artur Gentil Silva Pinto
 * @version 1.0
 * @since 2024-05-28
 */
public class RouteImp implements Route {
    /**
    * Número inicial de aid boxes.
    */
    private final int INICIAL_AID_BOXES = 4;
    
    /**
    * Próximo código da próxima rota gerada.
    */
    private static int nextCode = 0;
    
    /**
    * Código da Rota.
    */
    private final int code;
    
    /**
    * Número de aid boxes no array de AidBox.
    */
    private int cont_aidbox;
    
    /**
    * Coleção de caixas de suprimento que constituem a rota.
    */
    private AidBox[] aidbox;
    
    /**
    * Veículo utilizado na rota.
    */
    private Vehicle vehicle;
    
    /**
     * Distância total da rota.
     */
    private double tot_distance;
   
    /**
     * Duração total da rota.
     */
    private double tot_duration;
   
    /**
    * Método construtor de RouteCla
    * 
    * @param vehicle veículo utilizado na rota
    */      
    public RouteImp(Vehicle vehicle) {
        this.code = nextCode++;
        this.cont_aidbox = 0;
        this.aidbox = new AidBox[INICIAL_AID_BOXES];
        this.vehicle = vehicle;
        this.tot_distance = 0;
        this.tot_duration = 0;
    }
   
    /**
    * Método para comparar se as aid boxes são iguais
    * 
    * @param aidbox aid box a comparar
    * @return i, se as aid boxes forem iguais
    * @return -1, se as aid boxes forem diferentes
    */
    private int findAidBox(AidBox aidbox) {
        for (int i = 0; i < this.cont_aidbox; i++) {
            if (this.aidbox[i].equals(aidbox)) {
                return i;
            }
        }
        
        return -1;
    }
    
    /**
    * Método que duplica o espaço do array das aid boxes sempre que necessário.
    */
    private void raiseAidboxes() {
        AidBox[] temp = new AidBox[this.cont_aidbox * 2];
        
        for (int i = 0; i < this.cont_aidbox; i++) {
            temp[i] = this.aidbox[i];
        }
        
        this.aidbox = temp;
    }
    
    /**
    * Método para adicionar uma nova aid box à rota
    * 
    * @param aidbox aid box a adicionar
    * @throws RouteException se a aid box não puder ser adicionada à rota:
    * se a aid box for nula
    * se a aid box já existir na rota
    * se a aid box não for compatível (se não tiver um container que possa ser escolhido pelo veículo) com o veículo da rota
    * 
    */
    @Override
    public void addAidBox(AidBox aidbox) throws RouteException {
        if (aidbox == null) {
            throw new RouteException("A aid box a introduzir está nula!");
        }
        
        if (findAidBox(aidbox) != -1) {
            throw new RouteException("A aid box que está a tentar introduzir já existe na rota!");
        }
        
        if (aidbox.getContainers().length == 0) {
            throw new RouteException("A aid box que está a tentar introduzir não possui nenhum container!");
        }
        
        if (this.cont_aidbox == this.aidbox.length) {
            this.raiseAidboxes();
        }
        
        this.aidbox[this.cont_aidbox++] = aidbox;
    }

    /**
    * Método para remover uma aid box à rota
    * 
    * @param aidbox aid box a remover
    * @return temp, a aid box eliminada
    * @throws RouteException se a aid box não puder ser removida da rota:
    * se a aid box for nula
    * se a aid box não existir na rota
    */
    @Override
    public AidBox removeAidBox(AidBox aidbox) throws RouteException {
        if (aidbox == null) {
            throw new RouteException("A aid box a introduzir está nula!");
        }
        
        int pos = findAidBox(aidbox); 
        
        if (pos == -1) {
            throw new RouteException("A aid box que está a tentar remover não existe na rota!");
        }
        
        AidBox temp = aidbox;
        
        for (int i = pos; i < (this.cont_aidbox - 1); i++) {
            this.aidbox[i] = this.aidbox[i + 1];
        }
        
        this.aidbox[(this.cont_aidbox - 1)] = null;
        this.cont_aidbox--;
        return temp;        
    }

    /**
    * Método para verificar se a rota já contém uma aid box
    * 
    * @param aidbox aid box a verificar se existe na rota
    * @return true, se a aid box existir na rota
    * false, caso contrário
    */
    @Override
    public boolean containsAidBox(AidBox aidbox) {
        for (int i = 0; i < this.cont_aidbox; i++) {
            if (this.aidbox[i].equals(aidbox)) {
                return true;
            }
        }
        
        return false;
    }

    /**
    * Método para substituir uma aid box da rota
    * 
    * @param from a aid box da rota a ser subtituida
    * @param to aid box a ser resposta no lugar da aidbox a ser substituida
    * @throws RouteException se a aid box não puder ser substituida na rota:
    * se alguma das aid boxes for nula (seja a aid box a substituir ou a aid box a ser reposta no lugar da aid box a ser substituida)
    * se a aid box origem não existir na rota
    * se a aid box destino já existir na rota
    * se a aid box a inserir não for compatível (não possui um container que possa ser escolhido pelo veículo) com o veículo da rota.
    */
    @Override
    public void replaceAidBox(AidBox from, AidBox to) throws RouteException {
        if(from == null || to == null) {
            throw new RouteException("Uma das aid boxes está nula!");
        }
        
        int pos_aid = this.findAidBox(from);
        
        if(pos_aid == -1) {
            throw new RouteException("A aid box que está como referência de onde é para introduzir não existe!");
        }
        
        if(this.findAidBox(to) != -1) {
            throw new RouteException("A aid box a trocar já existe na rota!");
        }
        
        if (from.getContainers().length == 0) {
            throw new RouteException("A aid box origem não possui nenhum container!");
        }
        
        Container[] containers = to.getContainers();
        boolean containerExists = false;
        
        for (Container container : containers) {
            if (container.getType() == this.vehicle.getSupplyType()) {
                containerExists = true;
            }
        }
        
        if (containerExists == false) {
            throw new RouteException("A caixa de suprimentos a inserir não tem nenhum contentor que possa ser recolhido pelo veículo da rota!");
        }
        
        this.aidbox[pos_aid] = to;
    }

    /**
    * Método para substituir uma aid box da rota
    * 
    * @param after aid box da rota que passará a ter a aid box toInsert a lhe suceder
    * @param toInsert aid box a ser inserida depois da aid box local
    * @throws RouteException se a aid box não puder ser adicionada à rota:
    * se alguma das aid boxes for nula (seja a aid box local ou a aid box a inserir após a anterior)
    * se a aid box local não existir na rota
    * se a aid box a substituir já existir na rota
    * se a aid box a inserir não for compatível (não possui um container que possa ser escolhido pelo veículo) com o veículo da rota.
    */
    @Override
    public void insertAfter(AidBox after, AidBox toInsert) throws RouteException {
        if (after == null || toInsert == null) {
            throw new RouteException("Uma das aid boxes está nula!");
        }
        
        int pos_aid = this.findAidBox(after);
        
        if (pos_aid == -1) {
            throw new RouteException("A aid box que está como referência de onde é para introduzir não existe!");
        }
        
        if (this.findAidBox(toInsert) != -1) {
            throw new RouteException("A aid box a introduzir já existe na rota!");
        }
        
        if (after.getContainers().length == 0) {
            throw new RouteException("A aid box que está a tentar introduzir não possui nenhum container!");
        }   
        
        Container[] containers = toInsert.getContainers();
        boolean containerExists = false;
        
        for (Container container : containers) {
            if (container.getType() == this.vehicle.getSupplyType()) {
                containerExists = true;
            }
        }
        
        if (containerExists == false) {
            throw new RouteException("A caixa de suprimentos a inserir não tem nenhum contentor que possa ser recolhido pelo veículo da rota!");
        }
        
        if (this.cont_aidbox == this.aidbox.length) {
            System.out.println("O array de rotas está cheio!");
        }

        for (int i = this.cont_aidbox; i > pos_aid; i--) {
            this.aidbox[i] = this.aidbox[i - 1];
        }
                
        this.aidbox[pos_aid] = toInsert;
        this.cont_aidbox++;
    } 

    /**
    * Método getter que retorna a rota
    * 
    * @return a rota (conjunto de aid boxes)
    */
    @Override
    public AidBox[] getRoute() {
        if (this.aidbox.length == this.cont_aidbox) {
            return this.aidbox;
        }
        
        AidBox[] temp = new AidBox[this.cont_aidbox];
        
        for (int i = 0; i < this.cont_aidbox; i++) {
            temp[i] = this.aidbox[i];
        }
        
        return temp;
    }
    
    /**
    * Método getter que retorna o veículo da rota
    * 
    * @return o veículo da rota
    */
    @Override
    public Vehicle getVehicle() {
        return this.vehicle;
    }

    /**
    * Método getter que retorna a distância total (em metros) entre todas as aid boxes da rota
    * 
    * @return a distância total (em metros) entre todas as aid boxes da rota
    */
    @Override
    public double getTotalDistance() {
        for (int i = 0; i < (this.cont_aidbox - 1); i++) {
            try {
                this.tot_distance = this.tot_distance + this.aidbox[i].getDistance(this.aidbox[(i + 1)]);
            } catch (AidBoxException ex) {               
                System.out.println("Exceção " + ex.getMessage());
            }
        }
         
        return this.tot_distance;
    }

    /**
    * Método getter que retorna a duração total (em segundos) entre todas as aid boxes da rota
    * 
    * @return a duração total (em segundos) entre todas as aid boxes da rota
    */
    @Override
    public double getTotalDuration() {
        for (int i = 0; i < (this.cont_aidbox - 1); i++) {
            try {
                this.tot_duration = this.tot_duration + this.aidbox[i].getDuration(this.aidbox[(i + 1)]);
            } catch (AidBoxException ex) {               
                System.out.println("Exceção " + ex.getMessage());
            }
        }
         
        return this.tot_duration;
    }

    /** Método equals de RouteImp
     * 
     * Compara uma rota com o objeto recebido
     * 
     * @param obj o objeto a ser comparado com uma rota
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
        
        final RouteImp other = (RouteImp) obj;
        
        if (Objects.equals(this.code, other.code) || (Arrays.deepEquals(this.aidbox, other.aidbox) && Objects.equals(this.vehicle, other.vehicle))) {
            return true;
        }
        
        return false;
    }

    /**
     * Método toString, que retorna a representação em string de uma rota.
     *
     * @return uma string que representa uma rota.
     */
    @Override
    public String toString() {
        return "RouteImp: {" + "INICIAL_AID_BOXES = " + INICIAL_AID_BOXES + ", code = " + code + ", cont_aidbox = " + cont_aidbox + ", aidbox = " + aidbox + ", vehicle = " + vehicle + ", tot_distance = " + tot_distance + ", tot_duration = " + tot_duration + '}';
    }
    
}
