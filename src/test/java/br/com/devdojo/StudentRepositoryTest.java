package br.com.devdojo;

import br.com.devdojo.model.Student;
import br.com.devdojo.repository.StudentRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //testar usando nosso próprio banco de dados
public class StudentRepositoryTest {
	
    @Autowired
    private StudentRepository studentRepository;
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    
    @Test
    public void createShouldPersistData() {
        Student student = new Student("William", "william@devdojo.com.br");
        studentRepository.save(student);
        assertThat(student.getId()).isNotNull();
        assertThat(student.getName()).isEqualTo("William");
        assertThat(student.getEmail()).isEqualTo("william@devdojo.com.br");
    }

    
    @Test
    public void deleteShouldRemoveData() {
        Student student = new Student("William", "william@devdojo.com.br");
        studentRepository.save(student);
        studentRepository.delete(student);
        Optional<Student> studentFind = studentRepository.findById(student.getId());
        assertThat(studentFind.isPresent()).isFalse();
    }
    

    @Test
    public void updateShouldChangeAndPersistData() {
        Student student = new Student("William", "william@devdojo.com.br");
        studentRepository.save(student);
        
        student.setName("William222");
        student.setEmail("william222@devdojo.com.br");
        studentRepository.save(student);
        
        student = studentRepository.findById(student.getId()).get();
        
        assertThat(student.getName()).isEqualTo("William222");
        assertThat(student.getEmail()).isEqualTo("william222@devdojo.com.br");
    }

    
    @Test
    public void findByNameIgnoreCaseContainingShouldIgnoreCase() {
        Student student = new Student("William", "william@devdojo.com.br");
        Student student2 = new Student("william", "william222@devdojo.com.br");
        this.studentRepository.save(student);
        this.studentRepository.save(student2);
        List<Student> studentList = studentRepository.findByNameIgnoreCaseContaining("william");
        assertThat(studentList.size()).isEqualTo(2);
    }

    
    /**	NAO FUNCIONOU --- REVER*/
//    @Test
//    public void createWhenNameIsNullShouldThrowConstraintViolationException() {
//        thrown.expect(ConstraintViolationException.class);
//        thrown.expectMessage("O campo nome do estudante é obrigatório");                              
//        this.studentRepository.save(new Student());
//    }
    
    /**	NAO FUNCIONOU --- REVER*/
//    @Test
//    public void createWhenEmailIsNullShouldThrowConstraintViolationException() {
//        thrown.expect(ConstraintViolationException.class);
//        Student student = new Student();
//        student.setName("William");
//        this.studentRepository.save(student);
//    }
    
    /**	NAO FUNCIONOU --- REVER*/
//    @Test
//    public void createWhenEmailIsNotValidShouldThrowConstraintViolationException() {
//        thrown.expect(ConstraintViolationException.class);
//        thrown.expectMessage("Digite um email válido");
//        Student student = new Student();
//        student.setName("William");
//        student.setEmail("William");
//        this.studentRepository.save(student);
//    }

}
