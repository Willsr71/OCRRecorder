package sr.will.multiocrrecorder.config;


import java.awt.*;
import java.io.Serializable;
import java.util.List;

public class Config implements Serializable {
    public Threading threading = new Threading();
    public List<Section> sections = List.of(
            new Section("temperature", 10, new Rectangle(1170, 620, 180, 80)),
            new Section("voltage", 1000, new Rectangle(730, 630, 185, 80))
    );

    public static class Threading {
        public int miscThreads = 4;
        public int scheduledThreads = 4;
    }

    public String imageFolder = "Y:\\ocr";
    public String output = "images";

    public static class Section {
        public String name;
        public double divisor;
        public Rectangle rectangle;

        public Section(String name, double divisor, Rectangle rectangle) {
            this.name = name;
            this.divisor = divisor;
            this.rectangle = rectangle;
        }
    }
}
