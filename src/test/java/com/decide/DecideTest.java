package com.decide;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

import com.decide.Decide.CONNECTORS;


public class DecideTest {

    Decide DEFAULT = new Decide(
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
         0, 0, 0, 0, 0, 0, 0, 0,
        null, null, new double[0], new double[0]
        );

    //Example: Typical test syntax
    @Test
    public void verifyNoExceptionsThrown(){
        //Just calls the main method with an epty argument
        Decide.main(new String[] {});
    }


    // Tests for the `decide` function
    
    /**
     * Requirements: See `DECIDE` specification in documentation
     * Contract:
     *      Precondition:   `x` and `y` vectors have different length
     *      Postcondition:  `Decide-constructor` throws an `IllegalArgumentException`
     */
    @Test(expected = IllegalArgumentException.class)
    public void DecideVerifyExceptionThrown(){
        boolean[] puv = new boolean[15];
        CONNECTORS[][] lcm = new CONNECTORS[15][15];
        double [] x = {0,0,0,0,0}; //5 x-values
        double [] y = {0,0,0,0};   //4 y-values
        new Decide(
            1, 1, 1, 1, 1, 1, 1, 
            1, 1, 1, 1, 1, 1, 1, 
            1, 1, 1, 1, 1, lcm, puv, x, y);
        
    }

