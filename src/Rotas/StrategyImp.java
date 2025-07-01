package Rotas;

import Classes.InstitutionImp;
import Vehicles.VehicleImp;
import com.estg.core.AidBox;
import com.estg.core.Container;
import com.estg.core.Institution;
import com.estg.core.ItemType;
import com.estg.core.exceptions.AidBoxException;
import com.estg.pickingManagement.Route;
import com.estg.pickingManagement.RouteValidator;
import com.estg.pickingManagement.Strategy;
import com.estg.pickingManagement.Vehicle;
import com.estg.pickingManagement.exceptions.RouteException;

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
 * StrategyImp, classe que define a estratégia para gerar rotas e coletar containers
 * 
 * @author Artur Gentil Silva Pinto
 * @version 1.0
 * @since 2024-06-01
 */

/**
 * StrategyImp, classe que define a estratégia para gerar rotas e coletar containers
 * 
 * @author Francisco Miguel Pereira Oliveira
 * @version 1.0
 * @since 2024-05-25
 */
public class StrategyImp implements Strategy {
    
    /**
     * Contador dos veículos usados pela instituição.
     */
    private int cont_vei_used;

    /**
     * Número total de veículos que a instituição tem.
     */
    private int tot_vei; 
    
    /**
     * Número total de containers que a instituição tem.
     */
    private int tot_container;
    
    /**
     * Número total de containers recolhidos pelos veículos da instituição.
     */
    private int container_picked;
    
    /**
     * Distância total percorrida pelos veículos usados pela instituição.
     */
    private double tot_distance; 

    /**
     * Duração total gasta no percurso pelos veículos usados pela instituição.
     */
    private double tot_duration; 
    
    /**
    * Método getter do número de veículos usados
    * 
    * @return o número de veículos usados
    */
    public int getUsedVehicle() {
        return this.cont_vei_used;
    }
    
    /**
    * Método getter do número de veículos que não recolheram containers
    * 
    * @return o número de veículos que não recolheram containers
    */
    public int getNonContainers() {
        return this.tot_container - this.container_picked;
    }
    
    /**
    * Método getter do número total de containers recolhidos
    * 
    * @return o número total de containers recolhidos
    */
    public int getContainer() {
        return this.container_picked;
    }
    
    /**
    * Método getter do número de veículos não usados
    * 
    * @return o número de veículos não usados
    */
    public int getNoUsedVehicles() {        
        return this.tot_vei - this.cont_vei_used;
    }
    
    /**
    * Método getter da distância total percorrida pelos veículos usados
    * 
    * @return a distância total percorrida pelos veículos usados
    */
    public double getTotDistance() {
        return this.tot_distance;
    }
    
    /**
    * Método getter da duração total gasta no percurso pelos veículos usados
    * 
    * @return a distância total percorrida pelos veículos usados
    */
    public double getTotDuration() {
        return this.tot_duration;
    }
    
    /**
     * Método que organiza todas as aid boxes da rota, criando o menor caminho possível
     * 
     * @param route rota, cujas aid boxes vão ser ordenadas
     * @param instn instituição
     */
    public void organizate_route(Route route, Institution instn) {
        try {                                      
            AidBox[] aid_routes = route.getRoute();

            for (int i1 = 0; i1 < aid_routes.length; i1++) {
                try {
                    if (instn.getDistance(aid_routes[i1]) < instn.getDistance(aid_routes[0])) {
                        AidBox temp = aid_routes[0]; 
                        route.replaceAidBox(aid_routes[0], aid_routes[i1]);
                        route.replaceAidBox(aid_routes[i1], temp);

                        aid_routes[0] = aid_routes[i1];
                        aid_routes[i1] = temp;
                    }
                } catch (AidBoxException ex) {
                    System.out.println("Exceção: " + ex.getMessage());
                }
            }

            for (int i1 = 0; i1 < aid_routes.length; i1++) {
                for (int y1 = i1 + 1; y1 < (aid_routes.length - 1); y1++) {
                    try {
                        if (aid_routes[i1].getDistance(aid_routes[y1]) > aid_routes[i1].getDistance(aid_routes[y1 + 1])) {
                            AidBox temp = aid_routes[y1]; 
                            route.replaceAidBox(aid_routes[y1], aid_routes[y1 + 1]);
                            route.replaceAidBox(aid_routes[y1 + 1], temp);

                            aid_routes[y1] = aid_routes[y1 + 1];
                            aid_routes[y1 + 1] = temp;
                        }
                    } catch (AidBoxException ex) {
                        System.out.println("Exceção: " + ex.getMessage());
                    }
                }
            }
        } catch (RouteException ex) {
            System.out.println("Exceção: " + ex.getMessage());
        }
    }
    
