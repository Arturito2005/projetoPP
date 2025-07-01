package Classes;

import Vehicles.VehicleImp;
import com.estg.core.AidBox;
import com.estg.core.Container;
import com.estg.core.ItemType;
import com.estg.core.exceptions.AidBoxException;
import com.estg.core.exceptions.ContainerException;
import com.estg.core.exceptions.MeasurementException;
import com.estg.core.exceptions.PickingMapException;
import com.estg.core.exceptions.VehicleException;
import com.estg.pickingManagement.PickingMap;
import com.estg.pickingManagement.Vehicle;
import com.estg.core.Institution;
import com.estg.core.Measurement;
import java.time.LocalDateTime;

/*
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
 * InstitutionImp, classe que representa uma instituição
 * 
 * @author Francisco Miguel Pereira Oliveira
 * @version 1.0
 * @since 2024-05-25
 */
public class InstitutionImp implements Institution {
    /**
     * Tamanho inicial de um array.
     */
    private static final int INICIAL_SIZE = 5; 
    
    /**
     * Nome da instituição.
     */
    private String name; 

    /**
     * Número total de mapas da instituição.
     */
    private int numPickingMaps;
    
    /**
     * Coleção total de rotas da instituição.
     */
    private PickingMap[] pickingMaps; 
    
    /**
     * Número total de caixas de suprimentos da instituição.
     */
    private int numAidBoxes; 
    
    /**
     * Coleção de caixas de suprimentos da instituição.
     */
    private AidBoxImp[] aidBoxes; 
    
    /**
     * Número total de veículos ativos da instituição.
     */
    private int numVehicles; 
    
    /**
     * Coleção de veículos ativos da instituição.
     */
    private Vehicle[] vehicles;
    
    /**
     * Número total de veículos inativos da instituição.
     */
    private int numDisabledVehicles;
    
    /**
     * Coleção de veículos inativos da instituição.
     */
    private Vehicle[] disabledVehicles; 
    
    /**
     * Número total de distâncias da instituição às respetivas aid boxes.
     */
    private int numDistances; 
    
    /**
     * Coleção de distâncias da instituição às respetivas aid boxes.
     */
    private Distance[] distances; 
    
    /**
    * Método construtor de InstitutionImp
    * 
    * @param name nome da instituição
    */
    public InstitutionImp(String name) {
        this.name = name;
        this.vehicles = new Vehicle[INICIAL_SIZE];
        this.disabledVehicles = new Vehicle[INICIAL_SIZE];
        this.aidBoxes = new AidBoxImp[INICIAL_SIZE];
        this.pickingMaps = new PickingMap[INICIAL_SIZE];
        this.numVehicles = 0;
        this.numPickingMaps = 0;
        this.numAidBoxes = 0;
        this.numDistances = 0;
        this.distances = new Distance[INICIAL_SIZE];
    }
    
    /**
    * Método getter do nome da instituição
    * 
    * @return o nome da instituição
    */
    @Override
    public String getName() {
        return this.name;
    }
    
    /**
     * Metodo para retornar o número total de containers que a instituição possui
     * 
     * @return o número total de containers que a instituição possui
     */
    public int getNumContainers() {
        int num_cont = 0;

        for (AidBox aid: this.getAidBoxes()) {
           num_cont = num_cont + aid.getContainers().length;
        }

        return num_cont;
    }
     
    /**
    * Método que pesquisa por uma aid box
    * 
    * @param code código da aid box
    * @return i, que corresponde à posição da aid box, se a aidbox for encontrada
    * -1, se a aid box não for encontrada
    */
    private int findAidBox(String code) {
        for (int i = 0; i < this.numAidBoxes; i++) {
            if (this.aidBoxes[i].getCode().equals(code)) {
                return i;
            }
        }
        
        return -1;
    }
    
    /**
    * Método que duplica o espaço do array das caixas de suprimento sempre que necessário.
    */
    private void raiseAidboxes() {
        AidBoxImp[] temp = new AidBoxImp[this.aidBoxes.length * 2];
        
        for (int i = 0; i < this.numAidBoxes; i++) {
            temp[i] = this.aidBoxes[i];
        }
        
        this.aidBoxes = temp;
    }
    
    /**
    * Método que duplica o espaço do array das distâncias sempre que necessário.
    */
    private void raiseDistances() {
        Distance[] temp_dis = new Distance[this.numDistances * 2]; 
        
        for(int i = 0; i < this.numDistances; i++) {
            temp_dis[i] = this.distances[i];
        }
        
        this.distances = temp_dis;
    }
    
