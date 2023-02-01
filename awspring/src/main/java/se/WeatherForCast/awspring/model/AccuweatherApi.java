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
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author: Athmar Mubark  <p>athmar.mubark@yahoo.com<p/>
 * @version: 1.0
 * @since: 23/01-25
 * <br>
 * <p>Making first api call to get location key and then the second call to get daily forcast and formatted to sting</p>
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
    private String getKey(Weather weather, HttpEntity<Object> entity, HashMap<String, String> params) throws JsonProcessingException {
        String urlTemplate = uriTemplate(locationUri(weather));
        String json = getResponse(entity, urlTemplate, params).getBody();
        return getKeyFromJson(json, weather.getQuery(), ApiConstants.KEY);
    }


    /**
     * this method will send the request and get the response from accWeather api
     * <br>
     *
     * @param entity      Adding api key authorization as a query params before sending request
     * @param urlTemplate Uri template
     * @param params      saving api key
     * @return Represents an HTTP response entity, consisting of headers and body.
     */
    private HttpEntity<String> getResponse(HttpEntity<Object> entity, String urlTemplate, HashMap<String, String> params) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(urlTemplate, HttpMethod.GET, entity, String.class, params);
    }


    /**
     * this method will send the request and get the response from accWeather api
     * <br>
     *
     * @param json  json data from the first api call
     * @param value location name t.ex Stockhom or any other city
     * @param key   required field name in json.
     * @return location key will be returned as a string.
     */
    private String getKeyFromJson(String json, String value, String key) throws JsonProcessingException {
        AtomicReference<Object> cache = new AtomicReference<>();
        JsonNode jsonNode = objectMapper.readTree(json);
        jsonNode.forEach(node -> {
            JsonNode locatedNode = node.path(ApiConstants.ADMINISTRATIVE_AREA).path(ApiConstants.Localized_Name);
            if (locatedNode.asText().equalsIgnoreCase(value)) {
                cache.set(node.findValuesAsText(key));
            }
        });
        if (cache != null) return cache.get()
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

    public String callApi(Weather weather) throws JsonProcessingException {
        HttpEntity<Object> entity = getEnitiy();
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiConstants.API_KEY, weather.getApikey());
        this.key = getKey(weather, entity, params);
        String dailyForCast = getForcast(weather, entity, params);
        return dailyForCast;
    }

    //Adding api key authorization as a query params before sending request
    private HttpEntity<Object> getEnitiy() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        return new HttpEntity<>(headers);
    }


    //Get final uri template
    private String uriTemplate(String url) {
        String urlTemplate = UriComponentsBuilder.fromHttpUrl(url).queryParam(ApiConstants.API_KEY, "{apikey}").encode().toUriString();
        return urlTemplate;
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
    private String getForcast(Weather weather, HttpEntity<Object> entity, HashMap<String, String> params) throws JsonProcessingException {
        String urlDailyCast = buildSimpleUri(weather.getBase(), weather.getDaily(), key);
        String result = getResponse(entity, uriTemplate(urlDailyCast), params).getBody();
        return buildTextMessage(weather, result);
    }


    /**
     * making all required api call to get weather forcast from AccuWeather api
     * <br>
     *
     * @param weather Get all required configuration from properties file
     * @param result  json data
     * @return String final text message that will be shown to the end user
     */
    private String buildTextMessage(Weather weather, String result) throws JsonProcessingException {
        HashMap<String, String> messages = new HashMap<>();
        return putMessageInMap(weather, result, messages);
    }

    private String putMessageInMap(Weather weather, String result, HashMap<String, String> messages) throws JsonProcessingException {
       StringBuilder finalText = new StringBuilder();
        finalText.append(ApiConstants.DATE +" : " + retrieveValueFromJson(result, ApiConstants.DAILY_FORECASTS, ApiConstants.DATE) +"\n");
        finalText.append(ApiConstants.TEMPERATURE
                +" : "
                + retrieveValueFromJson(result, ApiConstants.DAILY_FORECASTS, ApiConstants.TEMPERATURE, ApiConstants.MINIMUM, ApiConstants.VALUE)
                + " - "
                + retrieveValueFromJson(result, ApiConstants.DAILY_FORECASTS, ApiConstants.TEMPERATURE, ApiConstants.MAXIMUM, ApiConstants.VALUE)
                + " " + retrieveValueFromJson(result, ApiConstants.DAILY_FORECASTS, ApiConstants.TEMPERATURE, ApiConstants.MINIMUM, ApiConstants.UNIT)
                + " \n");
        finalText.append(ApiConstants.DAY +" : " +retrieveValueFromJson(result, ApiConstants.DAILY_FORECASTS, ApiConstants.DAY, ApiConstants.ICONPHRASE) +"\n");
        finalText.append(ApiConstants.NIGHT +" : "+retrieveValueFromJson(result, ApiConstants.DAILY_FORECASTS, ApiConstants.DAY, ApiConstants.ICONPHRASE) + "\n");
        finalText.append(ApiConstants.LOCATION +" : "+ weather.getQuery() +"\n");
        return finalText.toString();
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