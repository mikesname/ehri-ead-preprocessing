package eu.ehri.project.preprocessing;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import static eu.ehri.project.preprocessing.Helpers.isEndElement;
import static eu.ehri.project.preprocessing.Helpers.isStartElement;

public class UseDID_ID_Label {

    /**
     * extract identifiers used by institutionen like City
     * Archives Amsterdam, where identifier is given as attribute
     * in the <did> node(<did id="xxxx">
     */

    // for instance archives amsterdam
    public static void use_did_label(String eadfile)
            throws XMLStreamException, FactoryConfigurationError, IOException {

        String outputPath = eadfile.replace(".xml", "_mainids.xml");

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();

        try (FileInputStream fileInputStreamEAD = new FileInputStream(eadfile);
             FileWriter fileWriter = new FileWriter(outputPath)) {

            XMLEventWriter writer = factory.createXMLEventWriter(fileWriter);
            XMLEventReader reader = XMLInputFactory.newInstance()
                    .createXMLEventReader(fileInputStreamEAD);

            XMLEvent end = eventFactory.createDTD("\n");

            String value = null;

            boolean top = false;

            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();

                writer.add(event);

                if (isStartElement(event, "revisiondesc")) {
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
                            .createCharacters("EHRI added a unitid with label \"ehri_main_identifier\" to indicate the"
                                    + " identifier provided by the institution that EHRI will use as the identifier of"
                                    + " the unit."));
                    writer.add(eventFactory.createEndElement("", null,
                            "item"));
                    writer.add(end);
                    writer.add(eventFactory.createEndElement("", null,
                            "change"));
                }

                if (isStartElement(event, "did")) {
                    @SuppressWarnings("unchecked")
                    Iterator<Attribute> attributes = event.asStartElement()
                            .getAttributes();
                    while (attributes.hasNext()) {
                        Attribute attribute = attributes.next();
                        if (attribute.getName().toString().equals("id")) {
                            value = attribute.getValue();
                        }
                    }
                }

                if (event.isStartElement()) {
                    if (event.asStartElement().getName().getLocalPart()
                            .equals("archdesc")) {
                        top = true;
                    }
                }

                if (isEndElement(event, "did")) {
                    top = false;
                }

                if (isEndElement(event, "head")) {
                    if (top) {
                        event = reader.nextEvent();
                        writer.add(end);
                        writer.add(eventFactory.createStartElement("", null,
                                "unitid"));
                        writer.add(eventFactory.createAttribute("label",
                                "ehri_main_identifier"));
                        writer.add(eventFactory.createCharacters(value));
                        writer.add(eventFactory.createEndElement("", null,
                                "unitid"));
                        writer.add(end);
                    }
                }

                if (isStartElement(event, "did") && !top) {
                    reader.nextEvent();
                    writer.add(end);
                    writer.add(eventFactory.createStartElement("", null,
                            "unitid"));
                    writer.add(eventFactory.createAttribute("label",
                            "ehri_main_identifier"));
                    writer.add(eventFactory.createCharacters(value));
                    writer.add(eventFactory.createEndElement("", null,
                            "unitid"));
                    writer.add(end);
                }
            }
            writer.close();
            reader.close();
        }
    }
}
