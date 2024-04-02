package br.com.erudio.integrationtests.controller;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.erudio.config.TestConfigs;
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.erudio.model.Person;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

//Essa class de test de integracao tera todo o ciclo de vida
//do CONTROLLER para o SERVICE e dps para o REPOSITORY ou seja
//pecorre toda as camadas
//
//herdando o ABSTRACTINTEGRATIONTEST q é onde tem as CONFIG para
//rodar o CONTAINER com o MYSQL

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
class PersonControllerIntegrationTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;
	private static Person person;
	
	@BeforeAll
	public static void setup() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		//setando as CONFIG do SPECIFICATION q e do TIPO REQUESTSPECIFICATION
		//q serve para CONCENTRAR as INFO q serao REPETIDAS em MAIS DE UM TEST
		specification = new RequestSpecBuilder()
				.setBasePath("/person")
				.setPort(TestConfigs.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();

		person = new Person("Leandro", 
				"Costa", 
				"leandro@erudio.com.br",
				"Uberlandia - Minas Gerais - Brasil",
				"Male");
	}
	
	//TEST INTEGRATION - TESTANDO o METODO CREATE PERSON... com TEST de INTEGRACAO
	@DisplayName("JUnit integration given Person Object Test when Create One Person Should Return A Person Object")
	@Order(1)
	@Test
	void integrationTestGivenPersonObject_when_CreateOnePerson_ShouldReturnAPersonObject() throws JsonMappingException, JsonProcessingException {
		//
		//vamos fazer uma REQUISICAO do TIPO POST para o metodo CREATE do
		//PERSONCONTROLLER.JAVA... Passsando um OBJ do tipo PERSON
		//
		//chamando o GIVEN passando o OBJ SPEC(com a configuracao para fazer
		//requisicoes)
		//
		//no CONTENTTYPE nos passamos a CLASS TESTCONFIG e vamos passar q o TIPO
		//de conteudo a ser enviado e no FORMATO JSON
		//
		//e no .BODY nos passamos o OBJ q sera CAD, o OBJ PERSON...
		//
		//no WHEN nos informamos q sera uma REQUISICAO do tipo POST
		//
		//o statuscode é 200, ou seja q deu ok
		//
		//o EXTRACT e para nos extrairmos o BODY como uma STRING
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.body(person)
		.when()
			.post()
		.then()
			.statusCode(200)
		.extract()
			.body()
				.asString();

		//chamando o metodo READVALUE do OBJECTMAPPER e passando a nossa
		//VAR CONTENT (q tem o retorno do POST do PERSON no formato JSON)
		//e passando o PERSON.CLASS... Ou seja e para PEGA o q ta na VAR
		//CONTENT e criar um OBJ PERSON
		Person createPerson = objectMapper.readValue(content, Person.class);
		person = createPerson;
		
		//ASSERT para poder verificar se O RETORNO do METODO CREATE
		//ta conforme o esperado
		assertNotNull(createPerson);
		assertNotNull(createPerson.getId());
		assertNotNull(createPerson.getFirstName());	
		assertNotNull(createPerson.getLastName());
		assertNotNull(createPerson.getAddress());
		assertNotNull(createPerson.getGender());
		assertNotNull(createPerson.getEmail());
				
		//verificando valores ESPECIFICOS
		assertTrue(createPerson.getId() > 0);
		assertEquals("Leandro", createPerson.getFirstName());	
		assertNotNull("Costa", createPerson.getLastName());
		assertNotNull("Uberlandia - Minas Gerais - Brasil", createPerson.getAddress());
		assertNotNull("Male", createPerson.getGender());
		assertNotNull("leandro@erudio.com.br", createPerson.getEmail());
	}
	
	//test[System Under Test]_[Condition or State Change]_[Expected Result]
	@DisplayName("JUnit integration given Person Object Test when Update One Person Should Return A Updated Person Object")
	@Order(2)
	@Test
	void integrationTestGivenPersonObject_when_UpdateOnePerson_ShouldReturnAUpdatedPersonObject() throws JsonMappingException, JsonProcessingException {
		person.setFirstName("Leonardo");
		person.setEmail("leonardo@erudio.com.br");

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.body(person)
		.when()
			.put()
		.then()
			.statusCode(200)
		.extract()
			.body()
				.asString();
		
		Person updatedPerson = objectMapper.readValue(content, Person.class);
		person = updatedPerson;
		
		assertNotNull(updatedPerson);
		assertNotNull(updatedPerson.getId());
		assertNotNull(updatedPerson.getFirstName());	
		assertNotNull(updatedPerson.getLastName());
		assertNotNull(updatedPerson.getAddress());
		assertNotNull(updatedPerson.getGender());
		assertNotNull(updatedPerson.getEmail());
		
		assertTrue(updatedPerson.getId() > 0);
		assertEquals("Leonardo", updatedPerson.getFirstName());	
		assertNotNull("Costa", updatedPerson.getLastName());
		assertNotNull("Uberlandia - Minas Gerais - Brasil", updatedPerson.getAddress());
		assertNotNull("Male", updatedPerson.getGender());
		assertNotNull("leonardo@erudio.com.br", updatedPerson.getEmail());
	}
	
	
	//TEST INTEGRATION Testando o METODO FINDBYID do PERSONCONTROLLER.JAVA
	//
	//test[System Under Test]_[Condition or State Change]_[Expected Result]
	@DisplayName("JUnit integration given Person Object when findById Should Return A Person Object")
	@Order(3)
	@Test
	void integrationTestGivenPersonObject_when_findById_ShouldReturnAPersonObject() throws JsonMappingException, JsonProcessingException {

		var content = given().spec(specification)
				.pathParam("id", person.getId())
		.when()
			.get("{id}")
		.then()
			.statusCode(200)
		.extract()
			.body()
				.asString();

		Person foundPerson = objectMapper.readValue(content, Person.class);

		assertNotNull(foundPerson);
		assertNotNull(foundPerson.getId());
		assertNotNull(foundPerson.getFirstName());	
		assertNotNull(foundPerson.getLastName());
		assertNotNull(foundPerson.getAddress());
		assertNotNull(foundPerson.getGender());
		assertNotNull(foundPerson.getEmail());

		assertTrue(foundPerson.getId() > 0);
		assertEquals("Leonardo", foundPerson.getFirstName());	
		assertNotNull("Costa", foundPerson.getLastName());
		assertNotNull("Uberlandia - Minas Gerais - Brasil", foundPerson.getAddress());
		assertNotNull("Male", foundPerson.getGender());
		assertNotNull("leonardo@erudio.com.br", foundPerson.getEmail());
	}

	//test[System Under Test]_[Condition or State Change]_[Expected Result]
	@DisplayName("JUnit integration given Person Object when findAll Should Return a Persons List")
	@Order(4)
	@Test
	void integrationTest_when_findByAll_ShouldReturnAPersonsList() throws JsonMappingException, JsonProcessingException {

		Person anotherPerson = new Person("Gabriela", 
				"Rodriguez", 
				"gabi@erudio.com.br",
				"Uberlandia - Minas Gerais - Brasil",
				"Female"
			);
		
		given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.body(anotherPerson)
		.when()
			.post()
		.then()
			.statusCode(200);			
		var content = given().spec(specification)
		.when()
			.get()
		.then()
			.statusCode(200)
		.extract()
			.body()
				.asString();
		
		Person[] MyArray = objectMapper.readValue(content, Person[].class);
		List<Person> people = Arrays.asList(MyArray);
		Person foundPersonOne = people.get(0);
		
		assertNotNull(foundPersonOne);
		assertNotNull(foundPersonOne.getId());
		assertNotNull(foundPersonOne.getFirstName());	
		assertNotNull(foundPersonOne.getLastName());
		assertNotNull(foundPersonOne.getAddress());
		assertNotNull(foundPersonOne.getGender());
		assertNotNull(foundPersonOne.getEmail());
		
		assertTrue(foundPersonOne.getId() > 0);
		assertEquals("Leonardo", foundPersonOne.getFirstName());	
		assertNotNull("Costa", foundPersonOne.getLastName());
		assertNotNull("Uberlandia - Minas Gerais - Brasil", foundPersonOne.getAddress());
		assertNotNull("Male", foundPersonOne.getGender());
		assertNotNull("leonardo@erudio.com.br", foundPersonOne.getEmail());

		Person foundPersonTwo = people.get(1);

		assertNotNull(foundPersonTwo);
		assertNotNull(foundPersonTwo.getId());
		assertNotNull(foundPersonTwo.getFirstName());	
		assertNotNull(foundPersonTwo.getLastName());
		assertNotNull(foundPersonTwo.getAddress());
		assertNotNull(foundPersonTwo.getGender());
		assertNotNull(foundPersonTwo.getEmail());

		assertTrue(foundPersonTwo.getId() > 0);
		assertEquals("Gabriela", foundPersonTwo.getFirstName());	
		assertNotNull("Rodriguez", foundPersonTwo.getLastName());
		assertNotNull("Uberlandia - Minas Gerais - Brasil", foundPersonTwo.getAddress());
		assertNotNull("Female", foundPersonTwo.getGender());
		assertNotNull("gabi@erudio.com.br", foundPersonTwo.getEmail());
	
		
	}	
	
	//test[System Under Test]_[Condition or State Change]_[Expected Result]
	@DisplayName("JUnit integration given Person Object when delete Should Return No Content")
	@Order(5)
	@Test
	void integrationTestGivenPersonObject_when_delete_ShouldReturnNoContent() throws JsonMappingException, JsonProcessingException {

		var content = given().spec(specification)
				.pathParam("id", person.getId())
		.when()
			.delete("{id}")
		.then()
			.statusCode(204);		
	}
}

