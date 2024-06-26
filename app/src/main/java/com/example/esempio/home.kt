package com.example.esempio

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.esempio.models.AccountDisabilitati
import com.example.esempio.models.Professor
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Classe per la gestione della schermata home dell'utente.
 * Terminato il caricamento, l'utente può decidere quale operazione svolgere
 * all'interno dell'applicazione. Login, Registrazione, Login con Google, Ricerca Professori.
 *
 * Il metodo "fetchData" si limita a leggere nel database di firebase.
 * Esso non ha alcuno scopo ma testando l'applicazione abbiamo notato che non è possibile in
 * alcun modo effettuare il login senza tale metodo. Se invece accediamo al database prima di effettuare
 * l'autenticazione dell'utente, l'applicazione funziona correttamente.
 *
 * La classe contiene inoltre i metodi per la gestione del login con google.
 * Se il professore effettua il login con google per la prima volta con la mail selezionata,
 * verrà creato un nuovo utente in firebase. E' quindi necessario verificare se la mail utilizzata
 * è gia presente nel database oppure se bisogna creare un nuovo utente con una nuova chiave pimaria.
 *
 * E' infine necessario verificare se l'account autenticato con google
 * non sia stato precedentemente disabilitato dagli admin.
 */

class Home : AppCompatActivity() {

