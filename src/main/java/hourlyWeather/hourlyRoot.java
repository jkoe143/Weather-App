package hourlyWeather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class hourlyRoot {
    public String type;
    public hourlyGeometry geometry;
    public hourlyProperties properties;
}