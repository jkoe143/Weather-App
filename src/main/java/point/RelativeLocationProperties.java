package point;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RelativeLocationProperties {
    public String city;
    public String state;
}
