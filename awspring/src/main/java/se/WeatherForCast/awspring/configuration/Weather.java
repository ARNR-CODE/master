package se.WeatherForCast.awspring.configuration;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author Athmar Mubark  <p>athmar.mubark@yahoo.com<p/>
 * @version 1.0
 * @since 23/01-25
 * Container class to get all configuration from application.yaml
 */

@Component
@PropertySource("classpath:application.yaml")
@ConfigurationProperties("weather")
public class Weather {

    private String apikey;
    private String base;
    private String search;
    private String daily;
    private String query;

    public Weather() {
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getDaily() {
        return daily;
    }

    public void setDaily(String daily) {
        this.daily = daily;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "\n apikey='" + apikey + '\'' +
                ", \n base='" + base + '\'' +
                ",\n search='" + search + '\'' +
                ",\n daily='" + daily + '\'' +
                ",\n query='" + query + '\'' +
                "}\n";
    }
}