    /**
     * Método que adiciona uma distância da instituição a uma aid box
     * 
     * @param distance, distância a ser inserida no array de Distances
     * @return true, caso a distância possa ser adicionada
     */
    public boolean addDistance(Distance distance) {
        if(this.numDistances == this.distances.length) {
            this.raiseDistances();
        }
        
        this.distances[this.numDistances++] = distance;
        return true;
    }
    
    /**
     * Método que adiciona uma distância entre duas aid boxes da instituição
     * 
     * @param aidBox_Inst, aid box que receberá a nova distance
     * @param distance, distância entre as duas aid boxes
     * @return true, se conseguir adicionar a distância,
     * false se a aid box que recebe a distância não pertencer à instituição
     */
    public boolean addDistanceAid(String aidBox_Inst, Distance distance) {
        int pos = this.findAidBox(aidBox_Inst);
        
        if(pos == -1) {
            System.out.println("A aid box inserida não existe na instituição!");
            return false;
        }
        
        this.aidBoxes[pos].addDistance(distance);
        return true;
    }
    
    /**
    * Método que adiciona uma caixa de suprimentos à coleção de caixas de suprimentos da instituição
    * 
    * @param aidbox caixa de suprimentos
    * @return true, se a caixa de suprimentos foi adicionada com sucesso
    * false, se não foi possível adicionar a caixa de suprimentos
    * @throws AidBoxException, se a caixa de suprimentos for nula,
    * ou se contiver pelo menos dois contentores duplicados do mesmo tipo
    */
    @Override
    public boolean addAidBox(AidBox aidbox) throws AidBoxException {
        if (aidbox == null) {
            throw new AidBoxException("A caixa de suprimentos é nula!");
        }
        
        if (this.numAidBoxes == this.aidBoxes.length) {
            this.raiseAidboxes();
        }
        
        if (findAidBox(aidbox.getCode()) != -1) {
            throw new AidBoxException("A caixa de suprimentos já faz parte da instituição!");
        }
        
        this.aidBoxes[this.numAidBoxes++] = (AidBoxImp) aidbox;
        return true;     
    }
    
    /**
    * Método que verifica se o container recebido já faz parte de alguma aid box da instituição
    * 
    * @param cont_eq contentor
    * @return i, a posição do container, caso este seja encontrado
    * @return -1, caso contrário
    */
    private int findAidContainer(Container cont_eq) {
        for (int i = 0; i < this.numAidBoxes; i++) {
            Container[] container = this.aidBoxes[i].getContainers();
            for (Container cont: container) {
                if (cont.equals(cont_eq)) {
                    return i;
                }
            }
        }
        
        return -1;
    }
    
    /**
    * Método que adiciona uma medição a um contentor da instituição
    * 
    * @param msrmnt medição de um contentor
    * @param cntnr contentor
    * @return true, se a medição foi adicionada com sucesso ao contentor
    * false, se o contentor tiver uma medição com uma data igual à data do contentor recebido
    * @throws ContainerException se o contentor não existir
    * @throws MeasurementException se o valor da medição for negativo ou ultrapassar o valor da capacidade máxima do contentor
    */
    @Override
    public boolean addMeasurement(Measurement msrmnt, Container cntnr) throws ContainerException, MeasurementException {
        int pos_aid = this.findAidContainer(cntnr);
        
        if (pos_aid == -1) {
            throw new ContainerException("O contentor não existe!");
        }
        
        if (msrmnt.getValue() < 0) {
            throw new MeasurementException("O valor da medição é negativo!");
        }

        if (msrmnt.getValue() > cntnr.getCapacity()) {
            throw new MeasurementException("O valor da medição ultrapassa a capacidade máxima do contentor!");
        }

        for (Measurement measurement : cntnr.getMeasurements()) {
            if (measurement.getDate().equals(msrmnt.getDate())) {
                return false;
            }
        }

        for (Container container : this.aidBoxes[pos_aid].getContainers()) {
            if (container.equals(cntnr)) {
                container.addMeasurement(msrmnt);
                return true;
            }
        }
        
        return false;
    }
    
    /**
    * Método getter do conjunto de caixas de suprimentos da instituição
    * 
    * @return o conjunto de caixas de suprimentos da instituição
    */
    @Override
    public AidBox[] getAidBoxes() {
        if (this.numAidBoxes == this.aidBoxes.length) {
            return this.aidBoxes;
        }

        AidBox[] temp = new AidBox[this.numAidBoxes];

        for (int i = 0; i < this.numAidBoxes; i++) {
            temp[i] = this.aidBoxes[i];
        }

        return temp;
    }
    
