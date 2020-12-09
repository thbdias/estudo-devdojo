package br.com.devdojo;

import static java.util.Arrays.asList;
import static org.springframework.http.HttpMethod.DELETE;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import br.com.devdojo.model.Student;
import br.com.devdojo.repository.StudentRepository;

/**
 * @author William Suane for DevDojo on 7/11/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // toda vez que iniciar teste utilizar uma porta aleatória
@AutoConfigureMockMvc
public class StudentEndpointTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	@MockBean
	private StudentRepository studentRepository;

	@Autowired
	private MockMvc mockMvc;

	
	@TestConfiguration
	static class Config {
		@Bean
		public RestTemplateBuilder restTemplateBuilder() {
			return new RestTemplateBuilder().basicAuthentication("admin", "devdojo");
		}
	}
	

	@Before
	public void setup() {
		Optional<Student> optionalStudent = Optional.of(new Student(1L, "Legolas", "legolas@lotr.com"));
		BDDMockito.when(studentRepository.findById(1L)).thenReturn(optionalStudent);
	}

	
	@Test
	public void listStudentsWhenUsernameAndPasswordAreIncorrectShouldReturnStatusCode401() {
		System.out.println(port);
		restTemplate = restTemplate.withBasicAuth("1", "1");
		ResponseEntity<String> response = restTemplate.getForEntity("/v1/protected/students/", String.class);
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(401);
	}
	

	@Test
	public void getStudentsByIdWhenUsernameAndPasswordAreIncorrectShouldReturnStatusCode401() {
		System.out.println(port);
		restTemplate = restTemplate.withBasicAuth("1", "1");
		ResponseEntity<String> response = restTemplate.getForEntity("/v1/protected/students/1", String.class);
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(401);
	}

	
	@Test
	public void listStudentsWhenUsernameAndPasswordAreCorrectShouldReturnStatusCode200() {
		List<Student> students = asList(new Student(1L, "Legolas", "legolas@lotr.com"),
				new Student(2L, "Aragorn", "aragorn@lotr.com"));
		BDDMockito.when(studentRepository.findAll()).thenReturn(students);
		ResponseEntity<String> response = restTemplate.getForEntity("/v1/protected/students/list", String.class);
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
	}

	
	@Test
	public void getStudentsByIdWhenUsernameAndPasswordAreCorrectShouldReturnStatusCode200() {
		ResponseEntity<Student> response = restTemplate.getForEntity("/v1/protected/students/{id}", Student.class, 1L);
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
	}

	
	@Test
	public void getStudentsByIdWhenUsernameAndPasswordAreCorrectAndStudentDoesNotExistShouldReturnStatusCode404() {
		ResponseEntity<Student> response = restTemplate.getForEntity("/v1/protected/students/{id}", Student.class, -1);
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(404);
	}
	

	@Test
	public void deleteWhenUserHasRoleAdminAndStudentExistsShouldReturnStatusCode200() {
		BDDMockito.doNothing().when(studentRepository).deleteById(1L);
		ResponseEntity<String> exchange = restTemplate.exchange("/v1/admin/students/{id}", DELETE, null, String.class,
				1L);
		Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
	}
	

	/** DELETE COM REST TEMPLATE */
	@Test
	public void deleteWhenUserHasRoleAdminAndStudentDoesNotExistShouldReturnStatusCode404_RestTemplate()
			throws Exception {
		BDDMockito.doNothing().when(studentRepository).deleteById(1L);
		ResponseEntity<String> exchange = restTemplate.exchange("/v1/admin/students/{id}", DELETE, null, String.class,
				-1L);
		Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(404);
	}

	
	/** DELETE COM MOCK MVC */
	@Test
	@WithMockUser(username = "xx", password = "xx", roles = { "USER", "ADMIN" })
	public void deleteWhenUserHasRoleAdminAndStudentDoesNotExistShouldReturnStatusCode404_MockMvc() throws Exception {
		BDDMockito.doNothing().when(studentRepository).deleteById(1L);
		mockMvc.perform(MockMvcRequestBuilders
							.delete("/v1/admin/students/{id}", -1L))
							.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	
    /** DELETE COM MOCK MVC */
    @Test
    @WithMockUser(username = "xx", password = "xx", roles = {"USER"})
    public void deleteWhenUserDoesNotHaveRoleAdminShouldReturnStatusCode403_MockMvc() throws Exception {
        BDDMockito.doNothing().when(studentRepository).deleteById(1L);
//        ResponseEntity<String> exchange = restTemplate.exchange("/v1/admin/students/{id}", DELETE, null, String.class, -1L);
//        Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(404);
        mockMvc.perform(MockMvcRequestBuilders
                			.delete("/v1/admin/students/{id}", -1L))
                			.andExpect(MockMvcResultMatchers.status().isForbidden());
    }
    
    
    @Test
    public void createWhenNameIsNullShouldReturnStatusCode400BadRequest() throws Exception{
        Student student = new Student(3L, null, "sam@lotr.com");
        BDDMockito.when(studentRepository.save(student)).thenReturn(student);
        ResponseEntity<String> response = restTemplate.postForEntity("/v1/admin/students/", student, String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(400);
        Assertions.assertThat(response.getBody()).contains("fieldMessage","O campo nome do estudante é obrigatório");
    }
    
    
    @Test
    public void createShouldPersistDataAndReturnStatusCode201() throws Exception{
        Student student = new Student(3L, "Sam", "sam@lotr.com");
        BDDMockito.when(studentRepository.save(student)).thenReturn(student);
        ResponseEntity<Student> response = restTemplate.postForEntity("/v1/admin/students/", student, Student.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(201);
        Assertions.assertThat(response.getBody().getId()).isNotNull();
    }
}
