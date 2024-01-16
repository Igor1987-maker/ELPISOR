package pagesObjects.oms;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Enumeration;

import org.openqa.selenium.WebElement;

public class PageUtils {
	
	public static void main(String[] args) {
		String s ="";
		try {
			s = getMacAdress(); // media access control
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public static void type(String text, WebElement el) {
		el.click();
		el.clear();
		el.sendKeys(text);
	}
	
	public static String getMacAdress() throws SocketException, UnknownHostException {
		String macAdress ="";
		InetAddress ipAddress = InetAddress.getLocalHost(); 
		System.out.println("Current IP name : " + ipAddress.getHostName());
		System.out.println("Current IP address : " + ipAddress.getHostAddress());
	    NetworkInterface networkInterface = NetworkInterface.getByInetAddress(ipAddress);
	       
	    byte[] macAddressBytes = networkInterface.getHardwareAddress();
	   // String s = Base64.getEncoder().encodeToString(macAddressBytes);
	        StringBuilder macAddressBuilder = new StringBuilder();
        for (int i = 0; i < macAddressBytes.length; i++) {
	         String macAddressHexByte = String.format("%02X",macAddressBytes[i]);
	          macAddressBuilder.append(macAddressHexByte);
	            if (i != macAddressBytes.length - 1)
	            {
	                macAddressBuilder.append("-");
	            }
	        }
        macAdress = macAddressBuilder.toString();
        System.out.println(macAdress);
        
	return macAdress;
		/*NetworkInterface network = NetworkInterface.getByInetAddress(ip);
		 byte[] mac = network.getHardwareAddress();
		 StringBuilder sb = new StringBuilder();
         for (int i = 0; i < mac.length; i++) {
             sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
         }
         System.out.println(sb.toString());
         ips = sb.toString();
         */
		
		//Enumeration<NetworkInterface> net = NetworkInterface.getNetworkInterfaces();
		
		//GetNetworkAddress.GetAddress("mac");   //FC-B3-BC-56-DF-05
	        
	        //Current IP name : Igor
	      //  Current IP address : 10.1.10.26
	      //  1C-69-7A-49-B2-EC
	        
	        
	}
	

}
