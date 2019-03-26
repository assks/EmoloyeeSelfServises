package in.technitab.ess.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.technitab.ess.R;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.model.Policy;
import in.technitab.ess.util.ConstantVariable;
import in.technitab.ess.util.NetConnection;
import in.technitab.ess.util.UserPref;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectListActivity extends AppCompatActivity {

    @BindView(R.id.web)
    WebView web;
    ProgressDialog mProgressDialog;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private Policy policy;
    private NetConnection connection;
    private RestApi api;
    private UserPref userPref;
    private Resources resources;
    String toolbarTile = "", module = "", fileUrl = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);
        ButterKnife.bind(this);

        init();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            module = bundle.getString(ConstantVariable.MIX_ID.ACTION);
            if (module.equalsIgnoreCase(resources.getString(R.string.tec))) {
                toolbarTile = bundle.getString(resources.getString(R.string.project));
                fileUrl = bundle.getString("file_url");
                setToolbar();
                loadFileOnWebView("http://drive.google.com/viewerng/viewer?embedded=true&url=", fileUrl);
            } else if (module.equalsIgnoreCase(resources.getString(R.string.policies))) {
                int id = bundle.getInt(ConstantVariable.UserPrefVar.USER_ID);
                toolbarTile = bundle.getString(ConstantVariable.UserPrefVar.NAME);
                setToolbar();
                fetchPolicyFile(id);
            } else {
                toolbarTile = bundle.getString(ConstantVariable.UserPrefVar.NAME);
                fileUrl = bundle.getString("file_url");
                setToolbar();
                loadFileOnWebView("http://drive.google.com/viewerng/viewer?embedded=true&url=", fileUrl);
            }
        }

    }


    private void init() {
        connection = new NetConnection();
        userPref = new UserPref(this);
        api = APIClient.getClient().create(RestApi.class);
        resources = getResources();
    }

    private void fetchPolicyFile(int id) {
        if (connection.isNetworkAvailable(this)) {

            Call<Policy> call = api.fetchPolicyFile(userPref.getUserId(), id);

            call.enqueue(new Callback<Policy>() {
                @Override
                public void onResponse(@NonNull Call<Policy> call, @NonNull Response<Policy> response) {
                    if (response.isSuccessful()) {
                        Policy stringResponse = response.body();
                        if (stringResponse != null) {
                            policy = stringResponse;
                            if (!stringResponse.isError()) {
                                loadFileOnWebView(policy.getBaseUrl(), policy.getFileUrl());
                            } else {
                                progressBar.setVisibility(View.GONE);
                                showMessage(stringResponse.getMessage());
                            }

                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Policy> call, @NonNull Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    if (t instanceof SocketTimeoutException) {
                        showMessage(resources.getString(R.string.slow_internet_connection));
                    }
                }
            });

        } else {
            showMessage(resources.getString(R.string.internet_not_available));
        }
    }


    private void showMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }


    @SuppressLint("SetJavaScriptEnabled")
    private void loadFileOnWebView(String baseUrl, String fileUrl) {
        web.getSettings().setJavaScriptEnabled(true);
        web.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                web.setVisibility(View.VISIBLE);
            }


        });
        //String pdf = "http://ess.technitab.in/web_service/study/CRN2212212161.pdf";
        //web.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + pdf);

        web.loadUrl(baseUrl + fileUrl);
    }


    private void setToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setSubtitle(toolbarTile);
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timesheet_fliter, menu);
        MenuItem menuItem = menu.findItem(R.id.filter);
        menuItem.setIcon(getResources().getDrawable(R.drawable.ic_file));
        return true;

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.filter);
        menuItem.setVisible(module.equalsIgnoreCase(resources.getString(R.string.policies)) || module.equalsIgnoreCase(resources.getString(R.string.tec)));
        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.filter) {

            if (module.equalsIgnoreCase(resources.getString(R.string.policies))) {
                if (policy != null)
                    download(policy.getDownloadFileUrl());
            } else {
                download(fileUrl);
            }
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }


    private void download(String path) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Downloading...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);

        final DownloadTask downloadTask = new DownloadTask(this);
        downloadTask.execute(path, getOutputMediaFile());

        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                downloadTask.cancel(true);
            }
        });
    }


    private String getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "ess");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String path = mediaStorageDir.getPath();
        String childFolderName = module.equalsIgnoreCase(resources.getString(R.string.tec))?"TEC":"Policy";
            File subFolder = new File(path, childFolderName);
            if (!subFolder.exists()) {
                if (!subFolder.mkdirs()) {
                    return null;
                }
            }
            path = subFolder.getAbsolutePath() + "/";
        return path;
    }


    @SuppressLint("StaticFieldLeak")
    private class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        private DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                String[] urlParts = sUrl[0].split("/");

                String fileName = urlParts[urlParts.length - 1];

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                int fileLength = connection.getContentLength();
                input = connection.getInputStream();
                output = new FileOutputStream(sUrl[1] + fileName);

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;

                    if (fileLength > 0)
                        onProgressUpdate((int) (total * 100 / fileLength));

                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @SuppressLint("WakelockTimeout")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            if (pm != null) {
                mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
                mWakeLock.acquire();
            }
            mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            mProgressDialog.dismiss();
            if (result != null)
                Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(context, "File downloaded. Please check " + getOutputMediaFile(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
