package t15b1.taskcrusher.storage;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import t15b1.taskcrusher.commons.exceptions.DataConversionException;
import t15b1.taskcrusher.commons.util.XmlUtil;

/**
 * Stores userInbox data in an XML file
 */
public class XmlFileStorage {
    /**
     * Saves the given userInbox data to the specified file.
     */
    public static void saveDataToFile(File file, XmlSerializableUserInbox addressBook)
            throws FileNotFoundException {
        try {
            XmlUtil.saveDataToFile(file, addressBook);
        } catch (JAXBException e) {
            assert false : "Unexpected exception " + e.getMessage();
        }
    }

    /**
     * Returns userInbox book in the file or an empty user Inbox
     */
    public static XmlSerializableUserInbox loadDataFromSaveFile(File file) throws DataConversionException,
                                                                            FileNotFoundException {
        try {
            return XmlUtil.getDataFromFile(file, XmlSerializableUserInbox.class);
        } catch (JAXBException e) {
            throw new DataConversionException(e);
        }
    }

}
