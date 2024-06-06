package main;

import java.util.ArrayList;

import igeo.ICurve;
import igeo.IG;
import processing.core.PApplet;
import processing.core.PImage;
import wblut.geom.WB_GeometryFactory;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;

public class FileLoader {
    private static final int height = 600;
    private static final int width = 600;

    public PImage site;
    public ArrayList<Float> pixGrey;

    private WB_GeometryFactory gf;
    public ArrayList<WB_Point> pts;
    public WB_Polygon boundary;

    public FileLoader(PApplet app) {
        site = app.loadImage("./src/main/resources/0712site_.jpg");

        pixGrey = new ArrayList<Float>();
        getGrey(app);
    }

    private void getGrey(PApplet app) {
        site.loadPixels();
        app.loadPixels();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int loc = (int) (x + y * width);
                // The functions red(), green(), and blue() pull out the three color components
                // from a pixel.
                float r = app.red(site.pixels[loc]);
                pixGrey.add(r);
                // Image Processing would go here
                // If we were to change the RGB values, we would do it here, before setting the
                // pixel in the display window.
            }
        }
        System.out.println(pixGrey.get(120100));
    }

}
