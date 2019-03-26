package in.technitab.ess.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
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
import in.technitab.ess.R;

public class ImageToPdfActivity extends AppCompatActivity {

    @BindView(R.id.imageToPdf)
    ImageView imageToPdf;
    private String imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_to_pdf);
        ButterKnife.bind(this);

        setToolbar();
        Intent intent = getIntent();
        imageUri = intent.getStringExtra("uri");
        Glide.with(this).load(imageUri).into(imageToPdf);

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_attendance_leave, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_leave);
        menuItem.setTitle(getResources().getString(R.string.save_pdf));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_leave) {
            savePdf();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    private void savePdf() {

        try {
            Document document = new Document();
            File file = getOutputMediaFile();
            if (file != null) {
                PdfWriter.getInstance(document, new FileOutputStream(file.getAbsoluteFile())); //  Change pdf's name.
                document.open();
                Image image = Image.getInstance(imageUri);  // Change image's name and extension.
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

}
