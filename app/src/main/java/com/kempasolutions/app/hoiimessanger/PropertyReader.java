package com.kempasolutions.app.hoiimessanger;

import android.content.Context;
import android.content.res.AssetManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Archana on 8/12/2016.
 */
public class PropertyReader {

    private Context context;
    private Properties properties;

    public PropertyReader(Context context) {
        this.context = context;
        properties = new Properties();
    }

    public Properties getMyProperties() {
        try {
            File propertyFile=new File(context.getFilesDir()+"/app.properties");
            if(!propertyFile.exists()) propertyFile.createNewFile();
            InputStream inputStream = new FileInputStream(propertyFile);
            properties.load(inputStream);
            inputStream.close();

        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        return properties;
    }
}