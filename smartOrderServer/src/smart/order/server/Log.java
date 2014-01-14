package smart.order.server;

public class Log {

	public static enum LogLevel {
		
		LOG_NOTHING,
		LOG_ERROR_ONLY,
		LOG_INFO_AND_ERROR
		
	}
	
	private static boolean logInfo = false;
	private static boolean logError = true;
	
	public static void setLogLevel(LogLevel logLevel) {
		
		switch(logLevel) {
		
			case LOG_NOTHING:
				logInfo = false;
				logError = false;
				break;
			case LOG_ERROR_ONLY:
				logInfo = false;
				logError = true;
				break;
			case LOG_INFO_AND_ERROR:
				logInfo = true;
				logError = true;
				break;
			default:
				logInfo = false;
				logError = true;	
		
		}
	}
	
	
	public static void info(String infoString) {

		if(logInfo)
			System.out.print("   ***@INFO*** " + infoString);
	}
	
	public static void error(String errorString) {
		
		if(logError)
			System.err.print("   ***@Err*** " + errorString);
	}
	
}
