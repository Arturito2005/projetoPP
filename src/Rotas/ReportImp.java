package Rotas;

import com.estg.pickingManagement.Report;
import java.time.LocalDateTime;

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
 * ReportImp, classe que geras as estatísticas das rotas geradas
 * 
 * @author Artur Gentil Silva Pinto
 * @version 1.0
 * @since 2024-05-25
 */

/**
 * ReportImp, classe que geras as estatísticas das rotas geradas
 * 
 * @author Francisco Miguel Pereira Oliveira
 * @version 1.0
 * @since 2024-05-25
 */
public class ReportImp implements Report{

    /**
     * Estratégia associada ao relatório.
     */
    private StrategyImp stra; 

    /**
    * Método construtor de ReportCla
    * 
    * @param str estratégia optada
    */
    public ReportImp(StrategyImp str) {
        this.stra = str;
    }

    /**
    * Método getter do número de veículos usados
    * 
    * @return o número de veículos usados
    */
    @Override
    public int getUsedVehicles() {
        return this.stra.getUsedVehicle();
    }
    
    /**
    * Método getter do número de veículos não utilizados
    * 
    * @return o número de veículos não utilizados
    */
    @Override
    public int getNotUsedVehicles() {
        return this.stra.getNoUsedVehicles();
    }

    /**
    * Método getter do número total de containers recolhidos
    * 
    * @return o número total de containers recolhidos
    */
    @Override
    public int getPickedContainers() {
        return this.stra.getContainer();
    }
    
    /**
    * Método getter do número de veículos que não recolheram containers
    * 
    * @return o número de veículos que não recolheram containers
    */
    @Override
    public int getNonPickedContainers() {
        return this.stra.getNonContainers();
    }

    /**
    * Método getter da distância total (em Km) percorrida pelos veículos
    * 
    * @return a distância total percorrida pelos veículos
    */
    @Override
    public double getTotalDistance() {
        return this.stra.getTotDistance();
    }

    /**
    * Método getter da duração total (em segundos) gasta pelos veículos
    * 
    * @return a duração total gasta pelos veículos
    */
    @Override
    public double getTotalDuration() {
        return this.stra.getTotDuration();
    }

    /**
    * Método getter da data do relatório
    * 
    * @return a data do relatório
    */
    @Override
    public LocalDateTime getDate() {
        return LocalDateTime.now();
    }
}