    @Test
    /**
     * Requirements: See `DECIDE` specification in documentation
     * Contract:
     *      Precondition:   The points are constructed such that all LICs return true
     *                      and the `PUV` is true for all LICs
     *                      and all entries in the `LCM` is set to `ANDD`.
     *      Postcondition:  `DECIDE` returns true
     */
    public void DecideTestPositive() {
        boolean[] puv = new boolean[15];
        Arrays.fill(puv, true);
        CONNECTORS[][] lcm = new CONNECTORS[15][15];
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                lcm[i][j] = CONNECTORS.ANDD;
            }
        }
        double[] xs = {1, 2.3, 1.5, -1.5,  0.5,  2.5,  -1};
        double[] ys = {1, 1.2, 2.6,    2, -1.5, -0.5, 2.5};
        /**
         * LIC0: (1, 1) & (2.3, 1.2) has distance ~1.32 > (length1 = 1.3) --> True
         * LIC1: (1, 1), (2.3, 1.2), (1.5, 2.6) cannot be contained by circle with r = 0.8 --> True
         * LIC2: Angle of (1, 1), (2.3, 1.2), (1.5, 2.6) is ~1.2 < PI - PI/4 --> True
         * LIC3: Area of (1, 1), (2.3, 1.2), (1.5, 2.6) is 0.99 > 0.95 --> True
         * LIC4: The points 3, 4, 5 are in 3 different quadrants, 3 > 2 --> True
         * LIC5: The second and third points have decreasing x-values --> True
         * LIC6: The fourth point is farthest away from the line of the first five points with a distance ~2.65 > 2.6 --> True
         * LIC7: The first and fourth points have a distance of ~2.69 > 1.3 --> True
         * LIC8: The first, third, and fifth points cannot be contained in a circle with r = 0.8 --> True
         * LIC9: The first, third, and fifth points produce an angle of ~0.06 < PI - epsilon --> True
         * LIC10: The area of the first, third, and sixth points is ~1.575 > 0.95 --> True
         * LIC11: (1, 1) and (-1.5, 2) are separated by 2 intervening points and -1.5 - 1 = -2.5 < 0 --> True 
         * LIC12: The first and fourth points have a distance of ~2.69 > 1.3. The fourth and seventh points have a distance of ~0.7 < 0.8.
         * LIC13: None of the available set of three points fit in a circle with r = 0.8. The first, third, and fifth points fit in a circle with r = 2.2.
         * LIC14: All produced triangle have an area greater than 0.95. The area of the first, fourth, and sixth points is 1.125 < 1.2
         */
        Decide dec = new Decide(
            1.3, 0.8, Math.PI / 4, 0.95, 
            3, 2, 2.6, 5, 2, 1, 1, 
            1, 1, 1, 2, 2, 0.8, 2.2, 1.2, 
            lcm, puv, xs, ys);

        assertTrue("The variable assignment should return 'true' for all LICs and launch.", dec.DECIDE());
    }

    @Test
    /**
     * Requirements: See `DECIDE` specification in documentation
     * Contract:
     *      Precondition:   The points are constructed such that all LICs except one return true
     *                      and the `PUV` is true for all LICs
     *                      and all entries in the `LCM` is set to `ANDD`. 
     *      Postcondition:  `DECIDE` returns false
     */
    public void DecideTestNegative() {
        boolean[] puv = new boolean[15];
        Arrays.fill(puv, true);
        CONNECTORS[][] lcm = new CONNECTORS[15][15];
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                lcm[i][j] = CONNECTORS.ANDD;
            }
        }
        double[] xs = {1, 2.3, 1.5, -1.5,  0.5,  2.5,  -1};
        double[] ys = {1, 1.2, 2.6,    2, -1.5, -0.5, 2.5};
        /**
         * Change `length2` such that that `LIC12` returns false.
         * LIC12: The first and fourth points have a distance of ~2.69 > 1.3. The fourth and seventh points have a distance of ~0.7 > 0.6.
         */
        Decide dec = new Decide(
            1.3, 0.8, Math.PI / 4, 0.95, 
            3, 2, 2.6, 5, 2, 1, 1, 
            1, 1, 1, 2, 2, 0.6, 2.2, 1.2, 
            lcm, puv, xs, ys);

        assertFalse("The variable assignment should return 'false' for LIC12 and not launch.", dec.DECIDE());
    }
    
    @Test
    /**
     * When using the same setup as the negative test, but omits `LIC12` in the `PUV`, 
     * `DECIDE` should return true again since we don't use that LIC.
     * 
     * Requirements: See `DECIDE` specification in documentation
     * Contract:
     *      Precondition:   The points are constructed such that all LICs except one return true
     *                      and the `PUV` is true for all LICs except for the one that fails
     *                      and the `LCM` is set to a diagonal matrix with `ANDD` on the diagonal and `NOTUSED` elsewhere.  
     *      Postcondition: `DECIDE` returns true
     */
    public void DecidePUVCorrectlyExcludesLIC() {
        boolean[] puv = new boolean[15];
        Arrays.fill(puv, true);
        CONNECTORS[][] lcm = new CONNECTORS[15][15];
        // Initialize the LCM to a diagonal matrix so that each row is only represented by its own LIC.
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (i == j) {
                    lcm[i][j] = CONNECTORS.ANDD;
                } else {
                    lcm[i][j] = CONNECTORS.NOTUSED;
                }
                
            }
        }
        puv[12] = false; // Ignore the results from `LIC12`!!

        double[] xs = {1, 2.3, 1.5, -1.5,  0.5,  2.5,  -1};
        double[] ys = {1, 1.2, 2.6,    2, -1.5, -0.5, 2.5};
        /**
         * LIC12: The first and fourth points have a distance of ~2.69 > 1.3. The fourth and seventh points have a distance of ~0.7 > 0.6.
         */
        Decide dec = new Decide(
            1.3, 0.8, Math.PI / 4, 0.95, 
            3, 2, 2.6, 5, 2, 1, 1, 
            1, 1, 1, 2, 2, 0.6, 2.2, 1.2, 
            lcm, puv, xs, ys);

        assertTrue("`DECIDE` should ignore the result of `LIC12`!", dec.DECIDE());
    }

    @Test
    /**
     * Requirements: See `DECIDE` specification in documentation
     * Contract:
     *      Precondition:   All elements in the `PUV` are set to false
     *                      and all elements in the `LCM` are set to ANDD.
     *      Postcondition:  `DECIDE` returns true
     */
    public void DecideTestFalsePUV() {
        boolean[] puv = new boolean[15];
        CONNECTORS[][] lcm = new CONNECTORS[15][15];
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                lcm[i][j] = CONNECTORS.ANDD;
            }
        }
        Decide dec = new Decide(
            0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, lcm, puv, new double[1], new double[1]);
        
        // When all elements in the `PUV` are set to false, `DECIDE` should always return true. 
        assertTrue(dec.DECIDE());
    }

    @Test
    /**
     * Requirements: See `DECIDE` specification in documentation
     * Contract:
     *      Precondition:   All elements in the `PUV` is set to true
     *                      and all elements in the `LCM` are set to NOTUSED.
     *      Postcondition:  `DECIDE` returns true
     */
    public void DecideTestFalseLCM() {
        boolean[] puv = new boolean[15];
        Arrays.fill(puv, true);
        CONNECTORS[][] lcm = new CONNECTORS[15][15];
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                lcm[i][j] = CONNECTORS.NOTUSED;
            }
        }
        Decide dec = new Decide(
            0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, lcm, puv, new double[1], new double[1]);
        
        // When all elements in the `LCM` are set to NOTUSED, `DECIDE` should always return true. 
        assertTrue(dec.DECIDE());
    }
    
    @Test
    /**
     * Requirements: See `LIC10` documentation
     * Contract:
     *      Precondition:   `numPoints < 5` or `e_pts` < 1 or `f_pts` < 1 or `e_pts` + `f_pts` > `numPoints` - 3
     *      Postcondition:  `LIC10` returns false
     */
    public void LIC10TestBounds() {
        boolean res = DEFAULT.LIC10(4, null, null, 3, 3, 2);
        assertFalse("LIC10 should return false when number of points is less than 5", res);
        res = DEFAULT.LIC10(5, null, null, 0, 3, 2);
        assertFalse("LIC10 should return false when e_pts is less than 5", res);
        res = DEFAULT.LIC10(5, null, null, 2, 0, 2);
        assertFalse("LIC10 should return false when f_pts is less than 5", res);
    }

    @Test
    /**
     * Requirements: See `LIC10` documentation
     * Contract:
     *      Precondition:   There exists three consecutive points that form a triangle with area greater than `area1`
     *      Postcondition:  `LIC10` returns true
     */
    public void LIC10TestPositive() {
        int e_pts = 1;
        int f_pts = 2;
        double[] x = {2.4, 7, 5.4, 5.2, 2.6, 4.5, 8.8};
        double[] y = {3.9, 3, 2, 5.9, 1.9, 0.4, 1.6};
        // The first, third, and sixth points form a triangle with area ~3.26
        double area1 = 3.25;
        boolean res = DEFAULT.LIC10(x.length, x, y, e_pts, f_pts, area1);
        assertTrue(res);
    }

    @Test
    /**
     * Requirements: See `LIC10` documentation
     * Contract:
     *      Precondition:   No three consecutive points form a triangle with area greater than `area1`
     *      Postcondition:  `LIC10` returns false
     */
    public void LIC10TestNegative() {
        int e_pts = 1;
        int f_pts = 2;
        double[] x = {2.4, 7, 5.4, 5.2, 2.6, 4.5, 8.8};
        double[] y = {3.9, 3, 2, 5.9, 1.9, 0.4, 1.6};
        // The first, third, and sixth points form a triangle with area ~3.26
        double area1 = 3.27;
        boolean res = DEFAULT.LIC10(x.length, x, y, e_pts, f_pts, area1);
        assertFalse(res);
    }

    @Test
    /**
     * Requirements: See `LIC6` documentation
     * Contract:
     *      Precondition:   `n_pts` is not in the interval 3 <= `n_pts` <= `numPoints` or `dist` is negative
     *      Postcondition:  `LIC6` returns false
     */
    public void LIC6TestBoundaries() {
        boolean res = DEFAULT.LIC6(10, null, null, 11, 0);
        assertFalse("LIC6 should return false when n_pts > numPoints", res);
        res = DEFAULT.LIC6(10, null, null, 2, 0);
        res &= DEFAULT.LIC6(10, null, null, -2, 0);
        assertFalse("LIC6 should return false when n_pts < 3", res);
        res = DEFAULT.LIC6(10, null, null, 5, -1);
        assertFalse("LIC6 should return false when dist < 0", res);
    }

    @Test
    /**
     * Requirements: See `LIC6` documentation
     * Contract:
     *      Precondition:   There exists a set of `n_pts` consecutive points such that the largest 
     *                      distance from a point to the line from the first and last points is greater than `dist`
     *      Postcondition:  `LIC6` returns true
     */
    public void LIC6TestPositive() {
        // The distance from [9.4, 10.3] (point #4) to the line [9.1, 2.1]-[13.9, 4.6] (#2-#6) is ~7.13
        double[] x = {4.8, 9.1, 3.6, 9.4, 10.5, 13.9};
        double[] y = {11.1, 2.1, 4.7, 10.3, 5.3, 4.6};
        double dist = 7.1;
        boolean res = DEFAULT.LIC6(x.length, x, y, 5, dist);
        assertTrue(res);
    }

    @Test
    /**
     * Requirements: See `LIC6` documentation
     * Contract:
     *      Precondition:   No set of `n_pts` consecutive points exists such that the largest distance from a 
     *                      point to the line from the first and last points is greater than `dist`
     *      Postcondition:  `LIC6` returns false
     */
    public void LIC6TestNegative() {
        // The distance from [9.4, 10.3] (point #4) to the line [9.1, 2.1]-[13.9, 4.6] (#2-#6) is ~7.13
        double[] x = {4.8, 9.1, 3.6, 9.4, 10.5, 13.9};
        double[] y = {11.1, 2.1, 4.7, 10.3, 5.3, 4.6};
        double dist = 7.2;
        boolean res = DEFAULT.LIC6(x.length, x, y, 5, dist);
        assertFalse(res);
    }

    @Test
    /**
     * Requirents: See documentation of `containedInCircle`
     * Contract:
     *      Precondition:   Three points can be contained in circle with radius `radius`
     *      Postcondition:  `containedInCircle` returns true
     */
    public void containedInCirclePositive() {
        double[][] points = {{1.5, 0.5}, {1, 4}, {3.5, 2.5}}; // These points form a circle with radius ~1.822
        double radius = 2;
        boolean res = DEFAULT.containedInCircle(points, radius);
        assertTrue(res);
        radius = 1.83; // This should work as well
        res = DEFAULT.containedInCircle(points, radius);
        assertTrue(res);
    }

    @Test
    /**
     * Requirents: See documentation of `containedInCircle`
     * Contract:
     *      Precondition:   Three points cannot be contained in circle with radius `radius`
     *      Postcondition:  `containedInCircle` returns false
     */
    public void containedInCircleNegative() {
        double[][] points = {{1.5, 0.5}, {1, 4}, {3.5, 2.5}}; // These points form a circle with radius ~1.822
        double radius = 1.8;
        boolean res = DEFAULT.containedInCircle(points, radius);
        assertFalse(res);
    }

    @Test
    /**
     * Requirements: See `LIC13` documentation
     * Contract:
     *      Precondition:   There exists three points separated by a_pts and b_pts points, 
     *                      respectively, that cannot be contained in a circle with radius `radius1`.
     *                      There also exists three points separated as above that can be contained in a circle with radius `radius2`
     *      Postcondition:  `LIC13` returns true
     */
    public void LIC13TestPositive() {
        double[] x = {7.2, 12.8, 5.6, 15.5, 15.3, 12.1, 19.6, 8.9};
        double[] y = {6.2, 12.5, 12, 6.3, 1.4, 6.4, 13.1, 15.5};
        // The points (5.6, 12), (15.3, 1.4), (8.9, 15.5) cannot be contained in a circle with radius 7.6 (Only two of the points).
        // The points (12.8, 12.5), (15.5, 6.3), (19.6, 13.1) can just about be contained in a circle with radius 4.25.
        double radius1 = 7.6, radius2 = 4.25;
        boolean res = DEFAULT.LIC13(x.length, x, y, 1, 2, radius1, radius2);
        assertTrue(res);
    }

    @Test
    /**
     * Requirements: See `LIC13` documentation
     * Contract:
     *      Precondition:   The precondition as described in the positive test (`LIC13TestPositive`) is not met
     *      Postcondition:  `LIC13` returns false
     */
    public void LIC13TestNegative() {
        double[] x = {7.2, 12.8, 5.6, 15.5, 15.3, 12.1, 19.6, 8.9};
        double[] y = {6.2, 12.5, 12, 6.3, 1.4, 6.4, 13.1, 15.5};
        double radius1 = 7.9, radius2 = 4.25;
        // This should return false since all sets of three points can now be contained in the circle with radius 7.9.
        boolean res = DEFAULT.LIC13(x.length, x, y, 1, 2, radius1, radius2);
        assertFalse(res);

        radius1 = 7.6; radius2 = 4.15;
        // This should also return false since no set of points can be contained in the circle with radius 4.15.
        res = DEFAULT.LIC13(x.length, x, y, 1, 2, radius1, radius2);
        assertFalse(res);
    }

    @Test
    /**
     * Requirements: See `LIC0` documentation
     * Contract:
     *      Precondition:   Two consecutive points are more than a distance `length1` apart.
     *      Postcondition:  `LIC0` returns true
     */
    public void LIC0TestPositive() {
        double[] x = {2.5, 3.3, 6.6, 5.5, 5.1};
        double[] y = {1.4, 4.4, 2.7, 1.2, 5};
        // The most far apart consecutive points are (5.5, 1.2) and (5.1, 5) with a distance of ~3.82
        double length1 = 3.82;
        boolean res = DEFAULT.LIC0(5, x, y, length1);
        assertTrue(res);
    }

    @Test
    /**
     * Requirements: See `LIC` documentation
     * Contract:
     *      Precondition:   Two consecutive points are less than (or equal to) a distance `length1` apart.
     *      Postcondition:  `LIC0` returns false
     */
    public void LIC0TestNegative() {
        double[] x = {2.5, 3.3, 6.6, 5.5, 5.1};
        double[] y = {1.4, 4.4, 2.7, 1.2, 5};
        // The most far apart consecutive points are (5.5, 1.2) and (5.1, 5) with a distance of ~3.82
        double length1 = 3.83;
        boolean res = DEFAULT.LIC0(5, x, y, length1);
        assertFalse(res);
    }


    @Test
    /**
     * Requirements: See `LIC8` documentation
     * Contract:
     *      Precondition: numPoints is less than 5: numPoints = 4,
     *                    or a_pts is less than 1: a_pts = 0,
     *                    or b_pts is less than 1: b_pts = 0.
     *      Postcondition: LIC8 returns false in all three cases.
    */
    public void LIC8TestFalseBoundaries(){
        double[] x = {7.2, 12.8, 5.6, 15.5, 15.3, 12.1, 19.6, 8.9};
        double[] y = {6.2, 12.5, 12, 6.3, 1.4, 6.4, 13.1, 15.5};
        double r1 = 7.9;
        boolean res = DEFAULT.LIC8(4, x, y, 1, 1, r1);
        assertFalse("Should return false with less than 5 points", res);

        res = DEFAULT.LIC8(x.length, x, y, 0, 1, r1);
        assertFalse("Should return false with a_pts < 1", res);

        res = DEFAULT.LIC8(x.length, x, y, 1, 0, r1);
        assertFalse("Should return false with b_pts < 1", res);
    }

    @Test
    /**
     * Requirements: See `LIC8` documentation
     * Contract:
     *      Precondition: There exists a set of three points that are 
     *                    separated by one consecutive intervening point 
     *                    that can all be contained in a circle of radus 5.   
     *      Postcondition: LIC8 returns false
    */
    public void LIC8TestInCircle(){
        //Should find three points in a circle of radius 5
        //when points (1, 1) - (3, 3) - (5, 5) can all be contained
        //inside a circle of raius 5.
        //a_pts = 1, b_pts = 1 --> startP * midP * endP
        double[] x = {1, 2, 3, 4, 5, 6, 7, 8};
        double[] y = {1, 2, 3, 4, 5, 6, 7, 8};
        double r1 = 5;

        boolean res = DEFAULT.LIC8(x.length, x, y, 1, 1, r1);

        assertFalse("Should find three points in a circle of radius 5", res);

    }

    @Test
    /**
     * Requirements: See `LIC8` documentation
     * Contract:
     *      Precondition: No set of three points that are each separated by
     *                    one consecutive intervening point can be contained
     *                    inside a circle of radius 2.
     *      Postcondition: LIC8 returns true
    */
    public void LIC8TestNotInCircle(){
        //Three points with one consecutive intervening point should 
        //be minimum 5 points where the length should exceed 2.
        //a_pts = 1, b_pts = 1 --> startP * midP * endP --> 5 total points
        double[] x = {1, 2, 3, 4, 5, 6, 7, 8};
        double[] y = {1, 2, 3, 4, 5, 6, 7, 8};
        double r1 = 2;

        boolean res = DEFAULT.LIC8(x.length, x, y, 1, 1, r1);

        assertTrue("Should not find three points in a circle of radius 2 with 5 points", res);
    }


    @Test
    /**
     * Requirements: See `LIC9` documentation
     * Contract:
     *      Precondition: numPoints is less than 5: numPoints = 4,
     *                    or c_pts is less than 1: c_pts = 0,
     *                    or d_pts is less than 1: d_pts = 0,
     *                    or c_pts + d_pts is lower than numPoints-3: 
     *                    c_pts = 2, d_pts = 2, numPoints = 8.
     *      Postcondition: LIC9 returns false in all three cases.
     */
    public void LIC9TestFalseBoundaries(){
        double[] x = {7.2, 12.8, 5.6, 15.5, 15.3, 12.1, 19.6, 8.9};
        double[] y = {6.2, 12.5, 12, 6.3, 1.4, 6.4, 13.1, 15.5};
        double r1 = 7.9;
        //numPoints = 4 LIC9 returns false.
        boolean res = DEFAULT.LIC9(4, x, y, 1, 1, r1);
        assertFalse("Should return false with less than 5 points", res);

        //c_pts = 0, should return false.
        res = DEFAULT.LIC9(x.length, x, y, 0, 1, r1);
        assertFalse("Should return false with a_pts < 1", res);

        //d_pts = 0, should return false.
        res = DEFAULT.LIC9(x.length, x, y, 1, 0, r1);
        assertFalse("Should return false with b_pts < 1", res);

        //c_pts = 2, d_pts =2, numPoints = 8 --> (2+2 <=8-3) --> 4 <=5 --> false
        res = DEFAULT.LIC9(x.length, x, y, 2, 2, r1);
        assertFalse("Should return false when c_pts+d_pts <= NumPoints-3", res);
    }

    @Test
    /**
     * Requirements: See `LIC9` documentation
     * Contract:
     *      Precondition: There exists and orthogonal angle between 
     *                    (2, 1), (3, 1) and (3, 2) with (3, 1) being 
     *                    the vertex where each point is separated by one
     *                    consecutive intervening point.
     *      Postcondition: LIC9 returns true
     */
    public void LIC9TestOrthogonalAngle(){
        //The points are separated by one consecutive intervening point
        //where there is an orthogonal angle between (2, 1), (3, 1) and (3, 2).
        //
        //             (3, 2)
        //(1,1) (2, 1) (3, 1) (4,1)
        double[] x = {1, 2, 3, 4, 3};
        double[] y = {1, 1, 1, 1, 2};
        double epsilon = 0;

        //Angle between (1,1) and (2,2) from (2,1) should be pi/2 --> return true
        boolean res = DEFAULT.LIC9(x.length, x, y, 1, 1, epsilon);

        assertTrue("Should find three points with an orthogonal angle", res);
    }

    @Test
    /**
     * Requirements: See `LIC9` documentation
     * Contract:
     *      Precondition: There exists a set of points on the x-axis
     *                    with coinciding points.
     *      Postcondition: LIC9 returns false
     */
    public void LIC9TestNoAngle(){
        //The middle point in and the last point are coinciding points in
        //the array.
        //(1,1) (2, 1) (3, 1) (4,1)
        double[] x = {1, 2, 3, 4, 3};
        double[] y = {1, 1, 1, 1, 1};
        double epsilon = 0;
        
        boolean res = DEFAULT.LIC9(x.length, x, y, 1, 1, epsilon);

        assertFalse("Should not find an angle as the points coincide with the vertex", res);
    }

    @Test
    /**
     * Requirements: See `LIC1` documentation
     * Contract:
     *      Precondition:    There exists three consecutive points that cannot be contained in a circle with radius `radius1`
     *      Postcondition:   `LIC1` returns true
     */
    public void LIC1TestPositive() {
        double[] x = {7.2, 12.8, 5.6, 15.3, 8.9};
        double[] y = {6.2, 12.5, 12, 1.4, 15.5};
        // The points (5.6, 12), (15.3, 1.4), (8.9, 15.5) cannot be contained in a circle with radius 7.6 (Only two of the points).
        double radius1 = 7.6;
        boolean res = DEFAULT.LIC1(x.length, x, y, radius1);
        assertTrue(res);
    }

    @Test
    /**
     * Requirements: See `LIC1` documentation
     * Contract:
     *      Precondition:    Every set of three consecutive points can be contained in a circle with radius `radius1`
     *      Postcondition:   `LIC1` returns true
     */
    public void LIC1TestNegative() {
        double[] x = {7.2, 12.8, 5.6, 15.3, 8.9};
        double[] y = {6.2, 12.5, 12, 1.4, 15.5};
        // The points (5.6, 12), (15.3, 1.4), (8.9, 15.5) should be able to be contained in a circle with radius 7.8.
        double r = 7.8;
        boolean res = DEFAULT.LIC1(x.length, x, y, r);
        assertFalse(res);
    }

    @Test
    /**
    * Test that LIC4 correctly returns true when requirements are met.
    * Requirements: See `LIC4` documentation
    * Contract:    
    *   Precondition: There exists at least one set of Q_PTS consecutive data points that lie in more 
	*   than QUADS quadrants. 
    *   Postcondition:  `LIC4` returns true.
    */
    public void LIC4_testTrue(){
        /*
        * X[1..3]Y[1..3] (3 points = q_pts) lie in 3 different quadrants, which is > 2
        */
        double[] X = {0, -2, 3, -3, 3, -2};
        double[] Y = {0, 1, 2, -2, -3, 7};
        boolean res = DEFAULT.LIC4(X.length, X, Y, 3, 2);
        assertTrue("One in each quadrant should return true", res);
    }

    @Test
    /**
    * Test that LIC4 correctly returns false when requirements are not met.
    * Requirements: See `LIC4` documentation
    * Contract:    
    *   Precondition: There does not exists at least one set of Q PTS consecutive data points that lie in more 
	*   than QUADS quadrants. 
    *   Postcondition:  `LIC4` returns false.
    */    
    public void LIC4_testFalse(){
        double[] X = {0, -1, 2, 1, 2, 9};
        double[] Y = {0, 0, 1, 1, 3, 7};
        /*
        * There never 3 consecutive points that lie in more than 2 quadrants in this setup.
        */
        boolean res = DEFAULT.LIC4(X.length, X, Y, 3, 2);
        assertFalse("There are never more than 2 quads per 3 consecutive elements", res);
    }

    @Test
    /**
    * Test that LIC4 correctly returns false when input variables are out of bounds.
    * Requirements: See `LIC4` documentation
    * Contract:    
    *   Precondition: (1): Q_PTS <= QUADS
                      (2): QUADS < 1
                      (3): Q_PTS > NUMPOINTS 
    *   Postcondition:  `LIC4` returns false.
    */    
    public void LIC4_testInputBounds(){
        double[] X = {0, 0, 2, 1, 2, 9};
        double[] Y = {0, 0, 1, 1, 3, 7};
        /*
        * (1): q_pts <= number of quadrants cant never return true
        */
        boolean res = DEFAULT.LIC4(X.length, X, Y, 2, 2);
        assertFalse("q_pts can not be <= number of quadrants", res);
        /*
        * (2): quads < 1 should return false
        */
        res = DEFAULT.LIC4(X.length, X, Y, 2, 0);
        assertFalse("quads < 1 should return false", res);
        /*
        * (3): q_pts > NumPoints should return false
        */
        res = DEFAULT.LIC4(X.length, X, Y, 7, 2);
        assertFalse("q_pts > NumPoints should return false", res);
    }

    @Test
    /**
     * Requirements: See `LIC7` documentation
     * Contract:
     *      Precondition:  At least one of the input parameters are invalid
     *      Postcondition: `LIC7` returns false
     */
    public void LIC7TestBoundaries() {
        // Test boundaries for the number of points.
        assertFalse(DEFAULT.LIC7( 2, null, null, 0, 0.0));
        assertFalse(DEFAULT.LIC7( 0, null, null, 0, 0.0));
        assertFalse(DEFAULT.LIC7(-5, null, null, 0, 0.0));
        // Test boundaries for the separation value.
        assertFalse(DEFAULT.LIC7(3, null, null,  0, 0.0));
        assertFalse(DEFAULT.LIC7(3, null, null, -2, 0.0));
        assertFalse(DEFAULT.LIC7(3, null, null,  3, 0.0));
        // Test boundaries for the length value.
        assertFalse(DEFAULT.LIC7(3, null, null, 1, -2.00));
        assertFalse(DEFAULT.LIC7(3, null, null, 1, -1e-5));
    }

    @Test
    /**
     * Requirements: See `LIC7` documentation
     * Contract:
     *      Precondition:  There exists two points with a distance greater than 1.9
     *      Postcondition: `LIC7` returns true
     */
    public void LIC7TestPositive() {
        // Test positive outcome with positive distance.
        double[] x0 = {1.0, 2.0, 3.0};
        double[] y0 = {1.0, 1.0, 1.0};
        assertTrue(DEFAULT.LIC7(3, x0, y0, 1, 1.9));
        // Test positive outcome with negative distance.
        double[] x1 = {3.0, 2.0, 1.0};
        double[] y1 = {1.0, 1.0, 1.0};
        assertTrue(DEFAULT.LIC7(3, x1, y1, 1, 1.9));
    }

    @Test
    /**
     * Requirements: See `LIC7` documentation
     * Contract:
     *      Precondition:  There are no two points with a distance greater than 5.0
     *      Postcondition: `LIC7` returns false
     */
    public void LIC7TestNegative() {
        // Test negative outcome with too large length.
        double[] x = {-1.5, -1.5,  1.5, 1.5};
        double[] y = {-1.5,  1.5, -1.5, 1.5};
        assertFalse(DEFAULT.LIC7(4, x, y, 1, 5.0));
    }

    @Test
    /**
     * Requirements: See `LIC14` documentation
     * Contract:
     *      Precondition:  At least one of the input parameters are invalid
     *      Postcondition: `LIC14` returns false
     */
    public void LIC14TestBoundaries() {
        // Test boundaries for the number of points.
        assertFalse(DEFAULT.LIC14(4, null, null, 0, 0, 0, 0));
        assertFalse(DEFAULT.LIC14(0, null, null, 0, 0, 0, 0));
        // Test boundaries for the separation values.
        assertFalse(DEFAULT.LIC14(5, null, null, 0, 2, 0, 0));
        assertFalse(DEFAULT.LIC14(5, null, null, 2, 0, 0, 0));
        assertFalse(DEFAULT.LIC14(5, null, null, 2, 2, 0, 0));
        // Test boundaries for the area values.
        assertFalse(DEFAULT.LIC14(5, null, null, 1, 1, -1e-5, 0));
        assertFalse(DEFAULT.LIC14(5, null, null, 1, 1, 0, -1e-5));
    }

    @Test
    /**
     * Requirements: See `LIC14` documentation
     * Contract:
     *      Precondition:  There exists triangles with areas greater than 4.4 and less than 0.6
     *      Postcondition: `LIC14` returns true
     */
    public void LIC14TestPositive() {
        // Test positive outcome with negative point area.
        double[] x = {2, 2, 0, 0, 1, 0, 0, 3, 1};
        double[] y = {2, 0, 0, 2, 1, 3, 0, 0, 2};
        assertTrue(DEFAULT.LIC14(9, x, y, 2, 1, 4.4, 0.6));
    }

    @Test
    /**
     * Requirements: See `LIC14` documentation
     * Contract:
     *      Precondition:  There are no two triangles with areas greater than 8.0 or less than 2.5
     *      Postcondition: `LIC14` returns false
     */
    public void LIC14TestNegative() {
        double[] x = {0, 1, 3, 0, 2, 3};
        double[] y = {0, 1, 0, 3, 2, 3};
        // Test negative outcome with too large area1.
        assertFalse(DEFAULT.LIC14(6, x, y, 2, 1, 8.0, 5.0));
        // Test negative outcome with too small area2.
        assertFalse(DEFAULT.LIC14(6, x, y, 2, 1, 4.0, 2.5));
    }


    @Test
    /**
     * Requirements: See `LIC12` documentation
     * Contract:
     *      Precondition: numPoints is less than 3, numPoints = 2,
     *                    or k_pts is less than 0, k_pts = -1,
     *                    or length1 is less than 0, length1 = -1,
     *                    or length2 is less than 0, length2 = -1.
     *      Postcondition: LIC12 returns false in all four cases
     */
    public void LIC12TestFalseBoundaries(){
        double[] x = {7.2, 12.8, 5.6, 15.5, 15.3, 12.1, 19.6, 8.9};
        double[] y = {6.2, 12.5, 12, 6.3, 1.4, 6.4, 13.1, 15.5};

        //NumPoints = 2 --> false
        boolean res = DEFAULT.LIC12(2, x, y, 1, 1, 1);
        assertFalse("Should return false with less than 3 points", res);

        //k_pts = -1 --> false
        res = DEFAULT.LIC12(x.length, x, y, -1, 1, 1);
        assertFalse("Should return false with k_pts < 0", res);

        //length1 = -1 --> false
        res = DEFAULT.LIC12(x.length, x, y, 1, -1, 1);
        assertFalse("Should return false with length1 < 0", res);

        //length2 = -1 --> false
        res = DEFAULT.LIC12(x.length, x, y, 2, 1, -1);
        assertFalse("Should return false when length2 < 0", res);
    }


    @Test
    /**
     * Requirements: See `LIC12` documentation
     * Contract:
     *      Precondition: There exists one pair of points that are 
     *                    separated by two consecutive intervening points
     *                    in the array with a distance greater than 1 while
     *                    there also exists a pair of points that are again
     *                    separated by two consecutive intervening points in 
     *                    the array but have a distance between them of less than 1.
     *      Postcondition: LIC12 returns true
     */
    public void LIC12TestTwoPointPairs(){
        // k_pst = 2, separated by two pts

        //Find the pair with dist > (length1 = 1) --> (1, 1), (4, 1) --> (dist = 3)
        //Find the pair with dist < (length2 = 1) --> (2, 1), (2, 1) --> (dist = 0)
        //(1,1), (2, 1), (2, 1), (3, 1) (4,1)     Points
        double[] x = {1, 2, 3, 4, 2};
        double[] y = {1, 1, 1, 1, 1};

        boolean res = DEFAULT.LIC12(x.length, x, y, 2, 1, 1);

        assertTrue("Should find two pairs with 2 points between with a distance greater than 1 and one less than 1.", res);

    }

    @Test
    /**
     * Requirements: See `LIC12` documentation
     * Contract:
     *      Precondition: There does exists one pair of points that are 
     *                    separated by two consecutive intervening points
     *                    in the array with a distance greater than 1 while
     *                    there does not exists a pair of points that are again
     *                    separated by two consecutive intervening points in 
     *                    the array but have a distance between them of less than 3.
     *      Postcondition: LIC12 returns false
     */
    public void LIC12TestNegativeTwoPointPairs(){
        // k_pst = 2, separated by two pts
        //Find the pair with dist > (length1 = 1) --> (1, 1), (4, 1) --> (dist = 3)
        //Find the pair with dist < (length2 = 3) --> None exist
        //(1,1), (2, 1), (3, 1) (4,1) (5, 1)    Points
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {1, 1, 1, 1, 1};

        boolean res = DEFAULT.LIC12(x.length, x, y, 2, 1, 3);

        assertFalse("Should not find two pairs with the second pair having a dist > 3.", res);

    }

    @Test
    /**
     * Requirements: See `LIC5` documentation
     * Contract:
     *      Precondition: All sets of three points have the same coordinates,
     *                    or all sets of three points have decreasing x-values.            
     *      Postcondition: LIC5 returns false in both cases.
     */
    public void LIC5TestFalseBoundaries(){
        // All the same point (1, 1), difference in x-values should equal 0.
        double[] x = {1, 1, 1};
        double[] y = {1, 1, 1};
        boolean res = DEFAULT.LIC5(x.length, x, y);

        assertFalse("Should not find two points with (xj - xi) < 0 with the same point", res);

        // Only increasing x-values, difference in x-values should be greater than 0.
        double[] x1 = {0, 1, 5};
        double[] y1 = {1, 1, 1};
        res = DEFAULT.LIC5(x1.length, x1, y1);
        assertFalse("Should not find two points with (xj - xi) < 0 with increasing consecutive x-values", res);
    }

    @Test
    /**
     * Requirements: See `LIC5` documentation
     * Contract:
     *      Precondition: There exists a set of three points with consecutively
     *                    decreasing x-values.
     *      Postcondition: LIC5 returns true.
     */
    public void LIC5TestDecreasingX(){
        //x-values consecutively decrease. (x[j] - x[i]) < 0 should be true where i=j-1 
        double[] x = {3, 2, 1};
        double[] y = {1, 1, 1};

        boolean res = DEFAULT.LIC5(x.length, x, y);

        assertTrue("Should find two points with (xj - xi) < 0 with only decreasing x-values", res);
    }

    @Test
    /**
     * Requirements: See `LIC5` documentation
     * Contract:
     *      Precondition: There exists a set of three points with only
     *                    consecutively decreasing y-values with the same
     *                    x-value.
     *      Postcondition: LIC5 returns false.
     */
    public void LIC5TestDecreasingY(){
        //All three points have the same x-value where their difference should be 0.
        double[] x = {1, 1, 1};
        double[] y = {3, 2, 1};

        boolean res = DEFAULT.LIC5(x.length, x, y);

        assertFalse("Should not find two points with (xj - xi) < 0 with only decreasing y-values", res);
    }

    @Test
    /**
     * Requirements: See `LIC5` documentation
     * Contract:
     *      Precondition: There exists a set of three points with consecutively
     *                    decreasing both x- and y-values.
     *      Postcondition: LIC5 returns true.
     */
    public void LIC5TestConsecutiveXY(){
        //Decreasing both x- and y-values should find a difference (x[j] - x[i]) < 0 where i=j-1 
        double[] x = {3, 2, 1};
        double[] y = {3, 2, 1};

        boolean res = DEFAULT.LIC5(x.length, x, y);

        assertTrue("Should find two points with (xj - xi) < 0 with both decreasing x- and y-values", res);
    }


    @Test
    /**
    * Test that LIC11 correctly returns false when input variables are out of bounds.
    * Requirements: See `LIC11` documentation
    * Contract:    
    *   Precondition: G_PTS < 1.
    *   Postcondition:  `LIC11` returns false.
    */    
    public void LIC11_testInputBounds(){
        double[] X = {0, -2, 3, -3, 3, -2};
        double[] Y = {0, 1, 2, -2, -3, 7};
        /*
        * g_pts < 1 should return false
        */
        boolean res = DEFAULT.LIC11(X.length, X, Y, 0);
        assertFalse("g_pts < 1 should return false", res);
    }

    @Test
    /**
    * Test that LIC11 correctly returns true when requirements are met.
    * Requirements: See `LIC11` documentation
    * Contract:    
    *   Precondition: There exists at least one set of two data points, X[i]Y[i] and X[j]Y[j], 
	*   separated by exactly G PTS consecutive intervening points, such that X[j] - X[i] < 0. 
	*   (where i < j ).
    *   Postcondition:  `LIC11` returns true.
    */    
    public void LIC11_testTrue(){
        double[] X = {0, -2, 3, -3, 3, -2};
        double[] Y = {0, 1, 2, -2, -3, 7};
        /*
        * At least X[3] (with val -3) and X[0] (with val 0) separated by 2 intervening points, should return true.
        */
        boolean res = DEFAULT.LIC11(X.length, X, Y, 2);
        assertTrue("At least X[3](with val -3) and X[0](with val 0) should return true", res);
    }

    @Test
    /**
    * Test that LIC11 correctly returns false when requirements are not met.
    * Requirements: See `LIC11` documentation
    * Contract:    
    *   Precondition: There does not exist at least one set of two data points, X[i]Y[i] and X[j]Y[j], 
	*   separated by exactly G PTS consecutive intervening points, such that X[j] - X[i] < 0. 
	*   (where i < j ).
    *   Postcondition:  `LIC11` returns false.
    */    
    public void LIC11_testFalse(){
        double[] X = {1, 2, 3, 3, 4, 6};
        double[] Y = {0, 1, 2, -2, -3, 7};
        /*
        * Condition never met since every X[j] is > X[i] meaning it should return false.
        */
        boolean res = DEFAULT.LIC11(X.length, X, Y, 2);
        assertFalse("Condition is never met, should return false", res);
    }

    @Test
    /**
     * Test that LIC2 correctly returns false when input parameters are out of bounds.
     * Requirements: See `LIC2` documentation
    * Contract:    
    *      Precondition: (1): Either the first or the last of the three consecutive points (or both) coincides with the vertex
                             of the triangle.
                         (2): Epsilon < 0
                         (3): Epsilon >= PI  
    *      Postcondition: (1), (2), (3): `LIC2` returns false
    */
    public void LIC2_testBounds(){
        double[] X = {0, 1, 1, 1, 1, 3};
        double[] Y = {0, 0, 0, 1, 1, 3};
        /*
         *(1): Points X[1..4] Y[1..4] possible vertexes where either X[i-1]Y[i-1] or X[i+1]Y[i+1] is equal to X[i]Y[i].
         */
        boolean res = DEFAULT.LIC2(X.length, X, Y, Math.PI/2);
        assertFalse("X[1]Y[1] is equal to X[2]Y[2] and X[3]Y[3] is equal to X[4]Y[4] which should give false", res);
        
        /*
         * Create arrays that should otherwise return true (see LIC2_testTrue()).
         */
        double[] X1 = {1, 1, 3, 3, 4, 6};
        double[] Y1 = {1, 1, 1, -2, -3, 7};
        /*
        *(2): Epsilon < 0
        */
        res = DEFAULT.LIC2(X1.length, X1, Y1, -1);
        assertFalse("Epsilon < 0 which should give false", res);
        /*
        *(3): Epsilon >= PI
        */
        res = DEFAULT.LIC2(X1.length, X1, Y1, Math.PI * 1.1);
        assertFalse("Epsilon >= PI which should give false", res);
    }

    @Test
    /**
    * Test that LIC2 correctly returns true when requirements are met.
    * Requirements: See `LIC2` documentation
    * Contract:    
    *   Precondition:   There exists at least one set of three consecutive data points which
    *   form an angle such that: `angle` < (PI − EPSILON)  
    *   Postcondition:  `LIC2` returns true.
    */
    public void LIC2_testTrue(){
        double[] X = {1, 1, 3, 3, 4, 6};
        double[] Y = {1, 1, 1, -2, -3, 7};
        /*
        * Should be true, simple test (points X[1] Y[1], X[2] Y[2], X[3] Y[3]).
        */
        boolean res = DEFAULT.LIC2(X.length, X, Y, Math.PI/2);
        assertTrue("Should be true", res);
        /*
        * Should be true, points tested with calculator (points X[0] Y[0], X[1] Y[1], X[2] Y[2]).
        */
        double[] X1 = {1.9, 1.9, 1.95, 0.0};
        double[] Y1 = {4.0, 2.9, 4.0, 0.0};

        res = DEFAULT.LIC2(X.length, X1, Y1, Math.PI - Math.PI/36);
        assertTrue("Should be True", res);
    }

    @Test
    /**
    * Test that LIC2 correctly returns false when requirements are not met.
    * Requirements: See `LIC2` documentation
    * Contract:    
    *   Precondition:   There does not exists at least one set of three consecutive data points which
    *   form an angle such that: `angle` < (PI − EPSILON) or `angle` > (PI + EPSILON).
    *   Postcondition:  `LIC2` returns false.
    */
    public void LIC2_testFalse(){
        double[] X = {1.9, 1.9, 2.0};
        double[] Y = {4.0, 3.0, 4.0};
        /*
        * Should be false, points tested with calculator
        */
        boolean res = DEFAULT.LIC2(X.length, X, Y, Math.PI - Math.PI/36);
        assertFalse("Should be false", res);
    }

    @Test
    /**
     * Requirements: See `LIC3` documentation
     * Contract:    
     *      Precondition:   `area1` = 0
     *      Postcondition:  `LIC3` returns true, irrespective of the order of the points
     */
    public void LIC3TestZeroArea() {
        double[] xs = {1, 2, 3};
        double[] ys = {1, 2, 1};
        int[][] orders = {{0, 1, 2}, {1, 2, 0}, {2, 0, 1}, {0, 2, 1}};
        for (int[] order : orders) {
            // This tests that the area-calculation correctly handles all possible orders of points.
            // If it fails, it's probably missing an absolute value in the formula.
            double[] x = {xs[order[0]], xs[order[1]], xs[order[2]]};
            double[] y = {ys[order[0]], ys[order[1]], ys[order[2]]};
            var res = DEFAULT.LIC3(3, x, y, 0);
            assertTrue("LIC3 should always return true when area1 = 0", res);
        }
    }

    @Test
    /**
     * Requirements: See `LIC3` documentation
     * Contract:    
     *      Precondition:   There exists three consecutive points that form a traingle with area greater than `area1`
     *      Postcondition:  `LIC3` returns true
     */
    public void LIC3TestPositive() {
        double[] x = {4, 0.5, 2, 2.3};
        double[] y = {1.5, 3.5, 1, 3.5};
        double area1 = 2.8;
        var res = DEFAULT.LIC3(4, x, y, area1);
        assertTrue("The area of (4, 1.5), (0.5, 3.5), (2, 1) is 2.875 which is greater than 2.8", res);
    }

    @Test
    /**
     * Requirements: See `LIC3` documentation
     * Contract:    
     *      Precondition:   No set of three consecutive points form a traingle with area greater than `area1`
     *      Postcondition:  `LIC3` returns false
     */
    public void LIC3TestNegative() {
        double[] x = {4, 0.5, 2, 2.3};
        double[] y = {1.5, 3.5, 1, 3.5};
        double area1 = 2.9;
        var res = DEFAULT.LIC3(4, x, y, area1);
        assertFalse("The area of (4, 1.5), (0.5, 3.5), (2, 1) is 2.875 which is less than 2.9", res);
    }
}
