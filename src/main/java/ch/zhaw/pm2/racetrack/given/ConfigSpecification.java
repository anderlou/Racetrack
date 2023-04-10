package ch.zhaw.pm2.racetrack.given;

import java.io.File;

/**
 * Interface representing the mandatory functions of the configuration class.<br/>
 * IMPORTANT: This interface shall not be altered!<br/>
 * It specifies elements we use to test Racetrack for grading.<br/>
 * You may change or extend the default implementation provided in
 * {@link Config}<br/>
 * Full Javadoc can be found in the implementation file.
 */
public interface ConfigSpecification {

    File getTrackDirectory();

    void setTrackDirectory(File trackDirectory);

    File getMoveDirectory();

    void setMoveDirectory(File moveDirectory);

    File getFollowerDirectory();

    void setFollowerDirectory(File followerDirectory);

}
