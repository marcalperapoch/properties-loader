package com.vasplan.utilities.properties;

import com.vasplan.utilities.properties.model.InPropertiesFile;

/**
 * Created by marcalperapoch on 2/1/16.
 */
@InPropertiesFile
public class ExampleClassWithNoPropertiesFile {

    private String myProperty;

    public String getMyProperty() {
        return myProperty;
    }

    public void setMyProperty(String myProperty) {
        this.myProperty = myProperty;
    }
}
