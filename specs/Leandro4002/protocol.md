This protocol specifies the communication between a client and a server processing calculations.  
It is a protocol with state that was designed for education purposes.  
  
This protocol communicates json object encapsulated into TCP sockets. Every message (client to server, or server to client) is a json object. This object always contains the key `Type de message.` which indicate the type of message.  
The type ids are separated into different categories.
* Odd numbers : communication from client to server
* Even numbers : communication from server to client
* Negative numbers : Used to communicate errors

> This API needs the `Type de message.` ids to be stored in a 64 bits integer and the Note that the typeIds must be stored in a 64 bits integer. It also requires the support for accents.

Here is the official `Type de message.`s for the CALCUL (Calcul d'Algèbre Linéaire, Continue, Unilatéral et Limitié) API:
* `Bonjour.`
  * Used to initiate connexion.
  * Empty message.
* `Comment ça va ?`
  * Response to a client `Bonjour.` call. Used to validate connection initialization.
  * Contains a json array indexed at key `Opérandes supportées.` and containing all the supported operands.
* `Calcul, s'il te plaît.`
  * Contains a string indexed at key `Calcul à faire.`.
* `Voilà le résultat.`
  * Contains a string indexed at key `Voilà ce que j'ai trouvé.`
* `Merci.`
  * Empty message. Needed for politeness.
* `Au revoir.`
  * Can be send from client to server of server to client. The receiver closes the connection.
* `J'ai un problème.`
  * Contains a string indexed at key `Explication de mon problème.`, displaying an helpful message in french.
* `Excuse-moi.`
  * Necessary after an error. If not provided, the server won't run any further calculations.
  
## Initialization
First the client initiate the connexion with a `Bonjour.` request. Then, the server responds with a `Comment ça va ?` containing the supported operands.

## Calculation and error handling
The client sends a `Calcul, s'il te plaît.` with the calculations to do.  
The server parse the request and answers with a `Voilà le résultat.` with the result.  
If there is a problem with the calculations, the server sends a `J'ai un problème.` with a description (in french).  
When the client receives a `J'ai un problème.` or a `Voilà le résultat.`, it must respond respectively with a `Excuse-moi.` or a `Merci.` or else the server won't respond anymore.  

## Ending connexion
When no more calculations is needed or we want the connexion to end, either the client or the server sends a `Au revoir.` to the other. The agent receiving the `Au revoir.` closes the connection.