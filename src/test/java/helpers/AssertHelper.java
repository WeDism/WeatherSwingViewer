package helpers;

import org.junit.Assert;

public class AssertHelper {
    /**
     *
     * @param str
     * @param expected
     * @param actual
     */
    public static void assertTrue(String str, int expected, int actual) {
        Assert.assertTrue(String.format(str, expected, actual), actual == expected);
    }

}
