package eu.ehri.project.preprocessing;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Add an EHRI internal ID to each node of an EAD tree
 */

public class AddUnitIds {
    public static void main(String[] args) throws XMLStreamException,
            FactoryConfigurationError, IOException {

        String eadfile = args[0];
        String outputfile = eadfile.replace(".xml", "_ehriID.xml");

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        FileInputStream fileInputStreamEAD = new FileInputStream(eadfile);
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        XMLEventWriter writer = factory.createXMLEventWriter(new FileWriter(
                outputfile));
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();

        XMLEventReader xmlEventReaderEAD = XMLInputFactory.newInstance()
                .createXMLEventReader(fileInputStreamEAD);

        XMLEvent end = eventFactory.createDTD("\n");
        int counter = 0;
        boolean top = false;
        boolean hashead = LookForHead.hasHeadTag(eadfile);

        while (xmlEventReaderEAD.hasNext()) {
            XMLEvent event = xmlEventReaderEAD.nextEvent();
            writer.add(event);

            if (Helpers.isStartElement(event, "revisiondesc")) {
                writer.add(end);
                writer.add(eventFactory.createStartElement("", null,
                        "change"));
                writer.add(end);
                writer.add(eventFactory.createStartElement("", null,
                        "date"));
                writer.add(eventFactory.createCharacters(dateFormat.format(date)));
                writer.add(eventFactory.createEndElement("", null,
                        "date"));
                writer.add(end);
                writer.add(eventFactory.createStartElement("", null,
                        "item"));
                writer.add(eventFactory
                        .createCharacters("EHRI added a unitid with label \"ehri_internal_identifier\" to give every node a unique"
                                + " id."));
                writer.add(eventFactory.createEndElement("", null,
                        "item"));
                writer.add(end);
                writer.add(eventFactory.createEndElement("", null,
                        "change"));
            }

            if (Helpers.isStartElement(event, "archdesc")) {
                top = true;
            }
            if (Helpers.isEndElement(event, "did")) {
                top = false;
            }

            if (Helpers.isEndElement(event, "head") && top) {
                writer.add(end);
                writer.add(eventFactory.createStartElement("", null,
                        "unitid"));
                writer.add(eventFactory.createAttribute("label",
                        "ehri_internal_id"));
                writer.add(eventFactory.createCharacters("0"));
                writer.add(eventFactory
                        .createEndElement("", null, "unitid"));
                counter++;
            }

            if (Helpers.isStartElement(event, "did") && top && !hashead) {
                writer.add(end);
                writer.add(eventFactory.createStartElement("", null,
                        "unitid"));
                writer.add(eventFactory.createAttribute("label",
                        "ehri_internal_id"));
                writer.add(eventFactory.createCharacters("0"));
                writer.add(eventFactory
                        .createEndElement("", null, "unitid"));
                counter++;
            }

            if (Helpers.isStartElement(event, "did") && !top) {
                // && head == false) {
                writer.add(end);
                writer.add(eventFactory.createStartElement("", null,
                        "unitid"));
                writer.add(eventFactory.createAttribute("label",
                        "ehri_internal_id"));
                writer.add(eventFactory.createCharacters(String
                        .valueOf(counter)));
                writer.add(eventFactory
                        .createEndElement("", null, "unitid"));
                counter++;
            }
        }

        writer.close();
        xmlEventReaderEAD.close();
    }

}
