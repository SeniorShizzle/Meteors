// Copyright © 2014 Esteban Valle. All rights reserved.
//
// +1-775-351-4427
// esteban@thevalledesign.com
// http://facebook.com/SeniorShizzle

import org.junit.*;

public class ShipTest {

    @Test
    public void testPositionArrays(){
        Ship ship = Ship.getInstance();
        ship.setX(0);
        ship.setY(0);
        ship.setRotation(0);

        // Test rotation 0 and position (0,0)
        Assert.assertArrayEquals(ship.getXValues(), new int[]{-4, 20, -4, -2, -2});
        Assert.assertArrayEquals(ship.getYValues(), new int[]{8, 0, -8, -4, 4});

        ship.setX(50);
        ship.setY(50);
        ship.setRotation(Math.PI / 2);

        // Test another position and rotation
        Assert.assertArrayEquals(ship.getXValues(), new int[]{41, 50, 58, 54, 45});
        Assert.assertArrayEquals(ship.getYValues(), new int[]{45, 70, 45, 47, 47});

        // Test that the flame is in the right position
        Assert.assertArrayEquals(ship.getFlameXValues(), new int[]{46, 49, 53});
        Assert.assertArrayEquals(ship.getFlameYValues(), new int[]{48, 40, 48});

    }

    @Test
    public void testWrapAndVelocity(){

        // Tests velocity, wrap, and ensures wrap is after a buffer of 30 pixels
        Ship ship = Ship.getInstance();
        ship.setX(0);
        ship.setY(100);
        ship.setRotation(Math.PI);

        Assert.assertEquals(0, ship.x(), .01);

        int i = 0;
        do {ship.thrust(); ship.update();i++; } while (i < 10); // Thrust the ship
        Assert.assertTrue(ship.x() != 0);

        ship.setX(-40); // Placing the ship beyond the wrap threshold
        ship.update();
        Assert.assertFalse(ship.x() == -40); // Ensures the ship has wrapped

    }

    @Test
    public void testMaxVelocity(){
        Ship ship = Ship.getInstance();

        ship.setX(0);
        ship.setY(100);
        ship.setRotation(0);

        do {
            ship.thrust();
            ship.update();
        } while (ship.x() < 500);

        Assert.assertEquals(ship.y(), 100, .1);               // Make sure the ship hasn't moved up or down any
        Assert.assertTrue(ship.xVelocity() <= ship.maxSpeed()); // Make sure the ship's velocity isn't above its maximum speed
    }

    // More to come
}
