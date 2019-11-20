package com.aumento.shopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SuccessfullOrderActivity extends AppCompatActivity {

    private Intent intent;
    private String orderNo;

    TextView ordernoTextView;
    Button continueBT,manageOrderBT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successfull_order);

        intent = getIntent();
        orderNo = intent.getStringExtra("orderno");

        ordernoTextView = findViewById(R.id.ordernoTextView);
        ordernoTextView.setText("#"+orderNo);

        manageOrderBT = findViewById(R.id.manageOrderButton);
        manageOrderBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(SuccessfullOrderActivity.this,MyOrderActivity.class);
                startActivity(intent);
                finish();
            }
        });

        continueBT = findViewById(R.id.continuButton);
        continueBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(SuccessfullOrderActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
