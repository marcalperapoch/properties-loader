package com.vasplan.utilities.properties;

import com.vasplan.utilities.properties.model.InPropertiesFile;

import java.util.List;

/**
 * Created by marcalperapoch on 2/1/16.
 */
@InPropertiesFile
public class ExampleClass {

    private int myInt;
    private String myString;
    private List<String> myStringArray;
    private List<Integer> myIntegerArray;
    private int[] myNotListArray;
    private float[] myFloatArray;
    private String myNotInPropertiesAttribute;
    private String myRegularExpression;

    public int getMyInt() {
        return myInt;
    }

    public void setMyInt(int myInt) {
        this.myInt = myInt;
    }

    public String getMyString() {
        return myString;
    }

    public void setMyString(String myString) {
        this.myString = myString;
    }

    public List<String> getMyStringArray() {
        return myStringArray;
    }

    public void setMyStringArray(List<String> myStringArray) {
        this.myStringArray = myStringArray;
    }

    public List<Integer> getMyIntegerArray() {
        return myIntegerArray;
    }

    public void setMyIntegerArray(List<Integer> myIntegerArray) {
        this.myIntegerArray = myIntegerArray;
    }

    public int[] getMyNotListArray() {
        return myNotListArray;
    }

    public void setMyNotListArray(int[] myNotListArray) {
        this.myNotListArray = myNotListArray;
    }

    public float[] getMyFloatArray() {
        return myFloatArray;
    }

    public void setMyFloatArray(float[] myFloatArray) {
        this.myFloatArray = myFloatArray;
    }

    public String getMyNotInPropertiesAttribute() {
        return myNotInPropertiesAttribute;
    }

    public void setMyNotInPropertiesAttribute(String myNotInPropertiesAttribute) {
        this.myNotInPropertiesAttribute = myNotInPropertiesAttribute;
    }

    public String getMyRegularExpression() {
        return myRegularExpression;
    }

    public void setMyRegularExpression(String myRegularExpression) {
        this.myRegularExpression = myRegularExpression;
    }
}
