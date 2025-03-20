package point;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PointResponse {
    public String type;
    public Properties properties;
}

