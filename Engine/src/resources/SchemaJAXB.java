package resources;

import resources.generated.generated.AbsDescriptor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class SchemaJAXB {
    public static AbsDescriptor descList;

    private final static String JAXB_XML_GAME_PACKAGE_NAME = "resources.generated.generated";

    public static void getDescList(String fileName) {
        try {
            InputStream inputStream = new FileInputStream(fileName);
            descList = deserializeFrom(inputStream);

        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static AbsDescriptor deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (AbsDescriptor) u.unmarshal(in);
    }



}

