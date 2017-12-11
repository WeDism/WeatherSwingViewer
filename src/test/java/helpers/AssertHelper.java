package helpers;

import org.junit.Assert;

public class AssertHelper extends Assert{
    /**
     *  Extend assertTrue method with smart String
     * @param str format of string syntax
     * @param expected result
     * @param actual result
     */
    public static void assertTrue(String str, int expected, int actual) {
        Assert.assertTrue(String.format(str, expected, actual), actual == expected);
    }

}
