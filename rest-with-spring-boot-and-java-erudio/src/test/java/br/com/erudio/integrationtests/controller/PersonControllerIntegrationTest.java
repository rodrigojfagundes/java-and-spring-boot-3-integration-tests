package br.com.erudio.integrationtests.controller;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
	
	//TEST do TESTANDO o METODO CREATE PERSON... com TEST de INTEGRACAO
	@DisplayName("JUnit integration given Person Object Test when Create One Person Should Return A Person Object")
	@Order(1)
	@Test
	void integrationTestGivenPersonObject_when_CreateOnePerson_ShouldReturnAPersonObject() throws JsonMappingException, JsonProcessingException {

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
		//
		//e todo retorno vai ficar SALVO na VAR CONTENT
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
		
		

		//ASSERT 
		//para poder verificar se O RETORNO do METODO CREATE
		//ta conforme o esperado
		assertNotNull(createPerson);
		assertNotNull(createPerson.getId());
		assertNotNull(createPerson.getFirstName());	
		assertNotNull(createPerson.getLastName());
		assertNotNull(createPerson.getAddress());
		assertNotNull(createPerson.getGender());
		assertNotNull(createPerson.getEmail());
		
		assertTrue(createPerson.getId() > 0);
		assertEquals("Leandro", createPerson.getFirstName());	
		assertNotNull("Costa", createPerson.getLastName());
		assertNotNull("Uberlandia - Minas Gerais - Brasil", createPerson.getAddress());
		assertNotNull("Male", createPerson.getGender());
		assertNotNull("leandro@erudio.com.br", createPerson.getEmail());
	}
	
	
}



