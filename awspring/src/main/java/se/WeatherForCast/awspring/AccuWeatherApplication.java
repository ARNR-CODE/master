package se.WeatherForCast.awspring;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import se.WeatherForCast.awspring.configuration.AppConfig;
import se.WeatherForCast.awspring.service.WeatherServices;

import java.util.function.Supplier;

/**
 * @author: Athmar Mubark  <p>athmar.mubark@yahoo.com<p/>
 * @version: 1.0
 * @since: 23/01-25
 * Serverless application to get daily weather forecast from AccuWeather Api
 */

@SpringBootApplication
public class AccuWeatherApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccuWeatherApplication.class, args);
    }
}
