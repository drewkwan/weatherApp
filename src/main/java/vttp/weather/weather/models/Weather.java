package vttp.weather.weather.models;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Weather {
    private static final Logger logger = LoggerFactory.getLogger(Weather.class);
    
    private String city;
    private String temperature;
   
    //Need a lisst of condiitons
    public List<Conditions> conditions= new LinkedList<>(); 

    public List<Conditions> getConditions() {
        return conditions;
    }

    public void setConditions(List<Conditions> conditions) {
        this.conditions = conditions;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    
    
    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }


    //addCondition method
    public void addCondition(String description, String icon) {
        Conditions c = new Conditions();
        c.setDescription(description);
        c.setIcon(icon);
        conditions.add(c);
    }

    //create method
    public static Weather create(String json) throws IOException {
        Weather w = new Weather();
        try (InputStream is = new ByteArrayInputStream(json.getBytes())) {
            //create JsonReader that reads teh input stream
            JsonReader r = Json.createReader(is);
            //create Json Object then can get the String after that
            JsonObject o = r.readObject();
            logger.info(o.toString());
            w.city = o.getString("name");
            logger.info("city name > " + w.getCity()); //why not just.city
            JsonObject mainObj = o.getJsonObject("main");
            logger.info("mainObj" + mainObj.toString());
            w.temperature= mainObj.getJsonNumber("temp").toString();
            w.conditions = o.getJsonArray("weather").stream()
                            .map(v -> (JsonObject)v)
                            .map(v -> Conditions.createJson(v))
                            .toList();
        }
        return w;
    }


}
