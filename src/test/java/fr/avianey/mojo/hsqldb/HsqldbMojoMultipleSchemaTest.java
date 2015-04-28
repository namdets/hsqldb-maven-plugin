package fr.avianey.mojo.hsqldb;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HsqldbMojoMultipleSchemaTest extends HsqldbMojoTest {

    protected static void configureMojo(AbstractHsqldbMojo mojo) throws MojoExecutionException {
        mojo.driver = "org.hsqldb.jdbcDriver";
        mojo.address = "localhost";
        mojo.name = "xdb";
        mojo.path = "mem:test";
        mojo.databases = new ArrayList<DatabaseNamePathPair>();
        mojo.databases.add(new DatabaseNamePathPair("foo","mem:foo"));
        mojo.databases.add(new DatabaseNamePathPair("bar","mem:bar"));
        mojo.username = "sa";
        mojo.password = "";
        mojo.validationQuery = "SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS";
        mojo.skip = false;
    }

    @Before
    public void init() throws Exception {
    	startMojo = new StartHsqldbMojo();
        stopMojo = new StopHsqldbMojo();
        configureMojo(startMojo);
        configureMojo(stopMojo);
        startMojo.failIfAlreadyRunning = false;
        stopMojo.failIfNotRunning = false;
        
        if (startMojo.isRunning()) {
            Assert.fail("HSQLDB should not be running before a test is run...");
        }
    }
    
    @After
    public void stop() throws MojoExecutionException, MojoFailureException {
        super.stop();
    }
    
    @Test
    public void simple() throws Exception {
        super.simple();
    }

    @Test
    public void alreadyStarted() throws Exception {
        super.alreadyStarted();
    }

    @Test
    public void skip() throws Exception {
    	super.skip();
    }
    
    @Test
    public void bothDatabasesAreAvailable() throws Exception {
    	super.init();
    	startMojo.execute();
    	DataSource fooDs = createDataSource("jdbc:hsqldb:hsql//localhost/foo");
    	DataSource barDs = createDataSource("jdbc:hsqldb:hsql//localhost/bar");
    	testDataSourceConnection(fooDs);
    	testDataSourceConnection(barDs);
    	stopMojo.execute();
    }
    
    private void testDataSourceConnection(DataSource ds) throws SQLException{
    	Connection connection = ds.getConnection();
		connection.createStatement().execute("SELECT * FROM INFORMATION_SCHEMA.SYSTEM_TABLES");
    	connection.close();
    }
    
    private DataSource createDataSource(String url){
    	BasicDataSource ds = new BasicDataSource();
    	ds.setDriverClassName("org.hsqldb.jdbcDriver");
    	ds.setUrl(url);
    	ds.setValidationQuery("SELECT * FROM INFORMATION_SCHEMA.SYSTEM_TABLES");
    	return ds;
    }
    
}
