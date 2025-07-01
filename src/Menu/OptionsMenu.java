package Menu;

import Classes.AidBoxImp;
import Classes.ContainerImp;
import Classes.GeographicCoordinatesImp;
import Classes.InstitutionImp;
import Classes.MeasurementImp;
import Classes.PickingMapImp;
import Vehicles.RefrigeratedVehicle;
import Rotas.RouteValidatorImp;
import Vehicles.VehicleImp;
import JSON.HTTPProviderImp;
import JSON.ImporterImp;
import com.estg.core.AidBox;
import com.estg.core.Container;
import com.estg.core.ItemType;
import com.estg.core.exceptions.AidBoxException;
import com.estg.core.exceptions.ContainerException;
import com.estg.core.exceptions.InstitutionException;
import com.estg.core.exceptions.MeasurementException;
import com.estg.core.exceptions.PickingMapException;
import com.estg.core.exceptions.VehicleException;
import com.estg.pickingManagement.Route;
import com.estg.pickingManagement.Vehicle;
import Rotas.ReportImp;
import Rotas.RouteGeneratorImp;
import Rotas.StrategyImp;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
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
 * Classe OptionsMenu, classe que contém todas as opções do menu do programa
 * @author Artur Gentil Silva Pinto
 * @version 1.0
 * @since 2024-06-09
*/
public class OptionsMenu {
    private InstitutionImp institucion; // Instituição que receberá os dados
    
    private BufferedReader reader; // Variável para ler a informação dada pelo utilizador
    
    private static boolean error; // Booleano que informa sobre a existência de um erro
  
    /**
    * Método construtor da classe OptionMenu
    * 
    * @param institucion instituição a receber a alteração
    */ 
    public OptionsMenu(InstitutionImp institucion) {
        this.institucion = institucion;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.error = false;
    }
    
    /**
    * Método que mostra as opções para os tipos de veículos e containers.
    */
    private void textType() {
        System.out.println("1 - Perishable Food");
        System.out.println("2 - Non Perishable Food");
        System.out.println("3 - Clothing");
        System.out.println("4 - Medicine");
    }
     
    /**
     * Método que retorna um ItemType, de acordo com a posição recebida
     * 
     * @param pos posição selecionada para o tipo,
     * os valores dessa posição são os mesmos do método textType
     */
    private ItemType type(int pos) {
        switch(pos) {
            case 1: {
                return ItemType.PERISHABLE_FOOD;
            }
            case 2: {
                return ItemType.NON_PERISHABLE_FOOD;
            }
            case 3: {
                return ItemType.CLOTHING;
            }
            case 4: {
                return ItemType.MEDICINE;
            }
            default: {
                return null;
            }            
        }
    }
    
    /**
     * Método que permite criar um veículo novo para a instituição.
     */
    private void create_vehicle() {
        String matricula = "";
        double max_cap = 0;
        int pos = 0;
        ItemType type_vehicle = null;
        
        try {          
            do {
                System.out.print("Matrícula: ");
                matricula = reader.readLine();
            } while (matricula.equals(""));

            do {
                this.error = false;
                System.out.print("Insira a capacidade máxima do veículo: ");
                try {
                    max_cap = Integer.parseInt(reader.readLine());
                } catch (NumberFormatException nfe) {
                    System.out.println("Não introduza letras!");
                    this.error = true;
                }             
            } while (max_cap <= 0 || this.error == true);

            do {
                this.error = false;
                this.textType();
                System.out.print("Introduza o tipo do veículo: ");
                try {
                    pos = Integer.parseInt(reader.readLine());
                } catch (NumberFormatException nfe) {
                    System.out.println("Não introduza letras!");
                    this.error = true;
                }     
            } while ((pos < 1 || pos > 4) || this.error == true);        
            
            type_vehicle = this.type(pos);
           
            if (type_vehicle == ItemType.PERISHABLE_FOOD) {
                double num_km = 0;
                
                do {
                    this.error = false;
                    System.out.print("Introduza o número máximo de km que o veículo poderá percorrer --> ");
                    try {
                        num_km = Double.parseDouble(reader.readLine());
                    } catch (NumberFormatException nfe) {
                        System.out.println("Não introduza letras!");
                        this.error = true;
                    }                         
                } while (num_km <= 0 || this.error == true);
                
                try {
                    this.institucion.addVehicle(new RefrigeratedVehicle(matricula, max_cap, num_km));
                } catch (VehicleException ex) {
                    System.out.println("Exceção: " + ex.getMessage());
                }
            } else {
                try {
                    this.institucion.addVehicle(new VehicleImp(matricula, max_cap, type_vehicle));
                } catch (VehicleException ex) {
                    System.out.println("Exceção: " + ex.getMessage());
                }
            }
        } catch (IOException ex) {
            System.out.println("Exceção: " + ex.getMessage());
        }
    }
    
