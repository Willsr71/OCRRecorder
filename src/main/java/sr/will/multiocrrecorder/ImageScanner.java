package sr.will.multiocrrecorder;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import sr.will.multiocrrecorder.config.Config;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ImageScanner {
    private int lastImage = 0;
    private final File folder;
    ITesseract tesseract;

    public ImageScanner() {
        folder = new File(App.config.imageFolder);

        tesseract = new Tesseract();
        tesseract.setLanguage("ssd_int");
        tesseract.setPageSegMode(6);
        tesseract.setConfigs(List.of("digits"));
        tesseract.setVariable("classify_bln_numeric_mode", "1");
    }

    public void run() {
        try {
            scan();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void scan() throws IOException, TesseractException {
        String[] files = folder.list(this::accept);

        for (String name : files) {
            File file = new File(folder, name);
            ImageResult result = new ImageResult(file.lastModified(), name);

            BufferedImage image = ImageIO.read(file);
            for (Config.Section section : App.config.sections) {
                result.addValue(section, processSection(image, section));
            }
            App.instance.addResult(result);
            ImageIO.write(image, "JPG", new File(App.config.output, name));
        }
    }

    public String processSection(BufferedImage image, Config.Section section) throws TesseractException {
        Graphics2D graphics = image.createGraphics();

        String result = tesseract.doOCR(image, section.rectangle);

        graphics.setColor(Color.RED);
        graphics.draw(section.rectangle);
        graphics.setFont(Font.getFont(Font.SANS_SERIF));
        graphics.drawString(result, section.rectangle.x, section.rectangle.y + section.rectangle.height);
        graphics.dispose();

        return result;
    }

    private boolean accept(File dir, String name) {
        String[] parts = name.split("\\.");
        if (parts.length != 3) return false;
        int num;
        try {
            num = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            return false;
        }

        if (num > lastImage) {
            lastImage = num;
            return true;
        } else {
            return false;
        }
    }
}
