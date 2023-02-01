package se.WeatherForCast.awspring;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.WeatherForCast.awspring.configuration.AppConfig;
import se.WeatherForCast.awspring.configuration.Weather;


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
        String text = appConfig.getTextMessage().get();
        System.out.println(text);
        Assertions.assertNotNull(text);
    }

    @Test
    public void testConfig() {
        String config = weather.toString();
        Assertions.assertNotNull(config);
    }

//qwerty only for testing
    @Test
    public void testConsumer() {
        appConfig.consumer().accept("Sara");
    }
}
