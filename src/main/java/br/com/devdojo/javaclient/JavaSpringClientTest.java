package br.com.devdojo.javaclient;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import br.com.devdojo.handler.RestResponseExceptionHandler;
import br.com.devdojo.model.Student;

/**
 * @author William Suane for DevDojo on 7/5/17.
 */
public class JavaSpringClientTest {
    public static void main(String[] args) {
    	
    	JavaClientDAO javaClientDAO = new JavaClientDAO();
    	
    	javaClientDAO.findById(10);
    	
    	javaClientDAO.listAll();

    	Student studentPost = new Student();
    	studentPost.setName("John Wick");
    	studentPost.setEmail("john@pencil.com");
    	
    	javaClientDAO.save(studentPost);
    	
    	studentPost.setId(10L);
    	studentPost.setName("nome teste 2");
    	javaClientDAO.update(studentPost);
    	
    	javaClientDAO.delete(10L);
    	
    }    
}
