package dtth.com.millionaire.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.wang.avi.AVLoadingIndicatorView;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;

import java.util.ArrayList;
import java.util.Random;

import dtth.com.millionaire.AnimationApp;
import dtth.com.millionaire.R;
import dtth.com.millionaire.database.AppPreferencesManager;
import dtth.com.millionaire.database.DBQuestion;
import dtth.com.millionaire.models.Player;
import dtth.com.millionaire.models.Question;

public class PlayActivity  extends AppCompatActivity implements View.OnClickListener {
    AppPreferencesManager manager;
    ImageView iv_stop, iv_5050, iv_hoiykien;
    Button btn_dapanA, btn_dapanB, btn_dapanC, btn_dapanD;
    TextView tv_time, tv_coin, tv_cauhoi, tv_socau;
    DrawerLayout drawer;
    NavigationView mNavigation;
    ArrayList<Question> listQuestion;
    DBQuestion myDataBase;
    ArrayList<String> tien;
    int count = 0, trueCase, time = 30, coin, num50;
    MediaPlayer mediaPlayer;
    private AVLoadingIndicatorView avi;
    String indicator;
    private static DemNguocRunnable mDemnguocRun;
    private static Handler mDemnguocHandler;
    ListView lv_item;
    boolean isRunning = false;

    public static final int[] SOUND_QUESTIONS = {
            R.raw.ques01, R.raw.ques02, R.raw.ques03,
            R.raw.ques04, R.raw.ques05, R.raw.ques06,
            R.raw.ques07, R.raw.ques08, R.raw.ques09,
            R.raw.ques10, R.raw.ques11, R.raw.ques12,
            R.raw.ques13, R.raw.ques14, R.raw.ques15, };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        manager = new AppPreferencesManager(this);

        iv_stop = (ImageView) findViewById(R.id.imageView_stop);
        iv_5050 = (ImageView) findViewById(R.id.imageView_5050);
        iv_hoiykien = (ImageView) findViewById(R.id.imageView_hoiykien);

        btn_dapanA = (Button) findViewById(R.id.btn_a);
        btn_dapanB = (Button) findViewById(R.id.btn_b);
        btn_dapanC = (Button) findViewById(R.id.btn_c);
        btn_dapanD = (Button) findViewById(R.id.btn_d);

        tv_time = (TextView) findViewById(R.id.textView_tg);
        tv_coin = (TextView) findViewById(R.id.textView_diem);
        tv_cauhoi = (TextView) findViewById(R.id.textview_cauhoi);
        tv_socau = (TextView) findViewById(R.id.textview_cau);

        listQuestion = new ArrayList<>();
        myDataBase = new DBQuestion(this);
        try {
            myDataBase.createDataBase();
            myDataBase.openDataBase();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        listQuestion = myDataBase.getdata(this);

        tien = new ArrayList<>();

        tien.add("15. 500,000,000 VNĐ");
        tien.add("14. 200,000,000 VNĐ");
        tien.add("13. 150,000,000 VNĐ");
        tien.add("12. 100,000,000 VNĐ");
        tien.add("11. 70,000,000 VNĐ");
        tien.add("10. 50,000,000 VNĐ");
        tien.add("9.  30,000,000 VNĐ");
        tien.add("8.  20,000,000 VNĐ");
        tien.add("7.  15,000,000 VNĐ");
        tien.add("6.  8,000,000 VNĐ");
        tien.add("5.  5,000,000 VNĐ");
        tien.add("4.  2,000,000 VNĐ");
        tien.add("3.  1,000,000 VNĐ");
        tien.add("2.  500,000 VNĐ");
        tien.add("1.  200,000 VNĐ");

        iv_stop.setOnClickListener((View.OnClickListener)this);
        iv_5050.setOnClickListener((View.OnClickListener)this);
        iv_hoiykien.setOnClickListener((View.OnClickListener)this);

        btn_dapanA.setOnClickListener((View.OnClickListener)this);
        btn_dapanB.setOnClickListener((View.OnClickListener)this);
        btn_dapanC.setOnClickListener((View.OnClickListener)this);
        btn_dapanD.setOnClickListener((View.OnClickListener)this);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigation = (NavigationView) findViewById(R.id.nav_view);
        lv_item = (ListView) findViewById(R.id.lv_menu);

        // set width navigation drawer
        int width = getResources().getDisplayMetrics().widthPixels / 2;
        DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) mNavigation.getLayoutParams();
        params.width = width;
        mNavigation.setLayoutParams(params);
        //drawer.openDrawer(GravityCompat.START);

