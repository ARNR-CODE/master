package se.WeatherForCast.awspring.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import se.WeatherForCast.awspring.model.AccuweatherApi;
import se.WeatherForCast.awspring.service.WeatherServices;


import java.util.Map;
import java.util.function.Function;
@Configuration
public class AppConfig {
 private final WeatherServices weatherServices;

    public AppConfig(WeatherServices weatherServices) {
        this.weatherServices = weatherServices;
    }

    @Bean
    public AccuweatherApi getAccuweatherApi() {
        return new AccuweatherApi(new ObjectMapper());
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public Function<String,Map<String,String>> getTextMessage() {
        return (input) -> weatherServices.getWeatherToday();
    }
}
