package Rotas;

import Vehicles.RefrigeratedVehicle;
import Vehicles.VehicleImp;
import com.estg.core.AidBox;
import com.estg.core.Container;
import com.estg.core.ItemType;
import com.estg.core.Measurement;
import com.estg.core.exceptions.AidBoxException;
import com.estg.pickingManagement.Route;
import com.estg.pickingManagement.RouteValidator;

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
 * RouteValidatorImp, classe que valida uma rota
 * 
 * @author Artur Gentil Silva Pinto
 * @version 1.0
 * @since 2024-05-25
 */

/**
 * RouteValidatorImp, classe que valida uma rota
 * 
 * @author Francisco Miguel Pereira Oliveira
 * @version 1.0
 * @since 2024-05-25
 */
public class RouteValidatorImp implements RouteValidator {
    
    /**
     * Metodo que simula a inserção ad aidbox a comparar na route, para simular a distancia mais curta possível, caso ela fosse inserida na rota
     * 
     * @param route, rota que receberá temporariamente a aid box
     * @param aid, aid box a inserir temporariamente
     * @return a distancia total da rota, caso a aid box fosse inserida
     */
    public double tot_distance(Route route, AidBox aid) {
        double tot_distance = 0;
        AidBox[] aid_routes = new AidBox[route.getRoute().length + 1];

        AidBox[] temp_aid = route.getRoute();
        
        for(int i = 0; i < temp_aid.length; i++) {
            aid_routes[i] = temp_aid[i];
        }
        
        aid_routes[aid_routes.length - 1] = aid;
        
        for (int i = 0; i < aid_routes.length; i++) {
            for (int y1 = i + 1; y1 < (aid_routes.length - 1); y1++) {
                try {
                    if(aid_routes[i].getDistance(aid_routes[y1]) > aid_routes[i].getDistance(aid_routes[y1 + 1])) {
                        AidBox temp = aid_routes[y1]; 
                        aid_routes[y1] = aid_routes[y1 + 1];
                        aid_routes[y1 + 1] = temp;
                    }
                } catch (AidBoxException ex) {
                    System.out.println("Exceção: " + ex.getMessage());
                }
            }
        }
        
        for(int  i= 0; i < (aid_routes.length - 1); i ++) {
            try {
                tot_distance = aid_routes[i].getDistance(aid_routes[i + 1]);
            } catch (AidBoxException ex) {
                System.out.println("Exceção: " + ex.getMessage());
            }
        }
       return tot_distance;
    }
     
    /**
    * Método que valida uma determinada rota
    * 
    * @param route rota
    * @param aidox caixa de suprimentos
    * @return true, se a rota é válida, isto é, se a caixa de suprimentos puder ser adicionada à rota,
    * se o veículo da rota é compatível com a aidbox
    * (ou seja, se a aidbox contém, pelo menos, um container do mesmo tipo), se o veículo da rota não estiver cheio e
    * se a capacidade do container estiver, pelo menos, 75% preenchida (estratégia)
    * false, caso alguma das verificações acima falhe
    */
    @Override
    public boolean validate(Route route, AidBox aidox) {
        if (route == null || aidox ==null) {
            throw new NullPointerException("A rota ou a aid box recebida está a null!");
        }
        
        if (route.containsAidBox(aidox)) {
            System.out.println("A rota já possui esta aid box!");
            return false;
        }
        
        Container[] container_list = aidox.getContainers();
        
        for (Container container: container_list) {
            if (route.getVehicle().getSupplyType() == container.getType()) {
                Measurement[] mesua = container.getMeasurements();  
                
                if (mesua.length == 0) {
                    System.out.println("O container não contém medições!");
                    return false;
                }
                
                if (route.getVehicle() instanceof VehicleImp) {
                    if ((container.getCapacity() - container.getCapacity() * 0.25) <= (mesua[(mesua.length - 1)].getValue()) 
                        | !(container.getType().equals(ItemType.PERISHABLE_FOOD))) {
                        if (route.getVehicle().getMaxCapacity() >= ((VehicleImp) route.getVehicle()).getAtualCapacity() + mesua[(mesua.length - 1)].getValue()) {   
                             if (route.getVehicle() instanceof RefrigeratedVehicle) {
                                if (((RefrigeratedVehicle) route.getVehicle()).getMaxKm() <= this.tot_distance(route, aidox)) {
                                    return false;
                                }
                            }                     

                            ((VehicleImp) route.getVehicle()).updateCapacity(mesua[mesua.length - 1].getValue());
                            return true;      
                        }
                    }                    
                }
                return false;
            }
        }
        return false; 
    }
}
