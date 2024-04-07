package fr.uga.l3miage.spring.tp3.components;

import fr.uga.l3miage.spring.tp3.exceptions.technical.CandidateNotFoundException;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
import org.hibernate.annotations.NotFound;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class CandidateComponentTest {
    @Autowired
    private CandidateComponent candidateComponent;
    @MockBean
    private CandidateRepository candidateRepository;




    //######### Début Test methode getCandidatById #####################################################
    @Test  // Ici on teste si la methode renvoie bien une exception si on ne trouve pas de candidat dans la base avce l'Id correspondant
    void getCandidatByIdNotFound(){
        //Given
        when(candidateRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when-then
        assertThrows(CandidateNotFoundException.class, ()->candidateComponent.getCandidatById(anyLong()));
    }

    @Test // Ici on teste si elle ne renvoie pas d'exception en comportement normale
    void getCandidatByIdFound(){
        //Given
        CandidateEntity candidateEntity = CandidateEntity
                .builder() //15 et 5
                .firstname("CedB")
                .lastname("KENB")
                .email("Bblabla@gmail.com")
                .phoneNumber("62121313")
                .birthDate(LocalDate.parse("2003-02-15"))
                .hasExtraTime(Boolean.parseBoolean("False"))
                .build();

        when(candidateRepository.findById(anyLong())).thenReturn(Optional.of(candidateEntity));
        assertDoesNotThrow( ()->candidateComponent.getCandidatById(anyLong()));
    }
    //######### Fin Test methode getCandidatById #####################################################




    //######### Début Test methode getAllEliminatedCandidate #####################################################
    @Test // Ici on teste si elle ne renvoie pas d'exception en comportement normale
    void getAllEliminatedCandidate(){

        //**********************Debut préGiven*****************************
        CandidateEvaluationGridEntity candidateEvaluationGridEntity15 = CandidateEvaluationGridEntity
                .builder()
                .grade(15)
                .build();

        CandidateEvaluationGridEntity candidateEvaluationGridEntity5 = CandidateEvaluationGridEntity
                .builder()
                .grade(8)
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

        // Relations bidirectionnelles sens CandidateEvaluationGridEntitieS ---> CandidateEntity
        candidateEvaluationGridEntity15.setCandidateEntity(candidateEntity);
        candidateEvaluationGridEntity8.setCandidateEntity(candidateEntity);

        candidateEvaluationGridEntity14.setCandidateEntity(candidateEntity1);
        candidateEvaluationGridEntity8.setCandidateEntity(candidateEntity1);
        candidateEvaluationGridEntity7_5.setCandidateEntity(candidateEntity1);

        candidateEvaluationGridEntity17.setCandidateEntity(candidateEntity2);

        //*******************Fin PréGiven******************************

        Set<CandidateEntity> EnsCand = Set.of(candidateEntity, candidateEntity1, candidateEntity2);
        //un ensemble de candidats, pour simuler la base de données
        //Given
        when( candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(15)).thenReturn(EnsCand);

        //then - when
        assertDoesNotThrow( ()->candidateComponent.getAllEliminatedCandidate());
    }
    //######### Fin Test methode getAllEliminatedCandidate #####################################################



}
