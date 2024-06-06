package main;

import java.util.ArrayList;

import blobDetection.Blob;
import blobDetection.BlobDetection;
import blobDetection.EdgeVertex;
import igeo.ICurve;
import igeo.IG;
import igeo.IVec;
import processing.core.PApplet;

/**
 * compute blobs of the PGraphic
 * @author zhangbz
 *
 */
public class BlobManager {
    BlobDetection bd;
    ArrayList<ArrayList<EdgeVertex>> startAll;
    ArrayList<ArrayList<EdgeVertex>> endAll;
    private float threshold = 0.4f;

    public BlobManager(PApplet app) {
        bd = new BlobDetection(app.width, app.height);
        bd.setPosDiscrimination(false);
        bd.setThreshold(threshold);
    }

    /**
     * record vertices of each segment
     */
    public void recordEdgeVertex() {
        startAll = new ArrayList<ArrayList<EdgeVertex>>();
        endAll = new ArrayList<ArrayList<EdgeVertex>>();
        Blob b;
        EdgeVertex eA, eB;

        for (int n = 0; n < bd.getBlobNb(); n++) {
            b = bd.getBlob(n);
            ArrayList<EdgeVertex> currStart = new ArrayList<EdgeVertex>();
            ArrayList<EdgeVertex> currEnd = new ArrayList<EdgeVertex>();
            for (int m = 0; m < b.getEdgeNb(); m++) {
                eA = b.getEdgeVertexA(m);
                eB = b.getEdgeVertexB(m);
                if (eA != null && eB != null) {
                    currStart.add(eA);
                    currEnd.add(eB);
                }
            }
            startAll.add(currStart);
            endAll.add(currEnd);
        }
    }

    /**
     * save blob edges to local .3dm file
     * @param app
     */
    public void saveEdgesTo3dm(PApplet app, String path) {
        IG.init();
        for (ArrayList<EdgeVertex> currStart : startAll) {
            IVec[] edgeVertexList = new IVec[currStart.size()];
            for (int i = 0; i < currStart.size(); i++) {
                edgeVertexList[i] = new IVec(currStart.get(i).x * app.width, currStart.get(i).y * app.height);
            }
            new ICurve(edgeVertexList, true).layer("edges");
        }
        System.out.println(startAll.size());
        IG.save(path);
    }

    /**
     * draw edges
     * @param app
     */
    public void displayEdges(PApplet app) {
        app.noFill();

        Blob b;
        EdgeVertex eA, eB;
        for (int n = 0; n < bd.getBlobNb(); n++) {
            b = bd.getBlob(n);
            app.strokeWeight(1);
            app.stroke(255, 50, 50);
            for (int m = 0; m < b.getEdgeNb(); m++) {
                eA = b.getEdgeVertexA(m);
                eB = b.getEdgeVertexB(m);
                if (eA != null && eB != null) {
                    app.line(eA.x * app.width, eA.y * app.height, eB.x * app.width, eB.y * app.height);
                }
            }
        }
    }

}
