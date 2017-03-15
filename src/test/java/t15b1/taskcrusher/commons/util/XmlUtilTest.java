package t15b1.taskcrusher.commons.util;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import t15b1.taskcrusher.commons.util.FileUtil;
import t15b1.taskcrusher.commons.util.XmlUtil;
import t15b1.taskcrusher.model.UserInbox;
import t15b1.taskcrusher.storage.XmlSerializableUserInbox;
import t15b1.taskcrusher.testutil.AddressBookBuilder;
import t15b1.taskcrusher.testutil.TestUtil;

public class XmlUtilTest {

    private static final String TEST_DATA_FOLDER = FileUtil.getPath("src/test/data/XmlUtilTest/");
    private static final File EMPTY_FILE = new File(TEST_DATA_FOLDER + "empty.xml");
    private static final File MISSING_FILE = new File(TEST_DATA_FOLDER + "missing.xml");
    private static final File VALID_FILE = new File(TEST_DATA_FOLDER + "validAddressBook.xml");
    private static final File TEMP_FILE = new File(TestUtil.getFilePathInSandboxFolder("tempAddressBook.xml"));

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getDataFromFile_nullFile_AssertionError() throws Exception {
        thrown.expect(AssertionError.class);
        XmlUtil.getDataFromFile(null, UserInbox.class);
    }

    @Test
    public void getDataFromFile_nullClass_AssertionError() throws Exception {
        thrown.expect(AssertionError.class);
        XmlUtil.getDataFromFile(VALID_FILE, null);
    }

    @Test
    public void getDataFromFile_missingFile_FileNotFoundException() throws Exception {
        thrown.expect(FileNotFoundException.class);
        XmlUtil.getDataFromFile(MISSING_FILE, UserInbox.class);
    }

    @Test
    public void getDataFromFile_emptyFile_DataFormatMismatchException() throws Exception {
        thrown.expect(JAXBException.class);
        XmlUtil.getDataFromFile(EMPTY_FILE, UserInbox.class);
    }

    @Test
    public void getDataFromFile_validFile_validResult() throws Exception {
        XmlSerializableUserInbox dataFromFile = XmlUtil.getDataFromFile(VALID_FILE, XmlSerializableUserInbox.class);
        assertEquals(9, dataFromFile.getTaskList().size());
        assertEquals(0, dataFromFile.getTagList().size());
    }

    @Test
    public void saveDataToFile_nullFile_AssertionError() throws Exception {
        thrown.expect(AssertionError.class);
        XmlUtil.saveDataToFile(null, new UserInbox());
    }

    @Test
    public void saveDataToFile_nullClass_AssertionError() throws Exception {
        thrown.expect(AssertionError.class);
        XmlUtil.saveDataToFile(VALID_FILE, null);
    }

    @Test
    public void saveDataToFile_missingFile_FileNotFoundException() throws Exception {
        thrown.expect(FileNotFoundException.class);
        XmlUtil.saveDataToFile(MISSING_FILE, new UserInbox());
    }

    @Test
    public void saveDataToFile_validFile_dataSaved() throws Exception {
        TEMP_FILE.createNewFile();
        XmlSerializableUserInbox dataToWrite = new XmlSerializableUserInbox(new UserInbox());
        XmlUtil.saveDataToFile(TEMP_FILE, dataToWrite);
        XmlSerializableUserInbox dataFromFile = XmlUtil.getDataFromFile(TEMP_FILE, XmlSerializableUserInbox.class);
        assertEquals((new UserInbox(dataToWrite)).toString(), (new UserInbox(dataFromFile)).toString());
        //TODO: use equality instead of string comparisons

        AddressBookBuilder builder = new AddressBookBuilder(new UserInbox());
        dataToWrite = new XmlSerializableUserInbox(
                builder.withPerson(TestUtil.generateSamplePersonData().get(0)).withTag("Friends").build());

        XmlUtil.saveDataToFile(TEMP_FILE, dataToWrite);
        dataFromFile = XmlUtil.getDataFromFile(TEMP_FILE, XmlSerializableUserInbox.class);
        assertEquals((new UserInbox(dataToWrite)).toString(), (new UserInbox(dataFromFile)).toString());
    }
}