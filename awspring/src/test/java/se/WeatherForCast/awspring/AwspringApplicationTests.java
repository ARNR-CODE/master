package se.WeatherForCast.awspring;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.WeatherForCast.awspring.configuration.AppConfig;
import se.WeatherForCast.awspring.configuration.Weather;

import java.util.Map;


@SpringBootTest
class AwspringApplicationTests {
    @Autowired
    AppConfig appConfig;
    @Autowired
    Weather weather;

    @Test
    void contextLoads() {
    }

    @Test
    public void testTextMessage() {
        Map<String, String> weatherData = appConfig.getTextMessage().apply("");
        System.out.println(weatherData.entrySet());
        Assertions.assertFalse(weatherData.entrySet().isEmpty());
    }

    @Test
    public void testConfig() {
        String config = weather.toString();
        Assertions.assertNotNull(config);
    }
}
