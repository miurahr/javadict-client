package mt.rcasha.dict.client;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Test1 {

    @Test
    public void testCase1() throws Exception {

        // System.out.println("Definitions of word 'Hello':");

        final DictClient client = new DictClient("dico.gnu.org.ua");
        // final List<DefinitionResponse> resp = client.getDefinitions(
        // DictClient.DATABASE_ALL, "hello");
        //
        // for (final DefinitionResponse dr : resp) {
        // System.out.println(dr.getTextualInformation());
        // }
        final String EXPECT_STRING = "DEFINE database word";
        assertEquals(EXPECT_STRING, client.getHelp().substring(0, EXPECT_STRING.length()));

    }
}
