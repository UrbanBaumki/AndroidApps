package si.banani.sound;

/**
 * Created by Urban on 20.1.2017.
 */

public interface AudioSubject {


    void addObserver(AudioObserver observer);
    void removeObserver(AudioObserver observer);
    void removeAllObservers();
    void notify(AudioObserver.AudioCommand command, AudioObserver.AudioTypeEvent event);

}
