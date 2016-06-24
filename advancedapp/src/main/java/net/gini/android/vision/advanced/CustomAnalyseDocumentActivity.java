package net.gini.android.vision.advanced;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import net.gini.android.vision.GiniVisionError;
import net.gini.android.vision.analyse.AnalyseDocumentFragmentListener;
import net.gini.android.vision.analyse.AnalyseDocumentFragmentStandard;
import net.gini.android.vision.scanner.Document;
import net.gini.android.vision.scanner.photo.Photo;
import net.gini.android.visionadvtest.R;

public class CustomAnalyseDocumentActivity extends Activity implements AnalyseDocumentFragmentListener {

    AnalyseDocumentFragmentStandard mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_analyse_document);
        createFragment();
        showFragment();
    }

    private void createFragment() {
        mFragment = AnalyseDocumentFragmentStandard.createInstance(Document.fromPhoto(Photo.fromJpeg(new byte[]{}, 0)));
    }

    private void showFragment() {
        getFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_analyse_document, mFragment)
                .commit();
    }

    @Override
    public void onAnalyseDocument(Document document) {
        mFragment.startScanAnimation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mFragment.onDocumentAnalysed();
                mFragment.stopScanAnimation();
                Toast.makeText(CustomAnalyseDocumentActivity.this, "Photo was analyzed", Toast.LENGTH_SHORT).show();
            }
        }, 1000);
    }

    @Override
    public void onError(GiniVisionError error) {

    }
}
