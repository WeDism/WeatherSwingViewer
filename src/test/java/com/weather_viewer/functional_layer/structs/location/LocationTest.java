package com.weather_viewer.functional_layer.structs.location;

import com.weather_viewer.functional_layer.structs.location.concrete_location.City;
import com.weather_viewer.functional_layer.structs.location.concrete_location.Country;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.Is.is;

public class LocationTest {

    @Test
    public void equalsLocationCity() {
        City moscow = new City("Moscow");
        City moscow1 = new City("moscow");
        City samara = new City("samara");
        Assert.assertTrue("Assert cities", moscow.equals(moscow1));
        Assert.assertTrue("Assert cities", moscow1.equals(moscow));
        Assert.assertNotEquals("Assert not equals", samara.equals(moscow));
    }

    @Test
    public void equalsLocationCountry() {
        Country uk = new Country("uk");
        Country uk1 = new Country("uK");
        Country ru = new Country("Ru");
        Assert.assertTrue("Assert cities", uk.equals(uk1));
        Assert.assertTrue("Assert cities", uk1.equals(uk));
        Assert.assertThat(uk1, is(uk));
        Assert.assertNotEquals("Assert not equals", ru.equals(uk));
    }

    @Test
    public void hashCodeLocation() {
        Country uk = new Country("uk");
        Country uk1 = new Country("uK");
        Country ru = new Country("Ru");
        Assert.assertEquals(uk.hashCode(), uk1.hashCode());
        Assert.assertNotEquals(uk.hashCode(), ru.hashCode());
    }

    @Test
    public void newInstance() {
        Country us1 = new Country("Us");
        Country us = Location.newInstance(us1);
        Assert.assertTrue(!(us == us1));
        Assert.assertThat(us, is(us1));
    }
}