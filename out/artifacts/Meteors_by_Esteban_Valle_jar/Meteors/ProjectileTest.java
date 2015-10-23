// Copyright © 2014 Esteban Valle. All rights reserved.
//
// +1-775-351-4427
// esteban@thevalledesign.com
// http://facebook.com/SeniorShizzle

import org.junit.*;

public class ProjectileTest {
    @Test
    public void testExpiration(){
        Projectile bullet = new Projectile(100, 100, 10, 10, 0, 1, 100);
        Assert.assertFalse(bullet.isExpired);
        try {
            Thread.sleep(120); // Wait 120 ms to catch any timer coalescing errors (if Java does that)
        } catch (Exception e) {
            System.out.println(e.getCause());
        }
        Assert.assertTrue(bullet.isExpired);

    }

    @Test
    public void testVelocity(){
        Projectile bullet = new Projectile(100, 100, 10, 0, 0, 1, 100);

        for (int i = 0; i < 10; i++){
            bullet.update();
        }

        Assert.assertEquals(220, bullet.x, .1); // The bullet has a yVelocity of 10 (10 * cos(0) = 10)
        Assert.assertEquals(100, bullet.y, .1); // The bullet has a yVelocity of 0
        System.out.println(bullet.x);
    }

    // More to come
}
