package si.banani.serialization;


import java.util.HashMap;

import si.banani.conversations.ChapterConversation;
import si.banani.maps.MapFactory;

/**
 * Created by Urban on 5.2.2017.
 */

public class ChapterDescriptor {
    private HashMap<String, Chapter> chapterData = new HashMap<String, Chapter>();

    public ChapterDescriptor(){

    }

    public Chapter getChapter(String mapType){
        return chapterData.get(mapType);
    }
    public void addChapter(String mapType, Chapter chapter){
        chapterData.put(mapType, chapter);
    }

}