    /**
     * Método que compara duas matrículas.
     * 
     * @param matricula matrícula a comparar
     * @return vei, um veículo habilitado, caso as duas matrículas sejam iguais
     * @return null, se não encontrar essa matrícula em nenhum veículo habilitado da institucion
     */
    private Vehicle find_matricula_vei(String matricula) {
        for (Vehicle vei: this.institucion.getVehicles()) {
            if (vei instanceof VehicleImp) {
                if (((VehicleImp) vei).getMatricula().equals(matricula)) {
                    return vei;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Método que compara duas matrículas.
     * 
     * @param matricula matrícula a comparar
     * @return vei, um veículo desabilitado, caso as duas matrículas sejam iguais
     * @return null, se não encontrar essa matrícula em nenhum veículo desabilitado da institucion
     */
    private Vehicle find_matricula_disVei(String matricula) {
        for (Vehicle vei: this.institucion.getDisabledVehicles()) {
            if (vei instanceof VehicleImp) {
                if (((VehicleImp) vei).getMatricula().equals(matricula)) {
                    return vei;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Método para selecionar um veículo
     * 
     * @return vei, se a matrícula introduzida pelo utilizador pertence a um veículo da instituição
     * @retun null, se a matrícula introduzida pelo utilizador não pertencer a nenhum veículo
     */
    private Vehicle select_vei() {
        String matricula = "";
        String repet = "";
        Vehicle vei = null;
        
        do {
            repet = "N";
            
            try {
                do {
                    System.out.print("Introduza a matrícula do veículo: ");
                    matricula = this.reader.readLine();
                } while(matricula.equals(""));
            } catch (IOException ex) {
                System.out.println("Exceção: " + ex.getMessage());
            }
            
            vei = this.find_matricula_vei(matricula);

            if (vei != null) {
                System.out.println("Veículo encontrado!");
                return vei;
            }
            
            vei = this.find_matricula_disVei(matricula);

            if (vei != null) {
                System.out.println("Veículo encontrado!");
                return vei;
            }
                try {
                    do {
                        System.out.println("A matrícula introduzida não pertence a nenhum veículo da instituição... Pretende pesquisar novamente (S: sim/ N: não)?");
                        repet = reader.readLine();
                    } while (repet.equals(""));
                } catch (IOException ex) {
                    System.out.println("Exceção: " + ex.getMessage());
                }
        } while (repet.equals("S") || repet.equals("s"));
        
        return null;
    }
    
    /**
    * Metodo para ativar um veículo da instituição
    * 
    * @param vei veículo a ativar
    */
    private void enable_vehicle(Vehicle vei) {
        try {
            this.institucion.enableVehicle(vei);
        } catch (VehicleException ex) {
            System.out.println("Exceção: " + ex.getMessage());
        }
    }
    
    /**
     * Metodo para desativar um veículo da instituição
     * 
     * @param vei veículo a desativar
     */
    private void disable_vehicle(Vehicle vei) {
        try {
            this.institucion.disableVehicle(vei);
        } catch (VehicleException ex) {
            System.out.println("Exceção: " + ex.getMessage());
        }
    }
    
    /**
     * Método que exibe toda a informação do conjunto de veículos recebido
     * 
     * @param vei_list array de veículos a listar
     */
    private void listarVeiculos(Vehicle[] vei_list) {
        int i = 0;
        
        for (Vehicle vei: vei_list) {
            System.out.println("Veículo nº " + ++i);
            System.out.println(vei.toString());
        }
    }
    
    /**
     * Método para o menu de veículos que possui 7 opções:
     * 
     * 0 - Voltar ao menu principal, retorna ao menu da instituição.
     * 1 - Adicionar veículo, adiciona um novo veículo à instituição.
     * 2 - Desativar veículo, permite desativar um veículo da instituição. 
     * 3 - Ativar veículo, permite ativar um veículo da instituição.
     * 4 - Listar todos os veículos ativos, exibe todos os veículos ativos da instituição.
     * 5 - Listar todos os veículos inativos, exibe todos os veículos inativos da instituição.
     * 6 - Listar todos os veículos, exibe todos os veículos da instituição.
     */
    public void menu_vehicles() {
        int op = -1;
        Vehicle vei = null; 
        Scanner scanner = new Scanner(System.in);
                
        do {
            System.out.println("1- Adicionar veículo");
            System.out.println("2 - Desativar veículo");
            System.out.println("3 - Ativar veículo");
            System.out.println("4 - Listar todos os veículos ativos");
            System.out.println("5 - Listar todos os veículos inativos");
            System.out.println("6 - Listar todos os veículos");
            System.out.println("0 - Voltar ao menu principal");
            System.out.print("Escolha uma opção: ");
            
            try {
                op = scanner.nextInt();
            } catch (InputMismatchException ex) {
                System.out.println("Caractere inválido!");
                scanner.next(); 
            }
           
            switch (op) {
                case 0: {
                    break;
                }
                case 1: {
                    this.create_vehicle();
                    break;
                } case 2: {
                    if (this.institucion.getVehicles().length > 0) {
                        vei = this.select_vei();

                        if (vei != null) {
                            this.disable_vehicle(vei);
                        }
                    } else {
                        System.out.println("Todos os veículos estão inativos!");
                    }
                    break;
                } case 3: {
                    if (this.institucion.getDisabledVehicles().length > 0) {
                        vei = this.select_vei();

                        if (vei != null) {
                            this.enable_vehicle(vei);
                        }
                    } else {
                        System.out.println("Todos os veículos estão ativos!");
                    } 
                    break;
                } case 4: {
                    if (this.institucion.getVehicles().length > 0) {
                        this.listarVeiculos(this.institucion.getVehicles());
                    } else {
                        System.out.println("Não existem veículos ativos!");
                    }
                    break;
                } case 5: {
                    if (this.institucion.getDisabledVehicles().length > 0) {
                        this.listarVeiculos(this.institucion.getDisabledVehicles());
                    } else {
                        System.out.println("Não existem veículos inativos!");
                    }
                    break;
                } case 6: {
                    if ((this.institucion.getVehicles().length + this.institucion.getDisabledVehicles().length > 0)) {
                        System.out.println("Veículos Ativos"); 
                        if (this.institucion.getVehicles().length > 0) {
                            this.listarVeiculos(this.institucion.getVehicles()); 
                        } else {
                            System.out.println("Não existem veículos ativos!");
                        }
                        
                        System.out.println("Veículos Inativos"); 
                        if (this.institucion.getDisabledVehicles().length > 0) {
                            this.listarVeiculos(this.institucion.getDisabledVehicles());
                        } else {
                            System.out.println("Ainda não existem veículos inativos!");
                        }
                    }
                    break;
                } default: {
                    System.out.println("Opção Inválida!");
                    break;
                }
            }                                  
        } while(op != 0);
    }
    
    /**
    * Método para comparar duas aid boxes pelo código
    * 
    * @param code código da aid box a ser comparada
    * 
    * @return i, posição da aid box, caso esteja seja encontrada
    * @return -1, caso a aid box não exista na instituição
    */
    private int find_aidBox(String code) {
        AidBox[] aid = this.institucion.getAidBoxes();
        
        for (int i = 0; i < aid.length; i++) {
            if (aid[i].getCode().equals(code)) {
                return i;
            }
        }
        
        return -1;
    }

    /**
     * Método para adicionar um novo container a uma aid box da instituição.
     */
    public void addContainer() {
        int pos = 0;
        String repet = "";
        String cod_aid = "";
        
        do {
            repet = "N";

            try {
                do {
                    System.out.print("Introduza o código de uma aid box: ");
                    cod_aid = reader.readLine();
                } while (cod_aid.equals(""));
            } catch (IOException ex) {
                System.out.println("Exceção: " + ex.getMessage());
            }
            
            pos = this.find_aidBox(cod_aid);
            
            if (pos == -1) {
                try {
                    do{
                        System.out.println("O código introduzido não pertence a nenhuma aid box da instituição... Pretende introduzir de novo? (S: sim/ N: não)");
                        repet = reader.readLine();
                    } while(repet.equals(""));
                } catch (IOException ex) {
                    System.out.println("Exceção: " + ex.getMessage());
                }
            }
        } while (repet.equals("S") || repet.equals("s"));

        if (pos != -1) {
            if (this.institucion.getAidBoxes()[pos].getContainers().length < 4) {
                String code_container = "";
                double capacity = 0;
                int pos_type = 0;
                ItemType typeContainer = null;
                
                try {
                    do{
                        System.out.print("Introduza o código do container: "); 
                        code_container = reader.readLine();
                    } while (cod_aid.equals(""));
                } catch (IOException ex) {
                    System.out.println("Exceção: " + ex.getMessage());
                }

                try {
                    do {
                        this.error = false;
                        this.textType();   
                        System.out.print("Introduza o tipo do veículo: ");
                        
                        try {
                            pos_type = Integer.parseInt(reader.readLine());
                        } catch (NumberFormatException nfe) {
                            System.out.println("Não introduza letras!");
                            this.error = true;
                        }   
                    } while ((pos_type < 1 || pos_type > 4) || this.error == true);  
                } catch (IOException ex) {
                    System.out.println("Exceção: " + ex.getMessage());
                }

                typeContainer = this.type(pos_type);

                try {
                    do {
                        this.error = false;
                        System.out.print("Introduza a capacidade máxima do container: ");

                        try {                        
                            capacity = Double.parseDouble(reader.readLine());
                        } catch (NumberFormatException nfe) {
                            System.out.println("Não introduza letras!");
                            this.error = true;
                        } 
                    } while(capacity <= 0 || this.error == true);
                } catch (IOException ex) {
                    System.out.println("Exceção: " + ex.getMessage());
                }

                try {
                    this.institucion.getAidBoxes()[pos].addContainer(new ContainerImp(code_container, typeContainer, capacity));
                } catch (ContainerException ex) {
                    System.out.println("Exceção: " + ex.getMessage());
                }
            } else {
                System.out.println("Número máximo de containers atingido: (4)");
            }
        }
    }
    
    /**
     * Método que procura por um container em todas as aid boxes da instituição.
     * 
     * @param cod_container código do container a ser procurado
     * 
     * @return container, caso o container a procurar exista em alguma aid box da instituição
     * @return null, caso o container não seja encontrado
     */
    private Container find_container(String cod_container) {
        for (AidBox aid: institucion.getAidBoxes()) {
            for (Container container: aid.getContainers()) {
                if (container.getCode().equals(cod_container)) {
                    return container;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Método para adicionar uma nova medição a um container da instituição.
     */
    public void addMeasurement()  {
        Container container = null;
        String repet = "N";
        String cod_container = "";
        
        do {
            repet = "N";
            System.out.print("Introduza o código do container: ");
            
            try {
                cod_container = reader.readLine();
            } catch (IOException ex) {
                System.out.println("Exceção: " + ex.getMessage());
            }
            
            container = this.find_container(cod_container);
            
            if (container == null) {
                System.out.println("O código introduzido não pertence a nenhum container da instituição... Pretende introduzir de novo? (S: sim / N: não)");

                try {
                    repet = reader.readLine();
                } catch (IOException ex) {
                    System.out.println("Exceção: " + ex.getMessage());
                }
            }
        } while (repet.equals("S") || repet.equals("s"));
        
        if (container != null) {
            double value_med = 0;      
   
  
            do {
                this.error = false;
                
                System.out.print("Introduza uma medição: ");
                
                try {
                    value_med = Double.parseDouble(reader.readLine());
                } catch (NumberFormatException nfe) {
                    System.out.println("Não introduza letras!");
                    this.error = true;
                } catch (IOException ex) {  
                    System.out.println("Exceção: " + ex.getMessage());
                }
                
                if (container.getCapacity() < value_med) {
                    System.out.println("O valor introduzido é maior do que a capacidade máxima do container!");
                    System.out.println("Introduza uma medição menor ou igual a " + container.getCapacity() + "!");
                }
            } while (value_med <= 0 || container.getCapacity() < value_med || this.error == true);

            try {
                this.institucion.addMeasurement(new MeasurementImp(LocalDateTime.now(), value_med), container);
            } catch (ContainerException | MeasurementException ex) {
                System.out.println("Exceção: " + ex.getMessage());
            }
        }
    }
    
    /**
     * Método para adicionar uma nova aid box à instituição.
     */
    public void addAidBox() {
        String code = "";
        String zone = "";                        
        String ref_local = "";
        double latitude = 0;
        double longitude = 0;
        
        try {            
            do {
                System.out.print("Introduza o código da aid box: ");
                code = reader.readLine();
            } while (code.equals(""));

            do {
                System.out.print("Introduza a zona da aid box: ");
                zone = reader.readLine();
            } while (zone.equals(""));

            do {
                System.out.print("Introduza a referência do local da aid box: ");
                ref_local = reader.readLine();
            } while (ref_local.equals(""));
           
            System.out.println("Introdução das coordenadas da aid box!");

            do {
                this.error = false;
                System.out.print("Introduza a latitude da aid box: ");
                try {
                    latitude = Double.parseDouble(reader.readLine());
                } catch (NumberFormatException nfe) {
                    System.out.println("Não introduza letras!");
                    this.error = true;
                }  
            } while (this.error == true);

           
            do {
                this.error = false;
                System.out.print("Introduza a longitude da aid box: ");
                try {
                    longitude = Double.parseDouble(reader.readLine());
                } catch (NumberFormatException nfe) {
                    System.out.println("Não introduza letras!");
                    this.error = true;
                }  
            } while (this.error == true);
            
            try {
                institucion.addAidBox(new AidBoxImp(code, zone, ref_local, new GeographicCoordinatesImp(latitude, longitude)));
            } catch (AidBoxException ex) {
                System.out.println("Exceção: " + ex.getMessage());
            }
        } catch (IOException ex) {
            System.out.println("Exceção: " + ex.getMessage());
        }
    }
    
    /**
     * Método para adicionar um novo picking map à instituição
     * 
     * @return true, se conseguir adicionar o picking map,
     * false, caso contrário
     */
    public boolean addPickingMap() {
        try {
            RouteGeneratorImp routegen = new RouteGeneratorImp();
            StrategyImp strtg = new StrategyImp();
            RouteValidatorImp rv = new RouteValidatorImp();
            ReportImp report = new ReportImp(strtg);
            
            Route[] route_list = routegen.generateRoutes(this.institucion, strtg, rv, report);
            
            if (route_list.length > 0) {
                institucion.addPickingMap(new PickingMapImp(route_list));
                return true;
            }
        } catch (PickingMapException ex) {
            System.out.println("Exceção: " + ex.getMessage());
        }
        return false;
    }
    
    /**
     * Método para carregar todos os dados do JSON ou da Web para a instituição.
     * 
     * Este método tem um menu com 3 opções:
     * 1 - Fazer pedido web, carrega todos os dados da web para a instituição
     * 2 - Ler ficheiro JSON, carrega os dados dos 3 ficheiros JSON para a instituição 
     * 0 - Voltar ao menu principal, retorna o utilizador para o menu principal sem carregar nenhum dado
     * 
     * @param read_data, dados lidos
     * @return true, se conseguir carregar os dados do JSON ou da Web para a instituição,
     * false, caso não consiga ler ou o utilizador queira voltar para o menu principal
     */
    public boolean load_data(boolean read_data) {
        int op = -1;
        HTTPProviderImp http = null;
        ImporterImp impt = null;
        Scanner scanner = new Scanner(System.in);
        
        do {
            System.out.println("1 - Fazer pedido web");
            System.out.println("2 - Ler de ficheiro JSON");
            System.out.println("0 - Voltar ao menu principal");
            System.out.println("Introduza um número: ");
            
            try {
                op  = scanner.nextInt();
            } catch (InputMismatchException ex) {
                System.out.println("Caractere inválido!");
                scanner.next(); 
            }
            
            if (op < 0 | op > 2) {
                System.out.println("Intervalo fora do alcance!");
            }
        } while (op < 0 | op > 2);
        
        switch (op) {
            case 0: {
                break;
            }
            case 1: {
                http = new HTTPProviderImp();
                http.getAllURL(this.institucion);
                return true;                
            } case 2: {
                impt = new ImporterImp();
                
                try {
                    impt.importData(this.institucion);
                } catch (IOException | InstitutionException ex) {
                    System.out.println("Exceção: " + ex.getMessage());
                }
                return true;
            } default: {
                System.out.println("Opção Inválida!");
                break;
            }
        }
        
        return false;
    }
    
    /**
     * Método que vai buscar toda a informação da instituição.
     */
    public void info_inst() {
        System.out.println(this.institucion.toString());
    }
}