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
import com.estg.core.exceptions.MeasurementException;
import com.estg.io.HTTPProvider;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
 * HTTPProviderImp, classe que carrega os dados da API 
 * 
 * @author Artur Gentil Silva Pinto
 * @version 1.0
 * @since 2024-05-28
 */
public class HTTPProviderImp {
    /**
    * link onde se encontram todas as aidboxes da instituição (inclui as aidboxes e containers).
    */
    private static final String url_aidBoxes = "https://data.mongodb-api.com/app/data-docuz/endpoint/aidboxes";
    
    /**
    * link onde se encontram todas as aidboxes da instituição (inclui as aidboxes e containers,
    * mas só é possível carregar uma aidbox de cada vez pelo código).
    */
    private static String url_cod_container = "https://data.mongodb-api.com/app/data-docuz/endpoint/aidboxesbyid?codigo={codigo}";

    /**
    * link onde se encontram todas as medições a contentores da instituição.
    */
    private static final String url_readings = "https://data.mongodb-api.com/app/data-docuz/endpoint/readings";
    
    /**
    * link onde se encontram todas as distâncias da instituição
    * (mas só é possível carregar uma distância de cada vez entre duas aidboxes).
    */
    private static String url_distance = "https://data.mongodb-api.com/app/data-docuz/endpoint/distances?from={codigoOrigem}&to={codigoDestino}";
    
    /**
    * usado para ler ficheiros JSON.
    */
    private static JSONParser jsonP;
    
    /**
    * usado para interagir com a API.
    */
    private static HTTPProvider http;
    
    /**
    * Método que verifica se existe alguma aidbox (da coleção recebida) com o código recebido
    * 
    * @param aid coleção de caixas de suprimento
    * @param code código de uma caixa de suprimento
    * @return i, posição da caixa, se a caixa for encontrada
    * -1, caso contrário
    */
    private int find_aid(AidBox[] aid, String code) {
        for (int i = 0; i < aid.length; i++) {
            if (code.equals(aid[i].getCode())) {
                return i;
            }
        }
        
        return -1;
    }    
    
