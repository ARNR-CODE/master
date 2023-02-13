package se.WeatherForCast.awspring.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import se.WeatherForCast.awspring.configuration.Weather;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author: Athmar Mubark  <p>athmar.mubark@yahoo.com<p/>
 * @version: 1.0
 * @since: 23/01-25
 * <br>
 * <p>Get weather forecast from AccuWeather api </p>
 * <br>
 */

@Component
public class  AccuweatherApi {
    @Autowired
    private ObjectMapper objectMapper;
    private String key;
    public AccuweatherApi( ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Get location key and saving it to key params
     * <br>
     *
     * @param weather Get all required configuration from properties file
     * @param entity  Adding api key authorization as a query params before sending request
     * @param params  saving api key
     * @return location key
     */
    private String getKey(Weather weather, HttpEntity<Object> entity, HashMap<String, String> params)
            throws JsonProcessingException {
        String urlTemplate = uriTemplate(locationUri(weather));
        String json = getResponse(entity, urlTemplate, params).getBody();
        return getKeyFromJson(json, weather, ApiConstants.KEY);
    }


    /**
     * Get the response from AccuWeather Api
     * <br>
     *
     * @param entity      Adding api key authorization as a query params before sending request
     * @param urlTemplate Uri template
     * @param params      saving api key
     * @return Represents an HTTP response entity, consisting of headers and body.
     */
    private HttpEntity<String> getResponse(HttpEntity<Object> entity, String urlTemplate, HashMap<String,
            String> params) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(urlTemplate, HttpMethod.GET, entity, String.class, params);
    }


    /**
     * Get key value from json data.
     * <br>
     *
     * @param json  json data from the first api call
     * @param weather location name t.ex Stockhom or any other city
     * @param key   required field name in json.
     * @return location key will be returned as a string.
     */
    private String getKeyFromJson(String json, Weather weather, String key) throws JsonProcessingException {
        AtomicReference<Object> cache = new AtomicReference<>();
        JsonNode jsonNode = objectMapper.readTree(json);
        jsonNode.forEach(node -> {
            JsonNode cityName = node.path(ApiConstants.ADMINISTRATIVE_AREA).path(ApiConstants.Localized_Name);
            JsonNode countryName = node.path(ApiConstants.COUNTRY).path(ApiConstants.Localized_Name);
            if (cityName.asText().equalsIgnoreCase(weather.getQuery())
                    && countryName.asText().equalsIgnoreCase(weather.getCountry()))
                cache.set(node.findValuesAsText(key));

        });
        if (cache.get() != null) return cache.get()
                .toString()
                .replace("[", "")
                .replace("]", "");
        return "";
    }

    /**
     * Building any simple uri
     * <br>
     *
     * @param values all values that needs to build uri
     * @return Uri string.
     */
    private String buildSimpleUri(String... values) {
        StringBuilder url = new StringBuilder();
        for (String val : values)
            url.append(val);
        return url.toString();
    }

    /**
     * Location url to find location key for your location
     * <br>
     *
     * @param weather Get all reuired configuration from properties file
     * @return String uri
     */
    private String locationUri(Weather weather) {
        return buildSimpleUri(weather.getBase(), weather.getSearch(), weather.getQuery());
    }


    /**
     * making all required api call to get weather forcast from AccuWeather api
     * <br>
     *
     * @param weather Get all reuired configuration from properties file
     * @return String daily forcast
     */

    public  Map<String, String> callApi(Weather weather) throws JsonProcessingException {
        HttpEntity<Object> entity = getEnitiy();
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiConstants.API_KEY, weather.getApikey());
        this.key = getKey(weather, entity, params);
        return getForcast(weather, entity, params);
    }

    //Adding api key authorization as a query params before sending request
    private HttpEntity<Object> getEnitiy() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json");
        return new HttpEntity<>(headers);
    }


    //Get final uri template
    private String uriTemplate(String url) {
        return  UriComponentsBuilder
                .fromHttpUrl(url)
                .queryParam(ApiConstants.API_KEY, "{apikey}")
                .encode().toUriString();
    }


    /**
     * making all required api call to get weather forcast from AccuWeather api
     * <br>
     *
     * @param weather Get all reuired configuration from properties file
     * @param entity  Adding api key authorization as a query params before sending request
     * @param params  Adding required params before sending api request
     * @return String json data
     */
    private  Map<String, String> getForcast(Weather weather,
                                            HttpEntity<Object> entity,
                                            HashMap<String, String> params) throws JsonProcessingException {
        String urlDailyCast = buildSimpleUri(weather.getBase(), weather.getDaily(), key);
        String response = getResponse(entity, uriTemplate(urlDailyCast), params).getBody();
        return buildTextMessage(weather, response);
    }


    /**
     * making all required api call to get weather forcast from AccuWeather api
     * <br>
     *
     * @param weather Get all required configuration from properties file
     * @param response  json data
     * @return String final text message that will be shown to the end user
     */
    private  Map<String, String> buildTextMessage(Weather weather, String response) throws JsonProcessingException {
        return putMessageInMap(weather, response);
    }

    private Map<String, String> putMessageInMap(Weather weather, String response) throws JsonProcessingException {
        Map<String, String> messages = new HashMap<>();
        //Date field
        messages.put(ApiConstants.DATE, retrieveValueFromJson(response,
                ApiConstants.DAILY_FORECASTS, ApiConstants.DATE));
        //Temperature field t.ex min - max unit
        messages.put(ApiConstants.TEMPERATURE, retrieveValueFromJson(response,
                ApiConstants.DAILY_FORECASTS, ApiConstants.TEMPERATURE, ApiConstants.MINIMUM, ApiConstants.VALUE)
                + " - " + retrieveValueFromJson(response,
                ApiConstants.DAILY_FORECASTS, ApiConstants.TEMPERATURE, ApiConstants.MAXIMUM, ApiConstants.VALUE)
                + " " + retrieveValueFromJson(response,
                ApiConstants.DAILY_FORECASTS, ApiConstants.TEMPERATURE, ApiConstants.MINIMUM, ApiConstants.UNIT));

        //Day forecast
        messages.put(ApiConstants.DAY, retrieveValueFromJson(response,
                ApiConstants.DAILY_FORECASTS, ApiConstants.DAY, ApiConstants.ICONPHRASE));
        //Night forecast
        messages.put(ApiConstants.NIGHT, retrieveValueFromJson(response,
                ApiConstants.DAILY_FORECASTS, ApiConstants.DAY, ApiConstants.ICONPHRASE));
        //city and country
        messages.put(ApiConstants.LOCATION, weather.getQuery() + " ," + weather.getCountry());
        return messages;
    }


    //Retrieve specific values from Json data
    private String retrieveValueFromJson(String result, String... fieldNames) throws JsonProcessingException {
        StringBuilder message = new StringBuilder();
        JsonNode jsonNode = objectMapper.readTree(result);
        jsonNode.forEach(node -> {
            for (String field : fieldNames) {
                JsonNode newNode = node.path(field);
                if (!node.findValuesAsText(field).isEmpty()) message.append(node.findValuesAsText(field).get(0));
            }
        });
        return message.toString();
    }
}