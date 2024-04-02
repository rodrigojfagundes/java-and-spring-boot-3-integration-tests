package br.com.erudio.exceptions;

import java.io.Serializable;
import java.util.Date;


//class de excecoes
public class ExceptionResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	//atributos
	private Date timestamp;
	private String message;
	private String details;
	
	//criando o construtor
	public ExceptionResponse(Date timestamp, String message, String details) {
		this.timestamp = timestamp;
		this.message = message;
		this.details = details;
	}

	//criando os get e set
	public Date getTimestamp() {
		return timestamp;
	}

	public String getMessage() {
		return message;
	}

	public String getDetails() {
		return details;
	}
}
