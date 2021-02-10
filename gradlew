package uz.myapps.learnjava.signinfragments

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.fragment_sign_in.view.*
import uz.myapps.learnjava.MainActivity
import uz.myapps.learnjava.R
import uz.myapps.learnjava.SplashActivity
import uz.myapps.learnjava.models.User


class SignInFragment : Fragment() {
    val RC_SIGN_IN: Int = 1
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions
    lateinit var mAuth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_sign_in, container, false)
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
//        val email = arguments!!.getString("email")
//        val password = arguments!!.getString("password")
//        root.email_signin.setText(email)
//        root.password_signin.setText(password)
        root.btn_signin.setOnClickListener {
            val email = root.email_signin.text.toString()
            val password = root.password_signin.text.toString()
            if (email == "" || password == "") {
                Toast.makeText(
                    requireContext(),
                    "Iltimos, ma'lumotlarni to'liq kiriting",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                mAuth.signInWithEmailAndPassword(
                    email, password
                ).addOnCompleteListener(
                    requireActivity()
                ) { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    } else {
                        Log.w(
                            "fail tag",
                            "signInWithEmail:failure",
                            task.exception
                        )
                    }
                }
            }
        }
        root.cvgooglesignin.setOnClickListener {
            configureGoogleSignIn()
            signIn()
        }
        return root
    }

    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun configureGoogleSignIn() {
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), mGoogleSignInOptions)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    firebaseAuthWithGoogle(account)
                }
            } catch (e: ApiException) {
                Log.d("login", e.toString())
                Toast.makeText(requireContext(), "Google sign in failed:(", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun updateUI(name: String, firebaseUser: FirebaseUser, password: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(firebaseUser!!.uid)
            .get()
            .addOnSuccessListener { p0 ->

            }.addOnFailureListener {
                val user: MutableMap<String, Any> = HashMap()
                user["name"] = name
                user["email"] = firebaseUser.email.toString()
                user["password"] = password
                user["score"] = 0