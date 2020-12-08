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

    	RestTemplate restTemplate = new RestTemplateBuilder()
                .rootUri("http://localhost:8080/v1/protected/students")
                .basicAuthentication("abc", "devdojo")                
                .build();
    	
    	RestTemplate restTemplateAdmin = new RestTemplateBuilder()
                .rootUri("http://localhost:8080/v1/admin/students")
                .basicAuthentication("admin", "devdojo")                
                .build();
    	
    	//get by id com getForObject
    	Student student = restTemplate.getForObject("/{id}", Student.class, 10);
    	System.out.println(student);
    	
    	//get by id com getForEntity
    	ResponseEntity<Student> forEntity = restTemplate.getForEntity("/{id}", Student.class, 10);
    	System.out.println(forEntity.getBody());
    	
    	//get all com getForObject
    	Student[] students = restTemplate.getForObject("/list", Student[].class);
    	System.out.println(Arrays.toString(students));
    	
    	//get all com exchange
    	ResponseEntity<List<Student>> exchange = restTemplate.exchange("/list", HttpMethod.GET, null, new ParameterizedTypeReference<List<Student>>() {});
    	System.out.println(exchange.getBody());
    	    	
    	//post com exchange
    	Student studentPost = new Student();
    	studentPost.setName("John Wick");
    	studentPost.setEmail("john@pencil.com");
    	ResponseEntity<Student> exchangePost = restTemplateAdmin.exchange("/", HttpMethod.POST, new HttpEntity<>(studentPost, createJSONHeader()), Student.class);
    	System.out.println(exchangePost);
    	
    	//post com getForObject
    	Student studentPostForObject = restTemplateAdmin.postForObject("/", studentPost, Student.class);
    	System.out.println(studentPostForObject);
    	
    	//post com getForEntity
    	ResponseEntity<Student> studentPostForEntity = restTemplateAdmin.postForEntity("/", studentPost, Student.class);
    	System.out.println(studentPostForEntity);
    	
//        Student studentPost = new Student();
//        studentPost.setName("John Wick 2");
//        studentPost.setEmail("john@pencil.com");
//        studentPost.setId(29L);
//        JavaClientDAO dao = new JavaClientDAO();
//        System.out.println(dao.findById(111));
//        List<Student> students = dao.listAll();
//        System.out.println(students);
//        System.out.println(dao.save(studentPost));
//        dao.update(studentPost);
//        dao.delete(29);
    }
    
    private static HttpHeaders createJSONHeader() {
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	return headers;
    }

}