    private lateinit var firebaseRef : DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var googleApiClient: GoogleApiClient
    private val signIn = 9001


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // Configurazione del GoogleApiClient
        googleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this) { _ ->
                Toast.makeText(applicationContext, "Erorre di connessione", Toast.LENGTH_SHORT).show()
            }
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()

        auth = FirebaseAuth.getInstance()

        firebaseRef = FirebaseDatabase.getInstance().getReference("Professori")

        fetchData()
    }

    fun googleLogin(view: View){
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
        startActivityForResult(signInIntent, signIn)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == signIn) {
            val result = data?.let { Auth.GoogleSignInApi.getSignInResultFromIntent(it) }
            if (result?.isSuccess == true) {
                // Accesso con Google riuscito, autenticazione con Firebase
                val account = result.signInAccount
                firebaseAuthWithGoogle(account)
            } else {
                // Accesso con Google non riuscito
                Toast.makeText(applicationContext, "Accesso con google non riuscito ${result?.status}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(acct?.idToken, null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val user = auth.currentUser

                    isAbilitato(user?.email.toString()){ emailAbilitata ->
                        if (emailAbilitata) {

                            Toast.makeText(applicationContext, "L'account selezionato è stato disabilitato dai nostri admin, " +
                                    "cambiare account o riprovare", Toast.LENGTH_SHORT).show()

                        }else{

                            Toast.makeText(applicationContext, "Login con Google riuscito: ${user?.email}", Toast.LENGTH_SHORT).show()

                            emailExist(user?.email.toString()){ emailPresente ->
                                if (!emailPresente) {

                                    saveData(user?.email.toString(), user?.displayName.toString())

                                }else{

                                    setDatiProf(user?.email.toString())

                                }
                            }

                            val intent = Intent(this, InformazioniProfessore::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                        }
                    }
                } else {
                    // Accesso con Firebase non riuscito
                    Toast.makeText(applicationContext, "Accesso a firebase non riuscito", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun setDatiProf(mailProf: String){

        firebaseRef = FirebaseDatabase.getInstance().getReference("Professori")

        firebaseRef.orderByChild("email").equalTo(mailProf)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (snap in snapshot.children) {
                        try {
                            val professor = snap.getValue(Professor::class.java)
                            val professorId = professor?.id

                            if (professorId != null) {
                                Professor.setVariabiliLogin(professorId, mailProf)
                            }
                        } catch (e: Exception) {
                            // Gestisci eventuali eccezioni durante il recupero dei dati del professore
                            Toast.makeText(applicationContext, "problema durante il login, riprovare", Toast.LENGTH_SHORT).show()
                        }
                    }

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Gestisci eventuali errori durante la lettura dei dati
                    Toast.makeText(applicationContext, "onCanceled: errore lettura dati", Toast.LENGTH_SHORT).show()

                }
            })

    }

    private fun emailExist(mailProf:String, callback: (Boolean) -> Unit){

        firebaseRef = FirebaseDatabase.getInstance().getReference("Professori")

        firebaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var isEmailFound = false

                for (snapshot in dataSnapshot.children) {

                    val professor = snapshot.getValue(Professor::class.java)
                    val professorEmail = professor?.email

                    if (professorEmail == mailProf) {
                        isEmailFound = true
                        break
                    }

                }
                callback(isEmailFound)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback(false)
            }
        })
    }

    private fun saveData(professorEmail: String, displayName:String){
        val database = FirebaseDatabase.getInstance()
        val professorsRef = database.getReference("Professori")

        val newProfessorRef = professorsRef.push()
        val professorId = newProfessorRef.key!!

        var (professorNome, professorCognome) = getNomeCognomeUtenteLoggato(displayName)
        val professorMaterie = ""
        val professorIndirizzo = ""
        val professorDescrizione = ""

        if (professorNome!=null && professorCognome!=null){
            val professor = Professor(professorId,professorEmail, professorNome, professorCognome, professorMaterie, professorIndirizzo, professorDescrizione)

            Professor.setVariabiliLogin(professorId, professorEmail)

            professorsRef.child(professorId).setValue(professor)
        }else{
            professorNome = ""
            professorCognome = ""

            val professor = Professor(professorId,professorEmail, professorNome, professorCognome, professorMaterie, professorIndirizzo, professorDescrizione)

            Professor.setVariabiliLogin(professorId, professorEmail)

            professorsRef.child(professorId).setValue(professor)
        }
    }

    private fun getNomeCognomeUtenteLoggato(displayName: String): Pair<String?, String?> {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if (user != null) {
            val nome: String?
            val cognome: String?

            if (displayName.contains(" ")) {
                val splitName = displayName.split(" ")
                nome = splitName[0]
                cognome = splitName.getOrNull(1)
            } else {
                nome = displayName
                cognome = null
            }

            return Pair(nome, cognome)
        } else {
            return Pair(null, null)
        }
    }

    private fun isAbilitato(mailProf:String, callback: (Boolean) -> Unit){

        firebaseRef = FirebaseDatabase.getInstance().getReference("AccountDisabilitati")

        firebaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var isEmailFound = false

                for (snapshot in dataSnapshot.children) {

                    val dati = snapshot.getValue(AccountDisabilitati::class.java)
                    val professorEmail = dati?.mailProf

                    if (professorEmail == mailProf) {
                        isEmailFound = true
                        break
                    }

                }
                callback(isEmailFound)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback(false)
            }
        })
    }

    private fun fetchData(){
        firebaseRef.addValueEventListener(object:ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                }
                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
    }

    fun passaRicerca(view: View){

        val intent = Intent(this, RicercaPage::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

    }
    fun passaLogin (view: View){
        val intent = Intent(this, LoginPage::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    fun passaRegistrazione (view: View){
        val intent = Intent(this, RegistrazionePage::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }


    fun infoAlert(view: View){
        AlertDialog.Builder(this)
            .setMessage("Benvenuti in SessioneStudio! Una applicazione sviluppata per studenti e professori. \n\n" +
                    " Grazie a SessioneStudio potrai cercare il professore più adatto alle tue esigenze e migliorare " +
                    "i risultati dei tuoi test. Scuole medie? Superiori? Università? Cosa aspetti! " +
                    "Premi il tasto 'Cerca' e inizia a studiare! \n\n Se invece sei un professore " +
                    "disposto a trasmettere i tuoi insegnamenti ai nuovi studenti, nessun problema! registrati, " +
                    "inserisci i tuoi dati e potrai iniziare!")
            .setCancelable(true)
            .setPositiveButton("ok", null)
            .show()
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setMessage("Uscire da Sessione Studio?")
            .setCancelable(true)
            .setPositiveButton("Ok") { _, _ ->
                super.onBackPressed() // Chiusura solo dopo conferma
            }
            .setNegativeButton("Annulla", null)
            .show()
    }

}
