package crimespotter.rhokpta.unisa.co.za.crimespotter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import crimespotter.rhokpta.unisa.co.za.crimespotter.utils.Constants;

public class LoginActivity extends AppCompatActivity {


    private static final String LOG_TAG = LoginActivity.class.getSimpleName();
    /* A dialog that is presented until the Firebase authentication finished. */
    private ProgressDialog mAuthProgressDialog;
    private ProgressDialog loginProgress;
    /* References to the Firebase */
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    /* Listener for Firebase session changes */
    private EditText mEditTextEmailInput, mEditTextPasswordInput;

    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor mSharedPrefEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        mSharedPrefEditor = mSharedPref.edit();

        mEditTextEmailInput = (EditText) findViewById(R.id.edit_text_email);
        mEditTextPasswordInput = (EditText) findViewById(R.id.edit_text_password);

        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle(getString(R.string.progress_dialog_loading));
        mAuthProgressDialog.setMessage(getString(R.string.progress_dialog_authenticating_with_firebase));
        mAuthProgressDialog.setCancelable(false);

        loginProgress = new ProgressDialog(this);
        loginProgress.setTitle(getString(R.string.progress_dialog_loading));
        loginProgress.setIndeterminate(true);
        loginProgress.setMessage(getString(R.string.progress_dialog_login));
        loginProgress.setCancelable(false);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mAuthProgressDialog.dismiss();
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    setAuthenticatedUserPasswordProvider(firebaseAuth);
                    /* Save provider name and encodedEmail for later use and start MainActivity */
                    mSharedPrefEditor.putString(Constants.KEY_PROVIDER, user.getProviderId()).apply();
                    mAuthProgressDialog.dismiss();

                }
            }
        };

    }

    private void setAuthenticatedUserPasswordProvider(FirebaseAuth authData) {

        if (authData != null) {
            FirebaseUser user = authData.getCurrentUser();

            if (user != null) {
                final String unprocessedEmail = user.getEmail().toLowerCase();
                final String user_uid = user.getUid();
                mSharedPrefEditor.putString(Constants.KEY_SIGNUP_EMAIL, unprocessedEmail).apply();

                /**
                 * Encode user email replacing "." with ","
                 * to be able to use it as a Firebase db key
                 */
                mSharedPrefEditor.putString(Constants.KEY_PROVIDER, Constants.PASSWORD_PROVIDER).apply();

                Intent intentMain = new Intent(LoginActivity.this, MainActivity.class);
                intentMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentMain);
                finish();
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    /**
     * Cleans up listeners tied to the user's authentication state
     */
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /**
     * Cleans up listeners tied to the user's authentication state
     */
    @Override
    public void onPause() {
        super.onPause();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /**
     * Sign in with Password provider when user clicks sign in button
     */
    public void onSignInPressed(View view) {
        signInPassword();
    }

    /**
     * Open CreateAccountActivity when user taps on "Sign up" TextView
     */
    public void onSignUpPressed(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * Sign in with Password provider (used when user taps "Done" action on keyboard)
     */
    public void signInPassword() {
        String email = mEditTextEmailInput.getText().toString();
        String password = mEditTextPasswordInput.getText().toString();

        /**
         * If email and password are not empty show progress dialog and try to authenticate
         */
        if (email.equals("")) {
            mEditTextEmailInput.setError(getString(R.string.error_cannot_be_empty));
            return;
        }

        if (password.equals("")) {
            mEditTextPasswordInput.setError(getString(R.string.error_cannot_be_empty));
            return;
        }
        mAuthProgressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful()) {

                }
                mAuthProgressDialog.dismiss();

            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Ygritte", e.getMessage());
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                //showSnackBar(e.getMessage());
            }
        });
    }

}
