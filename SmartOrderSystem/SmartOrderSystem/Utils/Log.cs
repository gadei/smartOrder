using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SmartOrderSystem.Utils
{
  class Log {

	public enum LogLevel {
		
		LOG_NOTHING,
		LOG_ERROR_ONLY,
		LOG_INFO_AND_ERROR
		
	}
	
	private static bool logInfo = true;
  private static bool logError = true;
	
	public static void setLogLevel(LogLevel logLevel) {
		
		switch (logLevel) {
      case LogLevel.LOG_NOTHING:
				logInfo = false;
				logError = false;
				break;
      case LogLevel.LOG_ERROR_ONLY:
				logInfo = false;
				logError = true;
				break;
      case LogLevel.LOG_INFO_AND_ERROR:
				logInfo = true;
				logError = true;
				break;
			default:
				logInfo = false;
				logError = true;
        break;
		}
	}
	
	
	public static void info(String infoString) {

		if(logInfo)
			Console.WriteLine("   ***@INFO*** " + infoString);
	}
	
	public static void error(String errorString) {
		
		if(logError)
      Console.Error.WriteLine("   ***@Err*** " + errorString);
	}
	
}
}
