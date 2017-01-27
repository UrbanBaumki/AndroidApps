package si.banani.conversations;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import java.util.HashMap;

/**
 * Created by Urban on 27.1.2017.
 */

public class ChapterConversation {
    private HashMap<String, Conversation> conversations = new HashMap<String, Conversation>();
    public ChapterConversation(){

    }
    public Conversation getChapterConversation(String chapterAndNumber){
        return conversations.get(chapterAndNumber);
    }
    public void addConversation (String key, Conversation conversation){
        conversations.put(key, conversation);
    }
    public void removeConversation(String key){
        conversations.remove(key);
    }

    public HashMap<String, Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(HashMap<String, Conversation> conversations) {
        this.conversations = conversations;
    }
    public void toJson(){
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        System.out.println(json.toJson(this));
    }
}
