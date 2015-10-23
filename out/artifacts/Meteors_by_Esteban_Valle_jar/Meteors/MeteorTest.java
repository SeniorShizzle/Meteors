// Copyright © 2014 Esteban Valle. All rights reserved.
//
// +1-775-351-4427
// esteban@thevalledesign.com
// http://facebook.com/SeniorShizzle

import org.junit.*;

public class MeteorTest {
    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testMeteorConstructor(){
        Meteor myMeteor = new Meteor(8,9,5,2);
        Assert.assertEquals(myMeteor.x, 8, 0);
        myMeteor = new Meteor(7,6,5,4);           // One size too big
        Assert.assertEquals(0, myMeteor.size, 0);
    }

    // More to come
}
