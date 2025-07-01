package Rotas;

import com.estg.core.Institution;
import com.estg.pickingManagement.Report;
import com.estg.pickingManagement.Route;
import com.estg.pickingManagement.RouteGenerator;
import com.estg.pickingManagement.RouteValidator;
import com.estg.pickingManagement.Strategy;

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
 * RouteGeneratorImp, classe que gera as rotas para uma dada instituição
 * 
 * @author Francisco Miguel Pereira Oliveira
 * @version 1.0
 * @since 2024-05-25
 */
public class RouteGeneratorImp implements RouteGenerator {

    /**
    * Método que verifica se uma caixa de suprimentos já foi adicionada à instituição
    * 
    * @param instn instituição
    * @param strtg estratégia optada
    * @param rv validador de rota
    * @param report relatório com as estatísticas das rotas geradas
    * @return conjunto de rotas geradas para a instituição
    */
    @Override
    public Route[] generateRoutes(Institution instn, Strategy strtg, RouteValidator rv, Report report) {
        if (instn == null || strtg == null || rv == null || report == null) {
            throw new NullPointerException("Null Pointer Exception!");
        }
         
        Route[] route_list = strtg.generate(instn, rv);
        
        System.out.println("Nº de veículos usados: " + report.getUsedVehicles());
        System.out.println("Nº de veículos não usados: " + report.getNotUsedVehicles());
        System.out.println("Data do relatório: " + report.getDate());
        System.out.println("Nº de containers não usados: " + report.getNonPickedContainers());        
        System.out.println("Nº de containers usados: " + report.getPickedContainers());
        System.out.println("Distância total percorrida pelos veículos: " + report.getTotalDistance());
        System.out.println("Tempo total gasto nos percursos dos veículos: " + report.getTotalDuration());
        
        return route_list;
    }
 
}
