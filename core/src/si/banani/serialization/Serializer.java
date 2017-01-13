package si.banani.serialization;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import java.io.File;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Urban on 11.1.2017.
 */

public class Serializer {

    private static Serializer serializer;
    private static FileHandle fileHandle;
    private static HashMap<String, FileHandle> files;
    private static HashMap<String, Object> objects;
    private static String filePath = "bin/";
    private static String suffix = ".sav";
    private static Json json;
    public Serializer(){
        files = new HashMap<String, FileHandle>();
        objects = new HashMap<String, Object>();
        json = new Json();
        readAndStoreFiles();
    }

    public static Serializer getInstance(){
        if(serializer == null)
        {
            serializer = new Serializer();
        }
        return serializer;
    }

    public  void readAndStoreFiles(){

        if(Gdx.files.isLocalStorageAvailable()){

            FileHandle[] fileHandles = Gdx.files.local("./bin").list(suffix);

            for( FileHandle f : fileHandles){

                files.put(f.nameWithoutExtension(), f);

            }

        }else
            return;

    }

    public  boolean doesFileExist(String fileName){
        return files.containsKey(fileName);
    }
    public  void addObjectDescriptor(String fileName, Object object){
        objects.put(fileName, object);

    }

    public  void serializeAllObjects(){
        Iterator it = objects.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)it.next();
            saveObject((String) pair.getKey() , pair.getValue(), true);
            it.remove();
        }
    }

    public  void saveObject(String fileName, Object object, boolean overwrite){

        String fullName = fileName + suffix;

        if(Gdx.files.local(filePath  + fullName).exists() && !overwrite)
            return;

        FileHandle file = null;

        if(Gdx.files.isLocalStorageAvailable()){
            json.setOutputType(JsonWriter.OutputType.json);
            file = Gdx.files.local(filePath+fullName);
            file.writeString(json.toJson(object), !overwrite);
            files.put(fileName, file);
        }

        objects.put(fileName, object);

    }
    public   <T extends Object> T loadDescriptor(Class<T> type, String fileName){

        T serializedObject = null;

        if(!objects.containsKey(fileName))
        {
            if(files.containsKey(fileName))
                serializedObject = json.fromJson(type, files.get(fileName).readString());

        }else
            serializedObject = (T) objects.get(fileName);

        return serializedObject;
    }

}
