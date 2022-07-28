package vttp.weather.weather.service;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import vttp.weather.weather.models.Weather;


@Service //need to annotate your service classes
public class WeatherSvc {
    private static final Logger logger = LoggerFactory.getLogger(WeatherSvc.class);
    private static String URL ="https://api.openweathermap.org/data/2.5/weather"; //URL is all uppercase and less the query string cos youll append that later
    private boolean hasKey;

    @Value("${open.weather.map}") //@ value is standard but check what that means. go and key in the stuff into app properties
    private String apiKey;

    @PostConstruct //what is this for? this comes before the contructor
    private void init() {
        hasKey = null != apiKey;
        logger.info(">>>> API KEY set: " + hasKey);
        
    }

    public Optional<Weather> getWeather (String city) { //nanikore
        String weatherUrl = UriComponentsBuilder.fromUriString(URL) //append the query param
                            .queryParam("q", city.replaceAll(" ", "+"))
                            .queryParam("units", "metric")
                            .queryParam("appid", apiKey)
                            .toUriString();

        logger.info(">>>> Complete Weather URI API address: " + weatherUrl);
        //why ah what's this stuff below.
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = null;

        try {
            resp = template.getForEntity(weatherUrl, String.class);
            Weather w = Weather.create(resp.getBody());
            return Optional.of(w);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();

        }
        return Optional.empty();
    }
}


//The service is meant to unmarshall the json format that you receive from the API
//the controller will allow you to pick what you want to display on your web page