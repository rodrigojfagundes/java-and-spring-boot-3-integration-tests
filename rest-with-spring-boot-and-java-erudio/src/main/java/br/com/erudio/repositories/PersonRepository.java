package br.com.erudio.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.erudio.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

	Optional<Person> findByEmail(String email);
	
	@Query("select p from Person p where p.firstName =?1 and p.lastName =?2")
	Person findByJPQL(String firstName, String lastName);
	
	@Query("select p from Person p where p.firstName =:firstName and p.lastName =:lastName")
	Person findByJPQLNamedParameters(
			@Param("firstName") String firstName,
			@Param("lastName") String lastName);
	 
	@Query(value = "select * from person p where p.first_name =?1 and p.last_name =?2", nativeQuery = true)
	Person findByNativeSQL(String firstName, String lastName);
	
	@Query(value = "select * from person p where p.first_name =:firstName and p.last_name =:lastName", nativeQuery = true)
	Person findByNativeSQLwithNamedParameters(
			//usando a ANNOTATION @PARAM para para receber OS PARAMETROS q queremos usar para PESQUISAR
			@Param("firstName") String firstName,
			@Param("lastName") String lastName);
	
	
}