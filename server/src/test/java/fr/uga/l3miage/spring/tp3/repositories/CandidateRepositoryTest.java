package fr.uga.l3miage.spring.tp3.repositories;

import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.models.TestCenterEntity;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Set;

import static fr.uga.l3miage.spring.tp3.enums.TestCenterCode.*;
import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect" )
public class CandidateRepositoryTest {
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private TestCenterRepository testCenterRepository;

    @Test
    void testRequestfindAllByTestCenterEntityCode(){

        // créons des TestCenterEntity qui nous servirons pour le test
        TestCenterEntity testCenterEntity =  TestCenterEntity
                .builder()
                .code(GRE)  // on initialise que l'attribut dont on a besoin (code) pour ce context de test
                .build();

        TestCenterEntity testCenterEntity1 = TestCenterEntity
                .builder()
                .code(DIJ)
                .build();

        testCenterRepository.save(testCenterEntity);  // on enregsitre ces TestcenterEntity en Base
        testCenterRepository.save(testCenterEntity1);

        CandidateEntity candidateEntity = CandidateEntity
                .builder()
                .firstname("Ced")
                .lastname("KEN")
                .email("blabla@gmail.com")
                .phoneNumber("12121313")
                .birthDate(LocalDate.parse("2003-02-15"))
                .hasExtraTime(Boolean.parseBoolean("False"))
                .testCenterEntity(testCenterEntity)   // on affecte testCenterEntity à candidate comme atttribut
                .build();                                   // relation bidirectionnelle sens candidateEntity--->testCenterEntity

        CandidateEntity candidateEntity1 = CandidateEntity
                .builder()
                .firstname("Max")
                .lastname("BAN")
                .email("blibli@gmal.com")
                .phoneNumber("11111111")
                .birthDate(  LocalDate.parse("1999-01-23"))
                .hasExtraTime(Boolean.parseBoolean("True"))
                .testCenterEntity(testCenterEntity1)     // relation bidirectionnelle sens candidateEntity1--->testCenterEntity1
                .build();

        CandidateEntity candidateEntity2 = CandidateEntity
                .builder()
                .firstname("Carl")
                .lastname("NEN")
                .email("blabla@yahoo.com")
                .phoneNumber("44444444")
                .birthDate( LocalDate.parse("1884-04-17"))
                .hasExtraTime(Boolean.parseBoolean("False"))
                .testCenterEntity(testCenterEntity)     // relation bidirectionnelle sens candidateEntity2--->testCenterEntity
                .build();

        candidateRepository.save(candidateEntity);   // maintenent on enregistre les candidateEntity en base
        candidateRepository.save(candidateEntity1);
        candidateRepository.save(candidateEntity2);

        // relation bidirectionnelle sens testCenterEntity--->CandidateEntity
        testCenterEntity.setCandidateEntities(Set.of(candidateEntity, candidateEntity2)); // je sauvegarde les candidats 1 et 2 comme attributs de testCenterEntity
        testCenterEntity1.setCandidateEntities( Set.of(candidateEntity1));



        // when
        Set<CandidateEntity> candidateEntitiesResponses = candidateRepository.findAllByTestCenterEntityCode(DIJ); // testcenter présent
        //then
        assertThat(candidateEntitiesResponses).hasSize(1);
        assertThat(candidateEntitiesResponses.stream().findFirst().get().getTestCenterEntity().getCode()).isEqualTo(DIJ);

        // when
        Set<CandidateEntity> candidateEntitiesResponses2 = candidateRepository.findAllByTestCenterEntityCode(TOU); // testcenter non présent
        //then
        assertThat(candidateEntitiesResponses2).hasSize(0);
        // when
        Set<CandidateEntity> candidateEntitiesResponses3 = candidateRepository.findAllByTestCenterEntityCode(GRE); // testcenter non présent
        //then
        assertThat(candidateEntitiesResponses3).hasSize(2);
        assertThat(candidateEntitiesResponses3.stream().findFirst().get().getTestCenterEntity().getCode()).isEqualTo(GRE);





    }
}
