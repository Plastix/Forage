package io.github.plastix.forage.util;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class UnitUtilsTest {

    @Test
    public void milesToKilometer_isCorrect() {
        assertThat(UnitUtils.milesToKilometer(0)).isWithin(0).of(0);
        assertThat(UnitUtils.milesToKilometer(1)).isWithin(0).of(1.6093444978925633);
        assertThat(UnitUtils.milesToKilometer(0.5)).isWithin(0).of(0.8046722489462816);
        assertThat(UnitUtils.milesToKilometer(2)).isWithin(0).of(3.2186889957851266);

    }

    @Test
    public void metersToKilometers_isCorrect() {
        assertThat(UnitUtils.metersToKilometers(0)).isWithin(0).of(0);
        assertThat(UnitUtils.metersToKilometers(1)).isWithin(0).of(0.001);
        assertThat(UnitUtils.metersToKilometers(500)).isWithin(0).of(0.500);
        assertThat(UnitUtils.metersToKilometers(1000)).isWithin(0).of(1);
        assertThat(UnitUtils.metersToKilometers(1500)).isWithin(0).of(1.5);
    }

    @Test
    public void metersToMiles_isCorrect() {
        assertThat(UnitUtils.metersToMiles(0)).isWithin(0).of(0);
        assertThat(UnitUtils.metersToMiles(804.672)).isWithin(0.1).of(0.5);
        assertThat(UnitUtils.metersToMiles(1609.34)).isWithin(0.1).of(1);
        assertThat(UnitUtils.metersToMiles(3218.69)).isWithin(0.1).of(2);
    }

    @Test
    public void milesToFeet_isCorrect() {
        assertThat(UnitUtils.milesToFeet(1)).isWithin(0).of(5280);
        assertThat(UnitUtils.milesToFeet(0.5)).isWithin(0).of(2640);
        assertThat(UnitUtils.milesToFeet(0)).isWithin(0).of(0);
        assertThat(UnitUtils.milesToFeet(3)).isWithin(0).of(15840);
    }
}
