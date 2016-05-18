package eu.ehri.project.preprocessing;

import org.junit.Test;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;

/**
 * Unit test for simple AddPaths.
 */
public class AddPathsTest {


    /**
     * Rigourous Test :-)
     */
    @Test
    public void testApp() throws XMLStreamException, FactoryConfigurationError, IOException {
        URL url = ClassLoader.getSystemResource("its-gestapo.xml");
//        System.out.println(url);
        StringWriter writer = new StringWriter();
        AddPaths.addPath(url.getPath(), writer);

//        System.out.println(writer.toString());
    }
}
