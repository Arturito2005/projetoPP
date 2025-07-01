package JSON;

import Classes.AidBoxImp;
import Classes.ContainerImp;
import Classes.Distance;
import Classes.GeographicCoordinatesImp;
import Classes.InstitutionImp;
import Classes.MeasurementImp;
import com.estg.core.AidBox;
import com.estg.core.Container;
import com.estg.core.Institution;
import com.estg.core.ItemType;
import com.estg.core.exceptions.AidBoxException;
import com.estg.core.exceptions.ContainerException;
import com.estg.core.exceptions.InstitutionException;
import com.estg.core.exceptions.MeasurementException;
import com.estg.io.Importer;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
 * ImporterImp, classe que importa os dados dos 3 ficheiros JSON
 * 
 * @author Artur Gentil Silva Pinto
 * @version 1.0
 * @since 2024-05-25
 */
public class ImporterImp implements Importer {
    
    /**
     * Método para verificar qual é o tipo de item de um contentor
     * 
     * @param codigo_container contentor 
     * 
     * @return o tipo de item do contentor
     */
    private ItemType container_type(String codigo_container) {
        switch (codigo_container.charAt(0)) {
            case 'V': {
                return ItemType.CLOTHING;
            }
            case 'M': {
                return ItemType.MEDICINE;
            }
            case 'N': {
                return ItemType.NON_PERISHABLE_FOOD;
            }
            case 'P': {
                return ItemType.PERISHABLE_FOOD;
            }
            default: {
                throw new NullPointerException("O item type do container não existe!");
            }
        }
    }
    
    /**
     * Método para verificar se o código do container recebido como parâmetro existe em alguma aid box da instituição
     * 
     * @param instn insituição a verificar se o container existe 
     * @param container_find container a verificar se existe
     * 
     * @return i, a posição do container
     * @return -1, se não encontrar o container
     */
    private int findContainer(Institution instn, String container_find) {
        AidBox[] aid_list = instn.getAidBoxes();
        for (AidBox aidbox: aid_list) {
            Container[] container = aidbox.getContainers();
            for (int i = 0; i < container.length; i++) {
                if (container[i].getCode().equals(container_find)) {
                   return i;
                } 
            }
        }
        
        return -1;
    }
    
    /**
    * Método que lê o ficheiro "AidBoxes.json" (aid boxes e respetivos containers)
    * 
    * @param instn instituição para a qual os dados vão ser importados
    * @throws FileNotFoundException se o ficheiro não for encontrado
    * @throws IOException se o ficheiro não puder ser lido
    */
    private void getAidBoxesJSON(Institution instn) throws FileNotFoundException, IOException {
        JSONParser jsonP = new JSONParser();
        try {
            FileReader reader = new FileReader("./jsonFiles/AidBoxes.json");
            Object obj = jsonP.parse(reader);
            JSONArray aidBoxArray = (JSONArray) obj;        
   
            for (Object o: aidBoxArray) {
                JSONObject readingObj = (JSONObject) o;
               
                String codigo = (String) readingObj.get("Codigo");
                String zona = (String) readingObj.get("Zona");
                double latitude = (double) readingObj.get("Latitude");
                double longitude = (double) readingObj.get("Longitude");
                
                JSONArray contentor_JSON = (JSONArray) readingObj.get("Contentores");

                int cont_containers = 0; 
                ContainerImp[] container = new ContainerImp[contentor_JSON.size()];
                
                for (Object cont_obj : contentor_JSON) {
                    JSONObject readingObjConte = (JSONObject) cont_obj;
                    String codigo_cont = (String) readingObjConte.get("codigo");
                    
                    long capacity = (long) readingObjConte.get("capacidade");
                    
                    ContainerImp temp_container = new ContainerImp(codigo_cont, this.container_type(codigo_cont) ,capacity);
                    container[cont_containers++] = temp_container;
                }
                
                GeographicCoordinatesImp geo = new GeographicCoordinatesImp(latitude, longitude);
                
                AidBoxImp aid = new AidBoxImp(codigo, zona, geo);
                
                for (int i = 0; i < cont_containers; i++) {
                    try {
                        aid.addContainer(container[i]);
                    } catch (ContainerException ex) {
                        System.out.println("Exceção: " + ex.getMessage());
                    }
                }
                
                try {
                    instn.addAidBox(aid);
                } catch (AidBoxException ex) {
                    System.out.println("Exceção: " + ex.getMessage());
                }
            }
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException("O ficheiro não foi encontrado!");
        } catch (IOException ex) {
            throw new IOException("Não foi possível ler o ficheiro!");
        } catch (ParseException ex) {
            System.out.println("Exceção: " + ex.getMessage());
        }
    }
    