        indicator = getIntent().getStringExtra("BallSpinFadeLoaderIndicator");   // animation thời gian
        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        avi.hide();
        mDemnguocHandler = new Handler();
        mDemnguocRun = new DemNguocRunnable();
        playSound(R.raw.ready);
        final Dialog dialog = new Dialog(PlayActivity.this, R.style.cust_dialog);

        dialog.setTitle("Thông báo");
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_box);
        dialog.setContentView(R.layout.ready_dialog);
        TextView text = (TextView) dialog.findViewById(R.id.textDialog);
        text.setText("Bạn đã sẵn sàng trở thành triệu phú?");
        dialog.show();

        Button declineButton = (Button) dialog.findViewById(R.id.declineButton);
        Button cancelButton = (Button) dialog.findViewById(R.id.cancelButton);
        // if decline button is clicked, close the custom dialog
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                count++;
                playSound(R.raw.batdauchoi);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                playSound(SOUND_QUESTIONS[count - 1]);
                tv_socau.setText("Câu " + count);
                tv_cauhoi.setText(listQuestion.get(count - 1).question);
                btn_dapanA.setText("A. " + listQuestion.get(count - 1).caseA);
                btn_dapanB.setText("B. " + listQuestion.get(count - 1).caseB);
                btn_dapanC.setText("C. " + listQuestion.get(count - 1).caseC);
                btn_dapanD.setText("D. " + listQuestion.get(count - 1).caseD);
                tv_time.setText(time + "");
                showDemNguoc();
                avi.show();
                playSoundLoop(R.raw.moc1);
                trueCase();
            }
        });
        cancelButton.setOnClickListener(view -> goMainActivity());
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            avi.hide();
            mediaPlayer.stop();
            mDemnguocHandler.removeCallbacks(mDemnguocRun);
            try {
                Thread.sleep(100);
                final Dialog dialog = new Dialog(PlayActivity.this, R.style.cust_dialog);
                //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                // Include dialog.xml file
                dialog.setContentView(R.layout.finish_dialog);
                // Set dialog title
                dialog.setTitle("Thông báo");
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_box);
                // set values for custom dialog components - text, image and button

                TextView text = (TextView) dialog.findViewById(R.id.textDialog);
                TextView text_coin = (TextView) dialog.findViewById(R.id.textDialog_coin);
                String tien = tv_coin.getText().toString();
                text.setText("Bạn đã dừng cuộc chơi. Cảm ơn bạn đã tham gia chương trình");
                text_coin.setText(tien+" VNĐ");
                dialog.setCancelable(false);
                // dialog.setContentView(view);
                dialog.show();

                Button btnOk = (Button) dialog.findViewById(R.id.btn_ok_finish);
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        updatecoin();
                        goMainActivity();
                    }
                });

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        if (isRunning == true) {
            mDemnguocRun.run();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        mDemnguocHandler.removeCallbacks(mDemnguocRun);
        super.onPause();
    }

    @Override
    public void onStop() {
        mediaPlayer.stop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        isRunning = false;
        mDemnguocHandler.removeCallbacks(mDemnguocRun);
        mediaPlayer.stop();
    }

    @Override
    public void onClick(final View view) {
        if (view.getId() == R.id.btn_a || view.getId() == R.id.btn_b ||
                view.getId() == R.id.btn_c || view.getId() == R.id.btn_d) {
            view.setBackgroundResource(R.drawable.select);
            btn_dapanA.setEnabled(false);
            btn_dapanB.setEnabled(false);
            btn_dapanC.setEnabled(false);
            btn_dapanD.setEnabled(false);
            mDemnguocHandler.removeCallbacks(mDemnguocRun); // dừng thời gian
            //view.setEnabled(false);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // nếu chọn đúng đáp án
                    if (view.getId() == trueCase) {
                        //anim(trueCase);
                        view.setEnabled(true);
                        view.setBackgroundResource(R.drawable.button3);
                        switch (trueCase) {
                            case R.id.btn_a:
                                playSound(R.raw.true_a);
                                break;

                            case R.id.btn_b:
                                playSound(R.raw.true_b);
                                break;

                            case R.id.btn_c:
                                playSound(R.raw.true_c);
                                break;

                            case R.id.btn_d:
                                playSound(R.raw.true_d);
                                break;
                        }
                        tienthuong(count);

                        try {
                            Thread.sleep(3500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        count++;
                        //playSound(SOUND_QUESTIONS[count-1]);
                        if (count < 16) {
                            hienthicauhoi();
                        }
                        if (count == 16) {
                            // trả lời đúng hết 15 câu
                            mediaPlayer.stop();
                            try {
                                Thread.sleep(5000);
                                avi.hide();

                                final Dialog dialog = new Dialog(PlayActivity.this, R.style.cust_dialog);
                                //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                // Include dialog.xml file
                                dialog.setContentView(R.layout.finish_dialog);
                                // Set dialog title
                                dialog.setTitle("Thông báo");
                                dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_box);
                                // set values for custom dialog components - text, image and button

                                TextView text = (TextView) dialog.findViewById(R.id.textDialog);
                                TextView text_coin = (TextView) dialog.findViewById(R.id.textDialog_coin);
                                String tien = tv_coin.getText().toString();
                                text.setText("Chúc mừng bạn đã trở thành triệu phú!");
                                text_coin.setText(tien);
                                dialog.setCancelable(false);
                                // dialog.setContentView(view);
                                dialog.show();
                                Button btnOk = (Button) dialog.findViewById(R.id.btn_ok_finish);
                                btnOk.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        updatecoin();
                                        goMainActivity();
                                    }
                                });

                                tv_cauhoi.setText("");
                                btn_dapanA.setText("");
                                btn_dapanB.setText("");
                                btn_dapanC.setText("");
                                btn_dapanD.setText("");
                                btn_dapanA.setBackgroundResource(R.drawable.button3);
                                btn_dapanB.setBackgroundResource(R.drawable.button3);
                                btn_dapanC.setBackgroundResource(R.drawable.button3);
                                btn_dapanD.setBackgroundResource(R.drawable.button3);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    // nếu chọn sai đáp án
                    else {
                        btn_dapanA.setEnabled(true);
                        btn_dapanB.setEnabled(true);
                        btn_dapanC.setEnabled(true);
                        btn_dapanD.setEnabled(true);
                        mediaPlayer.stop();
                        switch (trueCase) {
                            case R.id.btn_a:
                                view.setBackgroundResource(R.drawable.button3);
                                btn_dapanA.setBackgroundResource(R.drawable.select);
                                playSound(R.raw.lose_a);
                                anim(R.id.btn_a);
                                break;
                            case R.id.btn_b:
                                view.setBackgroundResource(R.drawable.button3);
                                btn_dapanB.setBackgroundResource(R.drawable.select);
                                playSound(R.raw.lose_b);
                                anim(R.id.btn_b);
                                break;
                            case R.id.btn_c:
                                view.setBackgroundResource(R.drawable.button3);
                                btn_dapanC.setBackgroundResource(R.drawable.select);
                                playSound(R.raw.lose_c);
                                anim(R.id.btn_c);
                                break;
                            case R.id.btn_d:
                                view.setBackgroundResource(R.drawable.button3);
                                btn_dapanD.setBackgroundResource(R.drawable.select);
                                playSound(R.raw.lose_d);
                                anim(R.id.btn_d);
                                break;
                        }
                        try {
                            Thread.sleep(3000);
                            final Dialog dialog = new Dialog(PlayActivity.this, R.style.cust_dialog);
                            //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            // Include dialog.xml file
                            dialog.setContentView(R.layout.finish_dialog);
                            // Set dialog title
                            dialog.setTitle("Thông báo");
                            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_box);
                            // set values for custom dialog components - text, image and button

                            TextView text = (TextView) dialog.findViewById(R.id.textDialog);
                            TextView text_coin = (TextView) dialog.findViewById(R.id.textDialog_coin);
                            text.setText("Thật tiếc bạn đã thua!");
                            finalcoin(text_coin, count);
                            dialog.setCancelable(false);
                            // dialog.setContentView(view);
                            dialog.show();
                            Button btnOk = (Button) dialog.findViewById(R.id.btn_ok_finish);
                            btnOk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    updatecoin();
                                    goMainActivity();
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, 2000);
        }
        // quyền trợ giúp 50/50
        if (view.getId() == R.id.imageView_5050) {
            iv_5050.setImageResource(R.drawable.help_5050_x);
            iv_5050.setEnabled(false);
            playSound(R.raw.sound5050);
            try {
                Thread.sleep(3000);
                if (trueCase == R.id.btn_a) {
                    btn_dapanB.setText("");
                    btn_dapanB.setEnabled(false);
                    btn_dapanC.setText("");
                    btn_dapanC.setEnabled(false);
                } else if (trueCase == R.id.btn_b) {
                    btn_dapanA.setText("");
                    btn_dapanA.setEnabled(false);
                    btn_dapanD.setText("");
                    btn_dapanD.setEnabled(false);
                } else if (trueCase == R.id.btn_c) {
                    btn_dapanD.setText("");
                    btn_dapanD.setEnabled(false);
                    btn_dapanB.setText("");
                    btn_dapanB.setEnabled(false);
                } else if (trueCase == R.id.btn_d) {
                    btn_dapanC.setText("");
                    btn_dapanC.setEnabled(false);
                    btn_dapanA.setText("");
                    btn_dapanA.setEnabled(false);
                }
                playSound(R.raw.s50);
                num50 = count;
            } catch (Exception e) {
            }
        }

        // xin dừng cuộc chơi
        if (view.getId() == R.id.imageView_stop) {
            stop();
        }

        // quyền trợ giúp hỏi ý kiến khán giả
        if (view.getId() == R.id.imageView_hoiykien) {
            mDemnguocHandler.removeCallbacks(mDemnguocRun);
            iv_hoiykien.setImageResource(R.drawable.help_audience_x);
            iv_hoiykien.setEnabled(false);
            playSound(R.raw.khan_gia);
            try{
                Thread.sleep(5000);
                initDialogAudience();
            }catch (Exception e){}
        }
    }

    public void hienthicauhoi() {
        trueCase();
        playSound(SOUND_QUESTIONS[count - 1]);
        action();
        tv_socau.setText("Câu " + count);
        tv_cauhoi.setText(listQuestion.get(count - 1).question);
        btn_dapanA.setText("A. " + listQuestion.get(count - 1).caseA);
        btn_dapanB.setText("B. " + listQuestion.get(count - 1).caseB);
        btn_dapanC.setText("C. " + listQuestion.get(count - 1).caseC);
        btn_dapanD.setText("D. " + listQuestion.get(count - 1).caseD);
        time = 30;
        tv_time.setText(time + "");
        showDemNguoc();
    }

    // gán đáp án đúng cho mỗi câu hỏi
    public void trueCase() {
        int dap_an = listQuestion.get(count - 1).trueCase;
        switch (dap_an) {
            case 1:
                trueCase = R.id.btn_a;
                break;
            case 2:
                trueCase = R.id.btn_b;
                break;
            case 3:
                trueCase = R.id.btn_c;
                break;
            case 4:
                trueCase = R.id.btn_d;
                break;
        }
    }

    // hiển thị tiền thuởng sau mỗi câu hỏi
    public void tienthuong(int count) {
        switch (count) {
            case 1:
                coin = 200000;
                tv_coin.setText(String.valueOf(coin));
                break;
            case 2:
                coin = 500000;
                tv_coin.setText(String.valueOf(coin));
                break;
            case 3:
                coin = 1000000;
                tv_coin.setText(String.valueOf(coin));
                break;
            case 4:
                coin = 2000000;
                tv_coin.setText(String.valueOf(coin));
                break;
            case 5:
                coin = 5000000;
                tv_coin.setText(String.valueOf(coin));
                break;
            case 6:
                coin = 8000000;
                tv_coin.setText(String.valueOf(coin));
                break;
            case 7:
                coin = 15000000;
                tv_coin.setText(String.valueOf(coin));
                break;
            case 8:
                coin = 20000000;
                tv_coin.setText(String.valueOf(coin));
                break;
            case 9:
                coin = 30000000;
                tv_coin.setText(String.valueOf(coin));
                break;
            case 10:
                coin = 50000000;
                tv_coin.setText(String.valueOf(coin));
                break;
            case 11:
                coin = 70000000;
                tv_coin.setText(String.valueOf(coin));
                break;
            case 12:
                coin = 100000000;
                tv_coin.setText(String.valueOf(coin));
                break;
            case 13:
                coin = 150000000;
                tv_coin.setText(String.valueOf(coin));
                break;
            case 14:
                coin = 200000000;
                tv_coin.setText(String.valueOf(coin));
                break;
            case 15:
                coin = 500000000;
                tv_coin.setText(String.valueOf(coin));
                break;
        }
    }

    // tính tiền thuởng khi chọn sai câu hỏi và hết thời gian
    public void finalcoin(TextView tv, int count) {
        if(count == 1) coin = 0;
        else if (count <= 5) coin = 200000;
        else if (count <= 10) coin = 5000000;
        else if (count <= 15) coin = 50000000;
        else coin = 50000000;
        tv.setText(String.valueOf(coin) + " VNĐ");
    }

    // update điểm vào shared preferences
    public void updatecoin() {
        manager.addPlayer(new Player(manager.getName(), coin));
    }

    // dừng cuộc chơi
    public void stop() {
        Dialog dlg = new Dialog(PlayActivity.this, R.style.cust_dialog);
        dlg.setTitle("Thông báo");
        dlg.getWindow().setBackgroundDrawableResource(R.drawable.dialog_box);
        dlg.setContentView(R.layout.back_on_game);
        TextView textView = dlg.findViewById(R.id.tv);
        textView.setText("Bạn có chắc chắn muốn dừng cuộc chơi?");
        dlg.show();

        //mDemnguocHandler.removeCallbacks(mDemnguocRun);
        Button yes, no;
        yes = dlg.findViewById(R.id.yes);
        no = dlg.findViewById(R.id.no);
        yes.setOnClickListener((view) -> {
            dlg.dismiss();
            avi.hide();
            mediaPlayer.stop();
            mDemnguocHandler.removeCallbacks(mDemnguocRun);
            try {
                Thread.sleep(100);
                final Dialog dialog = new Dialog(PlayActivity.this, R.style.cust_dialog);
                //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                // Include dialog.xml file
                dialog.setContentView(R.layout.finish_dialog);
                // Set dialog title
                dialog.setTitle("Thông báo");
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_box);
                // set values for custom dialog components - text, image and button

                TextView text = (TextView) dialog.findViewById(R.id.textDialog);
                TextView text_coin = (TextView) dialog.findViewById(R.id.textDialog_coin);
                String tien = tv_coin.getText().toString();
                text.setText("Bạn đã dừng cuộc chơi. Cảm ơn bạn đã tham gia chương trình");
                text_coin.setText(tien+" VNĐ");
                dialog.setCancelable(false);
                // dialog.setContentView(view);
                dialog.show();

                Button btnOk = (Button) dialog.findViewById(R.id.btn_ok_finish);
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        manager.addPlayer(new Player(manager.getName(), Integer.valueOf(tien)));
                        goMainActivity();
                    }
                });

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        no.setOnClickListener((view) -> {
            dlg.dismiss();
            //mDemnguocRun.run();
        });
    }

    // hiển thị dialog khi chức năng hỏi ý kiến
    public void initDialogAudience() {
        final Dialog dialog = new Dialog(this, R.style.cust_dialog);
        dialog.setTitle("Hỏi ý kiến khán giả");
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_box);
        dialog.setContentView(R.layout.ykien_dialog);
        //LayoutInflater layoutInflater = LayoutInflater.from(this);
        // View view = layoutInflater.inflate(R.layout.ykien_dialog, null);

        BarChart mBarChart;
        Button btnOk = (Button) dialog.findViewById(R.id.cancelButton);
        Random random = new Random();
        int rd1True = random.nextInt(10) + 50;
        int rdFalse1 = random.nextInt(10) + 5;
        int rdFalse2 = random.nextInt(10) + 5;
        int rdFalse3 = 100 - rd1True - rdFalse1 - rdFalse2;

        // nếu chọn 50-50 trước và sau đó chọn hỏi ý kiến khán giả
        if (iv_5050.isEnabled() == false && trueCase == R.id.btn_a && num50 == count) { // nếu đáp án đúng là A
            mBarChart = (BarChart) dialog.findViewById(R.id.barchart);
            rd1True = random.nextInt(10) + 50;
            rdFalse3 = 100 - rd1True;
            mBarChart.addBar(new BarModel("A", rd1True, 0xFF563456));
            mBarChart.addBar(new BarModel("B", 0, 0xFF563456));
            mBarChart.addBar(new BarModel("C", 0, 0xFF563456));
            mBarChart.addBar(new BarModel("D", rdFalse3, 0xFF563456));
            mBarChart.startAnimation();

        } else if (iv_5050.isEnabled() == false && trueCase == R.id.btn_b && num50 == count) {  // nếu đáp án đúng là B
            mBarChart = (BarChart) dialog.findViewById(R.id.barchart);
            rd1True = random.nextInt(10) + 50;
            rdFalse2 = 100 - rd1True;
            mBarChart.addBar(new BarModel("A", 0, 0xFF563456));
            mBarChart.addBar(new BarModel("B", rd1True, 0xFF563456));
            mBarChart.addBar(new BarModel("C", rdFalse2, 0xFF563456));
            mBarChart.addBar(new BarModel("D", 0, 0xFF563456));
            mBarChart.startAnimation();
        } else if (iv_5050.isEnabled() == false && trueCase == R.id.btn_c && num50 == count) {  // nếu đáp án đúng là C
            mBarChart = (BarChart) dialog.findViewById(R.id.barchart);
            rd1True = random.nextInt(10) + 50;
            rdFalse1 = 100 - rd1True;
            mBarChart.addBar(new BarModel("A", rdFalse1, 0xFF563456));
            mBarChart.addBar(new BarModel("B", 0, 0xFF563456));
            mBarChart.addBar(new BarModel("C", rd1True, 0xFF563456));
            mBarChart.addBar(new BarModel("D", 0, 0xFF563456));
            mBarChart.startAnimation();

        } else if (iv_5050.isEnabled() == false && trueCase == R.id.btn_d && num50 == count) {  // nếu đáp án đúng là D
            mBarChart = (BarChart) dialog.findViewById(R.id.barchart);
            rd1True = random.nextInt(10) + 50;
            rdFalse2 = 100 - rd1True;
            mBarChart.addBar(new BarModel("A", 0, 0xFF563456));
            mBarChart.addBar(new BarModel("B", rdFalse2, 0xFF563456));
            mBarChart.addBar(new BarModel("C", 0, 0xFF563456));
            mBarChart.addBar(new BarModel("D", rd1True, 0xFF563456));
            mBarChart.startAnimation();

        }

        // trước khi chọn trợ giúp 50-50 và chọn 50-50 với ý kiến ko cùng 1 câu
        else if (trueCase == R.id.btn_a) { // nếu đáp án đúng là A
            mBarChart = (BarChart) dialog.findViewById(R.id.barchart);
            mBarChart.addBar(new BarModel("A", rd1True, 0xFF563456));
            mBarChart.addBar(new BarModel("B", rdFalse1, 0xFF563456));
            mBarChart.addBar(new BarModel("C", rdFalse2, 0xFF563456));
            mBarChart.addBar(new BarModel("D", rdFalse3, 0xFF563456));
            playSound(R.raw.bg_audience);
            try {
                Thread.sleep(500);
                mBarChart.startAnimation();
            } catch (Exception e) {

            }

        } else if (trueCase == R.id.btn_b) {// nếu đáp án đúng là B
            mBarChart = (BarChart) dialog.findViewById(R.id.barchart);
            mBarChart.addBar(new BarModel("A", rdFalse1, 0xFF563456));
            mBarChart.addBar(new BarModel("B", rd1True, 0xFF563456));
            mBarChart.addBar(new BarModel("C", rdFalse2, 0xFF563456));
            mBarChart.addBar(new BarModel("D", rdFalse3, 0xFF563456));
            playSound(R.raw.bg_audience);
            try {
                Thread.sleep(500);
                mBarChart.startAnimation();
            } catch (Exception e) {

            }

        } else if (trueCase == R.id.btn_c) {// nếu đáp án đúng là C
            mBarChart = (BarChart) dialog.findViewById(R.id.barchart);
            mBarChart.addBar(new BarModel("A", rdFalse1, 0xFF563456));
            mBarChart.addBar(new BarModel("B", rdFalse2, 0xFF563456));
            mBarChart.addBar(new BarModel("C", rd1True, 0xFF563456));
            mBarChart.addBar(new BarModel("D", rdFalse3, 0xFF563456));
            playSound(R.raw.bg_audience);
            try {
                Thread.sleep(500);
                mBarChart.startAnimation();
            } catch (Exception e) {

            }

        } else if (trueCase == R.id.btn_d) {// nếu đáp án đúng là D
            mBarChart = (BarChart) dialog.findViewById(R.id.barchart);
            mBarChart.addBar(new BarModel("A", rdFalse1, 0xFF563456));
            mBarChart.addBar(new BarModel("B", rdFalse2, 0xFF563456));
            mBarChart.addBar(new BarModel("C", rdFalse3, 0xFF563456));
            mBarChart.addBar(new BarModel("D", rd1True, 0xFF563456));
            playSound(R.raw.bg_audience);
            try {
                Thread.sleep(500);
                mBarChart.startAnimation();
            } catch (Exception e) {

            }
        }

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mDemnguocRun.run();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    private void showDemNguoc() {
        if (!drawer.isDrawerOpen(GravityCompat.START)) {
            time = 30;
            mDemnguocHandler.removeCallbacks(mDemnguocRun);
            mDemnguocHandler.postDelayed(mDemnguocRun, 1000);
        }
    }

    private void handleDemnguoc() {
        time--;
        if (time == 0) {
            isRunning = false;
            mediaPlayer.stop();
            playSound(R.raw.out_of_time);
            switch (trueCase) {
                case R.id.btn_a:
                    btn_dapanA.setEnabled(false);
                    btn_dapanA.setBackgroundResource(R.drawable.answer_true);
                    playSound(R.raw.lose_a);
                    anim(R.id.btn_a);
                    break;
                case R.id.btn_b:
                    btn_dapanB.setEnabled(false);
                    btn_dapanB.setBackgroundResource(R.drawable.answer_true);
                    playSound(R.raw.lose_b);
                    anim(R.id.btn_b);
                    break;
                case R.id.btn_c:
                    btn_dapanC.setEnabled(false);
                    btn_dapanC.setBackgroundResource(R.drawable.answer_true);
                    playSound(R.raw.lose_c);
                    anim(R.id.btn_c);
                    break;
                case R.id.btn_d:
                    btn_dapanD.setEnabled(false);
                    btn_dapanD.setBackgroundResource(R.drawable.answer_true);
                    playSound(R.raw.lose_d);
                    anim(R.id.btn_d);
                    break;
            }
            try {
                Thread.sleep(500);
                final Dialog dialog = new Dialog(PlayActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                // Include dialog.xml file
                dialog.setContentView(R.layout.finish_dialog);
                // Set dialog title
                //dialog.setTitle("Thông báo");

                // set values for custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.textDialog);
                TextView text_coin = (TextView) dialog.findViewById(R.id.textDialog_coin);
                text.setText("Thật tiếc bạn đã thua!");
                finalcoin(text_coin, count);
                dialog.setCancelable(true);
                // dialog.setContentView(view);
                dialog.show();
                Button btnOk = (Button) dialog.findViewById(R.id.btn_ok_finish);
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        updatecoin();
                        goMainActivity();
                    }
                });
                tv_cauhoi.setText("");
                btn_dapanA.setText("");
                btn_dapanB.setText("");
                btn_dapanC.setText("");
                btn_dapanD.setText("");
                btn_dapanA.setBackgroundResource(R.drawable.button3);
                btn_dapanB.setBackgroundResource(R.drawable.button3);
                btn_dapanC.setBackgroundResource(R.drawable.button3);
                btn_dapanD.setBackgroundResource(R.drawable.button3);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tv_time.setText(time + "");
            mDemnguocHandler.removeCallbacks(mDemnguocRun);
            avi.hide();
        }
        //avi.setIndicator(indicator);
        // avi.show();
        else {
            isRunning = true;
            tv_time.setText(time + "");
            mDemnguocHandler.postDelayed(mDemnguocRun, 1000);
        }
    }

    // enable các button
    public void action() {
        btn_dapanA.setEnabled(true);
        btn_dapanB.setEnabled(true);
        btn_dapanC.setEnabled(true);
        btn_dapanD.setEnabled(true);
    }

    // animation khi đáp án đúng
    public void anim(int v) {
        YoYo.with(Techniques.Flash).duration(750).playOn(findViewById(v));
    }

    // hàm play âm thanh
    public void playSound(int type) {
        mediaPlayer = MediaPlayer.create(this, type);
        mediaPlayer.start();
    }

    public void playSoundLoop(int type) {
        mediaPlayer = MediaPlayer.create(this, type);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    // trở vê MainActivity
    public void goMainActivity() {
        mediaPlayer.stop();
        finish();
        try {
            AnimationApp anim = new AnimationApp();
            anim.unzoomAnimation(PlayActivity.this);
        } catch (Exception e) {

        }
    }

    private class DemNguocRunnable implements Runnable {
        @Override
        public void run() {
            handleDemnguoc();
        }
    }

}
