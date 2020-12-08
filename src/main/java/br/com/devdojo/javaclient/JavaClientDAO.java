package br.com.devdojo.javaclient;

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
 * @author William Suane for DevDojo on 7/6/17.
 */
public class JavaClientDAO {
    private RestTemplate restTemplate = new RestTemplateBuilder()
            .rootUri("http://localhost:8080/v1/protected/students")
            .basicAuthentication("abc", "devdojo")     
            .errorHandler(new RestResponseExceptionHandler())
            .build();
    
    private RestTemplate restTemplateAdmin = new RestTemplateBuilder()
            .rootUri("http://localhost:8080/v1/admin/students")
            .basicAuthentication("admin", "devdojo")
            .errorHandler(new RestResponseExceptionHandler())
            .build();

    public Student findById(long id) {
        return restTemplate.getForObject("/{id}", Student.class, id);
//        ResponseEntity<Student> forEntity = restTemplate.getForEntity("/{id}", Student.class, id);
    }

    public List<Student> listAll() {
    	//deu erro com pageable
//        ResponseEntity<PageableResponse<Student>> exchange = restTemplate.exchange("/", HttpMethod.GET, null,
//                new ParameterizedTypeReference<PageableResponse<Student>>() {
//                });
//        return exchange.getBody().getContent();    	

    	return restTemplate
    				.exchange("/list", HttpMethod.GET, null, new ParameterizedTypeReference<List<Student>>() {})
    				.getBody();
//    	Student[] students = restTemplate.getForObject("/list", Student[].class);
    }

    public Student save(Student student) {        
    	ResponseEntity<Student> exchangePost = restTemplateAdmin.exchange("/", HttpMethod.POST, new HttpEntity<>(student, createJSONHeader()), Student.class);
//    	Student studentPostForObject = restTemplateAdmin.postForObject("/", student, Student.class);
//    	ResponseEntity<Student> studentPostForEntity = restTemplateAdmin.postForEntity("/", student, Student.class);    	
        return exchangePost.getBody();
    }

    public void update(Student student){
        restTemplateAdmin.put("/",student);
    }
    public void delete(long id){
        restTemplateAdmin.delete("/{id}",id);
    }

    private static HttpHeaders createJSONHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
