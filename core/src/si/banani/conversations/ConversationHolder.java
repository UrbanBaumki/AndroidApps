package si.banani.conversations;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

import si.banani.serialization.Serializer;

/**
 * Created by Urban on 27.1.2017.
 */

public class ConversationHolder {

    private static ChapterConversation CHAPTER_CONVERSATION;

    private static String current_chapter = "11";
    private static Conversation current_conversation ;

    public static int currentSpeaker = 0;
    public static int currentIndex = 0;


    private static ConversationHolder conversationHolder;

    ConversationHolder(){
        Json json = new Json();
        CHAPTER_CONVERSATION =  json.fromJson(ChapterConversation.class, Gdx.files.internal("bin/conversations.sav"));
        current_conversation = CHAPTER_CONVERSATION.getChapterConversation(current_chapter);
    }

    public static ConversationHolder getInstance(){
        if(conversationHolder ==null)
            conversationHolder = new ConversationHolder();
        return conversationHolder;
    }


    public static String getCurrentText(){
        String s =  current_conversation.getDialogOfIndex(currentIndex);

        return s;
    }
    public static void next(){
        currentIndex ++;
        currentSpeaker = 1 - currentSpeaker;
    }
    public static String getCurrent_chapter(){ return current_chapter;}
    public static void setCurrent_chapter(String ch){
        current_chapter = ch;
        current_conversation = CHAPTER_CONVERSATION.getChapterConversation(current_chapter);
        currentSpeaker = 0; currentIndex = 0;
    }
    public static Conversation getCurrent_conversation(){ return current_conversation; }
    public static int getCurrentSpeaker(){ return currentSpeaker; }
}
