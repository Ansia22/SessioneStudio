# SessioneStudio
Progetto numero 34 "RipetizioniApp"

Programmazione di Dispositivi Mobili

Universita' degli studi dell'Insubria
Corso di laura triennale, informatica

Emulatore utilizzato: Medium Phone API 30, Android 11.0 ("R"), 1080x2400

## Descrizione del Progetto
"Sessione Studio" è un'applicazione per dispositivi mobili android sviluppata per studenti e professori con l'obiettivo di facilitare la ricerca di professori dediti alle ripetizioni scolastiche.
Gli insegnanti dovranno registrarsi inserendo email e password. Una volta creato l'account, dovranno inserire i propri dati personali tra cui nome, cognome, materie, orari e indirizzo per le ripetizioni. Tali dati saranno resi disponibili agli studenti in cerca di un insegnante che possa soddisfare le loro esigenze.
Gli studenti non dovranno creare un account e potranno iniziare subito a cercare il professore tra quelli elencati. Per mettersi in contatto direttamente con l'insegnante avrenno acceso alle sue informazioni personali, tra cui la sua mail paersonale. Inoltre si potranno scrivere feedback per valutare la qualità dell'insegnamento.

## Gestione applicazione degli Admin
Gli admin hanno il potere di eliminare i singoli feedback o l'intero account del professore. Nel caso in cui il profilo dell'insegnante venisse cancellato, verrebbero di conseguenza eliminati anche tutti i feedback legati all'account. Per accedere come amministratore bisogna effettuare il login inserendo come email e password "admin", "admin".

## Problemi/Aspeti migliorabili
L'applicazione da noi realizzata non è tutt'oggi utilizzabile ufficialmente dagli utenti. Infatti alcuni problemi di sicurezza, layout o sintassi potrebbero causare problemi sui diversi dispositivi. Ecco alcuni aspetti migliorabili:
### Gestione degli Admin
Qualsiasi utente a conoscenza di username e password può tranquillamente accedere ed eliminare account e feedback di professori e studenti. Questo problema di sicurezza mette a rischio i dati contenuti nel database di firebase.
### Problemi di Layout
L'applicazione non è disponibile per tutti i dispositivi di qualsiasi dimensione. Per testare l'applicazione utilizzando un emulatore si consiglia di scaricare quello utilizzato durante la programmazione dell'app "Medium Phone API 30, Android 11.0 ("R"), 1080x2400". Inoltre nei campi testo non è possibile utilizzare lettere accentate.
### Gestione onBackPress
L'utilizzo della funzione integrata su android"onBackPress" per tornare indietro potrebbe causare crash dell'app o dell'activity corrente. Pertanto si consiglia di utilizzare la freccia in alto a sinistra per tornare alla pagina precedente.
### Gestione delle Password
Per eliminare un account come admin è necessario salvare la password e la mail del professore nel database. Infatti possiamo eliminare solo utenti attualmente autenticati. Proprio per tale motivo se facessimo accedere gli utenti con account Google, Facebook, GitHub,... non avremmo la possibilità di eliminare account (non essendo a conoscenza delle password con cui hanno effettuato l'accesso). Inoltre non è possibile abilitare il recupero della password o la verifica tramite mail. La password dei prof è stata criptata utiizzando un semplice algoritmo e una chiave di cifratura/decifratura (facilmente reperibile all'interno del codice) con lo scopo di aumentare il livello di scurezza.
### Il porolema dell'anonimato
Proprio perchè non possiamo abilitare la verifica dell'account, gli utenti potranno registrarsi utilizzando informazoini false, mail non esistenti e scrivere feedback anonimi.
### Comunicazione Studenti-Professori
L'applicazione non dispone di una chat dedicata alla comunicazione tra insegnanti e alunni. Gli studenti che vogliono prendere ripetizioni devono cercare manualmente il professore e contattarlo in provato utilizzando la mail messa a disposizione. 

# Progetto realizzato da:
- 753575 Tagliabue	Federico
- 752919 Besana Davide
