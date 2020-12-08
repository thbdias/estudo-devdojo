package br.com.devdojo.javaclient;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
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
    	
    	//get by id
    	Student student = restTemplate.getForObject("/{id}", Student.class, 5);
    	ResponseEntity<Student> forEntity = restTemplate.getForEntity("/{id}", Student.class, 5);    	
    	System.out.println(student);
    	System.out.println(forEntity.getBody());
    	
    	//get all com getForObject
    	Student[] students = restTemplate.getForObject("/list", Student[].class);
    	System.out.println(Arrays.toString(students));
    	
    	//get all com exchange
    	ResponseEntity<List<Student>> exchange = restTemplate.exchange("/list", HttpMethod.GET, null, new ParameterizedTypeReference<List<Student>>() {});
    	System.out.println(exchange.getBody());
    	
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

}
