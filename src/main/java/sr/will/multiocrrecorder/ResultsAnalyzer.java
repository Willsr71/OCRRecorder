package sr.will.multiocrrecorder;

import sr.will.multiocrrecorder.config.Config;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResultsAnalyzer {
    private final List<ImageResult> results = new ArrayList<>();

    private final FileWriter allResults;
    private final FileWriter validResults;

    public ResultsAnalyzer() throws IOException {
        allResults = new FileWriter("results-all.csv", false);
        validResults = new FileWriter("results-valid.csv", false);
        StringBuilder line = new StringBuilder("timestamp,image,");
        for (Config.Section section : App.config.sections) {
            line.append(section.name).append(",");
        }
        line.deleteCharAt(line.length() - 1);
        line.append("\n");
        validResults.write(line.toString());
        validResults.flush();

        line.insert(16, "valid,");
        allResults.write(line.toString());
        allResults.flush();
    }

    public void addResult(ImageResult result) {
        if (result.valid) analyzeCurrent(result);
        if (result.valid) results.add(result);

        writeResult(result);
    }

    private void analyzeCurrent(ImageResult result) {
        ImageResult lastValid = getLastValid();
        if (lastValid == null) return;

        for (Config.Section section : App.config.sections) {
            if (!inBounds(lastValid.values.get(section.name), result.values.get(section.name))) {
                result.valid = false;
                return;
            }
        }
    }

    private ImageResult getLastValid() {
        for (int i = results.size() - 1; i >= 0; i--) {
            if (results.get(i).valid) return results.get(i);
        }
        return null;
    }

    private boolean inBounds(int validValue, int testValue) {
        double difference = Math.abs(validValue - testValue);
        return difference < 1000;
    }

    private void writeResult(ImageResult result) {
        try {
            allResults.write(result.getAllLine());
            allResults.flush();

            if (result.valid) {
                validResults.write(result.getValidLine());
                validResults.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
