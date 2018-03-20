package br.com.ancila.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.ancila.login.conexao.LoginConexao;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private SharedPreferences pref;

    private EditText _email;
    private EditText _password;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        _email = (EditText) findViewById(R.id.email);
        _password = (EditText) findViewById(R.id.password);
    }




    public void checkLogin(View arg0) {
        final String email = _email.getText().toString();
        final String password = _password.getText().toString();
        new AsyncLogin().execute(email,password);
    }







    //===========================================================================================
    //  CLASSE PARA FAZER REQUEST EM BACKGROUND
    //===========================================================================================

    private class AsyncLogin extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }



        @Override
        protected String doInBackground(String... params) {
            String token = LoginConexao.requestLogin(params);

            pref = getSharedPreferences("info", MODE_PRIVATE);

            SharedPreferences.Editor editor = pref.edit();
            editor.putString( "token",token );
            editor.commit();
            return token;
        }



        @Override
        protected void onPostExecute(String token) {

            if (token != null) {
                Intent intent = new Intent(LoginActivity.this, UsuarioActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, token);
                startActivity(intent);
                LoginActivity.this.finish();
            } else
                Toast.makeText(LoginActivity.this, "NÃO FOI POSSIVEL REALIZAR O LOGIN", Toast.LENGTH_LONG).show();
        }

    }

}
