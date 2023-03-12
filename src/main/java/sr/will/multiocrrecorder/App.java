package sr.will.multiocrrecorder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sr.will.multiocrrecorder.config.Config;
import sr.will.multiocrrecorder.util.FileUtil;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class App {
    public static final Logger LOGGER = LoggerFactory.getLogger("OCR");
    public static final Gson GSON = new GsonBuilder().create();

    public static App instance;
    public static Config config;
    public static ThreadPoolExecutor miscExecutor;
    public static ScheduledThreadPoolExecutor scheduledExecutor;
    private final ResultsAnalyzer resultsAnalyzer;

    public App() throws IOException {
        instance = this;
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));

        reload();

        ImageScanner imageScanner = new ImageScanner();
        resultsAnalyzer = new ResultsAnalyzer();

        miscExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(config.threading.miscThreads);
        scheduledExecutor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(config.threading.scheduledThreads);
        scheduledExecutor.scheduleAtFixedRate(imageScanner::run, 0, 1, TimeUnit.SECONDS);

        LOGGER.info("Done after {}ms!", System.currentTimeMillis() - ManagementFactory.getRuntimeMXBean().getStartTime());
    }

    public void stop() {
        LOGGER.info("Stopping!");

        LOGGER.info("Done");
    }

    public void reload() {
        config = FileUtil.getConfig();
        try {
            FileUtil.saveConfig(config);
        } catch (RuntimeException e) {
            LOGGER.error("Configuration error: {}", e.getMessage());
            FileUtil.saveConfig(config);
            stop();
        }
    }

    public void addResult(ImageResult result) {
        resultsAnalyzer.addResult(result);
    }
}
