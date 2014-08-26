package exceptions;

import java.io.Serializable;

public class AppException extends RuntimeException implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	public AppException(){
		this.message="";
	}
	public AppException(String message){
		this.message=message;
	}
	public String getMessage(){
		return this.message;
	}
}
