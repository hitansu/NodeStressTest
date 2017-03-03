package junit;

import org.junit.Assert;
import org.junit.Test;

import testengine.NODE_ACTION;
import testengine.Properties;
import testengine.ScriptBuilder;

public class TestScriptBuilder {

	@Test
	public void testGetSingleNodeStartScript() {
		String expectedScript= "sh "+Properties.ROOT_DIR+Properties.SINGLE_SERVER_SCRIPT+" "+"start";
		ScriptBuilder builder= new ScriptBuilder();
		String startScript = builder.getSingleNodeScript(NODE_ACTION.START, 1);
		Assert.assertEquals("Should be equal", expectedScript, startScript);
	}
	
	@Test
	public void testGetSingleNodeStopScript() {
		String expectedScript= "sh "+Properties.ROOT_DIR+Properties.SINGLE_SERVER_SCRIPT+" "+"stop";
		ScriptBuilder builder= new ScriptBuilder();
		String stopScript = builder.getSingleNodeScript(NODE_ACTION.STOP, 1);
		Assert.assertEquals("Should be equal", expectedScript, stopScript);
	}
	
	@Test
	public void testGetAllNodeStartScript() {
		String expectedScript= "sh "+Properties.ROOT_DIR+Properties.ALL_SERVER_SCRIPT+" "+"start";
		ScriptBuilder builder= new ScriptBuilder();
		String startScript = builder.getALLNodeScript(NODE_ACTION.START);
		Assert.assertEquals("Should be equal", expectedScript, startScript);
	}
	
	@Test
	public void testGetAllNodeStopScript() {
		String expectedScript= "sh "+Properties.ROOT_DIR+Properties.ALL_SERVER_SCRIPT+" "+"stop";
		ScriptBuilder builder= new ScriptBuilder();
		String stopScript = builder.getALLNodeScript(NODE_ACTION.STOP);
		Assert.assertEquals("Should be equal", expectedScript, stopScript);
	}

}
