package utils;

public class ForecastIconMatcher {
    public static String matchShortForecastToIcon(String shortForecast, boolean isDay) {
        if(shortForecast == null || shortForecast.trim().isEmpty())
            return isDay ? "Sunny.png" : "Clear-Night.png";

        String forecast = shortForecast.trim().toLowerCase();

        if (forecast.contains("severe thunderstorm")) return "Severe-Thunderstorm.png";

        if (forecast.contains("thunderstorm")) return "Severe-Thunderstorm.png";

        if (forecast.contains("heavy rain")) return "Heavy-Rain.png";

        if (forecast.contains("scatter showers")) return "Scatter-Showers-Sun.png";

        if (forecast.contains("snow")) return "Snow.png";

        if (forecast.contains("partly cloudy")) {
            if (forecast.contains("night")) return "Partly-Cloudy-Night.png";

            return "Partly-Cloudy.png";
        }

        if (forecast.contains("cloudy") && forecast.contains("clear")) {
            if (forecast.contains("night") || !isDay) return "Cloudy-Clear-Night.png";

            return "Cloudy-Clear-Sun.png";
        }

        if (forecast.contains("cloudy")) return "Cloudy.png";

        if (forecast.contains("humidity")) return "Humidity.png";

        if (forecast.contains("sunny")) return "Sunny.png";

        if (forecast.contains("clear") && forecast.contains("night")) return "Clear-Night.png";

        if (forecast.contains("clear")) return isDay ? "Sunny.png" : "Clear-Night.png";

        if (forecast.contains("wind")) return "Wind.png";

        return isDay ? "Sunny.png" : "Clear-Night.png";
    }
}
