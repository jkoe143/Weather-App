package hourlyWeather;

import java.util.ArrayList;
import java.util.Date;

public class hourlyProperties {
    public String units;
    public String forecastGenerator;
    public Date generatedAt;
    public Date updateTime;
    public String validTimes;
    public hourlyElevation elevation;
    public ArrayList<hourlyPeriod> periods;
}