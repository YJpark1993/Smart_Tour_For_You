package com.example.jeju;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TabHost;
import android.widget.Toast;

import org.mospi.moml.framework.pub.core.MOMLFragmentActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class SuccessActivity extends MOMLFragmentActivity {
    private double latitude = 35.1234, longitude = 12.2345;
    private LocationManager locManager; // 위치 정보 프로바이더
    private LocationListener locationListener; // 위치 정보가 업데이트시 동작
    private Location location;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        //loadApplication("/moml/applicationInfo_2.xml");
        //MOMLView momlView2 = (MOMLView) findViewById(R.id.momlView2);
        //setMomlView(momlView2);

        final TabHost host = (TabHost) findViewById(R.id.tabHost);
        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equalsIgnoreCase("Tab Two")) {
                    try {
                        //doJSONParser();
                    } catch (Exception e) {
                        //Toast.makeText(MainActivity.this, "먼저 항목을 체크해주세요", Toast.LENGTH_LONG).show();
                        host.setCurrentTab(0);
                    }
                } else if (tabId.equalsIgnoreCase("Tab One")) {
                    //saveCheck.setProgress(0);
                }
            }
        });
        host.setup();


        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");

        spec.setContent(R.id.tab1);
        //spec.setIndicator("", ResourcesCompat.getDrawable(getResources(), R.drawable.check, null));
        spec.setIndicator("b");
        host.addTab(spec);


        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("a");
        //spec.setIndicator("", ResourcesCompat.getDrawable(getResources(), R.drawable.search, null));
        host.addTab(spec);

    /*    webView = (WebView) findViewById(R.id.webview);
        //webview.setWebViewClinet(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                String urlString = webView.getUrl().toString();
            }
        });

        webView.loadUrl("http://ec2-54-167-71-119.compute-1.amazonaws.com");
*/
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "네트워크 설정을 해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        location = locManager.getLastKnownLocation(locManager.NETWORK_PROVIDER);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
        if (location == null) {
            latitude = 35.2321;
            longitude = 129.085;
        } else {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
        new PostHTTPServer().execute();
        onStop();
    }

    private class PostHTTPServer extends AsyncTask<Void, Void, String> {
        private String strUrl;
        private URL Url;
        private String strCookie;
        private String result;
        private BufferedWriter writer;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            strUrl = "http://ec2-54-167-71-119.compute-1.amazonaws.com/map.php"; //탐색하고 싶은 URL이다.
        }

        protected String doInBackground(Void... unused) {
            String content = executeClient();
            Log.d("xxx", "doInBackground function");
            return content;
        }

        // 모두 작업을 마치고 실행할 일 (메소드 등등)
        protected void onPostExecute(String result) {
            try {
                webView = (WebView) findViewById(R.id.webview);
                //webview.setWebViewClinet(new WebViewClient());
                webView.getSettings().setJavaScriptEnabled(true);

                webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        String urlString = webView.getUrl().toString();
                    }
                });

                webView.loadUrl(strUrl);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 실제 전송하는 부분
        public String executeClient() {
            Log.d("xxx", "executeClient function");
            try {
                Url = new URL(strUrl); // URL화 한다.
                HttpURLConnection conn = (HttpURLConnection) Url.openConnection(); // URL을 연결한 객체 생성.
                conn.setRequestMethod("POST"); //
                conn.setDoOutput(true); // 쓰기모드 지정
                conn.setDoInput(true); // 읽기모드 지정
                conn.setUseCaches(false); // 캐싱데이터를 받을지 안받을지
                conn.setDefaultUseCaches(false); // 캐싱데이터 디폴트 값 설정

                // 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다
                conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                //--------------------------
                //   서버로 값 전송

                OutputStream outStream = conn.getOutputStream();
                writer = new BufferedWriter(new OutputStreamWriter(outStream, "UTF-8"));
                writer.write(
                        "latitude=" + latitude + "&longitude=" + longitude); //요청 파라미터를 입력
                writer.flush();
                writer.close();
                outStream.close();
                strCookie = conn.getHeaderField("Set-Cookie"); //쿠키데이터 보관
                Log.d("xxx", "strCookie :" + strCookie);
                InputStream is = conn.getInputStream(); //input스트림 개방

                StringBuilder builder = new StringBuilder(); //문자열을 담기 위한 객체
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8")); //문자열 셋 세팅
                String line;

                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }

                result = builder.toString();
            } catch (MalformedURLException | ProtocolException exception) {
                exception.printStackTrace();
            } catch (IOException io) {
                io.printStackTrace();
            }
            return result;
        }

    }
}
