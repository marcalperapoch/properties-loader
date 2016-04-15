package com.vasplan.utilities.properties;

import com.vasplan.utilities.properties.model.PropertiesLoader;
import com.vasplan.utilities.properties.model.PropertiesLoaderException;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Created by marcalperapoch on 2/1/16.
 */
public class PropertiesLoaderTest {

    private static final String MY_TEST_PACKAGE = "com.vasplan.utilities.properties";

    private void initialize() {
        PropertiesLoader.initialize(MY_TEST_PACKAGE);
    }

    @Test
    public void shouldBeInitializedCorrectly() {
        initialize();
        assertThat(PropertiesLoader.isInitialized(), is(true));
    }

    @Test
    public void shouldLoadTheClass() {
        initialize();
        ExampleClass exampleClass = PropertiesLoader.getInstance(ExampleClass.class);
        assertThat(exampleClass, notNullValue());
    }

    @Test
    public void shouldLoadAllClassAttributes() {
        initialize();
        ExampleClass exampleClass = PropertiesLoader.getInstance(ExampleClass.class);
        assertThat(exampleClass.getMyInt(), equalTo(4));
        assertThat(exampleClass.getMyString(), equalTo("Hello world"));
        assertThat(exampleClass.getMyStringArray(), containsInAnyOrder("Hola", "3", "Adeu", "6"));
        assertThat(exampleClass.getMyIntegerArray(), containsInAnyOrder(8,9));
        assertThat(exampleClass.getMyNotListArray(), is(new int[]{1,2}));
        assertThat(exampleClass.getMyFloatArray(), is(new float[]{2.3f}));
        assertThat(exampleClass.getMyRegularExpression(), is("(\\d+)*"));
    }

    @Test
    public void shouldNotLoadAttributeIfNotPresent() {
        initialize();
        ExampleClass exampleClass = PropertiesLoader.getInstance(ExampleClass.class);
        assertThat(exampleClass.getMyNotInPropertiesAttribute(), nullValue());
    }

    @Test(expected = PropertiesLoaderException.class)
    public void shouldThrowExceptionIfNoPropertiesFilePresent() {
        initialize();
        ExampleClassWithNoPropertiesFile exampleClassWithNoPropertiesFile
                = PropertiesLoader.getInstance(ExampleClassWithNoPropertiesFile.class);
    }

    @Test(expected = PropertiesLoaderException.class)
    public void shouldThrowAnExceptionIfClassIsNull() {
        initialize();
        PropertiesLoader.getInstance(null);
    }
}
