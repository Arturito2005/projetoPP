package Testes;

import Classes.AidBoxImp;
import Classes.ContainerImp;
import Classes.GeographicCoordinatesImp;
import Classes.InstitutionImp;
import Classes.MeasurementImp;
import Vehicles.VehicleImp;
import Vehicles.RefrigeratedVehicle;
import Vehicles.VehicleImp;
import JSON.ImporterImp;
import Rotas.RouteImp;
import Rotas.RouteValidatorImp;
import com.estg.core.AidBox;
import com.estg.core.ItemType;
import com.estg.core.exceptions.AidBoxException;
import com.estg.core.exceptions.ContainerException;
import com.estg.core.exceptions.InstitutionException;
import com.estg.core.exceptions.MeasurementException;
import com.estg.core.exceptions.PickingMapException;
import com.estg.core.exceptions.VehicleException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

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
 * Testes, classe usada para testar a versão final do projeto
 * 
 * @author Artur Gentil Silva Pinto
 * @version 1.0
 * @since 2024-05-25
 */

/**
 * Testes, classe usada para testar a versão final do projeto
 * 
 * @author Francisco Miguel Pereira Oliveira
 * @version 1.0
 * @since 2024-05-25
 */
public class Testes {
    
    private static final Logger logger = Logger.getLogger(Testes.class.getName());
    
