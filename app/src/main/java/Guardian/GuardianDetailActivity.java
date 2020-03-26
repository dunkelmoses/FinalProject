package Guardian;
/**
 * Page to display details of an article and save article
 * Author: Hicham Soujae
 */


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;


import com.example.finalproject.R;
import com.google.android.material.snackbar.Snackbar;

/**
 * display detailed information about an article
 * allow user to save to article
 * send saved article information to database
 */

public class GuardianDetailActivity extends AppCompatActivity {
    /**
     * get article information from news main page
     * Connect and save to database     *
     * @param savedInstanceState
     */
    Article article;
    boolean isSaved;
    GuardianDB guardianDB;
    Button button5, button4;
    CoordinatorLayout cLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        //Using toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_detail);
        setSupportActionBar(toolbar);

        cLayout = findViewById(R.id.coordinatorLayout);

        article = (Article) getIntent().getSerializableExtra("article");
        String title = article.getTitle();
        String section = article.getSectionName();

        String url = article.getUrl();
        final String id = article.getId();

        TextView textView = findViewById(R.id.textView);
        textView.setText(title);
        TextView textView6 = findViewById(R.id.textView6);
        textView6.setText(section);


        TextView textView10 = findViewById(R.id.textView10);
        textView10.setText(url);

        button5 = findViewById(R.id.button5);

        guardianDB = new GuardianDB(this);
        isSaved = guardianDB.isSaved(id);

        String action;
        if (isSaved) {
            action = getString(R.string.action_delete);
        } else {
            action = getString(R.string.action_save);
        }

        button4 = findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //opening URL in web browser
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(article.getUrl()));
                startActivity(i);
            }
        });

        button5.setText(action);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSaved) {
                    showAlertDialog(id);
                } else {
                    guardianDB.saveArticle(article);
                    isSaved = guardianDB.isSaved(id);
                    //Show the Snackbar
                    Snackbar.make(cLayout, getString(R.string.article_saved), Snackbar.LENGTH_SHORT).show();
                    String action;
                    if (isSaved) {
                        action = getString(R.string.action_delete);
                    } else {
                        action = getString(R.string.action_save);
                    }
                    button5.setText(action);
                }
            }
        });
    }

    private void showAlertDialog(final String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(getString(R.string.article_delete_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                guardianDB.deleteArticle(id);
                isSaved = guardianDB.isSaved(id);
                Snackbar.make(cLayout, getString(R.string.article_deleted), Snackbar.LENGTH_SHORT).show();
                String action;
                if (isSaved) {
                    action = getString(R.string.action_delete);
                } else {
                    action = getString(R.string.action_save);
                }
                button5.setText(action);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(getString(R.string.article_delete_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setTitle(getString(R.string.action_delete));
        builder.setMessage(getString(R.string.article_delete_message));
        builder.show();
    }
}
