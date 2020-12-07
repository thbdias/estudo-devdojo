package br.com.devdojo.endpoint;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.devdojo.model.Student;
import br.com.devdojo.repository.StudentRepository;

@RestController
@RequestMapping("students")
public class StudentEndPoint {
	
	@Autowired
	private StudentRepository studentDAO;
	

	@GetMapping("/list")
	public ResponseEntity<?> listAll() {
		return new ResponseEntity<>(studentDAO.findAll(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getStudentById(@PathVariable("id") Long id) {	
		Optional<Student> student = studentDAO.findById(id);
        return new ResponseEntity<>(student.get(), HttpStatus.OK);
	}
	
	@GetMapping(path = "/findByName/{name}")
    public ResponseEntity<?> findStudentsByName(@PathVariable String name){
        return new ResponseEntity<>(studentDAO.findByNameIgnoreCaseContaining(name), HttpStatus.OK);
    }	
	
	@PostMapping()
	public ResponseEntity<?> save(@RequestBody Student student) {		
		return new ResponseEntity<>(studentDAO.save(student),HttpStatus.CREATED);
	}

	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {		
		studentDAO.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PutMapping()
	public ResponseEntity<?> update(@RequestBody Student student) {		
		studentDAO.save(student);
        return new ResponseEntity<>(HttpStatus.OK);
	}
}
