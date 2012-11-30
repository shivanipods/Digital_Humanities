package common;


import java.net.URL;

/**
 * 
 */
public class Resources {
    
    /**
     * Do not construct an instance of this class.
     */
    private Resources() {
        
    }

    /**
     * Return the URL of the filename under the resources directory
     * @param filename file_path
     * @return it returns the URL of that file 
     */
    public static URL getResource(String filename) {    
         URL url = Resources.class.getResource(filename); 
         return url;
    }
    
    
}
