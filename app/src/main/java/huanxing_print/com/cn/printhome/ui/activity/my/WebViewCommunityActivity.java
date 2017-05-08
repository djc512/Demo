package huanxing_print.com.cn.printhome.ui.activity.my;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.log.Logger;

public class WebViewCommunityActivity extends BaseActivity implements OnClickListener {
	private WebView webview;
	private TextView tv_title;
	private String url,loginToken;
	private String titleName;

    private ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;
    private final static int FILE_CHOOSER_RESULT_CODE = 10000;
	private final static int BARSTYLE = 0;
	@Override
	protected BaseActivity getSelfActivity() {
		return this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		initViews();
	}

	private void initViews() {
		titleName = getIntent().getStringExtra("titleName");
		url = getIntent().getStringExtra("webUrl");
		loginToken = baseApplication.getLoginToken();
		
		Logger.d("loginToken:-----------" + loginToken );
		webview = (WebView) findViewById(R.id.web_view);
		tv_title = (TextView) findViewById(R.id.tv_title);
		findViewById(R.id.ll_back).setOnClickListener(this);
		//url = "http://ecostar.inkin.cc/purchase/user/list";
		tv_title.setText(titleName);
		WebSettings s = webview.getSettings();     
		s.setBuiltInZoomControls(true);     
		s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);     
		s.setUseWideViewPort(true);     
		s.setLoadWithOverviewMode(true);        
		s.setSaveFormData(true);     
		s.setJavaScriptEnabled(true);     // enable navigator.geolocation     
		s.setGeolocationEnabled(true);     
		s.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");    
		s.setDomStorageEnabled(true);  
		webview.requestFocus();  
		//webview.setScrollBarStyle(0);

		synCookies(getSelfActivity(), url);
		webview.loadUrl(url);
		webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
		


		webview.setWebChromeClient(new WebChromeClient() {

		            // For Android < 3.0
		            public void openFileChooser(ValueCallback<Uri> valueCallback) {
		                uploadMessage = valueCallback;
		                openImageChooserActivity();
		            }

		            // For Android  >= 3.0
		            public void openFileChooser(ValueCallback valueCallback, String acceptType) {
		                uploadMessage = valueCallback;
		                openImageChooserActivity();
		            }

		            //For Android  >= 4.1
		            public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
		                uploadMessage = valueCallback;
		                openImageChooserActivity();
		            }

		            // For Android >= 5.0
		            @Override
		            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
		                uploadMessageAboveL = filePathCallback;
		                openImageChooserActivity();
		                return true;
		            }
		        });
//		        String targetUrl = "file:///android_asset/up.html";
//		        webview.loadUrl(targetUrl);
		    }

		    private void openImageChooserActivity() {
		        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
		        i.addCategory(Intent.CATEGORY_OPENABLE);
		        i.setType("image/*");
		        startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILE_CHOOSER_RESULT_CODE);
		    }

		    @Override
		    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		        super.onActivityResult(requestCode, resultCode, data);
		        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
		            if (null == uploadMessage && null == uploadMessageAboveL) return;
		            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
		            if (uploadMessageAboveL != null) {
		                onActivityResultAboveL(requestCode, resultCode, data);
		            } else if (uploadMessage != null) {
		                uploadMessage.onReceiveValue(result);
		                uploadMessage = null;
		            }
		        }
		    }

		    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
		    private void onActivityResultAboveL(int requestCode, int resultCode, Intent intent) {
		        if (requestCode != FILE_CHOOSER_RESULT_CODE || uploadMessageAboveL == null)
		            return;
		        Uri[] results = null;
		        if (resultCode == Activity.RESULT_OK) {
		            if (intent != null) {
		                String dataString = intent.getDataString();
		                ClipData clipData = intent.getClipData();
		                if (clipData != null) {
		                    results = new Uri[clipData.getItemCount()];
		                    for (int i = 0; i < clipData.getItemCount(); i++) {
		                        ClipData.Item item = clipData.getItemAt(i);
		                        results[i] = item.getUri();
		                    }
		                }
		                if (dataString != null)
		                    results = new Uri[]{Uri.parse(dataString)};
		            }
		        }
		        uploadMessageAboveL.onReceiveValue(results);
		        uploadMessageAboveL = null;
		    }
		


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_back:
			finishCurrentActivity();
			break;
		default:
			break;
		}
	}
	@SuppressWarnings("deprecation")
	private  void synCookies(Context context, String url) {  
	    CookieSyncManager.createInstance(context);  
	    CookieManager cookieManager = CookieManager.getInstance();  
	    cookieManager.setAcceptCookie(true);  
	    cookieManager.removeAllCookie();//移除  
	    //cookieManager.setCookie(url, "UID=EBFDA984906B62C444931EA0");
	    cookieManager.setCookie(url, "loginToken="+loginToken);//cookies是在HttpClient中获得的cookie  
	    cookieManager.setCookie(url, "platform="+ ConFig.PHONE_TYPE);
	    if (Build.VERSION.SDK_INT < 21) {  
	        CookieSyncManager.getInstance().sync();  
	    } else {  
	        CookieManager.getInstance().flush();  
	    } 
	}  
	
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){

			finishCurrentActivity();
        }
        return false;
    }
}