    /**
    * Método que retorna um contentor através da caixa de suprimentos correspondente, e ainda através do tipo de item que armazena
    * 
    * @param aidbox caixa de suprimentos
    * @param it tipo de item armazenado pelo contentor
    * @return o contentor correspondente
    * @throws ContainerException se a caixa de suprimentos não existir,
    * ou se não existir um contentor que armazene o item prescrito
    */
    @Override
    public Container getContainer(AidBox aidbox, ItemType it) throws ContainerException {
        boolean aidboxIsFound = false;
        boolean itIsFound = false;
        Container container = null;
        
        for (int i = 0; i < this.numAidBoxes; i++) {
            if (this.aidBoxes[i].equals(aidbox)) {
                aidboxIsFound = true;
                Container[] tmp = aidbox.getContainers();
                for (Container tmp1 : tmp) {
                    if (tmp1.getType().equals(it)) {
                        itIsFound = true;
                        container = tmp1;
                        break;
                    }
                }
                break;
            }
        }
        
        if (aidboxIsFound == false | itIsFound == false) {
            throw new ContainerException("A caixa de suprimentos não existe,"
                    + "ou não existe um contentor que armazene o item prescrito!");
        }
        
        return container;
    }
    
    /**
    * Método getter do conjunto de veículos disponíveis (ativos) da instituição
    * 
    * @return o conjunto de veículos disponíveis (ativos) da instituição
    */
    @Override
    public Vehicle[] getVehicles() {
        if (this.numVehicles == this.vehicles.length) {
            return this.vehicles;
        }

        Vehicle[] temp = new Vehicle[this.numVehicles];

        for (int i = 0; i < this.numVehicles; i++) {
            temp[i] = this.vehicles[i];
        }

        return temp;
    }
    
