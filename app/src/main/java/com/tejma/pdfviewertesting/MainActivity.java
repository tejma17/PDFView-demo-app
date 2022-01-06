package com.tejma.pdfviewertesting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.link.DefaultLinkHandler;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity {

    private PDFView pdfView;
    private TextView pageCountTv;
    private int shortAnimationDuration;
    private ProgressBar progressBar;

    private boolean isPageCountVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pdfView = findViewById(R.id.pdfView);
        pageCountTv = findViewById(R.id.count);
        progressBar = findViewById(R.id.progress_circular);

        //page count textview animation duration
        shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        //String driveFileUrl = "https://drive.google.com/file/d/1PSW36HsFyjiTsAJQhgSAWS1yPWbutq-k/view?usp=sharing";

        String pdfUrl = "https://drive.google.com/uc?export=download&id=1PSW36HsFyjiTsAJQhgSAWS1yPWbutq-k";
        //URL Format = https://drive.google.com/uc?export=download&id=<fileId>


        new RetrivePDFfromUrl().execute(pdfUrl);

    }

    // create an async task class for loading pdf file from URL.
    class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            // we are using inputstream
            // for getting out PDF.
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                // below is the step where we are
                // creating our connection.
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    // response is success.
                    // we are getting input stream from url
                    // and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                // this is the method
                // to handle errors.
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            // after the execution of our async
            // task we are loading our pdf in our pdf view.
            loadPDF(inputStream);
        }

    }

    private void loadPDF(InputStream inputStream) {
        pdfView.fromStream(inputStream)
                //update page counts in textview
                .onPageChange((page, pageCount) -> {
                    pageCountTv.setText(String.format("%s / %s", page + 1, pageCount));
                })
                .onTap(e -> {
                    //hide/show page counts
                    if(isPageCountVisible){
                        pageCountTv.animate()
                                .alpha(0f)
                                .setDuration(shortAnimationDuration)
                                .setListener(null);
                        isPageCountVisible = false;
                    }else{
                        pageCountTv.animate()
                                .alpha(1f)
                                .setDuration(shortAnimationDuration)
                                .setListener(null);
                        isPageCountVisible = true;
                    }

                    return false;
                })
                .onLoad(nbPages -> {
                    //get document information
                    PdfDocument.Meta meta = pdfView.getDocumentMeta();
                    Log.i("TITLE", meta.getTitle()+" "+meta.getAuthor()+" "+meta.getCreationDate());

                    progressBar.setVisibility(View.GONE);
                    pageCountTv.setVisibility(View.VISIBLE);
                })
                .enableSwipe(true)
                .swipeHorizontal(false)
                .spacing(5)
                .nightMode(false)
                .enableDoubletap(true)
                .linkHandler(new DefaultLinkHandler(pdfView))
                .enableAnnotationRendering(true)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }

}