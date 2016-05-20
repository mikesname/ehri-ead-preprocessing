package eu.ehri.project.preprocessing;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.IOException;

import static eu.ehri.project.preprocessing.Helpers.isStartElement;

public class TagFinder {
    public static boolean hasTag(String eadPath, String tag) throws XMLStreamException, FactoryConfigurationError, IOException {
        try (FileInputStream fileInputStreamEAD = new FileInputStream(eadPath)) {
            XMLEventReader xmlEventReaderEAD = XMLInputFactory.newInstance()
                    .createXMLEventReader(fileInputStreamEAD);
            try {
                while (xmlEventReaderEAD.hasNext()) {
                    XMLEvent event = xmlEventReaderEAD.nextEvent();
                    if (isStartElement(event, tag)) {
                        return true;
                    }
                }
            } finally {
                xmlEventReaderEAD.close();
            }
        }
        return false;
    }
}