    /**
     * Metodo para verificar se não existem container repetidos
     * @param route, array das rotas que já existem
     * @param code, codigo do contaiener a verificar
     * 
     * @return true, se o container já existtir em alguma rota
     */
    public boolean container_repet(Route[] route, String code) {                 
        for (int i = 0; i < route.length; i++) {  
            if (route[i] != null) {
                for (AidBox aid: route[i].getRoute()) {                  
                    for (Container container: aid.getContainers()) {
                        if (container.getCode().equals(code)) {
                            return true;
                        }
                    }
                }
            }
        }
        
        return false;
    }
    
    /**
    * Método que reoorganiza uma rota de acordo com a estratégia implementada (coloca em primeiro lugar as aidboxes que contêm, pelo menos, um container que armazena alimentos perecíveis)
    * 
    * @param routes conjunto de rotas
    */
     private void perishable_first(Route[] routes) {
        int cont_temp = 0;

        Route[] tempRoute = new Route[routes.length];

        for (Route route : routes) {
            if (route.getVehicle().getSupplyType().equals(ItemType.PERISHABLE_FOOD)) {
                tempRoute[cont_temp++] = route;
            }
        }

        for (Route route : routes) {
            if (!(route.getVehicle().getSupplyType().equals(ItemType.PERISHABLE_FOOD))) {
                tempRoute[cont_temp++] = route;
            }
        }

        for (int i = 0; i < routes.length; i++) {
            routes[i] = tempRoute[i];
        }
    }
    
    /**
    * Método que gera um conjunto de rotas para uma instituição, de acordo com a estratégia implementada no validador de rotas
    * 
    * @param instn instituição
    * @param rv validador de rotas
    * @return o conjunto de rotas geradas para a instituição
    */
    @Override
    public Route[] generate(Institution instn, RouteValidator rv) {       
        if (instn == null || rv == null) {
            throw new NullPointerException("Um dos dois parâmetros está null!");
        }
        
        this.cont_vei_used = 0;
        this.tot_container = 0;
        this.tot_distance = 0;
        this.container_picked = 0;
        this.tot_duration = 0;
        this.tot_vei = 0;
        
        Route[] route = new Route[instn.getVehicles().length];
        int cont_route = 0;
        this.tot_vei = instn.getVehicles().length;
        
        if (instn instanceof InstitutionImp) {
            this.tot_container = ((InstitutionImp) instn).getNumContainers();
        }

        for (Vehicle vei: instn.getVehicles()) {
            int cont_route_temp = 0;
            RouteImp temp_rout = new RouteImp(vei);
                
            for (AidBox aid: instn.getAidBoxes()) {
                for (Container container: aid.getContainers()) {
                    if (this.container_repet(route, container.getCode()) == false) {
                        if (rv.validate(temp_rout, aid)) {
                            try {                        
                                temp_rout.addAidBox(aid);
                            } catch (RouteException ex) {
                                System.out.println("Exceção: " + ex.getMessage());
                            }

                            organizate_route(temp_rout, instn);
                            cont_route_temp++;
                            this.container_picked++;
                        }
                    }
                } 
            }
            
            if (cont_route_temp > 0) {
                this.cont_vei_used++;
                route[cont_route++] = temp_rout;
                ((VehicleImp) vei).resetAtualCapacity();
            }
        }
        
        Route[] return_route = new Route[cont_route];
       
        for (int i = 0; i < cont_route; i++) {
           return_route[i] = route[i];
        }
        
        this.perishable_first(return_route);
        
        for (int i = 0; i < return_route.length; i++) {
            this.tot_distance = this.tot_distance + return_route[i].getTotalDistance();
            this.tot_duration = this.tot_duration + return_route[i].getTotalDuration();
        }
        
        return return_route;
    }
}