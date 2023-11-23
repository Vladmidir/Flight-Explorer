package FlightModel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class reads the config file and sets the values as system properties
 * This is done so that the values can be accessed from anywhere in the project
 */
public class ConfigReader {

    /**
     * This method reads the config file and sets the values as system properties
     */
    public void getPropValues() throws IOException {
        InputStream inputStream ;
        try {
            Properties prop = new Properties();
            String propFileName = "FlightModel/config.properties";

//            ClassLoader loader = Thread.currentThread().getContextClassLoader();
//            inputStream = loader.getResourceAsStream(propFileName);
            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            // get the property value and print it out
            String prop1 = prop.getProperty("AVIATIONSTACK_KEY");

            //after values are loaded you can do anything with them
            //here I will set them as System properties
            System.setProperty("AVIATIONSTACK_KEY",prop1);


        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
//            inputStream.close();
        }
    }
}