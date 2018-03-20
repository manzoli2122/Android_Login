package br.com.ancila.login.conexao;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by DesenvolvimentoPHP on 19/03/2018.
 */

public class LoginConexao {

    private static final String TAG = LoginConexao.class.getSimpleName();
    private static final String LOGIN_URL = "http://salao.homologacao.ancila.com.br/api/auth/login";

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;




    public static String requestLogin( String... params ) {
        URL url = null;
        HttpURLConnection conn;

        try {
            url = new URL(LOGIN_URL);
            Log.v(TAG, "URL LOGIN ------------------------------- > " + url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            // TODO retorno deve ter alguma forma de exibir usuario o tipo de erro ocorrido
            return null;
        }

        try {
            conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            Uri.Builder builder = new Uri.Builder().appendQueryParameter("email", params[0]).appendQueryParameter("password", params[1]);
            String query = builder.build().getEncodedQuery();
            Log.v(TAG, "paramentros ----------------------------------------- " + query);
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();
        }
        catch (IOException e1){
            e1.printStackTrace();
            // TODO retorno deve ter alguma forma de exibir usuario o tipo de erro ocorrido
            return null;
        }

        try {
            int response_code = conn.getResponseCode();
            if (response_code == HttpURLConnection.HTTP_OK) {

                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                String _resultJson = result.toString() ;
                Log.v(TAG, "Result ----------------------------------------- > " + _resultJson );

                try {
                    JSONObject responseJson = new JSONObject(_resultJson);

                    if (responseJson.has("access_token")) {
                        return responseJson.getString("access_token");
                    } else {
                        // TODO retorno deve ter alguma forma de exibir usuario o tipo de erro ocorrido
                        return null;
                    }
                }
                catch (JSONException e){
                    // TODO retorno deve ter alguma forma de exibir usuario o tipo de erro ocorrido
                    return null;
                }
            }else{
                // TODO retorno deve ter alguma forma de exibir usuario o tipo de erro ocorrido
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            // TODO retorno deve ter alguma forma de exibir usuario o tipo de erro ocorrido
            return null;
        } finally {
            conn.disconnect();
        }

    }







}
