package drawertab.com.drawer_tab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class OAuthLogin extends AppCompatActivity {
    SharedPreferences pref;
    public static final String MyPREFERENCES = "MyPrefs" ;
    String url,url_all;


    boolean authComplete = false;
    ProgressBar spinner;

    private WebView wv1;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in,R.anim.push_down_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        overridePendingTransition(R.anim.push_left_in, R.anim.push_up_out);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth_login);
        pref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        Intent i = getIntent();
        String Question = i.getStringExtra("QuestionLink");
        TextView authText = (TextView)findViewById(R.id.textView2);

        spinner.setVisibility(View.VISIBLE);
        if(Question!=null){
            authText.setVisibility(View.GONE);
            OpenUrl(Question);
            spinner.setVisibility(View.GONE);

        }
        else {
            doThis();

        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_oauth_login, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
    public void OpenUrl(String URL){

        wv1 = (WebView) findViewById(R.id.webv);
        wv1.clearHistory();
        wv1.clearFormData();
        wv1.clearCache(true);
        wv1.getSettings().setJavaScriptEnabled(true);
        wv1.loadUrl(URL);
//                url = "https://api.stackexchange.com/2.2/me?access_token=dIyuZr4JjRLWYeCv(T5Xyw))&site=stackoverflow&key=TKJxJqfiNKrcEL0VNCg74g(("

        wv1.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);


            }
        });

    }

    public void doThis() {
        if(Build.VERSION.SDK_INT >= 21){
            CookieManager.getInstance().removeAllCookies(null);
        }
        else{
            CookieManager.getInstance().removeAllCookie();
        }
        wv1 = (WebView) findViewById(R.id.webv);
        wv1.clearHistory();
        wv1.clearFormData();
        wv1.clearCache(true);
        wv1.getSettings().setJavaScriptEnabled(true);
        Log.v("PrivateBrowsing", "" + wv1.isPrivateBrowsingEnabled());
        url_all = "https://stackexchange.com/oauth/dialog?client_id=7669&redirect_uri=https://stackexchange.com/oauth/login_success&scope=read_inbox,no_expiry,write_access,private_info";
        url = "https://stackexchange.com/oauth/dialog?client_id=7669&redirect_uri=https://stackexchange.com/oauth/login_success&scope=read_inbox";
        wv1.loadUrl(url_all);
//                url = "https://api.stackexchange.com/2.2/me?access_token=dIyuZr4JjRLWYeCv(T5Xyw))&site=stackoverflow&key=TKJxJqfiNKrcEL0VNCg74g(("

        wv1.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (url.contains("access_token") && authComplete != true) {
                    Uri uri = Uri.parse(url);
                    Log.i("", "uri : " + uri);
                    String auth_token = url.split("#access_token=")[1].toString();
                    if (auth_token.contains("&") && auth_token.split("&")[1].toString() != null) {
                        auth_token = auth_token.split("&")[0].toString();
                    }
                    authComplete = true;

                    SharedPreferences.Editor edit = pref.edit();
                    edit.putString("auth_token", auth_token);
                    edit.putBoolean("authComplete", authComplete);
                    edit.commit();
                    onBackPressed();
//                    Toast.makeText(getApplicationContext(), "Authorization Code is: " + auth_token, Toast.LENGTH_SHORT).show();
                } else if (url.contains("error")) {
                    Log.i("", "ACCESS_DENIED_HERE");
                    authComplete = true;
                    Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                spinner.setVisibility(View.GONE);


            }
        });

    }
}
