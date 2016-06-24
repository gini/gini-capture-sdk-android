package net.gini.android.vision.easy;

import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import net.gini.android.vision.scanner.Document;

public class ReviewDocumentActivity extends net.gini.android.vision.reviewdocument.ReviewDocumentActivity {

    @Override
    public void onAddDataToResult(Intent result) {
        // TODO: add extractions to result
        result.putExtra(MainActivity.EXTRA_OUT_EXTRACTIONS, "extractions from review screen");
    }

    @Override
    public void onShouldAnalyseDocument(Document document) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onDocumentAnalysed();
                Toast.makeText(net.gini.android.vision.easy.ReviewDocumentActivity.this, "Photo was analyzed", Toast.LENGTH_SHORT).show();
            }
        }, 2000);
    }
}
