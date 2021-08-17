package zy.nav;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

/**
 * created by zhangyuan on 2020/5/29
 */
@RunWith(AndroidJUnit4.class)
public class NavUtilsTest extends NavBaseTest {

    @Test
    public void test_isParamEqual() {
        Assert.assertFalse(Utils.isParamEqual(null, null));
        Assert.assertFalse(Utils.isParamEqual(String.class, null));
        Assert.assertFalse(Utils.isParamEqual(Integer.class, null));
        Assert.assertFalse(Utils.isParamEqual(null, int.class));
        Assert.assertFalse(Utils.isParamEqual(null, void.class));
        Assert.assertTrue(Utils.isParamEqual(null, String.class));
        Assert.assertTrue(Utils.isParamEqual(null, Object.class));
        Assert.assertTrue(Utils.isParamEqual(null, Integer.class));


        Assert.assertTrue(Utils.isParamEqual(ArrayList.class, List.class));
        Assert.assertFalse(Utils.isParamEqual(List.class, ArrayList.class));


        Assert.assertTrue(Utils.isParamEqual(Integer.class, int.class));
        Assert.assertTrue(Utils.isParamEqual(int.class, int.class));
        Assert.assertTrue(Utils.isParamEqual(int.class, Integer.class));

        Assert.assertTrue(Utils.isParamEqual(Boolean.class, Boolean.TYPE));
        Assert.assertTrue(Utils.isParamEqual(Boolean.class, boolean.class));
        Assert.assertTrue(Utils.isParamEqual(boolean.class, Boolean.class));

        Assert.assertTrue(Utils.isParamEqual(Byte.class, Byte.TYPE));
        Assert.assertTrue(Utils.isParamEqual(Byte.class, byte.class));
        Assert.assertTrue(Utils.isParamEqual(byte.class, Byte.class));

        Assert.assertTrue(Utils.isParamEqual(Character.class, Character.TYPE));
        Assert.assertTrue(Utils.isParamEqual(Character.class, char.class));
        Assert.assertTrue(Utils.isParamEqual(char.class, Character.class));

        Assert.assertTrue(Utils.isParamEqual(Short.class, Short.TYPE));
        Assert.assertTrue(Utils.isParamEqual(Short.class, short.class));
        Assert.assertTrue(Utils.isParamEqual(short.class, Short.class));

        Assert.assertTrue(Utils.isParamEqual(Integer.class, Integer.TYPE));
        Assert.assertTrue(Utils.isParamEqual(Integer.class, int.class));
        Assert.assertTrue(Utils.isParamEqual(int.class, Integer.class));

        Assert.assertTrue(Utils.isParamEqual(Long.class, Long.TYPE));
        Assert.assertTrue(Utils.isParamEqual(Long.class, long.class));
        Assert.assertTrue(Utils.isParamEqual(long.class, Long.class));

        Assert.assertTrue(Utils.isParamEqual(Float.class, Float.TYPE));
        Assert.assertTrue(Utils.isParamEqual(Float.class, float.class));
        Assert.assertTrue(Utils.isParamEqual(float.class, Float.class));

        Assert.assertTrue(Utils.isParamEqual(Double.class, Double.TYPE));
        Assert.assertTrue(Utils.isParamEqual(Double.class, double.class));
        Assert.assertTrue(Utils.isParamEqual(double.class, Double.class));

        Assert.assertTrue(Utils.isParamEqual(Long.class, Long.class));
        Assert.assertFalse(Utils.isParamEqual(Long.class, void.class));
        Assert.assertFalse(Utils.isParamEqual(Long.class, int.class));
        Assert.assertFalse(Utils.isParamEqual(int.class, Long.class));


        Assert.assertTrue(Utils.isParamEqual(Integer.class, Object.class));
        Assert.assertTrue(Utils.isParamEqual(int.class, Object.class));


        Assert.assertTrue(Utils.isParamEqual(Child.class, Parent.class));
        Assert.assertFalse(Utils.isParamEqual(Parent.class, Child.class));


        Assert.assertTrue(Utils.isParamEqual(Integer[].class, int[].class));
        Assert.assertTrue(Utils.isParamEqual(int[].class, Integer[].class));

        Assert.assertTrue(Utils.isParamEqual(Child[].class, Parent[].class));
        Assert.assertFalse(Utils.isParamEqual(Parent[].class, Child[].class));


    }

    static class Parent {

    }

    static class Child extends Parent {

    }
}
