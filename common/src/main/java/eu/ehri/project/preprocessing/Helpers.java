package eu.ehri.project.preprocessing;

import javax.xml.stream.events.XMLEvent;

public class Helpers {
    static boolean isStartElement(XMLEvent event, String tag) {
        return event.isStartElement() &&
                event.asStartElement().getName().getLocalPart().endsWith(tag);
    }

    static boolean isEndElement(XMLEvent event, String tag) {
        return event.isEndElement() &&
                event.asEndElement().getName().getLocalPart().endsWith(tag);
    }
}
