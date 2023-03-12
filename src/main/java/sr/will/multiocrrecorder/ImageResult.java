package sr.will.multiocrrecorder;

import sr.will.multiocrrecorder.config.Config;

import java.util.HashMap;
import java.util.Map;

public class ImageResult {
    public final long timestamp;
    public final String file;
    public boolean valid = true;
    public final Map<String, Integer> values = new HashMap<>();
    public final Map<String, String> rawValues = new HashMap<>();

    public ImageResult(long timestamp, String file) {
        this.timestamp = timestamp;
        this.file = file;
    }

    public void addValue(Config.Section section, String value) {
        value = value.replace("\n", "");

        rawValues.put(section.name, value);
        try {
            values.put(section.name, Integer.parseInt(value));
        } catch (NumberFormatException e) {
            valid = false;
        }
    }

    public String getAllLine() {
        StringBuilder line = getBaseString();
        line.append(valid).append(",");
        for (Config.Section section : App.config.sections) {
            line.append(rawValues.get(section.name)).append(",");
        }
        return endLine(line);
    }

    public String getValidLine() {
        if (!valid) return null;
        StringBuilder line = getBaseString();
        for (Config.Section section : App.config.sections) {
            double realValue = values.get(section.name);
            realValue = realValue / section.divisor;
            line.append(realValue).append(",");
        }
        return endLine(line);
    }

    private StringBuilder getBaseString() {
        return new StringBuilder()
                .append(timestamp).append(",")
                .append(file).append(",");
    }

    private String endLine(StringBuilder line) {
        return line.deleteCharAt(line.length() - 1)
                .append("\n")
                .toString();
    }
}
