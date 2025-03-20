package point;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RelativeLocation {
    public String type;
    public RelativeLocationProperties properties;
}
