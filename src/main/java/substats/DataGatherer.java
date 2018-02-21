/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package substats;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.net.URI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 *
 * @author Keith
 */
public class DataGatherer {
    
    Client client;
    WebTarget freqWebTarget;
    Gson gson;
    URI frequencyUri;
    
    
    public DataGatherer(URI uri) {
        gson = new Gson();
        frequencyUri = uri;
    }
    
    
    public void setupClient() {
        //Set up the client
        client = ClientBuilder.newClient();
        
        //Create WebTarget for the service to query
        freqWebTarget = client.target(frequencyUri);
    }
    
    public PlatformData getPlatformData(String name) {
        List<String> queries = new ArrayList<>();
        queries.add(name);

        for (String s : queries) {
            //Add on the specific resource we want to target, deriving from original target
            WebTarget platformTarget = freqWebTarget.path(s);

            //Build an invication to send the actual GET command
            Invocation.Builder invocationBuilder = platformTarget.request(MediaType.APPLICATION_JSON);
            invocationBuilder.header("some-header", "true");

            //Directly query and get my response (Synchronously)
            long start = System.currentTimeMillis();
            Response response = invocationBuilder.get();
            long end = System.currentTimeMillis();
            System.out.println(response.getStatus());
            String json = response.readEntity(String.class);
            System.out.println(json);
            System.out.println("Time (ms): " + (end - start));
            
            Map<String, String> result = new Gson().fromJson(json, Map.class);
            
            JsonObject jsonRoot = gson.fromJson(json, JsonObject.class);
            
            
            String nameResult = jsonRoot.get("platformClass").getAsString();
            String typeResult = jsonRoot.get("type").getAsString();
            List<String> countryResult = gson.fromJson(jsonRoot.get("countries"), ArrayList.class);
            List<Integer> freqsResult = gson.fromJson(jsonRoot.get("frequencies"), new TypeToken<ArrayList<Integer>>(){}.getType());
            int bladesResult = jsonRoot.get("numBlades").getAsInt();
            int tpkResult = jsonRoot.get("turnsPerKnot").getAsInt();
            
            
            PlatformData p = new PlatformData(nameResult,
                                            typeResult,
                                            countryResult,
                                            freqsResult,
                                            bladesResult,
                                            tpkResult);
            return p;
        }
        return null;
    }
    
}
