package com.aumento.shopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.aumento.shopping.utils.GlobalPreference;

public class PaymentGatewayActivity extends AppCompatActivity {

    private WebView paymentGatewayWV;
    private GlobalPreference globalPreference;
    private String ip;
    private String uid;
    private Intent intent;
    private String orderNo;
    private String amt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway);

        globalPreference = new GlobalPreference(this);
        ip = globalPreference.RetriveIP();
        uid = globalPreference.getUserId();

        intent = getIntent();
        orderNo = intent.getStringExtra("orderno");
        amt = intent.getStringExtra("amt");

        paymentGatewayWV = findViewById(R.id.paymentgatewayWebView);
        paymentGatewayWV.clearCache(true);
        paymentGatewayWV.clearHistory();
        paymentGatewayWV.getSettings().setJavaScriptEnabled(true);
        paymentGatewayWV.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        paymentGatewayWV.loadUrl("http://"+ip+"/Aumento/Shopping/payment/sec.php?uid="+uid+"&amt="+amt);

        paymentGatewayWV.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                Log.d("WebView", "your current url when webpage loading.." + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("WebView", "your current url when webpage loading.. finish" + url);
               /* if(url.contains("Third.php"))
                {

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            paymentGatewayWV.loadUrl("http://"+ip+"/Aumento/Shopping/payment/Fourth.php");
                        }
                    }, 4000);
                }

                if(url.contains("Third.php"))
                {

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            paymentGatewayWV.loadUrl("http://"+ip+"/Aumento/Shopping/payment/Fourth.php");
                        }
                    }, 4000);
                }*/

                if(url.contains("complete.php"))
                {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            intent = new Intent(PaymentGatewayActivity.this,SuccessfullOrderActivity.class);
                            intent.putExtra("orderno",orderNo);
                            startActivity(intent);
                            finish();
                        }
                    }, 1000);
                }

                super.onPageFinished(view, url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onLoadResource(view, url);
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                System.out.println("when you click on any interlink on webview that time you got url :-" + url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }
}
