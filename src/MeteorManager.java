// Copyright © 2014 Esteban Valle. All rights reserved.
//
// +1-775-351-4427
// esteban@thevalledesign.com
// http://facebook.com/SeniorShizzle

import java.awt.*;
import java.awt.geom.Area;
import java.util.LinkedList;

/** The class responsible for managing and controlling meteors */
public class MeteorManager {

    /** The doubly-linked list containing the Meteor objects */
    public LinkedList<Meteor> meteors = new LinkedList<Meteor>();

    /** The instantiating object */
    private MeteorGame parent;

    /** The singleton Audio Manager */
    private MeteorAudioManager audioManager = MeteorAudioManager.getInstance();

    /** Constructs a MeteorManager with the parent of type MeteorGame */
    public MeteorManager(MeteorGame parent){
        this.parent = parent;
    }


    /** Instantiates a new Meteor object and places it in the linked list */
    public void addMeteor(double x, double y, double theta, int size){
       if (meteors.size() < 27) meteors.add(new Meteor(x, y, theta, size)); // The system will only track 26 meteors (inherited from original game design)
        //System.out.println("There are " + meteors.size() + " meteors");
    }


    /** Returns a specific meteor from the list */
    public Meteor getMeteor(Meteor meteor){
        if (meteors.contains(meteor)) return meteor;
        else return null;
    }

    public Meteor areaIntersectsWithMeteor(Shape shape){
        for (Meteor meteor : meteors) {
            Area testArea = new Area(shape);
            testArea.intersect(new Area(meteor.polygon()));  // We intersect the area of the shape and the meteor
            if (!testArea.isEmpty()) return meteor;          // If the intersection results in any residual area, we have a positive hit test
        }
        return null;
    }

    public boolean areaIntersectsWithMeteorAndDestroy(Shape shape){
        for (Meteor meteor : meteors) {
            Area testArea = new Area(shape);
            testArea.intersect(new Area(meteor.polygon()));  // We intersect the area of the shape and the meteor
            if (!testArea.isEmpty()) {
                destroy(meteor);
                return true;                                  // If the intersection results in any residual area, we have a positive hit test
            }
        }
        return false;
    }

    /** Destroys a meteor, and creates smaller meteors if applicable */
    public void destroy(Meteor meteor){

        int meteorSize = meteor.size;
        double x = meteor.x;
        double y = meteor.y;
        if (!meteors.remove(meteor)){
            System.out.println("Error removing meteor");
            return;
        }
        if (meteorSize > 1) {
           addMeteor(x, y, Math.random() * 6.28, meteorSize - 1);
           addMeteor(x, y, Math.random() * 6.28, meteorSize - 1);
           if (meteorSize == 2) {
               audioManager.explosionMedium();
           } else {
               audioManager.explosionLow();
           }

        } else {
            parent.addExplosion((int) meteor.x, (int) meteor.y);
            audioManager.explosionHigh();
        }

    }

    /** Checks to see if any meteors intersect the x and y coordinates. Returns that meteor if one intersects,
     * otherwise returns null.
     *  @return the first Meteor object which intersects with the point at ('x', 'y')
     *  @param x the x coordinate to test
     *  @param y the y coordinate to test
     *  */
    public Meteor isHit(double x, double y){
        for (Meteor meteor : meteors){
            if ((Math.pow((x-meteor.x), 2) + Math.pow((y-meteor.y), 2)) < (meteor.radius * meteor.radius)){
                System.out.println("Hit at: " + x + ", " + y + " : " + meteor.x + ", " + meteor.y + ", " + meteor.radius);
                return meteor;
            }
        }
        return null;
    }


    /**
     * Checks to see if any meteors intersect the x and y coordinates. If a meteor does intersect with those coordinates,
     * this method calls the MeteorManager.destroy(); method and automatically increments the score by calling parent.addScore(int);
     * This method will only destroy the first intersected meteor. If two meteors intersect the point, only one will be destroyed.
     * @param x the x coordinate to test
     * @param y the y coordinate to test
     * @return
     */
    public boolean isHitAndDestroy(double x, double y){
        for (Meteor meteor : meteors) {
            if ((Math.pow((x - meteor.x), 2) + Math.pow((y - meteor.y), 2)) < (meteor.radius * meteor.radius)) {
                destroy(meteor);
                switch (meteor.size) {
                    case 1:
                        parent.addScore(100);
                        break;
                    case 2:
                        parent.addScore(50);
                        break;
                    case 3:
                        parent.addScore(20);
                        break;
                    default:
                        break;
                }
                return true; // Could be expanded to utilize the Polygon.contains() method at additional overhead
            }
        }
        return false;
    }

    /** Updates and returns the x and y coordinates, and radius, of each meteor */
    public int[][] getMeteorsXY(){

        int[][] meteorXY = new int[meteors.size()][3];

        int i = 0;
        for (Meteor met : meteors) {
            met.update();
            meteorXY[i][0] = (int) met.x;
            meteorXY[i][1] = (int) met.y;
            meteorXY[i][2] = met.radius;
            i++;
        }
        return meteorXY;

    }

    /** Updates and returns an array of Polygons representing each meteor object. */
    public Polygon[] getMeteorPolygons(){
        Polygon[] meteorPolygons = new Polygon[meteors.size()];

        int i = 0;
        for (Meteor met : meteors) {
            met.update();
            meteorPolygons[i] = met.polygon();
            meteorPolygons[i] = met.polygon();
            meteorPolygons[i] = met.polygon();
            i++;
        }
        return meteorPolygons;

    }

    /** Returns TRUE if all of the meteors are destroyed */
    public boolean allMeteorsDestroyed(){
        return meteors.size() <= 0;
    }

}
