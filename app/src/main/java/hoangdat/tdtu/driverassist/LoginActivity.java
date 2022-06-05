package hoangdat.tdtu.driverassist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;



import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {
    //Google
    private SignInButton signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    private final int RC_SIGN_IN = 1;

    //Facebook
    private FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //REQUEST PERMISSION
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        //Google
        signInButton = findViewById(R.id.ggSignInButton);
        mAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null)
                {
                    updateUI(user);
                }
                else {
                    updateUI(null);
                }
            }
        };

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        signInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }
    public void signIn()
    {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //Google
        if (requestCode == RC_SIGN_IN)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            handleSignInResult(task);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completeTask)
    {
        try {
            GoogleSignInAccount acc = completeTask.getResult(ApiException.class);
            Toast.makeText(LoginActivity.this,"Đăng nhập thành công !",Toast.LENGTH_LONG).show();
            FirebaseGoogleAuth(acc);
        } catch (ApiException e)
        {
            //Toast.makeText(LoginActivity.this,e.toString(),Toast.LENGTH_LONG).show();
            Toast.makeText(LoginActivity.this,"Đăng nhập thất bại ! Chạy ứng dụng bằng Source Code cần đến key SHA1 !" ,Toast.LENGTH_LONG).show();
        }

    }
    private void FirebaseGoogleAuth(GoogleSignInAccount acc) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acc.getIdToken(),null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
//
                    Intent siOption = new Intent(LoginActivity.this, MainActivity.class);
                    LoginActivity.this.startActivity(siOption);

                } else {
                    Toast.makeText(LoginActivity.this, "Có lỗi xảy ra !", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void updateUI(FirebaseUser fUser)
    {
        GoogleSignInAccount accountGG = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (accountGG != null)
        {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            LoginActivity.this.startActivity(i);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null)
        {
            mAuth.removeAuthStateListener(authStateListener);
        }
    }
}