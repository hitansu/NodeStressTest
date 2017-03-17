package junit;

import java.util.Arrays;
import java.util.Collection;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import testengine.ScaleStressTestRunnerImpl;

@RunWith(Parameterized.class)
public class TestFile {

	private static ScaleStressTestRunnerImpl runnr;
	private static int initialNodes;
	
	public TestFile(int initialNodes)
	{
		TestFile.initialNodes = initialNodes;
	}
	
	@Parameterized.Parameters
	public static Collection<Integer[]> input() {
		return Arrays.asList(new Integer[][]{{4}});
	}
	
	
	@BeforeClass
	public static void set() {
		System.out.println("before class");
		runnr= new ScaleStressTestRunnerImpl(initialNodes);
	}
	
	@Before
	public void before()
	{ 
		System.out.println("before");
	    runnr.addNode(initialNodes);
	    if(initialNodes == 7) {
	   //   runnr.enableStressTest();
	    }

	}
	
	@AfterClass
	public static void after()
	{
	  //  runnr.terminate();
	}

	
	@Test
	public void test1()
	{
		System.out.println("Start of test");
		
		/*simulate long running test*/
		
		long sleep= 3000;
		if(initialNodes== 7) {
           sleep= 75000;
		}
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		System.out.println("End of test");
		

	}
}
