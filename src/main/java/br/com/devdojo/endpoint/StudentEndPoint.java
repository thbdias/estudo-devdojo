package br.com.devdojo.endpoint;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.devdojo.error.ResourceNotFoundException;
import br.com.devdojo.model.Student;
import br.com.devdojo.repository.StudentRepository;

@RestController
@RequestMapping("v1")
public class StudentEndPoint {
	
	@Autowired
	private StudentRepository studentDAO;
	

	@GetMapping("protected/students/list")
	public ResponseEntity<?> listAll(Pageable pageable) {
		return new ResponseEntity<>(studentDAO.findAll(pageable), HttpStatus.OK);
	}
	
	
	@GetMapping("protected/students/{id}")
	public ResponseEntity<?> getStudentById(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails) {	
		System.out.println(userDetails);
		verifyIfStudentExists(id);		
		Optional<Student> student = studentDAO.findById(id);			
        return new ResponseEntity<>(student.get(), HttpStatus.OK);
	}
	
	
	@GetMapping("protected/students/findByName/{name}")
    public ResponseEntity<?> findStudentsByName(@PathVariable String name){
        return new ResponseEntity<>(studentDAO.findByNameIgnoreCaseContaining(name), HttpStatus.OK);
    }	
	
	@PostMapping("admin/students")
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<?> save(@Valid @RequestBody Student student) {		
		return new ResponseEntity<>(studentDAO.save(student),HttpStatus.CREATED);
	}

	
	@DeleteMapping("/admin/students/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		verifyIfStudentExists(id);
		studentDAO.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PutMapping("/admin/students")
	public ResponseEntity<?> update(@RequestBody Student student) {	
		verifyIfStudentExists(student.getId());
		studentDAO.save(student);
        return new ResponseEntity<>(HttpStatus.OK);
	}
	
	private void verifyIfStudentExists(Long id){
        if (!studentDAO.findById(id).isPresent())
            throw new ResourceNotFoundException("Student not found for ID: "+id);
    }
}
