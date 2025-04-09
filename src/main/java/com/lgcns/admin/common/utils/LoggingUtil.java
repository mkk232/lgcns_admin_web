package com.lgcns.admin.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
public class LoggingUtil {
	
	/**
	 * 에러 메시지
	 * @param message
	 * @return
	 */
	public static String getExceptionMessage(String message) {
		String result = null;
		
		if(StringUtils.hasText(message)) {
			result = message + " (error_code: 010000E001)" + "\n \t {}";
		}
		
		return result;
	}
	
	/**
	 * 에러 메시지
	 * @param message
	 * @param errorCode
	 * @param subErrorMessage
	 * @return
	 */
	public static String getExceptionMessage(String message, String errorCode, List<String> subErrorMessage) {
		String result = null;
		
		if(StringUtils.hasText(message)) {
			if(subErrorMessage != null && subErrorMessage.size() > 0) {
				StringBuffer sb = new StringBuffer();
				
				for (String subError : subErrorMessage) {
					if(sb.length() > 0) {
						sb.append(", ");
					}
					sb.append(subError);
				}
				
				result = message + " (error_code: " + errorCode + ", sub_message: " + sb.toString() + ")" + "\n \t {}";
				
			} else {
				result = message + " (error_code: " + errorCode + ")" + "\n \t {}";
			}
		}
		
		return result;
	}

	/**
	 * info 로그
	 * @param nessage
	 * @param data
	 */
	public static void info(String nessage, Object... data) {
		log.info(nessage, data);
	}
	
	/**
	 * error 로그
	 * @param e
	 */
	public static void error(Exception e) {
		String message = getExceptionMessage(e.getMessage());
		StackTraceElement[] stackTraceElement = e.getStackTrace();
		
		log.error(message, stackTraceElement[0]);
	}
	
	/**
	 * error 로그
	 * @param e
	 * @param errorCode
	 * @param subErrorMessage
	 */
	public static void error(Exception e, String errorCode, List<String> subErrorMessage) {
		String message = getExceptionMessage(e.getMessage(), errorCode, subErrorMessage);
		StackTraceElement[] stackTraceElement = e.getStackTrace();
		
		log.error(message, stackTraceElement[0]);
	}
}
