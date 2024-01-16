package Objects.Logging;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

public class LogAppender extends FileAppender {
	
	

	@Override
	public void setLayout(Layout layout) {
		// TODO Auto-generated method stub
		setLayout(new PatternLayout("%d{HH:mm:ss}  %-5.5p  %t %m%n"));
	}

	@Override
	protected void subAppend(LoggingEvent event) {
		
		// TODO Auto-generated method stub
		
		super.subAppend(event);
	}
	
	

	

}
