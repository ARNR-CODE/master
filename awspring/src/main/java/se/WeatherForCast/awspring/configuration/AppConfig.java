package se.WeatherForCast.awspring.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import se.WeatherForCast.awspring.model.AccuweatherApi;
import se.WeatherForCast.awspring.service.WeatherServices;


import java.util.Map;
import java.util.function.Function;
/**
 * @author Athmar Mubark  <p>athmar.mubark@yahoo.com<p/>
 * @version 1.0
 * @since 23/01-25
 * Application configuration
 */

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


    /*This function will be run in AWS lambda and send output back to SNS service */
    @Bean
    public Function<String, Map<String, String>> getTextMessage() {
        //We will do nothing with input string! because Request handler is
        // only working with function not supplier interface!
        return (input) -> weatherServices.getWeatherToday();
    }
}
