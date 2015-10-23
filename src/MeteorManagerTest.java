// Copyright © 2014 Esteban Valle. All rights reserved.
//
// +1-775-351-4427
// esteban@thevalledesign.com
// http://facebook.com/SeniorShizzle

import org.junit.Before;
import org.junit.*;

public class MeteorManagerTest {
    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testAddMeteor(){
        MeteorManager manager = new MeteorManager(null);
        Assert.assertEquals(manager.meteors.size(), 0);
        manager.addMeteor(0, 0, 0, 0);
        Assert.assertEquals(manager.meteors.size(), 1);
        manager.addMeteor(0, 2, 0, 0);
        Assert.assertEquals(manager.meteors.size(), 2);

    }

    // More to come
}
