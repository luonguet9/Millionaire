package dtth.com.millionaire;


import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import dtth.com.millionaire.activities.GuideActivity;
import dtth.com.millionaire.activities.PlayActivity;
import dtth.com.millionaire.activities.RankActivity;
import dtth.com.millionaire.database.AppPreferencesManager;

public class MainActivity extends AppCompatActivity {
    Button play;
    ImageView rank, guide;
    AppPreferencesManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = findViewById(R.id.play);
        rank = findViewById(R.id.rank);
        guide = findViewById(R.id.guide);
        manager = new AppPreferencesManager(this);

        play.setOnClickListener((view) -> {

            Dialog dialog = new Dialog(this, R.style.cust_dialog);
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_box);
            dialog.setContentView(R.layout.name_dialog);
            EditText text = dialog.findViewById(R.id.txtName);
            dialog.show();

            Button btNext = (Button) dialog.findViewById(R.id.btNext);
            Button btCancel = (Button) dialog.findViewById(R.id.btCancel);

            btCancel.setOnClickListener(v -> {
                dialog.dismiss();
            });

            btNext.setOnClickListener(v -> {
                String name = text.getText().toString();
                if(name.isEmpty()) {
                    text.setError("Hãy nhập tên");
                } else {
                    manager.storeName(name);
                    dialog.dismiss();
                    startActivity(new Intent(MainActivity.this, PlayActivity.class));
                }
            });

        });

        rank.setOnClickListener((view) -> {
            startActivity(new Intent(this, RankActivity.class));
        });

        guide.setOnClickListener((view) -> {
            startActivity(new Intent(this, GuideActivity.class));
        });
    }
}
