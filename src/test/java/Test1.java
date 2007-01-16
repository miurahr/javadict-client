package mt.rcasha.dict.client;

import java.util.List;
import junit.framework.TestCase;

public class Test1 extends TestCase {

  public static void testCase1() throws Exception {

    System.out.println("Definitions of word 'Hello':");

    DictClient client = new DictClient();
    List<DefinitionResponse> resp = client.getDefinitions(DictClient.DATABASE_ALL, "hello");

    for ( DefinitionResponse dr : resp ) {
      System.out.println(dr.getTextualInformation());
    }
  }
}
