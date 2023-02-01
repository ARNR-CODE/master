package se.WeatherForCast.awspring.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import se.WeatherForCast.awspring.configuration.Weather;
import se.WeatherForCast.awspring.model.AccuweatherApi;

@Service
@ComponentScan(basePackages = "configuration")
public class WeatherServices {

    @Autowired
    private Weather weather;

    public String getWeatherToday() {
        try {
            AccuweatherApi weatherApp = new AccuweatherApi(new ObjectMapper());
            return weatherApp.callApi(weather);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("OBS, internal Error occurred : " + e.getMessage());
        }
    }

}
