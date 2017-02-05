package si.banani.serialization;

/**
 * Created by Urban on 5.2.2017.
 */

public class ProgressDescriptor {

    //Another POJO for easy serialization :)

    private String lastChapter;
    private Integer lastLevelOfChapter;
    private boolean chapterFinished;
    public ProgressDescriptor(){

    }

    public String getLastChapter() {
        return lastChapter;
    }

    public void setLastChapter(String lastChapter) {
        this.lastChapter = lastChapter;
    }

    public Integer getLastLevelOfChapter() {
        return lastLevelOfChapter;
    }

    public void setLastLevelOfChapter(Integer lastLevelOfChapter) {
        this.lastLevelOfChapter = lastLevelOfChapter;
    }

    public boolean isChapterFinished() {
        return chapterFinished;
    }

    public void setChapterFinished(boolean chapterFinished) {
        this.chapterFinished = chapterFinished;
    }
}
