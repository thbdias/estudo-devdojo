package br.com.devdojo.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author William Suane for DevDojo on 6/12/17.
 */
@ResponseStatus(HttpStatus.NOT_FOUND) //404
public class ResourceNotFoundException extends RuntimeException {
    
	//construtor
	public ResourceNotFoundException(String message) {
        super(message);
    }
	
}
