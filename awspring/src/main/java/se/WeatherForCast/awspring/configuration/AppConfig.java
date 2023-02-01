package se.WeatherForCast.awspring.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import se.WeatherForCast.awspring.model.AccuweatherApi;
import se.WeatherForCast.awspring.service.WeatherServices;

import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Supplier;

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
    public Supplier<String> getTextMessage() {
        return () -> weatherServices.getWeatherToday();
    }


    @Bean
    public Consumer<String> consumer () {
        return (input) -> {
            System.out.println(input.toUpperCase(Locale.ROOT));
        };
        //return System.out::println;
    }

}
