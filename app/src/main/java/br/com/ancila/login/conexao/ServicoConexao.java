package br.com.ancila.login.conexao;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
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

public class ServicoConexao {

    private static final String TAG = LoginConexao.class.getSimpleName();
    private static final String LOGIN_URL = "http://salao.homologacao.ancila.com.br/api/v1/servicos";

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;





    public static String[] requestTodosServicos( String token ) {
        URL url = null;
        HttpURLConnection conn;

        try {
            url = new URL(LOGIN_URL);
            Log.v(TAG, "URL TODOS SERVICOS ------------------------------- > " + url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            // TODO retorno deve ter alguma forma de exibir usuario o tipo de erro ocorrido
            return null;
        }

        try {
            conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            //conn.setRequestMethod("POST");

            conn.setRequestProperty("authorization", "Bearer " + token);

            //conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //conn.setDoInput(true);
            //conn.setDoOutput(true);



            //Uri.Builder builder = new Uri.Builder().appendQueryParameter("_token", token);
            //String query = builder.build().getEncodedQuery();
            Log.v(TAG, "paramentros ----------------------------------------- " );
            //OutputStream os = conn.getOutputStream();
            //BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(os, "UTF-8"));
            //writer.write(query);
            //writer.flush();
            //writer.close();
           // os.close();
            conn.connect();
        }
        catch (IOException e1){
            e1.printStackTrace();
            // TODO retorno deve ter alguma forma de exibir usuario o tipo de erro ocorrido
            return null;
        }

        try {

            Log.v(TAG, "Result 2 ----------------------------------------- > "  );
            int response_code = conn.getResponseCode();
            if (response_code == HttpURLConnection.HTTP_OK) {
                Log.v(TAG, "Result 3 ----------------------------------------- > "  );
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                String _resultJson = result.toString() ;
                Log.v(TAG, "Result ----------------------------------------- > " + _resultJson );

                String[] parseServicoData = null;

                try {
                    JSONObject responseJson = new JSONObject(_resultJson);
                    if (!responseJson.has("data")) {
                        return null;
                    }

                    JSONArray servicoArray = responseJson.getJSONArray("data");
                    parseServicoData = new String[servicoArray.length()];

                    for (int i = 0; i < servicoArray.length(); i++) {
                        String nome ;
                        String valor ;
                        JSONObject servicoObject = servicoArray.getJSONObject(i);
                        nome = servicoObject.getString("nome");
                        valor = servicoObject.getString("valor");
                        parseServicoData[i] = nome + " R$ " + valor ;
                    }
                    return parseServicoData;
                }
                catch (JSONException e){
                    return null;
                }
            }else{
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            conn.disconnect();
        }
    }


}
