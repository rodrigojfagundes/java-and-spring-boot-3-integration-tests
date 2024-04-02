package br.com.erudio.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.erudio.model.Person;

//classe de TESTE do PERSONREPOSITORY

@DataJpaTest
class PersonRepositoryTest {
	
	private Person person0;

	@BeforeEach
	public void setup() {
		person0 = new Person("Leandro", 
				"Costa", 
				"leandro@erudio.com.br",
				"Uberlandia - Minas Gerais - Brasil",
				"Male");
	}
	
	@Autowired
	private PersonRepository repository;
	
	//criando um METODO DE TEST, q vai TESTAR SE quando nos CRIAMOS um OBJ
	//do TIPO PERSON ele(PERSONREPOSITORY.JAVA) tem q retornar o OBJ PERSON
	//CRIADO...
	@DisplayName("JUnit test for Given Person Object when Save Then Return Saved Person")
	@Test
	void testGivenPersonObject_whenSave_ThenReturnSavedPerson() {
		
		//Given / Arrange
		//esta no metodo Setup()
		
		//When / Act
		
		//quando nos salvarmos UM PERSON no BANCO, o PERSONREPOSITORY.JAVA
		//precisa RETORNAR a PESSOA q foi salva (nome, email, etc...) apos ele
		//SALVAR no BANCO...
		//e o q ele retornar nos VAMOS SALVAR em uma VAR de nome SAVEDPERSON
		Person savedPerson = repository.save(person0);
		
		
		//Then / Assert

		//Verificando SE o q ta na VAR SAVEDPERSON nao e NULL
		assertNotNull(savedPerson);

		assertTrue(savedPerson.getId() > 0);
	}

	@DisplayName("JUnit test for Given Person List when findAll Then Return Saved Person List")
	@Test
	void testGivenPersonList_whenFindAll_ThenReturnSavedPersonList() {
		
		//Given / Arrange
		Person person1 = new Person("Leonardo", 
				"Costa", 
				"leonardo@erudio.com.br",
				"Uberlandia - Minas Gerais - Brasil",
				"Male");
		
		repository.save(person0);
		repository.save(person1);
		
		//When / Act
		List<Person> personList = repository.findAll();
		
		//Then / Assert
		assertNotNull(personList);
		
		assertEquals(2, personList.size());

	}
		
	@DisplayName("JUnit test for Given Person Object when Find By Id then Return Person Object")
	@Test
	void testGivenPersonObject_whenFindById_thenReturnPersonObject() {
		
		//Given / Arrange
		repository.save(person0);
		
		
		//When / Act
		Person savedPerson = repository.findById(person0.getId()).get();
				
		//Then / Assert		
		assertNotNull(savedPerson);
		assertEquals(person0.getId(), savedPerson.getId());

	}
	
	@DisplayName("JUnit test for Given Person Object when FindByEmail then Return Person Object")
	@Test
	void testGivenPersonObject_whenFindByEmail_thenReturnPersonObject() {
		
		//Given / Arrange
		repository.save(person0);
				
		//When / Act
		Person savedPerson = repository.findByEmail(person0.getEmail()).get();
		
		
		//Then / Assert
		assertNotNull(savedPerson);
		assertEquals(person0.getEmail(), savedPerson.getEmail());
		assertEquals(person0.getId(), savedPerson.getId());
	}
	
	@DisplayName("JUnit test for Given Person Object when Update Person then Return Updated Person Object")
	@Test
	void testGivenPersonObject_whenUpdatePerson_thenReturnUpdatedPersonObject() {
		
		//Given / Arrange
		repository.save(person0);
			
		//When / Act
		Person savedPerson = repository.findById(person0.getId()).get();
		savedPerson.setFirstName("Leonardo");
		savedPerson.setEmail("leonardo@erudio.com.br");
		
		Person updatedPerson = repository.save(savedPerson);
				
		//Then / Assert

		assertNotNull(updatedPerson);
		assertEquals("Leonardo", updatedPerson.getFirstName());
		assertEquals("leonardo@erudio.com.br", updatedPerson.getEmail());
		
		
	}
	
	@DisplayName("JUnit test for Given Person Object when Delete then Remove Person")
	@Test
	void testGivenPersonObject_whenDelete_thenRemovePerson() {
		
		//Given / Arrange
		repository.save(person0);
		
		
		//When / Act
		repository.deleteById(person0.getId());
				
		//Then / Assert
		Optional<Person> personOptional = repository.findById(person0.getId());

		assertTrue(personOptional.isEmpty());

	}

	@DisplayName("JUnit test for Given firstName And Last Name when Find By JPQL then Return Person Object")
	@Test
	void testGivenFirstNameAndLastName_whenFindByJPQL_thenReturnPersonObject() {
		
		//Given / Arrange
		repository.save(person0);

		String firstName = "Leandro";
		String lastName = "Costa";
		
		//When / Act
		Person savedPerson = repository.findByJPQL(firstName, lastName);
				
		//Then / Assert
		assertNotNull(savedPerson);
		assertEquals(firstName, savedPerson.getFirstName());
		assertEquals(lastName, savedPerson.getLastName());
	}
	
	
	@DisplayName("JUnit test for Given firstName And Last Name when FindByJPQLNamedParameters then Return Person Object")
	@Test
	void testGivenFirstNameAndLastName_whenFindByJPQLNamedParameters_thenReturnPersonObject() {
		
		//Given / Arrange
		repository.save(person0);
		String firstName = "Leandro";
		String lastName = "Costa";
		
		//When / Act
		Person savedPerson = repository.findByJPQLNamedParameters(firstName, lastName);
				
		//Then / Assert
		assertNotNull(savedPerson);
		assertEquals(firstName, savedPerson.getFirstName());
		
		//e verificando o LASTNAME tbm... COSTA
		assertEquals(lastName, savedPerson.getLastName());
	}	
	
	@DisplayName("JUnit test for Given firstName And Last Name when FindByNativeSQL then Return Person Object")
	@Test
	void testGivenFirstNameAndLastName_whenFindByNativeSQL_thenReturnPersonObject() {
		
		//Given / Arrange
		repository.save(person0);
		
		String firstName = "Leandro";
		String lastName = "Costa";
		
		//When / Act
		Person savedPerson = repository.findByNativeSQL(firstName, lastName);
				
		//Then / Assert
		assertNotNull(savedPerson);
		assertEquals(firstName, savedPerson.getFirstName());
		assertEquals(lastName, savedPerson.getLastName());
	}	
	
	@DisplayName("JUnit test for Given firstName And Last Name when FindByNativeSQLwithNameParameters then Return Person Object")
	@Test
	void testGivenFirstNameAndLastName_whenFindByNativeSQLwithNameParameters_thenReturnPersonObject() {
		
		//Given / Arrange
		repository.save(person0);
		
		String firstName = "Leandro";
		String lastName = "Costa";
		
		//When / Act
		Person savedPerson = repository.findByNativeSQLwithNamedParameters(firstName, lastName);		
		
		//Then / Assert
		assertNotNull(savedPerson);
		assertEquals(firstName, savedPerson.getFirstName());
		assertEquals(lastName, savedPerson.getLastName());
	}	
	
}
