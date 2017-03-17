package testengine;

public class Properties {

/*	static final String USER= "";
	static final String PASSWORD= "";
	static final String HOST= "";
	public static final String ROOT_DIR= "";
	public static final String SINGLE_SERVER_SCRIPT= "start_stop_SINGLE.sh";
	public static final String ALL_SERVER_SCRIPT= "start_stop_ALL.sh";
	static final String FIVE_SERVER_SCRIPT= "";
	static final int PORT= 22;
	**/
	
	/*root user for the machine*/
    static final String USER= "root"; //
    
    /*password for root user*/
    static final String PASSWORD= "pegasys1+";
    
    /*host names. It should be separated by , like host1,host2,host3 */
    static final String HOST= "10.150.68.171";
    
    /*root directory inside which all the tomcat instance are present*/
    public static final String ROOT_DIR= "/dsk01/tomcat7/";
    
    /*script to start & stop any tomcat instance*/
    public static final String SINGLE_SERVER_SCRIPT= "start_stop_SINGLE.sh";
    
    /*script to start & stop all tomcat instance*/
    public static final String ALL_SERVER_SCRIPT= "start_stop_ALL.sh";
    
    /*port for ssh*/
    static final int PORT= 22;
    
    /*dir under root directory where all tomcat instances are installed*/
    /*NOTE: Each instance should have same dir set up*/
    static final String[] tomcatServersLoc= {"system1", "system2", "system3", "system4", "system5", "system6", "system7"};
    
    /*wait for the application to be up*/
    static final long SERVER_BOOT_TIME= 12000;
    
    /*whether to wait for the application be up*/
    static final boolean WAIT_FOR_SERVER_UP= true;
    
    static int getMaxNode() {
    	return tomcatServersLoc.length;
    }
    
    static String getServerName(int i) {
    	return tomcatServersLoc[i];
    }

}
