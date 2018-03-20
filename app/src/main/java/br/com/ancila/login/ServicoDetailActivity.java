package br.com.ancila.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by DesenvolvimentoPHP on 19/03/2018.
 */

public class ServicoDetailActivity extends AppCompatActivity  {

    private static final String FORECAST_SHARE_HASHTAG = " #SalaoApp";

    private String mServico;
    private TextView mDadosDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servico_details);

        mDadosDisplay = (TextView) findViewById(R.id.tv_display_dados_for_servico);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                mServico = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
                mDadosDisplay.setText(mServico);
            }
        }
        else{
            mDadosDisplay.setText("intentThatStartedThisActivity igual a null !!");
        }

    }
}
