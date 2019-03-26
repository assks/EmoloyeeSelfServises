package in.technitab.ess.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;
import com.isseiaoki.simplecropview.callback.LoadCallback;
import com.isseiaoki.simplecropview.callback.SaveCallback;
import com.isseiaoki.simplecropview.util.Logger;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.technitab.ess.R;

public class CropImageActivity extends AppCompatActivity {

    private static final String TAG = CropImageActivity.class.getSimpleName();
    @BindView(R.id.cropImageView)
    CropImageView cropImageView;
    @BindView(R.id.buttonCloseImage)
    ImageButton buttonCloseImage;
    @BindView(R.id.buttonRotateLeft)
    ImageButton buttonRotateLeft;
    @BindView(R.id.buttonRotateRight)
    ImageButton buttonRotateRight;
    @BindView(R.id.buttonDone)
    ImageButton buttonDone;
    @BindView(R.id.cropView)
    LinearLayout cropView;
    @BindView(R.id.cropImage)
    ImageView cropImage;
    private Bitmap.CompressFormat mCompressFormat = Bitmap.CompressFormat.JPEG;
    private RectF mFrameRect = null;
    private Uri mSourceUri = null,mOutputUri= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);
        ButterKnife.bind(this);

        setToolbar();
        Intent intent = getIntent();
        if (intent != null) {
            String uri = intent.getStringExtra("uri");
            Log.d(TAG, " string uri " + uri);

            Uri imageUri = Uri.parse(uri);
            if (imageUri != null) {
                Log.d(TAG, imageUri.getPath());
                mSourceUri = imageUri;
                cropImageView.load(mSourceUri)
                        .initialFrameRect(mFrameRect)
                        .useThumbnail(true)
                        .execute(mLoadCallback);

                cropImageView.setCropMode(CropImageView.CropMode.FREE);
            }
        }
    }


    private void setToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getResources().getString(R.string.convert_pdf));
            actionBar.setBackgroundDrawable(new ColorDrawable());
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }


    @OnClick(R.id.buttonCloseImage)
    protected void onClose() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }


    @OnClick(R.id.buttonDone)
    protected void onDone() {
        cropImageView.crop(mSourceUri).execute(mCropCallback);
    }


    @OnClick(R.id.buttonRotateLeft)
    protected void onRotateLeft() {
        cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D);
    }

    @OnClick(R.id.buttonRotateRight)
    protected void onRotateRight() {
        cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
    }

    public Uri createSaveUri() {
        return createNewUri(this, mCompressFormat);
    }

    private Uri createNewUri(Context context, Bitmap.CompressFormat format) {
        long currentTimeMillis = System.currentTimeMillis();
        Date today = new Date(currentTimeMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String title = dateFormat.format(today);
        String dirPath = getDirPath();
        String fileName = "scv" + title + "." + getMimeType(format);
        String path = dirPath + "/" + fileName;
        File file = new File(path);
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/" + getMimeType(format));
        values.put(MediaStore.Images.Media.DATA, path);
        long time = currentTimeMillis / 1000;
        values.put(MediaStore.MediaColumns.DATE_ADDED, time);
        values.put(MediaStore.MediaColumns.DATE_MODIFIED, time);
        if (file.exists()) {
            values.put(MediaStore.Images.Media.SIZE, file.length());
        }

        ContentResolver resolver = context.getContentResolver();
        Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        SharedPreferences sp = getSharedPreferences("file_image", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("image", file.getAbsolutePath());
        editor.apply();
        return uri;
    }


    public static String getMimeType(Bitmap.CompressFormat format) {
        Logger.i("getMimeType CompressFormat = " + format);
        switch (format) {
            case JPEG:
                return "jpeg";
            case PNG:
                return "png";
        }
        return "png";
    }

    public static String getDirPath() {
        String dirPath = "";
        File imageDir = null;
        File extStorageDir = Environment.getExternalStorageDirectory();
        if (extStorageDir.canWrite()) {
            imageDir = new File(extStorageDir.getPath() + "/simplecropview");
        }
        if (imageDir != null) {
            if (!imageDir.exists()) {
                imageDir.mkdirs();
            }
            if (imageDir.canWrite()) {
                dirPath = imageDir.getPath();
            }
        }
        return dirPath;
    }


    private final LoadCallback mLoadCallback = new LoadCallback() {
        @Override
        public void onSuccess() {
        }

        @Override
        public void onError(Throwable e) {
        }
    };

    private final CropCallback mCropCallback = new CropCallback() {
        @Override
        public void onSuccess(Bitmap cropped) {
            Log.d("crop ", "enter");
            cropImageView.save(cropped)
                    .compressFormat(mCompressFormat)
                    .execute(createSaveUri(), mSaveCallback);
        }

        @Override
        public void onError(Throwable e) {
            Log.e("errorCrop ", e.getMessage());
        }
    };

    private final SaveCallback mSaveCallback = new SaveCallback() {
        @Override
        public void onSuccess(Uri outputUri) {
            mOutputUri = outputUri;
            cropView.setVisibility(View.GONE);
            Glide.with(CropImageActivity.this).load(outputUri).into(cropImage);
            cropImage.setVisibility(View.VISIBLE);
            Log.d("uri ", "" + outputUri);
        }

        @Override
        public void onError(Throwable e) {
            Log.e("errorSaved ", e.getMessage());
        }
    };



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_attendance_leave, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_leave);
        menuItem.setTitle(getResources().getString(R.string.save_pdf));
        menuItem.setVisible(false);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.menu_leave);
        menuItem.setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_leave) {
            if (mOutputUri != null) {
                savePdf();
            }else {
                cropImageView.crop(mSourceUri).execute(mCropCallback);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        savePdf();
                    }
                },200);
            }
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }


    private void savePdf() {

        SharedPreferences sp = getSharedPreferences("file_image", MODE_PRIVATE);
        String fileUri = sp.getString("image","");

        try {
            Document document = new Document();
            File file = getOutputMediaFile();
            if (file != null) {
                PdfWriter.getInstance(document, new FileOutputStream(file.getAbsoluteFile())); //  Change pdf's name.
                document.open();
                Image image = Image.getInstance(fileUri);  // Change image's name and extension.
                float scaler = ((document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin() - 0) / image.getWidth()) * 100; // 0 means you have no indentation. If you have any, change it.
                image.scalePercent(scaler);
                image.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
                document.add(image);
                document.close();

                Intent intent = new Intent();
                intent.putExtra("uri",file.getAbsolutePath());
                setResult(Activity.RESULT_OK,intent);
                finish();

            }
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }


    private File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "Ess");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator + "PDF_" + timeStamp + ".pdf");
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
