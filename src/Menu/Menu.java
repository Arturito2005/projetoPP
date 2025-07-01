package Menu;

import Classes.InstitutionImp;
import java.util.InputMismatchException;
import java.util.Scanner;

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
 * Classe Menu, classe que contém o menu de interação do utilizador com o programa
 * @author Artur Gentil Silva Pinto
 * @version 1.0
 * @since 2024-06-09
*/
public class Menu {

    /**
     * Método main que realiza o menu do programa, mais especificamente um menu com as opções da instituição, sabendo que esse menu tem 7 opções, sendo elas:
     * 0 - Sair, permite sair do programa.
     * 1 - Menu veículos, que leva para um sub-menu com todas as opções dos veículos.
     * 2 - Adicionar aid box, que permite ao utilizador adicionar uma nova aid box à instituição.
     * 3 - Adicionar container, que permite adicionar um novo container a uma aid box da instituição.
     * 4 - Adicionar measurement, que permite adicionar uma nova medição a um container da instituição.
     * 5 - Adicionar picking map, que permite adicionar um novo picking map à instituição.
     * 6 - Informação da instituição, que exibe todos os dados da instituição, desde o seu nome até às distâncias
     * entre as suas aid boxes.
     * 7 - Carregar dados, que permite carregar dados para a instituição, sendo que essa opção vai para um sub-menu onde
     * o utilizador poderá escolher de onde quer carregar essa informação (ficheiros JSON estáticos ou API dinâmica)
     * 
     */
    public static void main(String[] args) {
        InstitutionImp institucion = new InstitutionImp("Institucion");
        Scanner scanner = new Scanner(System.in);
        OptionsMenu op = new OptionsMenu(institucion);
        int option = -1;
        boolean read_data = false;

        do {
            System.out.println("------ Menu -------");
            System.out.println("0 - Sair");
            System.out.println("1 - Menu veículos");
            System.out.println("2 - Adicionar aid box");
            System.out.println("3 - Adicionar container");
            System.out.println("4 - Adicionar measurement");
            System.out.println("5 - Adicionar picking map");
            System.out.println("6 - Informação da instituição");
            System.out.println("7 - Carregar dados");
            System.out.print("Digite uma opção --> ");
            
            try {
                option = scanner.nextInt();
            } catch (InputMismatchException ex) {
                System.out.println("Caractere inválido!");
                scanner.next(); 
            }

            switch (option) {
                case 0: {
                    break;
                } case 1: {
                    System.out.println("Menu de veículos!");
                    op.menu_vehicles();
                    break;
                } case 2: {
                    System.out.println("Adicionar aid box!");
                    op.addAidBox();
                    break;
                } case 3: {
                    if (institucion.getAidBoxes().length > 0) {
                        System.out.println("Adicionar container!");
                        op.addContainer();
                    } else {
                        System.out.println("Adicione 1º uma aid box para poder adicionar um container!");
                    }
                    break;
                } case 4: {
                    if (institucion.getNumContainers() > 0) {
                        System.out.println("Opção adicionar measurement!"); 
                        op.addMeasurement();
                    } else {
                        System.out.println("Não é possível adicionar uma medição, pois ainda não existem containers!");
                    }
                    break;
                } case 5:{
                    if (institucion.getVehicles().length > 0 && institucion.getAidBoxes().length > 0) {
                        System.out.println("Adicionar picking map!");
                        op.addPickingMap();
                    } else {
                        System.out.println("Impossível adicionar o picking map. Verifique se a instituição tem aid boxes ou veículos disponíveis!");
                    }
                    break;
                } case 6: {
                    System.out.println("Dados da instituição!");
                    op.info_inst();
                    break;
                } case 7: {
                    if (read_data == false) {
                        read_data = op.load_data(read_data);                    
                    } else {
                        System.out.println("Já foram carregados os dados 1 vez!");
                    }
                    break;
                } default: {
                    System.out.println("Opção Inválida!");
                    break;
                }
            }
        } while (option != 0);
        
        scanner.close();
    }   
}
