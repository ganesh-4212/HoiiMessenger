package com.kempasolutions.app.hoiimessanger;

import android.content.Context;
import android.content.res.AssetManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Created by Archana on 8/12/2016.
 */
public class PropertyWriter {
    private Context context;

    public PropertyWriter(Context context) {
        this.context = context;
    }

    public void saveProperties(Properties properties) {
        try {
            File propertyFile=new File(context.getFilesDir()+"/app.properties");
            if(!propertyFile.exists()) propertyFile.createNewFile();
          FileOutputStream out = new FileOutputStream(propertyFile);
            properties.store(out,"");
            out.close();

        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }
}
