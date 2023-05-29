# Corso Ingegneria del Software 2023 - Cremona

> «_Se ni’ mondo esistesse un po’ di bene
e ognun si considerasse suo fratello,
ci sarebbe meno pensieri e meno pene
e il mondo ne sarebbe assai più bello»_

---- 
## Note implementative
#### Persistenza della partita 
Al fine di utilizzare la funzionalità di persitenza della partita è necessario
disporre di una cartella `saved` nella stessa directory del jar.
Nel caso la cartella non fosse presente, verrà auto-generata dal server 
nel momento della generazione di un salvataggio.

---

#### Protocollo di comunicazione 

Il protocollo di comunicazione è basato su un sistema di callback simile ai meccanismi usati dal protocollo HTTP: 
ogni funzione è identificata da un metodo (es. "UPDATE") e da un contenuto, il quale dipende dalla 
tipologia di messaggi scelti dal server (es. PlainTextMessage, JsonMessage, etc.). 

Oltre alle callback per i singoli metodi, è possibile definire anche dei middleware, sia a livello di 
singolo metodo, che a livello "globale". 
I middleware permettono di eseguire determinate operazioni prima 
dell'esecuzione di una callback specifica. 