    /**
    * Método que lê o ficheiro "Readings.json" (medições dos contentores)
    * 
    * @param instn instituição para a qual os dados vão ser importados
    * @throws FileNotFoundException se o ficheiro não for encontrado
    * @throws IOException se o ficheiro não puder ser lido
    */
    private void getReadingsJSON(Institution instn) throws FileNotFoundException, IOException {
        JSONParser jsonP = new JSONParser();
        try {
            FileReader reader = new FileReader("./jsonFiles/Readings.json");
            Object obj = jsonP.parse(reader);
            JSONArray contairarray = (JSONArray) obj;
            
            for (Object o: contairarray) {
                JSONObject readingObj = (JSONObject) o;
               
                String contentor_code = (String) readingObj.get("contentor");
                String data_string = (String) readingObj.get("data");
                
                Instant instant = Instant.parse(data_string);
                LocalDateTime data = instant.atOffset(ZoneOffset.UTC).toLocalDateTime();
                
                long valor = (long) readingObj.get("valor");
                
                MeasurementImp meas_temp = new MeasurementImp(data, valor);
                ContainerImp container_temp = new ContainerImp(contentor_code, this.container_type(contentor_code), valor);

                int pos_container = this.findContainer(instn, contentor_code);
                
                if (pos_container != -1) {
                    try {
                        instn.addMeasurement(meas_temp, container_temp);
                    } catch (ContainerException | MeasurementException ex) {
                        System.out.println("Exceção: " + ex.getMessage());
                    }
                }
                else {
                    System.out.println("O container lido não existe em nenhuma aidbox da instituição");
                }
            }
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException("O ficheiro não foi encontrado");
        } catch (IOException ex) {
            throw new IOException("Não foi possível ler o ficheiro");
        } catch (ParseException ex) {
            System.out.println("Exceção: " + ex.getMessage());
        }
    }
    
    /**
    * Método que lê o ficheiro "Distances.json" (distâncias entre as aid boxes e entre cada aid box e a base)
    * (a base é a instituição)
    * 
    * @param instn instituição para a qual os dados vão ser importados
    * @throws FileNotFoundException se o ficheiro não for encontrado
    * @throws IOException se o ficheiro não puder ser lido
    */
    private void getDistancesJSON(Institution instn) throws FileNotFoundException, IOException {
        if (instn instanceof InstitutionImp) {
            JSONParser jsonP = new JSONParser();
            try {
                FileReader reader = new FileReader("./jsonFiles/Distances.json");
                Object obj = jsonP.parse(reader); 
                JSONArray distancesArray = (JSONArray) obj;
                for (Object o: distancesArray) {
                    JSONObject readingObj = (JSONObject) o;

                    String from = (String) readingObj.get("from");
                    JSONArray contentores_JSON = (JSONArray) readingObj.get("to");

                    for (Object cont_obj : contentores_JSON) {
                        JSONObject readingObjDistance = (JSONObject) cont_obj;
                        String aidbox_cod = (String) readingObjDistance.get("name");
                        long distance_c = (long) readingObjDistance.get("distance");
                        long duration = (long) readingObjDistance.get("duration");


                        if (aidbox_cod.equals("Base")) {
                            Distance dist_base = new Distance(from, distance_c, duration);
                            ((InstitutionImp) instn).addDistance(dist_base);
                        }

                        Distance dist_temp = new Distance(aidbox_cod, distance_c, duration);
                        ((InstitutionImp) instn).addDistanceAid(from, dist_temp);                    
                    }
                }
            } catch (FileNotFoundException ex) {
                throw new FileNotFoundException("O ficheiro não foi encontrado!");
            } catch (IOException ex) {
                throw new IOException("Não foi possível ler o ficheiro!");
            } catch (ParseException ex) {
                System.out.println("Exceção: " + ex.getMessage());
            }
        } 
    }
    
    /**
    * Método que importa o conteúdo dos 3 ficheiros JSON para o programa
    * 
    * @param instn instituição para a qual os dados vão ser importados
    * @throws FileNotFoundException se o ficheiro não for encontrado
    * @throws IOException se o ficheiro não puder ser lido
    * @throws InstitutionException se a instituição for nula
    */
    @Override
    public void importData(Institution instn) throws FileNotFoundException, IOException, InstitutionException {
        if (instn == null) {
            throw new InstitutionException("A instituição recebida está a null!");
        }
        
        this.getAidBoxesJSON(instn);
        this.getReadingsJSON(instn);
        this.getDistancesJSON(instn);
    }
}