package si.banani.serialization;

/**
 * Created by Urban on 4.2.2017.
 */

public interface ProfileObserver {
    public static enum ProfileEvent{
        PROFILE_LOADED,
        SAVING_PROFILE,
        CLEAR_CURRENT_PROFILE
    }

    void onNotify(final Serializer profileManager, ProfileEvent event);
}
