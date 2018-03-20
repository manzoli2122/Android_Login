package br.com.ancila.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import br.com.ancila.login.conexao.ServicoConexao;

public class UsuarioActivity extends AppCompatActivity implements ServicoAdapter.ServicoAdapterOnClickHandler  {


    private String _token ;
    private RecyclerView mRecyclerView;
    private ServicoAdapter mServicoAdapter;

    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servico);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_servico);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mServicoAdapter = new ServicoAdapter(this);

        mRecyclerView.setAdapter(mServicoAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);




        Intent intentThatStartedThisActivity = getIntent();


        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                _token = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
            }
        }
        else{

        }

        loadServicoData();

    }






    private void loadServicoData() {
        showServicoDataView();
        new ServicoQueryTask().execute();
    }




    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }



    private void showServicoDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }




    @Override
    public void onClick(String dadosForServico) {
        Context context = this;
        Class destinationClass = ServicoDetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, dadosForServico);
        startActivity(intentToStartDetailActivity);
    }





    //===========================================================================================
    //  CLASSE PARA FAZER REQUEST EM BACKGROUND
    //===========================================================================================

    public class ServicoQueryTask extends AsyncTask<String, Void, String[]> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }


        @Override
        protected String[] doInBackground(String... params) {

            SharedPreferences shared = getSharedPreferences("info",MODE_PRIVATE);
            String string_temp = shared.getString("token","");
            String[] servicoSearchResultsData = ServicoConexao.requestTodosServicos(string_temp);
            // String[] servicoSearchResultsData = ServicoConexao.requestTodosServicos(_token);
            return servicoSearchResultsData ;
        }



        @Override
        protected void onPostExecute(String[] servicoSearchResults) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (servicoSearchResults != null) {
                showServicoDataView();
                mServicoAdapter.setServicoData(servicoSearchResults);

            } else {
                showErrorMessage();
            }
        }
    }





}
