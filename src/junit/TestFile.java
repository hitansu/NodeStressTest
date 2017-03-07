package junit;

import java.util.Arrays;
import java.util.Collection;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import testengine.StressTestRunner;

@RunWith(Parameterized.class)
public class TestFile {

	private static StressTestRunner runnr;
	private static int initialNodes;
	public TestFile(int initialNodes)
	{
		TestFile.initialNodes = initialNodes;
	}
	
	@Parameterized.Parameters
	public static Collection<Integer[]> input() {
		return Arrays.asList(new Integer[][]{ {4},{8},{16} });
	}
	
	@Before
	public void before()
	{ 
		runnr= new StressTestRunner(initialNodes);
	    runnr.startInitialNodes();
	    if(initialNodes == 16) {
	      runnr.stressTestEnabled();
	    }

	}
	
	@AfterClass
	public static void after()
	{
	    runnr.terminateAllNodes();
	}

	
	@Test
	public void test1()
	{
		System.out.println("Start of test");
		System.out.println(initialNodes);
		
		/*try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		System.out.println("End of test");

	}
	
}
