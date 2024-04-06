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
        Set<CandidateEntity> candidateEntitiesResponses = candidateRepository.findAllByTestCenterEntityCode(DIJ); // testcenter présent
        //then
        assertThat(candidateEntitiesResponses).hasSize(1);
        assertThat(candidateEntitiesResponses.stream().findFirst().get().getTestCenterEntity().getCode()).isEqualTo(DIJ);

        // when
        Set<CandidateEntity> candidateEntitiesResponses2 = candidateRepository.findAllByTestCenterEntityCode(TOU); // testcenter non présent
        //then
        assertThat(candidateEntitiesResponses2).hasSize(0);
        // when
        Set<CandidateEntity> candidateEntitiesResponses3 = candidateRepository.findAllByTestCenterEntityCode(GRE); // 2 testcenter  présents
        //then
        assertThat(candidateEntitiesResponses3).hasSize(2);
        assertThat(candidateEntitiesResponses3.stream().findFirst().get().getTestCenterEntity().getCode()).isEqualTo(GRE);
    }


    @Test
    void testRequestfindAllByCandidateEvaluationGridEntitiesGradeLessThan(){

        CandidateEvaluationGridEntity candidateEvaluationGridEntity15 = CandidateEvaluationGridEntity
                .builder()
                .grade(15)
                .build();

        CandidateEvaluationGridEntity candidateEvaluationGridEntity5 = CandidateEvaluationGridEntity
                .builder()
                .grade(5)
                .build();

        CandidateEvaluationGridEntity candidateEvaluationGridEntity7_5 = CandidateEvaluationGridEntity
                .builder()
                .grade(7.5)
                .build();

        CandidateEvaluationGridEntity candidateEvaluationGridEntity17 = CandidateEvaluationGridEntity
                .builder()
                .grade(17)
                .build();

        CandidateEvaluationGridEntity candidateEvaluationGridEntity14 = CandidateEvaluationGridEntity
                .builder()
                .grade(14)
                .build();

        CandidateEvaluationGridEntity candidateEvaluationGridEntity8 = CandidateEvaluationGridEntity
                .builder()
                .grade(8)
                .build();

        candidateEvaluationGridRepository.save(candidateEvaluationGridEntity15);
        candidateEvaluationGridRepository.save(candidateEvaluationGridEntity8);
        candidateEvaluationGridRepository.save(candidateEvaluationGridEntity7_5);
        candidateEvaluationGridRepository.save(candidateEvaluationGridEntity17);
        candidateEvaluationGridRepository.save(candidateEvaluationGridEntity14);
        candidateEvaluationGridRepository.save(candidateEvaluationGridEntity8);




        CandidateEntity candidateEntity = CandidateEntity
                .builder() //15 et 5
                .firstname("CedB")
                .lastname("KENB")
                .email("Bblabla@gmail.com")
                .phoneNumber("62121313")
                .birthDate(LocalDate.parse("2003-02-15"))
                .hasExtraTime(Boolean.parseBoolean("False"))
                .candidateEvaluationGridEntities(Set.of(candidateEvaluationGridEntity15, candidateEvaluationGridEntity8))
                .build();

        CandidateEntity candidateEntity1 = CandidateEntity
                .builder()
                .firstname("MaxB")
                .lastname("BANB")
                .email("Bblibli@gmal.com")
                .phoneNumber("51111111")
                .birthDate(  LocalDate.parse("1999-01-23"))
                .hasExtraTime(Boolean.parseBoolean("True"))
                .candidateEvaluationGridEntities( Set.of(candidateEvaluationGridEntity14, candidateEvaluationGridEntity8, candidateEvaluationGridEntity7_5))
                .build();

        CandidateEntity candidateEntity2 = CandidateEntity
                .builder()
                .firstname("CarlB")
                .lastname("NENB")
                .email("Bblabla@yahoo.com")
                .phoneNumber("54444444")
                .birthDate( LocalDate.parse("2022-04-17"))
                .hasExtraTime(Boolean.parseBoolean("False"))
                .candidateEvaluationGridEntities( Set.of( candidateEvaluationGridEntity17))
                .build();

        candidateRepository.save(candidateEntity);   // maintenant on enregistre les candidateEntity en base
        candidateRepository.save(candidateEntity1);
        candidateRepository.save(candidateEntity2);

        // Relations bidirectionnelles sens CandidateEvaluationGridEntitieS ---> CandidateEntity
        candidateEvaluationGridEntity15.setCandidateEntity(candidateEntity);
        candidateEvaluationGridEntity8.setCandidateEntity(candidateEntity);

        candidateEvaluationGridEntity14.setCandidateEntity(candidateEntity1);
        candidateEvaluationGridEntity8.setCandidateEntity(candidateEntity1);
        candidateEvaluationGridEntity7_5.setCandidateEntity(candidateEntity1);

        candidateEvaluationGridEntity17.setCandidateEntity(candidateEntity2);


        // when

        Set<CandidateEntity> candidateEntitiesResponses = candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(15);
        Set<CandidateEntity> candidateEntitiesResponses2 = candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(14);
        Set<CandidateEntity> candidateEntitiesResponses3 = candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(17);
        Set<CandidateEntity> candidateEntitiesResponses4 = candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(5);

        //then
        assertThat(candidateEntitiesResponses).hasSize(2);
        assertThat(candidateEntitiesResponses2).hasSize(2);
        assertThat(candidateEntitiesResponses3).hasSize(1);
        assertThat(candidateEntitiesResponses).hasSize(0);

        // ****Ce test sur la fonction RequestfindAllByCandidateEvaluationGridEntitiesGradeLessThan échoue,
         // ****celà nous prouve qu'elle ne retourne pas le résultat attendu?
    }

    @Test
    void testRequestfindAllByHasExtraTimeFalseAndBirthDateBefore(){
        CandidateEntity candidateEntity = CandidateEntity
                .builder()
                .firstname("CedC")
                .lastname("KENC")
                .email("Cblabla@gmail.com")
                .phoneNumber("72121313")
                .birthDate(LocalDate.parse("1973-02-15"))
                .hasExtraTime(Boolean.parseBoolean("False"))
                .build();

        CandidateEntity candidateEntity1 = CandidateEntity
                .builder()
                .firstname("MaxC")
                .lastname("BANC")
                .email("Cblibli@gmal.com")
                .phoneNumber("61111111")
                .birthDate(  LocalDate.parse("1999-01-23"))
                .hasExtraTime(Boolean.parseBoolean("True"))
                .build();

        CandidateEntity candidateEntity2 = CandidateEntity
                .builder()
                .firstname("CarlC")
                .lastname("NENB")
                .email("Cblabla@yahoo.com")
                .phoneNumber("64444444")
                .birthDate( LocalDate.parse("2001-04-17"))
                .hasExtraTime(Boolean.parseBoolean("False"))
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

        //Given
        assertThat(candidateEntitiesResponses).hasSize(1);
        assertThat(candidateEntitiesResponses2).hasSize(0);
        assertThat(candidateEntitiesResponses3).hasSize(0);
        assertThat(candidateEntitiesResponses4).hasSize(0);
        assertThat(candidateEntitiesResponses5).hasSize(1);
        assertThat(candidateEntitiesResponses6).hasSize(2);
        assertThat(candidateEntitiesResponses7).hasSize(0);


    }


}
