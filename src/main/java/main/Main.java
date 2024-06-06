package main;


import processing.core.PApplet;
import processing.core.PGraphics;

public class Main extends PApplet {
    DiffusionManager dm;
    BlobManager bm;
    //	FileLoader fl;

    PGraphics img;

    boolean showBlobEdge = false;
    boolean showSiteImage = false;

    public static void main(String[] args) {
        PApplet.main(Main.class.getName());
    }

    public void settings() {
        size(600, 600, P2D);
    }

    public void setup() {
        frameRate(25);
        smooth();
        colorMode(HSB, 1.0f);

        dm = new DiffusionManager(this);
//		fl = new FileLoader(this);

        bm = new BlobManager(this);
    }

    public void draw() {
        if (showBlobEdge == true) {
            dm.displaySite(this);
        }

        for (int k = 0; k < 15; k++) {
            dm.timestep();
        }
        dm.display(this);

        if (showBlobEdge == true) {
            bm.displayEdges(this);
        }
    }

    public void keyPressed() {
        if (key == 'c') {
            img = createGraphics(width, height);
            createBlobGraphics(this);
            bm.bd.computeBlobs(img.pixels);

            showBlobEdge = !showBlobEdge;
        }

        if (key == 'r') {
            showSiteImage = !showSiteImage;
        }

        if (key == 's') {
            bm.recordEdgeVertex();
            String path = "./src/main/resources/save.3dm";
            bm.saveEdgesTo3dm(this, path);
        }
    }

    private void createBlobGraphics(PApplet app) {
        img.beginDraw();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                img.set(i, j, color(0f, 0f, (float) (1 - dm.U[i][j])));
            }
        }
        img.endDraw();
    }
}
