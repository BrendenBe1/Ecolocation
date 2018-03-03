package ecolocation.ecolocation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends AppCompatActivity {
    private WebView webView;
    private String url;

    //constants for intent extras
    private static final String EXTRA_URL = "url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        //----------- Toolbar Setup
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_up_navigation);

        //---------- Initialize Widgets
        webView = (WebView) findViewById(R.id.web_view);

        //---------- Get Wikipedia Link & Open Web Page
        //get url
        url = getIntent().getStringExtra(EXTRA_URL);
        //open web page
        goToWebPage();
    }

    /**
     * Used the code from this tutorial:
     *          https://stackandroid.com/tutorial/android-webview-tutorial/
     */
    private void goToWebPage(){
        //create a WebView Client
        webView.setWebViewClient(new WebViewClient(){
//            ProgressDialog progressDialog;

            //this method enables opening links
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return true;
            }

            //show progressDialog while loading url
            public void onLoadResource(WebView view, String url){
//                if(progressDialog == null){
//                    progressDialog = new ProgressDialog(WebActivity.this);
//                    progressDialog.setMessage("Loading");
//                    progressDialog.show();
//                }
            }

            //hide progress dialog when page finishes loading
            public void onPageFinished(WebView view, String url){
//                try{
//                    if(progressDialog.isShowing()){
//                        progressDialog.dismiss();
//                        progressDialog = null;
//                    }
//                } catch(Exception e){
//                    e.printStackTrace();
//                }
            }
        });

        //enable JS
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }

    /**
     * If web page is opened & can go back, then let user go back inside the browser
     * If the web page is not opened or cannot go back, then do regular back action
     */
    @Override
    public void onBackPressed(){
        if(webView.canGoBack()){
            webView.goBack();
        }
        else{
           super.onBackPressed();
        }
    }

}
