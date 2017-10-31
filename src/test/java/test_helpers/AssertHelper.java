package test_helpers;

import org.junit.Assert;

public class AssertHelper {
    public static void assertTrue(String str, int expected, int actual) {
        Assert.assertTrue(String.format(str, expected, actual), actual == expected);
    }

}