    /**
    * Método que verifica se o código recebido pertence a um contentor de uma das caixas de suprimento
    * 
    * @param aid coleção de caixas de suprimento
    * @param container_find código de um contentor
    * @return i, posição do contentor, se o contentor for encontrado
    * -1, caso contrário
    */
    private int find_con(AidBox[] aid, String container_find) {
        for (AidBox aidbox: aid) {
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
    * Método que retorna o tipo de um contentor consoante o primeiro caractere do respetivo código
    * 
    * @param codigo_container código do contentor
    * @return o tipo do contentor (CLOTHING, MEDICINE, NON_PERISHABLE_FOOD, PERISHABLE_FOOD)
    */
    private ItemType type_Container(String codigo_container) {
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
    * Método que lê o URL para ir buscar as aid boxes à API, em formato JSON
    * 
    * @param inst instituição
    */
    public void getURLAidBoxes(Institution inst) {
        if (inst == null) {
            throw new NullPointerException("A instituição está nula!");
        }

        jsonP = new JSONParser();
        http = new HTTPProvider();

        String aidBoxesHttp = http.getFromURL(url_aidBoxes);
        if (!(aidBoxesHttp.equals("null"))) {
            try {
                Object obj = this.jsonP.parse(aidBoxesHttp);
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

                        ContainerImp temp_container = new ContainerImp(codigo_cont, this.type_Container(codigo_cont) ,capacity);
                        container[cont_containers++] = temp_container;
                    }

                    GeographicCoordinatesImp geo = new GeographicCoordinatesImp(latitude, longitude);

                    AidBoxImp aid = new AidBoxImp(codigo, zona, geo);

                    for (int i = 0; i < cont_containers; i++) {
                        try {
                            aid.addContainer(container[i]);
                        } catch (ContainerException ex) {
                            System.out.println("Exceção:" + ex.getMessage());
                        }
                    }

                    try {
                        inst.addAidBox(aid);
                    } catch (AidBoxException ex) {
                        System.out.println("Exceção:" + ex.getMessage());
                    }
                }
            } catch (ParseException ex) {
                System.out.println("Exceção:" + ex.getMessage());
            }
        } else {
            throw new NullPointerException("O link retorno null!");
        }   
    }
    
    /**
    * Método que lê o URL para ir buscar uma aid box específica pelo código à API (em formato JSON)
    * 
    * @param inst instituição
    * @param codigo código da aid box
    */
    public void getURLOneAid(Institution inst, String codigo) {        
        if (codigo == null || inst == null) {
            throw new NullPointerException("Um ou mais dos paramentros recebidos está null!");
        }
        
        if (find_aid(inst.getAidBoxes(), codigo) != -1) {
            throw new IndexOutOfBoundsException("O código introduzido já pertence à instituição!");
        }
        
        jsonP = new JSONParser();
        http = new HTTPProvider();
        String aidHTTP = http.getFromURL(url_cod_container.replace("{codigo}", codigo));
        
        if (!(aidHTTP.equals("null"))) {  
            try {
                Object obj = jsonP.parse(aidHTTP);
                JSONObject readingObj = (JSONObject) obj;
                String codigo_aid = (String) readingObj.get("Codigo");
                String zona_aid = (String) readingObj.get("Zona");
                double latitude_aid = (double) readingObj.get("Latitude");
                double longitude_aid = (double) readingObj.get("Longitude");

                JSONArray contentor_JSON = (JSONArray) readingObj.get("Contentores");

                int cont_containers = 0;
                ContainerImp[] container = new ContainerImp[contentor_JSON.size()];

                for (Object cont_obj : contentor_JSON) {
                    JSONObject readingObjConte = (JSONObject) cont_obj;
                    String codigo_cont = (String) readingObjConte.get("codigo");

                    long capacity = (long) readingObjConte.get("capacidade");

                    ContainerImp temp_container = new ContainerImp(codigo_cont, this.type_Container(codigo_cont) ,capacity);
                    container[cont_containers++] = temp_container;
                }

                GeographicCoordinatesImp geo_aid = new GeographicCoordinatesImp(latitude_aid, longitude_aid);

                AidBoxImp aid = new AidBoxImp(codigo_aid, zona_aid, geo_aid);

                for (int i = 0; i < cont_containers; i++) {
                    try {
                        aid.addContainer(container[i]);
                    } catch (ContainerException ex) {
                        System.out.println("Exceção:" + ex.getMessage());
                    }
                }

                try {
                    inst.addAidBox(aid);
                } catch (AidBoxException ex) {
                    System.out.println("Exceção:" + ex.getMessage());
                }

            } catch (ParseException ex) {
                System.out.println("Exceção:" + ex.getMessage());
            }
        } else {
            throw new NullPointerException("O código da aid box introduzida é nulo!");
        }
         
    } 
    
    /**
    * Método que lê o URL para ir buscar as medições dos contentores à API, em formato JSON
    * 
    * @param inst instituição
    */
    public void urlReadings(Institution inst) {
        if (inst == null) {
            throw new NullPointerException("A instituição está nula!");
        }
        
        try {
            jsonP = new JSONParser();
            http = new HTTPProvider();
            String readingsHttp = http.getFromURL(url_readings);
            if (!(readingsHttp.equals("null"))) {
                Object obj = jsonP.parse(readingsHttp);
                JSONArray readingsArray = (JSONArray) obj;
                for (Object o: readingsArray) {
                    JSONObject readingObj = (JSONObject) o;

                    String contentor_code = (String) readingObj.get("contentor");
                    String data_string = (String) readingObj.get("data");

                    Instant instant = Instant.parse(data_string);
                    LocalDateTime data = instant.atOffset(ZoneOffset.UTC).toLocalDateTime();

                    long valor = (long) readingObj.get("valor");

                    MeasurementImp mean_temp = new MeasurementImp(data, valor);
                    ContainerImp conatier_temp = new ContainerImp(contentor_code, this.type_Container(contentor_code), valor);

                    int pos_container = this.find_con(inst.getAidBoxes(), contentor_code);

                    if (pos_container != -1) {          
                        try {
                            inst.addMeasurement(mean_temp, conatier_temp);
                        } catch (ContainerException | MeasurementException ex) {
                            System.out.println("Exceção:" + ex.getMessage());
                        }
                    }
                    else {
                        System.out.println("O container lido não existe em nenhuma aid box da instituição!");
                    }
                }
            } else {
                throw new NullPointerException("O link retornou null!");
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
    * Método que lê o URL para ir buscar a distância entre duas aid boxes específicas à API, em formato JSON
    * 
    * @param inst instituição
    * @param code_origem, codigo da aidBox de destino 
    * @param cod_destino, codigo da aidBox destino
    *
    * @return true, se o url existe e consigui ler e guardar toda a informação na instituição. Retorna false se o url não existir ou se não conseguir guardar os dados na instituição 
    */
    public boolean getOneDistance(Institution inst, String code_origem, String cod_destino) {
        if (inst == null || code_origem == null || cod_destino == null) {
            throw new NullPointerException("Um ou mais dos paramentros recebidos está mal!");
        }
        
        if ((find_aid(inst.getAidBoxes(), code_origem) == -1) || (find_aid(inst.getAidBoxes(), cod_destino) == -1 && !(cod_destino.equals("Base")))) {
            throw new IndexOutOfBoundsException("O codigo introduzido não pertence há instituição!");
        }

        jsonP = new JSONParser();
        http = new HTTPProvider();

        String distancehttp = http.getFromURL(url_distance.replace("{codigoOrigem}", code_origem).replace("{codigoDestino}", cod_destino));
        if (!(distancehttp.equals("null")))  {
            try {
                Object obj = jsonP.parse(distancehttp);
                JSONObject readingObj = (JSONObject) obj;
                JSONArray contentores_JSON = (JSONArray) readingObj.get("to");

                for (Object cont_obj : contentores_JSON) {
                    JSONObject readingObjDistance = (JSONObject) cont_obj;
                    String aidbox_cod = (String) readingObjDistance.get("name");
                    long distance_c = (long) readingObjDistance.get("distance");
                    long duration = (long) readingObjDistance.get("duration");

                    
                    if (cod_destino.equals("Base")) {
                        Distance dist_base = new Distance(code_origem, distance_c, duration);
                        if (inst instanceof InstitutionImp) {
                            ((InstitutionImp) inst).addDistance(dist_base);
                        }
                    }
                        
                    if (inst instanceof InstitutionImp) {
                        Distance dist_temp = new Distance(aidbox_cod, distance_c, duration);
                        return ((InstitutionImp) inst).addDistanceAid(code_origem, dist_temp);
                    } else {
                        return false;
                    }
                }
            } catch (ParseException ex) {
                System.out.println("Exceção: " + ex.getMessage());
            } 
        } else {
            throw new NullPointerException("O link retornou null!");
        }
        return false;
    }
    
    /**
    * Método que lê o URL para ir buscar as distâncias entre todas as aid boxes
    * (e também de cada uma delas à base) à API, em formato JSON
    * (a base é a instituição)
    * 
    * @param inst instituição
    */
    public void getDistances(Institution inst) {       
        if (inst == null) {
            throw new NullPointerException("A insitituição introduzida é nula");
        }
        
        for (AidBox from: inst.getAidBoxes()) {
            for (AidBox to: inst.getAidBoxes()) {
                try {
                    this.getOneDistance(inst, from.getCode(), to.getCode());
                } catch (NullPointerException | IndexOutOfBoundsException ex) {
                    System.out.println("Exceção:" + ex.getMessage());
                }
            }
        }
        
        for (AidBox to: inst.getAidBoxes()) {
            try {
                this.getOneDistance(inst, to.getCode(), "Base");
            } catch (NullPointerException | IndexOutOfBoundsException ex) {
                System.out.println("Exceção: " + ex.getMessage());
            }
        }
    }
   
    /**
    * Método que lê o URL para ir buscar os ficheiros que contêm a informação toda
    * (exceto aqueles que precisam de um ou dois códigos específicos) à API, em formato JSON
    * 
    * @param inst instituição
    */
    public void getAllURL(Institution inst) {
        if (inst == null) {
            throw new NullPointerException("A instituição introduzida é nula!");
        }
        
        this.getURLAidBoxes(inst);
        this.urlReadings(inst);
        this.getDistances(inst);
    }
}
