package vttp.weather.weather.controller;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vttp.weather.weather.models.Weather;
import vttp.weather.weather.service.WeatherSvc;

@Controller //no rest controller cos thymeleaf
@RequestMapping(path="/weather") //why requestmapping and not getmapping or sth?
public class WeatherController {

    @Autowired //have to autowire the service (how come?)
    private WeatherSvc weatherSvc;

    @GetMapping
    public String getWeather(@RequestParam(required = true) String city, Model model){ //what is request param?
        Optional<Weather> opt = weatherSvc.getWeather(city);
        if (opt.isEmpty()) {
            return "weather";
        }
        model.addAttribute("weather", opt.get());
        return "weather";
    }
    
}
