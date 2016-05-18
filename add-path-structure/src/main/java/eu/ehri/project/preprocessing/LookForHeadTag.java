package eu.ehri.project.preprocessing;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


/**
 * Check whether <head> tag is present in the EAD
 *
 */


public class LookForHeadTag {

    public static boolean hasHeadTag(String eadfile) throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
        boolean result = false;

        FileInputStream fileInputStreamEAD = new FileInputStream(eadfile);

        XMLEventReader xmlEventReaderEAD = XMLInputFactory.newInstance()
                .createXMLEventReader(fileInputStreamEAD);

        while (xmlEventReaderEAD.hasNext()) {
            XMLEvent event = xmlEventReaderEAD.nextEvent();

            if (event.isStartElement()) {
                if (event.asStartElement().getName().getLocalPart()
                        .equals("head")) {
                    result = true;
                }
            }
        }

        return result;
    }

}
