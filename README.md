# vasplan-utilities
The aim of this repository is to provide useful libs to make my (and so many people as possible) life easy.

## Properties Loader
This is the first lib of the repository. The idea behind it it's simple: Provide an easy way to **map properties files into Java classes**.

### How does it work?
Assuming that you've managed to add the library to your project (in a future I hope to be able to offer it as a Maven dependency) you will only need to do a few things:

* Create the _class_ where you want to map the content of the _properties_ file and annotate it with the following anotation: **@InPropertiesFile**. Ex:
```
@InPropertiesFile
public class MyExampleClass {

    private String url;
    private int port;

    public MyExampleClass() {

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
```
* Create the corresponding properties file with the same name as the previous class. **Atention**: the path of the properties file (inside the _resource_ folder) should be the same as the @InPropertiesFile annotated class. For example:
```
src/ 
|--	main/
	|-- java/
	|	|-- com.vasplan.testing.properties
	|		|-- MyExampleClass.java
	|-- resources/
		|-- com.vasplan.testing.properties
			|-- MyExampleClass.properties
```

* Then, when your application starts initialize the PropertiesLoader class with the main package of your application. This should be the maximum path that includes all your @InPropertiesFile classes. Ex:
```
public static void main(String[] args) {
    PropertiesLoader.initialize("com.vasplan.testing.properties");
}
```
* Finally you are ready to obtain your mapped class as:
```
MyExampleClass myExampleClass = PropertiesLoader.getInstance(MyExampleClass.class);
```

### Supported types
TODO

