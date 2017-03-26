package project.taskcrusher.storage;

import java.io.IOException;
import java.util.Optional;

import project.taskcrusher.commons.exceptions.DataConversionException;
import project.taskcrusher.commons.util.JsonUtil;
import project.taskcrusher.model.UserPrefs;

/**
 * A class to access UserPrefs stored in the hard disk as a json file
 */
public class JsonUserPrefsStorage implements UserPrefsStorage {

    private String filePath;

    public JsonUserPrefsStorage(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return readUserPrefs(filePath);
    }

    /**
     * Similar to {@link #readUserPrefs()}
     * @param prefsFilePath location of the data. Cannot be null.
     * @throws DataConversionException if the file format is not as expected.
     */
    public Optional<UserPrefs> readUserPrefs(String prefsFilePath) throws DataConversionException {
        return JsonUtil.readJsonFile(prefsFilePath, UserPrefs.class);
    }

    @Override
    public void saveUserPrefs(UserPrefs userPrefs) throws IOException {
        JsonUtil.saveJsonFile(userPrefs, filePath);
    }

}
