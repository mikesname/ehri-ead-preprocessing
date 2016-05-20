package eu.ehri.project.preprocessing;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Detect the kind of identifier used for each node and if it exist, add it as main EHRI identifier
 *
 */
public class FindMainIdentifier {

    public static final String DID = "DID_ID";
    public static final String UNITID = "UNITID";

    /**
     * usage:
     * choose one of these:
     * <p/>
     * cmd eadfile DID
     * cmd eadfile UNITID
     * cmd eadfile UNITID attributeName attributeValue
     *
     * @param args
     * @throws XMLStreamException
     * @throws FactoryConfigurationError
     * @throws IOException
     */
    public static void main(String[] args) throws XMLStreamException, FactoryConfigurationError, IOException {
        String eadFile = args[0];

        String identifier;
        String attrName = null;
        String attrValue = null;

        if (args.length >= 2) {
            switch (args[1]) {
                case DID:
                    identifier = DID;
                    break;
                case UNITID:
                    if (args.length == 4) {
                        attrName = args[2];
                        attrValue = args[3];
                    }
                    identifier = UNITID;
                    break;
                default:
                    System.out.format("unknown identifier-location given, expected %s or %s, was: %s", DID, UNITID, args[1]);
                    identifier = DetectIdentifier.detectIdentifier(eadFile);
                    break;
            }
        } else {
            identifier = DetectIdentifier.detectIdentifier(eadFile);
        }
        System.out.printf("Identifier is %s (%s)\n", identifier, eadFile);
        String outputFile = eadFile.replace(".xml", "_mainids.xml");

        if (identifier.equals(DID)) {
            UseDID_ID_Label.use_did_label(eadFile);
        } else if (identifier.equals(UNITID)) {
            UseUNITID_Tag.use_unitid_tag(eadFile, attrName, attrValue, new FileWriter(outputFile));
        }
    }
}
