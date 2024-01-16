package services;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import Enums.AutoParams;
import Enums.TestRunnerType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
//import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;

@Service
public class Configuration extends GenericService {
	private static Configuration configuration = new Configuration();

	@Resource
	@Qualifier("Reporter")
	private Reporter report;
	
	// @Autowired
	// public void setReporter(Reporter report){
	// this.report=report;
	// }

	@Autowired
	NetService netService;

	public Configuration() {
		// System.out.println("Setup of Configuration");
		LoadConfig();
	}

	public void LoadConfig() {
		InputStream input = null;
		InputStream globaConfigInput = null;

		try {
			// globaConfigInput = new FileInputStream(
			// "files/properties/global.properties");

			// globaConfigInput = new FileInputStream(
			// "C:\\automation\\global.properties");

			globaConfigInput = new FileInputStream(
					"files\\configFiles\\global.properties");
			globalProperties.load(globaConfigInput);

			// input = new FileInputStream("C:\\qa.properties");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		String localPropertiesFile = null;
		// load a properties file
		try {

			String envFile = System.getProperty("nevFileParam");

			if (envFile != null) {
				localPropertiesFile = System.getProperty("nevFileParam");
			} else {
				localPropertiesFile = getAutomationParam(
						AutoParams.envFile.toString(), "envFileCMD");
			}
			// TODO
		

			TestRunnerType runnerType = getTestRunner();



			if (runnerType.equals(TestRunnerType.CI)) {
				

				String path = "files//configFiles/" + localPropertiesFile;
				

				input = new FileInputStream(new File(path));

				properties.load(input);
			} else {
				String path = "C:\\automation\\" + localPropertiesFile;
				System.out.println(path);
				input = new FileInputStream(path);
				properties.load(input);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Configuration getInstance() {

		return configuration;
	}

	private static final String LIST_FORMAT = "       %1$-70s| %2$s%n";
	public static final String OVERRIDE_PROPERTIES_FILENAME = "";

	protected Properties properties = new Properties();
	protected Properties globalProperties = new Properties();

	

	private static final Logger logger = LoggerFactory
			.getLogger(Configuration.class);
	/**
	 * Field service
	 */

	private static final String TEMPLATES_FOLDER = "templates";
	// public static InternetAddress FROM = null;
	// public static InternetAddress EMAIL_BILLING = null;

	public static final String SUPPORT_EMAIL = "";
	public static final String NOTIFICATIONS_EMAIL = "";
	public static final String MEMBERS_EMAIL = "";
	public static final String DO_NOT_REPLY_EMAIL = "";

	public static String HOST;

	// public static final Date STARTUP_DATE = new
	// Date(System.currentTimeMillis());
	//
	// static {
	// try {
	// // FROM = new InternetAddress("support@zao.com", "Zao Support");
	// // EMAIL_BILLING = new InternetAddress("billing@zao.com", "Zao Billing");
	// } catch (UnsupportedEncodingException e) {
	// e.printStackTrace();
	// }
	// }

	public void put(String key, String value) {

		Assert.notNull(key);
		Assert.notNull(value);
		properties.put(key, value);
	}

	public String getProperty(String key, String defaultValue) {

		return properties.getProperty(key, defaultValue);
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public String getGlobalProperties(String key) {
		return globalProperties.getProperty(key);
	}

	public String getProperty(String key) {

		return getProperty(key, null);
	}

	public String getStudentUserName() {
		return getProperty("student.user.name");
	}

	public String getStudentPassword() {
		return getProperty("student.user.password");
	}
	
	public String getPathToFirefoxDriver() {
		return getProperty("pathToFirefoxDriver");
	}

	public Set<Object> getKeys() {
		return properties.keySet();
	}

	public Properties getAll() {
		return properties;
	}

	public boolean isDevelopmentMode() {
		return isMode("development");
	}

	public boolean isQAMode() {
		return isMode("qa");
	}

	public String getProjectBuildVersion() {
		return getProperty("");
	}

	public String getDomain() {
		return getProperty("");
	}

	public String getCookieDomain() {
		return getProperty("");
	}

	public String getHostInformation() {
		try {
			InetAddress host = InetAddress.getLocalHost();
			return host.getHostName() + "- IP " + host.getHostAddress();
		} catch (UnknownHostException e) {
			return "Host information cannot be retrieved. " + e.getMessage();
		}
	}

	private String getHostname() {
		try {
			InetAddress host = InetAddress.getLocalHost();
			return host.getHostName();

		} catch (UnknownHostException e) {
			return "Host information cannot be retrieved. " + e.getMessage();
		}
	}

	public String getRunningHost() {
		return HOST;
	}

	public String getIpAddress() {
		try {
			InetAddress host = InetAddress.getLocalHost();
			return host.getHostAddress();
		} catch (UnknownHostException e) {
			return "Host information cannot be retrieved. " + e.getMessage();
		}
	}

	public void updateProperties(Properties updatedProperties)
			throws IOException {

		Enumeration enumeration = updatedProperties.propertyNames();
		while (enumeration.hasMoreElements()) {
			String newKey = enumeration.nextElement().toString();

			logger.info(
					"Merging property {} . value ({}) will be replaced with {} ",
					new Object[] { newKey, getProperty(newKey),
							updatedProperties.getProperty(newKey) });

			setProperty(newKey, updatedProperties.getProperty(newKey));
		}
	}

	public void setProperty(String newKey, String value) {
		properties.setProperty(newKey, value);
	}

	public String getHomeDirectory() {
		return System.getProperty("zao.home", getProperty("zao.home", "NONE"));
	}

	public boolean isNotifyAdminOnErrorEnabled() {
		return getProperty("", "true").equals("true");
	}

	public int getMaxNumOfActiveNotificationsToProcess() {
		return Integer.parseInt(getProperty("", "10"));

	}

	public boolean isTestMode() {
		return getProperty("", "").equals("test");
	}

	public String getApplicationAdminEmail() {
		return getProperty("", "");
	}

	public void updateProperty(String propertyKey, String propertyValue) {
		logger.info(
				"Merging property {} . value ({}) will be replaced with {} ",
				new Object[] { propertyKey, getProperty(propertyKey),
						propertyValue });
		setProperty(propertyKey, propertyValue);
	}

	public boolean isProductionMode() {
		return isMode("production");
	}

	public boolean isMode(String mode) {
		return getProperty("", "").equals(mode);
	}

	public String getEnvironmentConfiguration() {
		return getProperty("", "");
	}

	public String getHome() {
		return getProperty("", "");
	}

	public String getProjectBuildDate() {
		return getProperty("", "");
	}

	public String getEmailDevelopmentRecipients() {
		return getProperty("", "");
	}

	/**
	 * @return the parameter value according to this hierarchy: Param from Maven
	 *         CMD>properties file>pom profile>global properties file
	 */

	public String getAutomationParam(String paramName, String mavenCmdParam) {
		String value = null;
		
		report = new Reporter();
		
		// check in maven command line
		try {
			value = System.getProperty(mavenCmdParam);
			// check in properties file
			if (value != null) {
				
				if (mavenCmdParam.equalsIgnoreCase("prod2") || mavenCmdParam.equalsIgnoreCase("ci")) 
					trigger = "CI";
				
				report.report("got param from maven cmd: " + value);
				return value;
			}
			// check in properties file
			value = getProperty(paramName);
			if (value != null) {
				
				if (paramName.equalsIgnoreCase("prod2") || paramName.equalsIgnoreCase("ci")) 
					trigger = "Manually";
				
				report.report("got param from properties file: " + value);
				return value;
			}
			// check in pom profile
			value = System.getProperty(paramName);
			if (value != null) {
				report.report("got param from pom profile: " + value);
				
				if (mavenCmdParam.equalsIgnoreCase("ci") || mavenCmdParam.equalsIgnoreCase("prod2")) 
					trigger = "CI";
				
				return value;
			}
			// check in global properties
			value = globalProperties.getProperty(paramName);
			if (value != null) {
				//report.report("got from global properties: " + value);
				return value;
			} else {
				//report.report("value " + paramName + " not found");
				// org.junit.Assert.fail("Auto param value not found. Check properties file or maven CMD param");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (value == null) {
			value = "";
		}
		return value;
	}

	public void savePropertiesToFile() {

	}

	public String getLogerver() {
		String logServer = getGlobalProperties("logserverName");
		logServer = logServer.replace("\\\\", "");
		return logServer;
	}

	public String getSutUrl() {
		// TODO Auto-generated method stub
		return getProperty("sut.url");
	}

	public String getInstitutionId() {
		// TODO Auto-generated method stub

		return getProperty("institution.id");
	}

	public String getClassName() {
		// TODO Auto-generated method stub
		return configuration.getProperty("classname");
	}
	
	public String getNotificationClassName() {
		// TODO Auto-generated method stub
		return configuration.getProperty("classname.notification");
	}
	
	public String getUserAgentOS() {
		// TODO Auto-generated method stub
		return configuration.getProperty("OS");
	}
	
	public String getInstitutionName() {
		// TODO Auto-generated method stub

		return getProperty("institution.name");
	}
	public String getLocalInstitutionId() {
		// TODO Auto-generated method stub

		return getProperty("local_institutionId");
	}
	public String getLocalclassName() {
		// TODO Auto-generated method stub

		return getProperty("local_className");
	}
	
	public String getEDExcellenceClassName() {
		// TODO Auto-generated method stub

		return getProperty("classname.EDExcellence");
	}
}
