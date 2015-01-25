package rajesh.sciencehack;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.File;

/**
 * Created by user on 24/01/15.
 */
public class LiveWebView extends WebView {

    interface LoginListener{
        public void success(String userid, String token);
    }


    public static final int INPUT_FILE_REQUEST_CODE = 1;
    public static final String EXTRA_FROM_NOTIFICATION = "EXTRA_FROM_NOTIFICATION";

//    private WebView mWebView;
    public ValueCallback<Uri[]> mFilePathCallback;
//    private String mCameraPhotoPath;
    private LoginListener mLoginListener;
    Context mContext;
    public LiveWebView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        mContext = context;
        setWebViewClient();
    }

    public LiveWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        setWebViewClient();
    }

    public void setLoginListener(LoginListener listener){
        this.mLoginListener = listener;
    }

    @Override
    public boolean onCheckIsTextEditor() {
        return true;
    }

    boolean setWebViewClient()
    {

        setScrollBarStyle(SCROLLBARS_INSIDE_OVERLAY);
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus(View.FOCUS_DOWN);

        WebSettings webSettings = getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        webSettings.setUseWideViewPort(true);
        //addJavascriptInterface(new DemoJavaScriptInterface(), "demo");


        setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                        if (!v.hasFocus()) {
                            v.requestFocus();
                        }
                        break;
                }
                return false;
            }
        });


        this.setWebViewClient(new WebViewClient()
        {
            ProgressDialog dialog = new ProgressDialog(mContext);

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                Log.d("Url", url);
                String localUrl = null;
                if(url.startsWith("https://science-hack.herokuapp.com/auth/springrole/callback?access_token=")){
                    dialog.dismiss();
                    localUrl = url.replace("https://science-hack.herokuapp.com/auth/springrole/callback?", "");
                    String name = null, token = null;
                    for(String keyvalue : localUrl.split("&")){
                        String[] data = keyvalue.split("=");
                      if(data[0].equals("access_token")){
                          token = data[1];
                      }else{
                          name = data[1];
                      }
                    }
                    if(mLoginListener != null){
                        mLoginListener.success(name, token);
                    }
                }
                loadUrl(url);

                return true;
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(mContext, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                if(dialog!=null)
                {
                    dialog.setMessage("Loading...");
                    dialog.setIndeterminate(true);
                    dialog.setCancelable(true);
                    dialog.show();
                }

            }

            public void onPageFinished(WebView view, String url)
            {
                if(dialog!=null)
                {
                    dialog.cancel();
                }
            }
        });

        this.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
//                System.out.println("+-------------------------------");
//                System.out.println("|WebChromeClient onJsAlert	" + message);
//                System.out.println("+-------------------------------");
                result.confirm();
                return true;
            }

            public boolean onShowFileChooser(
                    WebView webView, ValueCallback<Uri[]> filePathCallback,
                    WebChromeClient.FileChooserParams fileChooserParams) {

                // Double check that we don't have any existing callbacks
                if (mFilePathCallback != null) {
                    mFilePathCallback.onReceiveValue(null);
                }
                mFilePathCallback = filePathCallback;

                // Set up the take picture intent
                Intent takePictureIntent = null;//new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
//                    // Create the File where the photo should go
//                    File photoFile = null;
//                    try {
//                        photoFile = createImageFile();
//                        takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
//                    } catch (IOException ex) {
//                        // Error occurred while creating the File
//                        Log.e(TAG, "Unable to create Image File", ex);
//                    }
//
//                    // Continue only if the File was successfully created
//                    if (photoFile != null) {
//                        mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
//                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
//                                Uri.fromFile(photoFile));
//                    } else {
//                        takePictureIntent = null;
//                    }
//                }

                // Set up the intent to get an existing image
                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("image/*");

                // Set up the intents for the Intent chooser
                Intent[] intentArray;
                if (takePictureIntent != null) {
                    intentArray = new Intent[]{takePictureIntent};
                } else {
                    intentArray = new Intent[0];
                }

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

                ((ActionBarActivity) mContext).startActivityForResult(chooserIntent, 1);

                return true;
            }

        });



        loadUrl("http://www.google.com");

        return true;
    }


}