    /**
    * Método getter do conjunto de veículos indisponíveis (inativos) da instituição
    * 
    * @return o conjunto de veículos indisponíveis (inativos) da instituição
    */
    public Vehicle[] getDisabledVehicles() {
        if (this.numDisabledVehicles == this.vehicles.length) {
            return this.disabledVehicles;
        }

        Vehicle[] temp = new Vehicle[this.numDisabledVehicles];

        for (int i = 0; i < this.numDisabledVehicles; i++) {
            temp[i] = this.disabledVehicles[i];
        }

        return temp;
    }
    /**
    * Método que verifica se um veículo já foi adicionado à instituição
    * 
    * @param vehicle veículo
    * @return true, se a instituição tiver um veículo igual ao que foi passado por parâmetro
    * false, se o veículo não foi encontrado
    */
    private boolean findVehicle(Vehicle vehicle) {
        for (int i = 0; i < this.numVehicles; i++) {
            if (this.vehicles[i].equals(vehicle)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
    * Método que duplica o espaço do array vehicles sempre que necessário.
    */
    private void raiseVehicles() {
        Vehicle[] temp = new Vehicle[this.vehicles.length * 2];
        
        for (int i = 0; i < this.numVehicles; i++) {
            temp[i] = this.vehicles[i];
        }
        
        this.vehicles = temp;
    }
    
    /**
    * Método que adiciona um veículo à coleção de veículos da instituição
    * 
    * @param vhcl veículo
    * @return true, se o veículo foi adicionado com sucesso
    * false, se não foi possível adicionar o veículo
    * @throws VehicleException se o veículo for nulo
    */
    @Override
    public boolean addVehicle(Vehicle vhcl) throws VehicleException {
        if (vhcl == null) {
            throw new VehicleException("O veículo é nulo!");
        }
        
        if (this.numVehicles == this.vehicles.length) {
            this.raiseVehicles();
        }
        
        if (findVehicle(vhcl)) {
            return false;
        }
        
        this.vehicles[this.numVehicles++] = vhcl;
        return true;          
    }
    
    /**
    * Método que duplica o espaço do array dos veículos desabilitados sempre que necessário.
    */
    private void raiseDisabledVehicles() {
        Vehicle[] tmp = new Vehicle[this.disabledVehicles.length * 2];
        
        System.arraycopy(this.disabledVehicles, 0, tmp, 0, this.numDisabledVehicles);
        
        this.disabledVehicles = tmp;
    }
    
    /**
    * Método que desativa um veículo da coleção de veículos da instituição
    * 
    * @param vhcl veículo
    * @throws VehicleException se o veículo não existir, ou se o veículo já se encontrar desativado
    */
    @Override
     public void disableVehicle(Vehicle vhcl) throws VehicleException {
        boolean haveVhcl = false;
         
        if (vhcl == null) {
            throw new VehicleException("O veículo é nulo!");
        }
        
        if (this.numDisabledVehicles == this.disabledVehicles.length) {
            this.raiseDisabledVehicles();
        }
        
        for (int i = 0; i < this.numDisabledVehicles; i++) {
            if (this.disabledVehicles[i].equals(vhcl)) {
                throw new VehicleException("O veículo " + vhcl + " já se encontra inativo!");
            }
        }
        
        for (int i = 0; i < this.numVehicles; i++) {
            if (this.vehicles[i].equals(vhcl)) {
                haveVhcl = true;
            }
        }
        
        if (haveVhcl == false) {
            throw new VehicleException("O veículo não existe!");
        }
        
        this.disabledVehicles[this.numDisabledVehicles++] = vhcl;
        
        for (int i = 0; i < this.numVehicles; i++) {
            if (this.vehicles[i].equals(vhcl)) {
                for (int j = i; j < this.numVehicles - 1; j++) {
                    this.vehicles[j] = this.vehicles [j + 1];
                }
                this.numVehicles--;
            }
        }
        
        if (vhcl instanceof VehicleImp) {
            System.out.println("O veículo com matrícula " + ((VehicleImp) vhcl).getMatricula() + " foi desabilitado com sucesso!");
        }
    }
    
    /**
    * Método que ativa um veículo da coleção de veículos da instituição
    * 
    * @param vhcl veículo
    * @throws VehicleException, se o veículo não existir,
    * ou se o veículo já se encontrar ativo
    */
    @Override
    public void enableVehicle(Vehicle vhcl) throws VehicleException {
        boolean haveVhcl = false;
        
        if (vhcl == null) {
            throw new VehicleException("O veículo é nulo!");
        }
        
        if (this.numVehicles == this.vehicles.length) {
            this.raiseVehicles();
        }
        
        for (int i = 0; i < this.numVehicles; i++) {
            if (this.vehicles[i] == vhcl) {
                if (vhcl instanceof VehicleImp) {
                    throw new VehicleException("O veículo com matrícula " + ((VehicleImp) vhcl).getMatricula() + " já se encontra ativo!");
                }
            }
        }
        
        for (int i = 0; i < this.numDisabledVehicles; i++) {
            if (this.disabledVehicles[i].equals(vhcl)) {
                haveVhcl = true;
            }
        }
        
        if (haveVhcl == false) {
            throw new VehicleException("O veículo não existe!");
        }
        
        this.vehicles[this.numVehicles++] = vhcl;
        
        for (int i = 0; i < this.numDisabledVehicles; i++) {
            if (this.disabledVehicles[i].equals(vhcl)) {
                for (int j = i; j < this.numDisabledVehicles - 1; j++) {
                    this.disabledVehicles[j] = this.disabledVehicles [j + 1];
                }
                this.numDisabledVehicles--;
            }
        }
        
        if (vhcl instanceof VehicleImp) {
            System.out.println("O veículo com matrícula " + ((VehicleImp) vhcl).getMatricula() + " foi habilitado com sucesso!");
        }
    }
    
    /**
    * Método getter do conjunto total de rotas da instituição
    * 
    * @return o conjunto total de rotas da instituição
    */
    @Override
    public PickingMap[] getPickingMaps() {
        if (this.numPickingMaps == this.pickingMaps.length) {
            return this.pickingMaps;
        } 
        
        PickingMap[] temp = new PickingMap[this.numPickingMaps];
            
        for (int i = 0; i < this.numPickingMaps; i++) {
            temp[i] = this.pickingMaps[i];
        }
        
        return temp;
    }
    
    /**
    * Método que retorna o conjunto total de mapas que a instituição tem
    * 
    * @param ldt data inicial do intervalo
    * @param ldt1 data final do intervalo
    * @return o conjunto total de mapas que a instituição tem, dentro do intervalo de datas definido
    */
    @Override
    public PickingMap[] getPickingMaps(LocalDateTime ldt, LocalDateTime ldt1) {
        PickingMap[] tmp = new PickingMap[this.numPickingMaps];
        int count = 0;
        
        for (PickingMap pickingMap : this.pickingMaps) {
            if (pickingMap.getDate().isAfter(ldt) && pickingMap.getDate().isBefore(ldt1)) {
                tmp[count++] = pickingMap;
            }
        }
        
        return tmp;       
    }
    
    /**
    * Método getter do conjunto atual de rotas da instituição
    * 
    * @return o conjunto atual de rotas da instituição
    * @throws PickingMapException se não existirem mapas na instituição
    */
    @Override
    public PickingMap getCurrentPickingMap() throws PickingMapException {
        return this.pickingMaps[this.numPickingMaps];
    }
    
    /**
    * Método que verifica se um conjunto de rotas já existe na instituição
    * 
    * @param pickingMap conjunto de rotas
    * @return true, se a instituição contém o conjunto de rotas recebido
    * false, se o conjunto de rotas não foi encontrado
    */
    private boolean findPickingMap(PickingMap pickingMap) {
        for (int i = 0; i < this.numPickingMaps; i++) {
            if (((PickingMapImp) pickingMap).equals(this.pickingMaps[i])) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
    * Método que duplica o espaço do array dos mapas da instituição sempre que necessário.
    */
    private void raisePickingMaps() {
        int capacity = this.pickingMaps.length * 2;
        PickingMap[] temp = new PickingMap[capacity];
        
         for (int i = 0; i < this.numPickingMaps; i++) {
            temp[i] = this.pickingMaps[i];
        }
        
        this.pickingMaps = temp;
    }
    
    /**
    * Método que adiciona um conjunto de rotas à instituição
    * 
    * @param pm conjunto de rotas
    * @return true, se o conjunto de rotas foi adicionado com sucesso
    * false, se não foi possível adicionar o conjunto de rotas
    * @throws PickingMapException se o mapa (conjunto de rotas) for nulo
    */
    @Override
    public boolean addPickingMap(PickingMap pm) throws PickingMapException {
        if (pm == null) {
            throw new PickingMapException("O mapa é nulo!");
        }
        
        if (this.numPickingMaps == this.pickingMaps.length) {
            this.raisePickingMaps();
        }
        
        if (findPickingMap(pm)) {
            return false;
        }
        
        this.pickingMaps[this.numPickingMaps++] = pm;
        return true; 
    }
    
    /**
     * Método para pesquisar se a aid box destino existe na instituição.
     */
    private int posAidDistance(String code) {
        for (int i = 0; i < this.numDistances; i++) {
            if (code.equals(this.distances[i].getAidDestino())) {
                return i;
            }
        }
        
        return -1;
    }

    /**
    * Método que retorna a distância entre a instituição e uma caixa de suprimentos
    * 
    * @param aidbox caixa de suprimentos
    * @return o valor da distância entre a instituição e a caixa de suprimentos
    * @throws AidBoxException, se a caixa de suprimentos não existir
    */
    @Override
    public double getDistance(AidBox aidbox) throws AidBoxException {
        boolean aidboxExists = false;
        
        if (aidbox == null) {
            throw new AidBoxException("A aid box está nula!");
        }
        
        for (int i = 0; i < this.numAidBoxes; i++) {
            if (this.aidBoxes[i].equals(aidbox)) {
                aidboxExists = true;
            }
        }
        
        if (aidboxExists == false) {
            throw new AidBoxException("A aid box não existe!");
        }
        
        int pos = this.posAidDistance(aidbox.getCode()); 
               
        return this.distances[pos].getDistance();
    }

    /** Método equals de InstitutionImp
     * 
     * Compara um nome (nome da instituição) com o objeto recebido
     * 
     * @param obj o objeto a ser comparado com um nome (nome da instituição)
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
        
        final InstitutionImp other = (InstitutionImp) obj;
        
        return this.name.equals(other.name);
    }

    /**
    * Método toString de InstitutionImp
    * 
    * @return os valores de name, vehicles, currentPickingMap, pickingMaps e aidBoxes
    */
    @Override
    public String toString() {
        return "InstitutionImp: {" + "name = " + this.name + ", numPickingMaps = " + this.numPickingMaps + ", pickingMaps = " + this.pickingMaps + ", numAidBoxes = " + this.numAidBoxes + ", aidBoxes = " + this.aidBoxes + ", numVehicles = " + this.numVehicles + ", vehicles = " + this.vehicles + ", numDisabledVehicles = " + this.numDisabledVehicles + ", disabledVehicles = " + this.disabledVehicles + ", numDistances=" + this.numDistances + ", distances=" + this.distances + '}';
    }
    
}