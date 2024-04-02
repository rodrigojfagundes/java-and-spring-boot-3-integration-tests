package br.com.erudio.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mysql.cj.x.protobuf.MysqlxCrud.Collection;

import br.com.erudio.exceptions.ResourceNotFoundException;
import br.com.erudio.model.Person;
import br.com.erudio.repositories.PersonRepository;


//class de test da PERSONSERVICES

@ExtendWith(MockitoExtension.class)
public class PersonServicesTest {
	
	@Mock
	private PersonRepository repository;

	@InjectMocks
	private PersonServices services;

	private Person person0;
	
	@BeforeEach
	public void setup() {
		person0 = new Person("Leandro", 
				"Costa", 
				"leandro@erudio.com.br",
				"Uberlandia - Minas Gerais - Brasil",
				"Male");
	}
	
	
	//implementando o TEST de SAVE PERSON

	//esse metodo vai testar se quando nos SALVAMOS um OBJ do tipo PERSON
	//é RETORNADO o OBJ PERSON q foi SALVO...
	@DisplayName("JUnit test for Given Person Object When Save person then return Person Object")
	@Test
	void testGivenPersonObject_WhenSavePerson_thenReturnPersonObject() {
		
		//Given / Arrange
		//QUANDO chamar o o metodo FINDBYEMAIL do REPOSITORY recebendo QUALQUER STRING
		//ele vai retornar um OBJ do tipo OPTIONAL VAZIO
		given(repository.findByEmail(anyString())).willReturn(Optional.empty());
		
		//QUANDO chamar o metodo SAVE do REPOSITORY, passando como argumento o PERSON0
		//o RETURN tem q ser um PERSON0... tem q ser o memso OBJ q foi passado tem q RETORNAR
		given(repository.save(person0)).willReturn(person0);
		
		//When / Act
		//criando um OBJ do tipo PERSON de nome SAVEDPERSON q vai receber o RETORNO
		//do metodo CREATE do SERVICES, apos passar o OBJ PERSON0
		Person savedPerson = services.create(person0);
		
		
		//Then / Assert
		//agora aqui vamos testar SE SAVEDPERSON nao ta NULL (ou seja se RETORNOU ALGO)
		assertNotNull(savedPerson);
		assertEquals("Leandro", savedPerson.getFirstName());		
		
	}
	
	//test[System Under Test]_[Condition or State Change]_[Expected Result]
	@DisplayName("JUnit test for Given Existing Email When Save Person then Throws Exception")
	@Test
	void testGivenExistingEmail_WhenSavePerson_thenThrowsException() {
		
		//Given / Arrange
		given(repository.findByEmail(anyString())).willReturn(Optional.of(person0));
				
		//When / Act

		assertThrows(ResourceNotFoundException.class, () -> {
			services.create(person0);
		});
				
		//Then / Assert
		verify(repository, never()).save(any(Person.class));
				
	}
	
	@DisplayName("JUnit test for Given Persons List When FindAll Persons then Return Persons List")
	@Test
	void testGivenPersonsList_WhenFindAllPersons_thenReturnPersonsList() {
		
		//Given / Arrange
		
		Person person1 = new Person("Leonardo", 
				"Costa", 
				"leonardo@erudio.com.br",
				"Uberlandia - Minas Gerais - Brasil",
				"Male");

		given(repository.findAll()).willReturn(List.of(person0, person1));
				
		//When / Act

		List<Person> personList = services.findAll();
				
		//Then / Assert
		assertNotNull(personList);
		assertEquals(2, personList.size());
				
	}
	
	@DisplayName("JUnit test for Given Empty Persons List When FindAll Persons then Return Empty Persons List")
	@Test
	void testGivenEmptyPersonsList_WhenFindAllPersons_thenReturnEmptyPersonsList() {
		
		//Given / Arrange
		given(repository.findAll()).willReturn(Collections.emptyList());
				
		//When / Act
		List<Person> personList = services.findAll();
				
		//Then / Assert
		assertTrue(personList.isEmpty());
		assertEquals(0, personList.size());
				
	}
	
	@DisplayName("JUnit test for Given Person Id When FindById then Return Person Object")
	@Test
	void testGivenPersonId_WhenFindById_thenReturnPersonObject() {
		
		//Given / Arrange
		given(repository.findById(anyLong())).willReturn(Optional.of(person0));
				
		//When / Act
		Person savedPerson = services.findById(1L);
				
		//Then / Assert
		assertNotNull(savedPerson);
		assertEquals("Leandro", savedPerson.getFirstName());
		
		
	}
		
	//TEST TESTANDO o METODO DE UPDATE
	@DisplayName("JUnit test for Given Person Object When Update Person then Return Updated Person Object")
	@Test
	void testGivenPersonObject_WhenUpdatePerson_thenReturnUpdatedPersonObject() {
		
		//Given / Arrange
		person0.setId(1L);		
		given(repository.findById(anyLong())).willReturn(Optional.of(person0));
		person0.setEmail("leonardo@erudio.com.br");
		person0.setFirstName("Leonardo");
		given(repository.save(person0)).willReturn(person0);
			
		//When / Act
		Person updatedPerson = services.update(person0);

		//Then / Assert

		assertNotNull(updatedPerson);
		assertEquals("Leonardo", updatedPerson.getFirstName());
		assertEquals("leonardo@erudio.com.br", updatedPerson.getEmail());	
	}
	

	@DisplayName("JUnit test for Given Person ID When Delete Person then Do Nothing")
	@Test
	void testGivenPersonID_WhenDeletePerson_thenDoNothing() {
		
		//Given / Arrange
		person0.setId(1L);
		given(repository.findById(anyLong())).willReturn(Optional.of(person0));
		willDoNothing().given(repository).delete(person0);
				
		//When / Act
		services.delete(person0.getId());
				
		//Then / Assert
		verify(repository, times(1)).delete(person0);
	}
	
}
