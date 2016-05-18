package eu.ehri.project.preprocessing;

import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.assertEquals;


public class DetectIdentifierTest {


    @Test
    public void testDetectIdentifier() throws Exception {
        URL url = ClassLoader.getSystemResource("EAD_30246.xml");
//        System.out.println(url);
        String did = DetectIdentifier.detectIdentifier(url.getPath());
        assertEquals("DID_ID", did);
    }
}
