package eu.ehri.project.preprocessing;

import javax.xml.stream.*;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

abstract class TransformerTask {

    protected final XMLOutputFactory factory = XMLOutputFactory.newInstance();
    protected final XMLEventFactory eventFactory = XMLEventFactory.newInstance();

    protected final String eadFilePath;

    public TransformerTask(String eadFilePath) {
        this.eadFilePath = eadFilePath;
    }

    public void run(String outputPath) throws XMLStreamException, FactoryConfigurationError, IOException {

        try (FileInputStream fileInputStreamEAD = new FileInputStream(eadFilePath);
             FileWriter fileWriter = new FileWriter(outputPath)) {

            XMLEventWriter writer = factory.createXMLEventWriter(fileWriter);
            XMLEventReader reader = XMLInputFactory.newInstance()
                    .createXMLEventReader(fileInputStreamEAD);
            try  {
                transform(reader, writer);
            } finally {
                writer.close();
                reader.close();
            }
        }
    }

    abstract void transform(XMLEventReader reader, XMLEventWriter writer) throws XMLStreamException, IOException;
}
