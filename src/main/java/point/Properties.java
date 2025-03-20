package point;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Properties {
    public String forecast;
    public String forecastHourly;
    public RelativeLocation relativeLocation;
}
