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

import static eu.ehri.project.preprocessing.Helpers.isEndElement;
import static eu.ehri.project.preprocessing.Helpers.isStartElement;

/**
 * Add a path to each node of an EAD tree
 */
public class AddPaths {

    public static void main(String[] args) throws XMLStreamException, FactoryConfigurationError, IOException {
        String eadFile = args[0];
        addPath(eadFile, eadFile.replace(".xml", "_wpath.xml"));
    }

    public static void addPath(String eadfile, String outputPath) throws XMLStreamException, FactoryConfigurationError, IOException {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();

        try (FileInputStream fileInputStreamEAD = new FileInputStream(eadfile);
             FileWriter fileWriter = new FileWriter(outputPath)) {

            XMLEventWriter writer = factory.createXMLEventWriter(fileWriter);
            XMLEventReader reader = XMLInputFactory.newInstance()
                    .createXMLEventReader(fileInputStreamEAD);

            XMLEvent end = eventFactory.createDTD("\n");

            boolean hasRevDesc = TagFinder.hasTag(eadfile, "revisiondesc");
            boolean hasHead = TagFinder.hasTag(eadfile, "head");
            boolean hasProfileDesc = TagFinder.hasTag(eadfile, "profiledesc");

            String node = "";
            String top = "0";
            int cntC01 = -1;
            int cntC02 = -1;
            int cntC03 = -1;
            int cntC04 = -1;
            int cntC05 = -1;
            int cntC06 = -1;
            int cntC07 = -1;
            int cntC08 = -1;
            int cntC09 = -1;
            int cntC10 = -1;
            int cntC11 = -1;
            int cntC12 = -1;
            boolean toplevel = false;

            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                writer.add(event);

                if (hasRevDesc) {
                    if (isStartElement(event, "revisiondesc")) {
                        node = "revisiondesc";
                        writeProvenance(writer, end, eventFactory);
                    }
                } else if (!hasRevDesc && hasProfileDesc) {
                    if (isEndElement(event, "profiledesc")) {
                        writer.add(end);
                        writer.add(eventFactory.createStartElement("", null, "revisiondesc"));
                        writeProvenance(writer, end, eventFactory);
                        writer.add(eventFactory.createEndElement("", null, "revisiondesc"));
                    }
                } else if (!hasHead && !hasProfileDesc) {
                    if (isEndElement(event, "filedesc")) {
                        writer.add(end);
                        writer.add(eventFactory.createStartElement("", null, "revisiondesc"));
                        writeProvenance(writer, end, eventFactory);
                        writer.add(eventFactory.createEndElement("", null, "revisiondesc"));
                    }
                }

                if (isStartElement(event, "archdesc")) {
                    node = "topnode";
                    toplevel = true;
                }

                if (isEndElement(event, "did")) {
                    toplevel = false;
                }
                if (isStartElement(event, "c01")) {
                    cntC01++;
                    node = "c01";
                }
                if (isStartElement(event, "c02")) {
                    node = "c02";
                    cntC02++;
                }

                if (isStartElement(event, "c03")) {
                    node = "c03";
                    cntC03++;
                }
                if (isStartElement(event, "c04")) {
                    node = "c04";
                    cntC04++;
                }

                if (isStartElement(event, "c05")) {
                    node = "c05";
                    cntC05++;
                }
                if (isStartElement(event, "c06")) {
                    node = "c06";
                    cntC06++;
                }
                if (isStartElement(event, "c07")) {
                    node = "c07";
                    cntC07++;
                }
                if (isStartElement(event, "c08")) {
                    node = "c08";
                    cntC08++;
                }
                if (isStartElement(event, "c09")) {
                    node = "c09";
                    cntC09++;
                }
                if (isStartElement(event, "c10")) {
                    node = "c10";
                    cntC10++;
                }
                if (isStartElement(event, "c11")) {
                    node = "c11";
                    cntC11++;
                }
                if (isStartElement(event, "c12")) {
                    node = "c12";
                    cntC12++;
                }

                if (isEndElement(event, "head") && toplevel) {
                    writer.add(end);
                    writer.add(eventFactory.createStartElement("", null, "unitid"));
                    writer.add(eventFactory.createAttribute("label", "ehri_structure"));
                    writer.add(eventFactory.createCharacters(top));
                    writer.add(eventFactory.createEndElement("", null, "unitid"));
                }

                if (isStartElement(event, "did") && toplevel && !hasHead) {
                    writer.add(end);
                    writer.add(eventFactory.createStartElement("", null, "unitid"));
                    writer.add(eventFactory.createAttribute("label", "ehri_structure"));
                    writer.add(eventFactory.createCharacters(top));
                    writer.add(eventFactory.createEndElement("", null, "unitid"));
                }

                if (isStartElement(event, "did") && !toplevel) {

                    writer.add(end);
                    writer.add(eventFactory.createStartElement("", null, "unitid"));
                    writer.add(eventFactory.createAttribute("label", "ehri_structure"));
                    if (node.equals("c01")) {
                        writer.add(eventFactory.createCharacters(top + "." + cntC01));
                    }
                    if (node.equals("c02")) {
                        writer.add(eventFactory.createCharacters(top + "." + cntC01 + "." + cntC02));
                    }
                    if (node.equals("c03")) {
                        writer.add(eventFactory.createCharacters(top + "." + cntC01 + "." + cntC02 + "." + cntC03));
                    }
                    if (node.equals("c04")) {
                        writer.add(eventFactory.createCharacters(top + "." + cntC01 + "." + cntC02 + "." + cntC03 + "."
                                + cntC04));
                    }
                    if (node.equals("c05")) {
                        writer.add(eventFactory.createCharacters(top + "." + cntC01 + "." + cntC02 + "." + cntC03 + "."
                                + cntC04 + "." + cntC05));
                    }
                    if (node.equals("c06")) {
                        writer.add(eventFactory.createCharacters(top + "." + cntC01 + "." + cntC02 + "." + cntC03 + "."
                                + cntC04 + "." + cntC05 + "." + cntC06));
                    }
                    if (node.equals("c07")) {
                        writer.add(eventFactory.createCharacters(top + "." + cntC01 + "." + cntC02 + "." + cntC03 + "."
                                + cntC04 + "." + cntC05 + "." + cntC06 + "." + cntC07));
                    }
                    if (node.equals("c08")) {
                        writer.add(eventFactory.createCharacters(top + "." + cntC01 + "." + cntC02 + "." + cntC03 + "."
                                + cntC04 + "." + cntC05 + "." + cntC06 + "." + cntC07 + "." + cntC08));
                    }
                    if (node.equals("c09")) {
                        writer.add(eventFactory.createCharacters(top + "." + cntC01 + "." + cntC02 + "." + cntC03 + "."
                                + cntC04 + "." + cntC05 + "." + cntC06 + "." + cntC07 + "." + cntC08 + "." + cntC09));
                    }
                    if (node.equals("c10")) {
                        writer.add(eventFactory.createCharacters(top + "." + cntC01 + "." + cntC02 + "." + cntC03 + "."
                                + cntC04 + "." + cntC05 + "." + cntC06 + "." + cntC07 + "." + cntC08 + "." + cntC09 + "."
                                + cntC10));
                    }
                    if (node.equals("c11")) {
                        writer.add(eventFactory.createCharacters(top + "." + cntC01 + "." + cntC02 + "." + cntC03 + "."
                                + cntC04 + "." + cntC05 + "." + cntC06 + "." + cntC07 + "." + cntC08 + "." + cntC09 + "."
                                + cntC10 + "." + cntC11));
                    }
                    if (node.equals("c12")) {
                        writer.add(eventFactory.createCharacters(top + "." + cntC01 + "." + cntC02 + "." + cntC03 + "."
                                + cntC04 + "." + cntC05 + "." + cntC06 + "." + cntC07 + "." + cntC08 + "." + cntC09 + "."
                                + cntC10 + "." + cntC11 + "." + cntC12));
                    }
                    writer.add(eventFactory
                            .createEndElement("", null, "unitid"));
                }

                if (isEndElement(event, "c01")) {
                    cntC02 = 0;
                }
                if (isEndElement(event, "c02")) {
                    cntC03 = 0;
                }
                if (isEndElement(event, "c03")) {
                    cntC04 = 0;
                }
                if (isEndElement(event, "c04")) {
                    cntC05 = 0;
                }
                if (isEndElement(event, "c05")) {
                    cntC06 = 0;
                }
                if (isEndElement(event, "c06")) {
                    cntC07 = 0;
                }
                if (isEndElement(event, "c07")) {
                    cntC08 = 0;
                }
                if (isEndElement(event, "c08")) {
                    cntC09 = 0;
                }
                if (isEndElement(event, "c09")) {
                    cntC10 = 0;
                }
                if (isEndElement(event, "c10")) {
                    cntC11 = 0;
                }
                if (isEndElement(event, "c11")) {
                    cntC12 = 0;
                }
            }
            writer.close();
            reader.close();
        }
    }

    private static void writeProvenance(XMLEventWriter writer, XMLEvent end, XMLEventFactory eventFactory) throws XMLStreamException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));

        writer.add(end);
        writer.add(eventFactory.createStartElement("", null, "change"));
        writer.add(end);
        writer.add(eventFactory.createStartElement("", null, "date"));
        writer.add(eventFactory.createCharacters(dateFormat.format(date)));
        writer.add(eventFactory.createEndElement("", null, "date"));
        writer.add(end);
        writer.add(eventFactory.createStartElement("", null, "item"));
        writer.add(eventFactory
                .createCharacters("EHRI added a unitid with label \"ehri_structure\" to indicate the "
                        + "structure of the EAD file on every c-node. This is done to make comparisons"
                        + " of two versions of the same EAD (as indicated by the eadid) possible."));
        writer.add(eventFactory.createEndElement("", null, "item"));
        writer.add(end);
        writer.add(eventFactory.createEndElement("", null, "change"));
        writer.add(end);
    }
}
