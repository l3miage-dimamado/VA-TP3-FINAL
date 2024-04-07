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
    @Autowired
    private  CandidateEvaluationGridRepository candidateEvaluationGridRepository;

    @Test
    void testRequestFindAllByTestCenterEntityCode(){
        //Given
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
                .birthDate( LocalDate.parse("2019-04-17"))
                .hasExtraTime(Boolean.parseBoolean("False"))
                .testCenterEntity(testCenterEntity)     // relation bidirectionnelle sens candidateEntity2--->testCenterEntity
                .build();

        candidateRepository.save(candidateEntity);   // maintenent on enregistre les candidateEntity en base
        candidateRepository.save(candidateEntity1);
        candidateRepository.save(candidateEntity2);

        // relation bidirectionnelle sens testCenterEntity--->CandidateEntitieS
        testCenterEntity.setCandidateEntities(Set.of(candidateEntity, candidateEntity2)); // je sauvegarde les candidats 1 et 2 comme attributs de testCenterEntity
        testCenterEntity1.setCandidateEntities( Set.of(candidateEntity1));



        // when
        Set<CandidateEntity> candidateEntitiesResponses = candidateRepository.findAllByTestCenterEntityCode(DIJ); // testcenter présent avec 1 candidat
        //then
        assertThat(candidateEntitiesResponses).hasSize(1);
        assertThat(candidateEntitiesResponses.stream().findFirst().get().getTestCenterEntity().getCode()).isEqualTo(DIJ);

        // when
        Set<CandidateEntity> candidateEntitiesResponses2 = candidateRepository.findAllByTestCenterEntityCode(TOU); // testcenter non présent
        //then
        assertThat(candidateEntitiesResponses2).hasSize(0);
        // when
        Set<CandidateEntity> candidateEntitiesResponses3 = candidateRepository.findAllByTestCenterEntityCode(GRE); // testcenter  présents avec 2 candidats
        //then
        assertThat(candidateEntitiesResponses3).hasSize(2);
        assertThat(candidateEntitiesResponses3.stream().findFirst().get().getTestCenterEntity().getCode()).isEqualTo(GRE);
    }

    @Test
    void testRequestFindAllByCandidateEvaluationGridEntitiesGradeLessThan(){
        //Given
        CandidateEvaluationGridEntity candidateEvaluationGrid10 = CandidateEvaluationGridEntity
                .builder()
                .grade(10)
                .build();

        CandidateEvaluationGridEntity candidateEvaluationGrid17 = CandidateEvaluationGridEntity
                .builder()
                .grade(17)
                .build();

        CandidateEvaluationGridEntity candidateEvaluationGrid4 = CandidateEvaluationGridEntity
                .builder()
                .grade(4)
                .build();

        CandidateEntity candidateEntity1 = CandidateEntity
                .builder()
                .birthDate(LocalDate.now())
                .hasExtraTime(false)
                .email("ici@test")
                .candidateEvaluationGridEntities(Set.of(candidateEvaluationGrid10, candidateEvaluationGrid17))
                .build();
        candidateEvaluationGrid10.setCandidateEntity(candidateEntity1);
        candidateEvaluationGrid17.setCandidateEntity(candidateEntity1);

        CandidateEntity candidateEntity2 = CandidateEntity
                .builder()
                .birthDate(LocalDate.now())
                .hasExtraTime(true)
                .email("ci2@test")
                .candidateEvaluationGridEntities(Set.of(candidateEvaluationGrid4))
                .build();

        candidateEvaluationGrid4.setCandidateEntity(candidateEntity2);


        candidateRepository.save(candidateEntity1);
        candidateRepository.save(candidateEntity2);
        candidateEvaluationGridRepository.save(candidateEvaluationGrid10);
        candidateEvaluationGridRepository.save(candidateEvaluationGrid17);
        candidateEvaluationGridRepository.save(candidateEvaluationGrid4);

        //when
        Set<CandidateEntity> candidateEntitiesResponses1 = candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(11.00);
        Set<CandidateEntity> candidateEntitiesResponses2 = candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(4);
        Set<CandidateEntity> candidateEntitiesResponses3 = candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(18.00);
        Set<CandidateEntity> candidateEntitiesResponses4 = candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(10.00);
        //then
        assertThat(candidateEntitiesResponses1).hasSize(2);
        assertThat(candidateEntitiesResponses2).hasSize(0);
        assertThat(candidateEntitiesResponses3).hasSize(2);
        assertThat(candidateEntitiesResponses4).hasSize(1);
    }

    @Test
    void testRequestFindAllByHasExtraTimeFalseAndBirthDateBefore(){
        //Given
        CandidateEntity candidateEntity = CandidateEntity
                .builder()
                .firstname("CedC")
                .lastname("KENC")
                .email("Cblabla@gmail.com")
                .phoneNumber("72121313")
                .birthDate(LocalDate.parse("1973-02-15"))
                .hasExtraTime(false)
                .build();

        CandidateEntity candidateEntity1 = CandidateEntity
                .builder()
                .firstname("MaxC")
                .lastname("BANC")
                .email("Cblibli@gmal.com")
                .phoneNumber("61111111")
                .birthDate(  LocalDate.parse("1999-01-23"))
                .hasExtraTime(true)
                .build();

        CandidateEntity candidateEntity2 = CandidateEntity
                .builder()
                .firstname("CarlC")
                .lastname("NENB")
                .email("Cblabla@yahoo.com")
                .phoneNumber("64444444")
                .birthDate( LocalDate.parse("2001-04-17"))
                .hasExtraTime(false)
                .build();

        candidateRepository.save(candidateEntity);   // maintenent on enregistre les candidateEntity en base
        candidateRepository.save(candidateEntity1);
        candidateRepository.save(candidateEntity2);

        //when
        Set<CandidateEntity> candidateEntitiesResponses = candidateRepository.findAllByHasExtraTimeFalseAndBirthDateBefore(LocalDate.parse("1978-02-15")); // 1 élément à trouver
        Set<CandidateEntity> candidateEntitiesResponses2 = candidateRepository.findAllByHasExtraTimeFalseAndBirthDateBefore(LocalDate.parse("1973-01-15")); // 0 élément à trouver
        Set<CandidateEntity> candidateEntitiesResponses3 = candidateRepository.findAllByHasExtraTimeFalseAndBirthDateBefore(LocalDate.parse("1973-02-14"));  // 0 élément à trouver
        Set<CandidateEntity> candidateEntitiesResponses4 = candidateRepository.findAllByHasExtraTimeFalseAndBirthDateBefore(LocalDate.parse("1973-02-15"));  // // 0 élément à trouver

        Set<CandidateEntity> candidateEntitiesResponses5 = candidateRepository.findAllByHasExtraTimeFalseAndBirthDateBefore(LocalDate.parse("2000-01-01"));  // // 1 élément à trouver
        Set<CandidateEntity> candidateEntitiesResponses6 = candidateRepository.findAllByHasExtraTimeFalseAndBirthDateBefore(LocalDate.parse("2002-01-01"));  // // 2 élément à trouver
        Set<CandidateEntity> candidateEntitiesResponses7 = candidateRepository.findAllByHasExtraTimeFalseAndBirthDateBefore(LocalDate.parse("1888-01-01"));  // // 0 élément à trouver

        //then
        assertThat(candidateEntitiesResponses).hasSize(1);
        assertThat(candidateEntitiesResponses2).hasSize(0);
        assertThat(candidateEntitiesResponses3).hasSize(0);
        assertThat(candidateEntitiesResponses4).hasSize(0);
        assertThat(candidateEntitiesResponses5).hasSize(1);
        assertThat(candidateEntitiesResponses6).hasSize(2);
        assertThat(candidateEntitiesResponses7).hasSize(0);


    }


}
