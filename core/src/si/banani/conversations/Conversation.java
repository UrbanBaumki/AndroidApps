package si.banani.conversations;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;


/**
 * Created by Urban on 27.1.2017.
 */

public class Conversation {

    private Array<String> dialog = new Array<String>();

    public Conversation(){

    }

    public void putDialog(String text){
        dialog.add(text);
    }
    public Array<String> getDialog() {
        return dialog;
    }
    public String getDialogOfIndex(int i){
        if(i >= dialog.size)
            return null;
        return dialog.get(i);
    }
    public void setDialog(Array<String> dialog) {
        this.dialog = dialog;
    }

    public void toJson(){
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        System.out.println(json.toJson(this));
    }

}
