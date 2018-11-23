# SYM Labo 2

## Transmission différée

```
Dans le cadre de cette manipulation vous pouvez rester sur une solution simple « en mémoire », vous indiquerez toutefois dans votre rapport les limitations de cette façon de faire et proposerez des outils et techniques mieux adaptés (sans forcément réaliser l’implémentation).
```

Vu que c'est stoqué en mémoire, si on quitte l'application les requêtes en attentes seront perdu. Pour se faire on pourrait enregister les requêtes sur la carte SD lorsque l'application est quitté et qu'on lancement de l'application, on les relances.

## Reponse question

### 1 Traitement des erreurs

```
Les interfaces AsyncSendRequest et CommunicationEventListener utilisées au point 3.1 restent très (et certainement trop) simples pour être utilisables dans une vraie application : que se passe-t-il si le serveur n’est pas joignable dans l’immédiat ou s’il retourne un code HTTP d’erreur ? Veuillez proposer une nouvelle version, mieux adaptée, de ces deux interfaces pour vous aider à illustrer votre réponse.
```



### 2 Authentification

```
Si une authentification par le serveur est requise, peut-on utiliser un protocole asynchrone ? Quelles seraient les restrictions ? Peut-on utiliser une transmission différée ?
```

Non, 

### 3 Threads concurrents

``` 
Lors de l'utilisation de protocoles asynchrones, c'est généralement deux threads différents qui se préoccupent de la préparation, de l'envoi, de la réception et du traitement des données. Quels problèmes cela peut-il poser ?
```

Problème de concurence. Il peut y avoir plusieurs envoie avant meme de recevoir la première reponse.

### 4 Ecriture différée

```
Lorsque l'on implémente l'écriture différée, il arrive que l'on ait soudainement plusieurs transmissions en attente qui deviennent possibles simultanément. Comment implémenter proprement cette situation (sans réalisation pratique) ? Voici deux possibilités :
- Effectuer une connexion par transmission différée
- Multiplexer toutes les connexions vers un même serveur en une seule connexion de 
	transport.Dans ce dernier cas, comment implémenter le protocole applicatif, quels
	avantages peut-on espérer de ce multiplexage, et surtout, comment doit-on planifier 
	les réponses du serveur lorsque ces dernières s'avèrent nécessaires ?

Comparer les deux techniques (et éventuellement d'autres que vous pourriez imaginer) et discuter des avantages et inconvénients respectifs.
```



### 5 Transmission d’objet

```
Quel inconvénient y a-t-il à utiliser une infrastructure de type REST/JSON n'offrant aucun service de validation (DTD, XML-schéma, WSDL) par rapport à une infrastructure comme SOAP offrant ces possibilités ? Est-ce qu’il y a en revanche des avantages que vous pouvez citer ?
```

On peut plus facilement faire des erreurs, moins réutilisable.

```
L’utilisation d’un mécanisme comme Protocol Buffers 8 est-elle compatible avec une
architecture basée sur HTTP ? Veuillez discuter des éventuelles avantages ou limitations par rapport à un protocole basé sur JSON ou XML ?
```



```
Par rapport à l’API GraphQL mise à disposition pour ce laboratoire. Avez-vous constaté des points qui pourraient être améliorés pour une utilisation mobile ? Veuillez en discuter, vous pouvez élargir votre réflexion à une problématique plus large que la manipulation effectuée.
```



### 6 Transmission compressée

```
Quel gain peut-on constater en moyenne sur des fichiers texte (xml et json sont aussi du texte) en utilisant de la compression du point 3.4 ? Vous comparerez vos résultats par rapport au gain théorique d’une compression DEFLATE, vous enverrez aussi plusieurs tailles de contenu pour comparer.
```



## Remarques

Ces questions sont très penible et sort en grande partie du context de ce labo...