    public static void main(String[] args) throws VehicleException, PickingMapException, AidBoxException, ContainerException, MeasurementException {
      
        InstitutionImp i1 = new InstitutionImp("Base1");
        VehicleImp v1 = new VehicleImp("FWF132WEF", 50, ItemType.CLOTHING);
        VehicleImp v2 = new RefrigeratedVehicle("MWFE34", 50, 80);
        
        ImporterImp import1 = new ImporterImp();
        try {
            import1.importData(i1);
        } catch (FileNotFoundException exc) {
            exc.printStackTrace();
        } catch (IOException exc) {
            exc.printStackTrace();
        } catch (InstitutionException exc) {
            exc.printStackTrace();
        }
        
        for (AidBox aidbox : i1.getAidBoxes()) {
            System.out.println(aidbox.getCode());
            System.out.println(aidbox.getCoordinates());
            System.out.println(aidbox.getZone());
            System.out.println(Arrays.toString(aidbox.getContainers()));
        }
        
        for (AidBox aidbox : i1.getAidBoxes()) {
            System.out.println("Aidbox " + aidbox.getCode() + ": " + aidbox.getContainer(ItemType.PERISHABLE_FOOD));
        }
        
        for (AidBox aidbox : i1.getAidBoxes()) {
            System.out.println("Distância de i1 até à aidbox " + aidbox.getCode()+ ":" + i1.getDistance(aidbox));
        }
        
        AidBox[] a = i1.getAidBoxes();
        
        for (int i = 0; i < i1.getAidBoxes().length; i++) {
            for (int j = 0; j < i1.getAidBoxes().length; j++) {
                System.out.println(a[i].getDistance(a[j]));
            }
        }
        
        try {
            System.out.println(i1.addVehicle(v1));
        } catch (VehicleException exc) {
            logger.log(Level.SEVERE, exc.getMessage(), exc);
        }
        
        try {
            System.out.println(i1.addVehicle(v2));
        } catch (VehicleException exc) {
            logger.log(Level.SEVERE, exc.getMessage(), exc);
        }
        
        /*
        try {
            System.out.println(i1.addVehicle(v1));
        } catch (VehicleException exc) {
            logger.log(Level.SEVERE, exc.getMessage(), exc);
        }
        */
        
        /*
        try {
            i1.enableVehicle(v1);
        } catch (VehicleException exc) {
            logger.log(Level.SEVERE, exc.getMessage(), exc);
        }
        */
        
        try {
            i1.disableVehicle(v1);
        } catch (VehicleException exc) {
            logger.log(Level.SEVERE, exc.getMessage(), exc);
        }
        
        try {
            i1.enableVehicle(v1);
        } catch (VehicleException exc) {
            logger.log(Level.SEVERE, exc.getMessage(), exc);
        }
        
        
        System.out.println(Arrays.toString(i1.getVehicles()));
        
        System.out.println(Arrays.toString(i1.getPickingMaps()));
        
        RouteImp r1 = new RouteImp(v1);
        GeographicCoordinatesImp g1 = new GeographicCoordinatesImp(45, 67);
        AidBoxImp a1 = new AidBoxImp("fwf", "fwfwe", g1);
        ContainerImp c1 = new ContainerImp("acc", ItemType.CLOTHING, 250);
        MeasurementImp m1 = new MeasurementImp(LocalDateTime.now(), 230);
        a1.addContainer(c1);
        c1.addMeasurement(m1);
        
        RouteValidatorImp rv1 = new RouteValidatorImp();
        
        System.out.println(rv1.validate(r1, a1));
        
        
        /*
        try {
            System.out.println(i1.getCurrentPickingMap());
        } catch (PickingMapException exc) {
            logger.log(Level.SEVERE, exc.getMessage(), exc);
        }
        */
        
        
        /*
        HTTPProvider httpProvider = new HTTPProvider();
        
        String api1 = httpProvider.getFromURL("https://data.mongodb-api.com/app/data-docuz/endpoint/aidboxes");
        String api2 = httpProvider.getFromURL("https://data.mongodb-api.com/app/data-docuz/endpoint/aidboxesbyid?codigo=CAIXF37");
        String api3 = httpProvider.getFromURL("https://data.mongodb-api.com/app/datadocuz/endpoint/distances?from=CAIXF37&to=CAIXF44");
        String api4 = httpProvider.getFromURL("https://data.mongodb-api.com/app/data-docuz/endpoint/readings");
        
        System.out.println("Aidboxes: " + api1);
        System.out.println("Aidbox: " + api2);
        System.out.println("Distances: " + api3);
        System.out.println("Readings: " + api4);
        */
        
        
        /**
         Testes Artur
         */
        
        /**
         *InstitutionCla i1 = new InstitutionCla("Base1");
        
        
        ImporterCla import1 = new ImporterCla();
        try {
            import1.importData(i1);
        } catch (IOException ex) {
            Logger.getLogger(Testes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstitutionException ex) {
            Logger.getLogger(Testes.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        RefrigeratedVehicle ve1 = new RefrigeratedVehicle("AB-12-24", 15000, 20000);
        VehiclesCla ve2 = new VehiclesCla("RQ-45-23", 23000, ItemType.MEDICINE);
        VehiclesCla ve3 = new VehiclesCla("AB-12-22", 55000, ItemType.CLOTHING);
        
        VehiclesCla ve7 = new VehiclesCla("QQ-12-22", 50000, ItemType.NON_PERISHABLE_FOOD);
        RefrigeratedVehicle ve4 = new RefrigeratedVehicle("AB", 121000, 25000);
        VehiclesCla ve5 = new VehiclesCla("TT", 250000, ItemType.MEDICINE);
        VehiclesCla ve6 = new VehiclesCla("RV", 43000, ItemType.CLOTHING);
        VehiclesCla ve8 = new VehiclesCla("QQ", 32000, ItemType.NON_PERISHABLE_FOOD);

        try {
            i1.addVehicle(ve1);
            i1.addVehicle(ve2);
            i1.addVehicle(ve3);
            i1.addVehicle(ve4);
            i1.addVehicle(ve5);
            i1.addVehicle(ve6);
            i1.addVehicle(ve7);
            i1.addVehicle(ve8);
        } catch (VehicleException ex) {
            Logger.getLogger(Testes.class.getName()).log(Level.SEVERE, null, ex);
        }
    
       
        
        System.out.println(ve1.getSupplyType());
        System.out.println(ve1.getMaxCapacity());
        //System.out.println(ve1.getMaxKm());
        
        ve1.equals(ve3);
        ve1.equals(ve2);
        
        System.out.println(ve1.toString());
        
        RouteGeneratorCla route_cal = new RouteGeneratorCla();
        RouteValidatorCla validator = new RouteValidatorCla();        
        StrategyCla strategy = new StrategyCla();
         
        MeasurementCla men1 = new MeasurementCla(LocalDateTime.now(), 12);
        MeasurementCla men2 = new MeasurementCla(LocalDateTime.MAX, 40);
        MeasurementCla men3 = new MeasurementCla(LocalDateTime.MAX, 1);      
        
        ContainerCla con1 = new ContainerCla("asdasd", ItemType.CLOTHING, 12);
        ContainerCla con2 = new ContainerCla("sdad", ItemType.PERISHABLE_FOOD, 43);
        ContainerCla con3 = new ContainerCla("M12", ItemType.MEDICINE, 45);
        
        try {
            con1.addMeasurement(men1);
            con2.addMeasurement(men2);
            con3.addMeasurement(men3);
        } catch (MeasurementException ex) {
            Logger.getLogger(Testes.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ReportCla report1 = new ReportCla(strategy);
        try {
            route_cal.generateRoutes(i1, strategy, validator, report1);
        } catch (PickingMapException ex) {
            Logger.getLogger(Testes.class.getName()).log(Level.SEVERE, null, ex);
        } 
         */
        
        /*
                GeographicCoordinatesCla geo1 = new GeographicCoordinatesCla(12, 32);
        GeographicCoordinatesCla geo2 = new GeographicCoordinatesCla(60, 34);
        GeographicCoordinatesCla geo3 = new GeographicCoordinatesCla(70, 67);
        GeographicCoordinatesCla geo4 = new GeographicCoordinatesCla(80, 34);
        GeographicCoordinatesCla geo5 = new GeographicCoordinatesCla(90, 9);
        GeographicCoordinatesCla geo6 = new GeographicCoordinatesCla(100, 19);
        GeographicCoordinatesCla geo7 = new GeographicCoordinatesCla(34, 89);
        GeographicCoordinatesCla geo8 = new GeographicCoordinatesCla(67, 34);
        
        AidBoxCla aid1 = new AidBoxCla("CAIXF41", "Zone", "Algarve", geo1);
        AidBoxCla aid2 = new AidBoxCla("CAIXF40", "Zone", "Algarve", geo2);
        AidBoxCla aid3 = new AidBoxCla("CAIXF39", "Exe", "Porto", geo3);
        AidBoxCla aid4 = new AidBoxCla("CAIXF43", "LLL", "Felguerias", geo4);
        AidBoxCla aid5 = new AidBoxCla("CAIXF50", "hjhjhj", "hjhjhjhjhjhh", geo5);
        
        Distance distance1 = new Distance("CAIXF41", 123, 12);
        Distance distance2 = new Distance("CAIXF40", 300, 11);
        Distance distance3 = new Distance("CAIXF42", 450, 12);
        aid1.getContainers();
        
        try {
            //Add funciona tudo
            aid1.addContainer(con1);
            //NulPointException funciona bem
            //aid1.addContainer(null);
            aid1.addContainer(con2);
            aid1.addContainer(con3);
            
            aid3.addContainer(con1);
            aid3.addContainer(con2);
            
            aid4.addContainer(con2);
            aid5.addContainer(con1);
        } catch (ContainerException ex) {
            Logger.getLogger(Testes.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        try {
            aidper.addContainer(persible1);
            aidper12.addContainer(persible2);
            aidper23.addContainer(persible3);
        } catch (ContainerException ex) {
            Logger.getLogger(Testes.class.getName()).log(Level.SEVERE, null, ex);
        }
                try {
            i1.addAidBox(aid1);
            i1.addAidBox(aid2);
            i1.addAidBox(aid3);
            i1.addAidBox(aidper12);
            i1.addAidBox(aidper23);
        } catch (AidBoxException ex) {
            Logger.getLogger(Testes.class.getName()).log(Level.SEVERE, null, ex);
        }         
        */
        
        //System.out.println("Fim dos testes");
        
        //HTTPProviderCla http = new HTTPProviderCla();
        //InstitutionCla i2 = new InstitutionCla("TesteHTTP");
        /*Primeiro link*/
        //O primeiro link funciona
        //http.getURLAidBoxes(i2);
        /*Segundo link*/
        /*
        try{
            http.getURLOneAid(i2, null);
            http.getURLOneAid(i2, "CAIXF37");
        } catch (NullPointerException ex) {
            Logger.getLogger(Testes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IndexOutOfBoundsException ex) {
            Logger.getLogger(Testes.class.getName()).log(Level.SEVERE, null, ex);
        } 
        */
        
        
        /*Terceiro link (Funciona)*/
        //http.urlReadings(i2);
        
        /*Quarto link*/
        //http.getDisance(i2, "CAIXF37", "CAIXF44");
        //Teste metodos
        //Está a guardar as bases na institucion
        /*Quinto link (Funciona)*/
        //http.getDistances(i2);
        //Fazer link que liga os 3 principais e testar
        /*Funciona excetu que as aidBoxes não tem distance para as base*/
        //http.getAllURL(i2);
       
        /*
        MeasurementCla men1 = new MeasurementCla(LocalDateTime.now(), 12);
        MeasurementCla men2 = new MeasurementCla(LocalDateTime.MAX, 40);
        MeasurementCla men3 = new MeasurementCla(LocalDateTime.MAX, 1);      
        
        ContainerCla con1 = new ContainerCla("asdasd", ItemType.CLOTHING, 12);
        ContainerCla con2 = new ContainerCla("sdad", ItemType.PERISHABLE_FOOD, 43);
        ContainerCla con3 = new ContainerCla("M12", ItemType.MEDICINE, 45);
        
        try {
            con1.addMeasurement(men1);
            con2.addMeasurement(men2);
            con3.addMeasurement(men3);
        } catch (MeasurementException ex) {
            Logger.getLogger(Testes.class.getName()).log(Level.SEVERE, null, ex);
        }
        
                GeographicCoordinatesCla geo1 = new GeographicCoordinatesCla(12, 32);
        GeographicCoordinatesCla geo2 = new GeographicCoordinatesCla(60, 34);
        GeographicCoordinatesCla geo3 = new GeographicCoordinatesCla(70, 67);
        GeographicCoordinatesCla geo4 = new GeographicCoordinatesCla(80, 34);
        GeographicCoordinatesCla geo5 = new GeographicCoordinatesCla(90, 9);
        GeographicCoordinatesCla geo6 = new GeographicCoordinatesCla(100, 19);
        GeographicCoordinatesCla geo7 = new GeographicCoordinatesCla(34, 89);
        GeographicCoordinatesCla geo8 = new GeographicCoordinatesCla(67, 34);
        
        AidBoxCla aid1 = new AidBoxCla("CAIXF41", "Zone", "Algarve", geo1);
        AidBoxCla aid2 = new AidBoxCla("CAIXF40", "Zone", "Algarve", geo2);
        AidBoxCla aid3 = new AidBoxCla("CAIXF39", "Exe", "Porto", geo3);
        AidBoxCla aid4 = new AidBoxCla("CAIXF43", "LLL", "Felguerias", geo4);
        AidBoxCla aid5 = new AidBoxCla("CAIXF50", "hjhjhj", "hjhjhjhjhjhh", geo5);
        
        aid1.getContainers();
        
        try {
            //Add funciona tudo
            aid1.addContainer(con1);
            //NulPointException funciona bem
            //aid1.addContainer(null);
            aid1.addContainer(con2);
            aid1.addContainer(con3);
            
            aid3.addContainer(con1);
            aid3.addContainer(con2);
            
            aid4.addContainer(con2);
            aid5.addContainer(con1);
        } catch (ContainerException ex) {
            Logger.getLogger(Testes.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Funciona bem
        try {
            aid1.deleteContainer(1);
        } catch (IndexOutOfBoundsException ex) {
            Logger.getLogger(Testes.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Penso sestar correto
        System.out.println(aid1.toString());
        */
        
        /*
        try {
            System.out.println("Distancia:" + aid1.getDistance(aid2));
        } catch (AidBoxException ex) {
            Logger.getLogger(Testes.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        
        /*Tester Route*/
        /*
                RouteCla rout1 = new RouteCla(ve1);
        RouteCla rout2 = new RouteCla(ve2);
        try {
            rout1.addAidBox(aid1);
            //Null funciona
            //rout1.addAidBox(null);
            //Equals Funciona
            //rout1.addAidBox(aid1);
            //Não ter container funciona
            //rout1.addAidBox(aid2);
            rout1.addAidBox(aid3);
            rout1.addAidBox(aid4);
            rout1.removeAidBox(aid3);
            rout1.addAidBox(aid5);

        } catch (RouteException ex) {
            Logger.getLogger(Testes.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        
        //Funciona
        //rout1.containsAidBox(aid5);
        //Está a dar erro 
        //rout1.getTotalDistance();
 
        //Falta testar insertAfter, gestDuration
        /*
                try {
 
            rout1.replaceAidBox(aid5, aid4);
        } catch (RouteException ex) {
            Logger.getLogger(Testes.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            rout1.insertAfter(aid4, aid3);
        } catch (RouteException ex) {
            Logger.getLogger(Testes.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println(rout1.toString());
        //GetDistance da rota está a dar sempre erro
        //System.out.println("Distancia total:" + rout1.getTotalDistance());
        
        RouteCla[] list_route = {rout1, rout2};
        */
        
       // PickingMapCla pick1 = new PickingMapCla(LocalDateTime.now(), list_route);
        //PickingMapCla pick2 = new PickingMapCla(LocalDateTime.MAX, null);
        
        //System.out.println("Lista de rotas " + Arrays.toString(pick2.getRoutes()));
        //System.out.println("Metodo toString" + pick2.toString());
        
        //System.out.println("Data: " + pick1.getDate());
        //System.out.println("Lista de rota" + Arrays.toString(pick1.getRoutes()));
        //System.out.println("Metodo toString" + pick1.toString());
    
       
            
        //MeasurementCla mensuTeste = new MeasurementCla(LocalDateTime.MAX, 12);
        //ContainerCla persible1 = new ContainerCla("DASDA", ItemType.PERISHABLE_FOOD, 12);
        //ContainerCla persible2 = new ContainerCla("DDDDD", ItemType.PERISHABLE_FOOD, 12);
        //ContainerCla persible3 = new ContainerCla("GGGGG", ItemType.PERISHABLE_FOOD, 12);
        
        /*try {
            persible1.addMeasurement(mensuTeste);
            persible3.addMeasurement(mensuTeste);
            persible2.addMeasurement(mensuTeste);
        } catch (MeasurementException ex) {
            Logger.getLogger(Testes.class.getName()).log(Level.SEVERE, null, ex);
        }
        

            
        System.out.println(Arrays.toString(i1.getVehicles()));
        */
        
    }
    
}
