package Objects.Logging;

import org.apache.log4j.Category;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggingEvent;

public class MyLoggingEvent extends LoggingEvent {

	public MyLoggingEvent(String fqnOfCategoryClass, Category logger,
			Priority level, Object message, Throwable throwable) {
		super(fqnOfCategoryClass, logger, level, message, throwable);
		// TODO Auto-generated constructor stub
	}
	
	

	

}
