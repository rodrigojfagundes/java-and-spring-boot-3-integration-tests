package br.com.erudio.controllers;

import static org.mockito.BDDMockito.*;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.erudio.exceptions.ResourceNotFoundException;
import br.com.erudio.model.Person;
import br.com.erudio.services.PersonServices;

@WebMvcTest
public class PersonControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper mapper;
	
	@MockBean
	private PersonServices service;
	
	private Person person;
	
	@BeforeEach
	public void setup() {
		person = new Person("Leandro", 
				"Costa", 
				"leandro@erudio.com.br",
				"Uberlandia - Minas Gerais - Brasil",
				"Male");
	}
	
	
	//metodo para TESTAR metodo CREATE do PERSONCONTROLLER.JAVA... Para pd CRIAR um novo
	//PERSON
	//
	//test[System Under Test]_[Condition or State Change]_[Expected Result]
	@Test
	@DisplayName("Given Person Object When Create Person Then Return Saved Person")
	void testGivenPersonObject_WhenCreatePerson_ThenReturnSavedPerson() throws JsonProcessingException, Exception {
		
		//Given / Arrange

		//QUANDO CHAMAR O METODO CREATE DO SERVICE, RECEBENDO QUALQUER INSTANCIA de 
		//PERSON.CLASS.... Vai RETORNAR(willAnswer) com uma funcao LAMBDA o ARGUMENTO
		//Ou seja VAI RETORNAR o OBJ q foi CRIADO... no CASO O PERSON q FOI CRIADO
		given(service.create(any(Person.class)))
			.willAnswer((invocation) -> invocation.getArgument(0));
		
		//When / Act
		
		//chamando o METODO PERFORM do MOCKMVC e ele vai fazer uma REQUISICAO do TIPO
		//POST para a URL /PERSON... Q no caso cai no PERSONCONTROLLER.JAVA
		//e o POST e o do METODO CREATE... E dai USANDO JSON nos vamos passar o 
		//CONTENT/CONTEUDO um OBJ PERSON com o CONTEUDO da requisicao... 
		//Ou seja vamos CAD o OBJ PERSON... e o OBJ MAPPER q pegou os DADOS do PERSON
		//e criou um JSON com as INFO...
		ResultActions response = mockMvc.perform(post("/person")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(person)));
		
		//Then / Assert
		
		//vamos verificar o RESULTADO, pois apos CAD um PERSON ele deve RETORNAR
		//o PERSON q foi CAD...
		response.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.firstName", is(person.getFirstName())))
		.andExpect(jsonPath("$.lastName", is(person.getLastName())))
		.andExpect(jsonPath("$.email", is(person.getEmail())));
		
		
	}
	
	@Test
	@DisplayName("Given List Of Persons When FindAll Persons Then Return Persons List")
	void testGivenListOfPersons_WhenFindAllPersons_ThenReturnPersonsList() throws JsonProcessingException, Exception {
		
		//Given / Arrange
		List<Person> persons = new ArrayList<>();
		persons.add(person);
		persons.add(new Person("Leonardo", 
				"Costa", 
				"leonardo@erudio.com.br",
				"Uberlandia - Minas Gerais - Brasil",
				"Male"));
		given(service.findAll()).willReturn(persons);
		
		//When / Act
		ResultActions response = mockMvc.perform(get("/person"));
		
		//Then / Assert
		response
		.andExpect(status().isOk())
		.andDo(print())
		.andExpect(jsonPath("$.size()", is(persons.size())));
		
	}

	@Test
	@DisplayName("Given Person Id When FindById Then Return Person Object")
	void testGivenPersonId_WhenFindById_ThenReturnPersonObject() throws JsonProcessingException, Exception {
		
		//Given / Arrange
		long personId = 1L;		
		given(service.findById(personId))
			.willReturn(person);
		
		//When / Act
		ResultActions response = mockMvc.perform(get("/person/{id}", personId));
		
		//Then / Assert
		response
		.andExpect(status().isOk())
		.andDo(print())
		.andExpect(jsonPath("$.firstName", is(person.getFirstName())))
		.andExpect(jsonPath("$.lastName", is(person.getLastName())))
		.andExpect(jsonPath("$.email", is(person.getEmail())));
		
		
	}

	@Test
	@DisplayName("Given Invalid Person Id When FindById Then Return Not Found")
	void testGivenInvalidPersonId_WhenFindById_ThenReturnNotFound() throws JsonProcessingException, Exception {
		
		//Given / Arrange
		long personId = 1L;
		given(service.findById(personId))
			.willThrow(ResourceNotFoundException.class);
		
		//When / Act
		ResultActions response = mockMvc.perform(get("/person/{id}", personId));
		
		//Then / Assert
		response
		.andExpect(status().isNotFound())
		.andDo(print());		
		
	}
	
	@Test
	@DisplayName("Given Update PersonWhen Update Then Return Updated Person Object")
	void testGivenUpdatePerson_WhenUpdate_ThenReturnUpdatedPersonObject() throws JsonProcessingException, Exception {
		
		//Given / Arrange
		long personId = 1L;
		
		given(service.findById(personId)).willReturn(person);
		given(service.update(any(Person.class)))
			.willAnswer((invocation) -> invocation.getArgument(0));
				
		//When / Act
		Person updatedPerson = new Person("Leonardo", 
				"Costa", 
				"leonardo@erudio.com.br",
				"Uberlandia - Minas Gerais - Brasil",
				"Male");
		
		ResultActions response = mockMvc.perform(put("/person")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(updatedPerson)));
		
		//Then / Assert
		response
		.andExpect(status().isOk())
		.andDo(print())
		.andExpect(jsonPath("$.firstName", is(updatedPerson.getFirstName())))
		.andExpect(jsonPath("$.lastName", is(updatedPerson.getLastName())))
		.andExpect(jsonPath("$.email", is(updatedPerson.getEmail())));
		
		
	}

	@Test
	@DisplayName("Given Unexistent Person When Update Then Return Not Found")
	void testGivenUnexistentPerson_WhenUpdate_ThenReturnNotFound() throws JsonProcessingException, Exception {
		
		//Given / Arrange
		long personId = 1L;
		given(service.findById(personId)).willReturn(person);

		given(service.update(any(Person.class)))
			.willAnswer((invocation) -> invocation.getArgument(1));
				
		//When / Act
		Person updatedPerson = new Person("Leonardo", 
				"Costa", 
				"leonardo@erudio.com.br",
				"Uberlandia - Minas Gerais - Brasil",
				"Male");
		
		ResultActions response = mockMvc.perform(put("/person")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(updatedPerson)));
		
		//Then / Assert
		response
		.andExpect(status().isNotFound())
		.andDo(print());
	}	
	
	@Test
	@DisplayName("Give Person Id When Delete Then Return Not Content")
	void testGivePersonId_WhenDelete_ThenReturnNotContent() throws JsonProcessingException, Exception {
		
		//Given / Arrange
		long personId = 1L;
		willDoNothing().given(service).delete(personId);
				
		//When / Act
		ResultActions response = mockMvc.perform(delete("/person/{id}", personId));
		
		//Then / Assert
		response
		.andExpect(status().isNoContent())
		.andDo(print());		
	}
	
}