package eu.ehri.project.preprocessing;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;

/**
 * Set of rules to detect the main node identifier used
 * by the repository
 *
 */

public class DetectIdentifier {

    static String identifier = "";

    public static String detectIdentifier(String eadfile)
            throws FileNotFoundException, XMLStreamException,
            FactoryConfigurationError {

        FileInputStream fileInputStreamEAD = new FileInputStream(eadfile);
        XMLEventReader xmlEventReaderEAD = XMLInputFactory.newInstance()
                .createXMLEventReader(fileInputStreamEAD);

        while (xmlEventReaderEAD.hasNext()) {
            XMLEvent event = xmlEventReaderEAD.nextEvent();
            if (event.isStartElement()) {
                if (event.asStartElement().getName().getLocalPart()
                        .equals("did")) {

                    @SuppressWarnings("unchecked")
                    Iterator<Attribute> attributes = event.asStartElement()
                            .getAttributes();
                    while (attributes.hasNext()) {
                        if (attributes.next().getName().toString().equals("id")) {
                            identifier = FindMainIdentifier.DID;
                            return identifier;
                        }
                    }

                } else {

                    if (event.asStartElement().getName().getLocalPart()
                            .equals("unitid")) {
                        identifier = FindMainIdentifier.UNITID;
//						@SuppressWarnings("unchecked")
//						Iterator<Attribute> attributes = event.asStartElement()
//								.getAttributes();
//						if (!attributes.hasNext()) {
//							identifier = "UNITID";
//						}
//						if (attributes.hasNext()){
//							while (attributes.hasNext()) {
//								if (attributes.next().getName().toString().equals("Bestandssignatur")) {
//									identifier = "UNITID";
//								}
//							}
//
//						}
                    }

                }

            }
        }
        xmlEventReaderEAD.close();
        return identifier;
    }

}
