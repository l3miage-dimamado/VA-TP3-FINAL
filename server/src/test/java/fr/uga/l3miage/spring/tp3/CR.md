# Compte Rendu TP3 Final Spring Test et VA L3 MIAGE 07 Avril 2024
## Binome 1 :Mamadou Diallo
## Binome 2: KENGNI Cedrix


## 1-Test de Candidate Repository
  -Nos test sur la 1ère et la 3ème requete réussissent
  -Mais nos test sur la 2ème requete findAllByCandidateEvaluationGridEntitiesGradeLessTha ne réussissent pas.
  -FindAllByCandidateEvaluationGridEntitiesGradeLessTha(15) par exemple (étudiant, dont l'une des notes est en dessous de 15)
  -Au lieu de renvoyer un ensemble de longueur au moins 1 ou 2, ça renvoie un ensemble vide. On a pas compris le problème

## 2-Tests des endpoints

### Test de CandidateComponent
    -Pour tester la méthode getAllEliminatedCandidate(), on rencotre un bug java au niveau de la methode Optional...() qu'on ne comprends pas
    -Ce bug serait-il du au fait que la methode getAllEliminatedCandidate() doit retourner un set, alors que Optional ne fonction pas avec des Set?