package blogreader.study.com.myproject22743;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import blogreader.study.com.myproject22743.models.Users;
import blogreader.study.com.myproject22743.utlis.Constants;

public class RegisterActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 9001;//Request Code for singng in
    private FirebaseAuth.AuthStateListener mAuthListener;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.sign_in).setOnClickListener(this);

        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mFirebaseUser=firebaseAuth.getCurrentUser();
                if(mFirebaseUser != null){
                    if(mFirebaseUser != null){
                        if(BuildConfig.DEBUG)
                            Log.d(TAG,"onAuthStateChanged:singed_in" + mFirebaseUser.getDisplayName());
                    }else{
                        if(BuildConfig.DEBUG) Log.d(TAG,"onAuthStateChanged:signed_out");
                    }
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener!=null) mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sign_in:showProgressDialog();
            signIn();
        }

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Activity.RESULT_OK) {
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    GoogleSignInAccount account = result.getSignInAccount();
                    firebaseAuthwithGoogle(account);
                } else {
                    hideProgressDialog();
                }
            } else {
                hideProgressDialog();
            }
        } else {
            hideProgressDialog();
        }
    }
    private void firebaseAuthwithGoogle(final GoogleSignInAccount account) {
        if(BuildConfig.DEBUG) Log.d(TAG,"firebaseAuthwithGoogle:" + account.getDisplayName());
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(BuildConfig.DEBUG) Log.d(TAG,"singInWithCredential:onComplete:" + task.isSuccessful());
                if(task.isSuccessful()){
                    String photoUrl = null;
                    if(account.getPhotoUrl() != null){
                        photoUrl = account.getPhotoUrl().toString();
                    }
                    Users user = new Users(
                            account.getDisplayName() + " " + account.getFamilyName(),
                            account.getEmail(),photoUrl,FirebaseAuth.getInstance().getCurrentUser().getUid());
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference userRef = database.getReference(Constants.USER_KEY);
                    userRef.child(account.getEmail().replace(".",","))
                            .setValue(user, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    startActivity(new Intent(RegisterActivity.this,NavDrawerActivity.class));
                                }
                            });
                    if(BuildConfig.DEBUG) Log.v(TAG, "Authentification successful");
                } else{hideProgressDialog();
            }
            }
        });
    }
    }