package com.daon.admin_onorder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.daon.admin_onorder.model.PrintOrderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sam4s.printer.Sam4sBuilder;
import com.sam4s.printer.Sam4sPrint;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    ImageView paymentBtn;
    ImageView orderBtn;
    ImageView serviceBtn;
    ImageView menuBtn;
    SharedPreferences pref;
    ImageView tableBtn;

    ImageView bottom_home;
    ImageView bottom_service;
    ImageView bottom_order;
    ImageView bottom_payment;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    AdminApplication app;
    String time;
    String table = "";
    String table_time = "";
    Sam4sPrint sam4sPrint;
    Sam4sPrint sam4sPrint2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = getSharedPreferences("pref", MODE_PRIVATE);
        if (app == null) {
            app = new AdminApplication();
        }
//        if (sam4sPrint == null) {
//            sam4sPrint = AdminApplication.getPrinter();
//            sam4sPrint2 = AdminApplication.getPrinter2();
//        }
        BackThread thread = new BackThread();  // 작업스레드 생성
        thread.setDaemon(true);  // 메인스레드와 종료 동기화
        thread.start();

        bottom_order = findViewById(R.id.bottom_menu3);
        bottom_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OrderActivity.class);
                startActivity(intent);
//                finish();
            }
        });
        bottom_service = findViewById(R.id.bottom_menu2);
        bottom_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ServiceActivity.class);
                startActivity(intent);
//                finish();
            }
        });
        bottom_payment = findViewById(R.id.bottom_menu4);
        bottom_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "준비중 입니다.", Toast.LENGTH_SHORT).show();

            }
        });
        menuBtn = findViewById(R.id.menu3);
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
//                startActivity(intent);
//                finish();
                Toast.makeText(MainActivity.this, "준비중 입니다.", Toast.LENGTH_SHORT).show();

            }
        });

        serviceBtn = findViewById(R.id.menu1);
        serviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ServiceActivity.class);
                startActivity(intent);
//                finish();
            }
        });

        paymentBtn = findViewById(R.id.menu4);
        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, PaymentActivity.class);
//                startActivity(intent);
//                finish();
                Toast.makeText(MainActivity.this, "준비중 입니다.", Toast.LENGTH_SHORT).show();
            }
        });

        orderBtn = findViewById(R.id.menu2);
        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OrderActivity.class);
                startActivity(intent);
//                finish();
            }
        });
        tableBtn = findViewById(R.id.menu5);
        tableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "준비중 입니다.", Toast.LENGTH_SHORT).show();
            }
        });
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        time = format2.format(calendar.getTime());
//        time = "2021-03-08";
        String time2 = format.format(calendar.getTime());

        FirebaseDatabase.getInstance().getReference().child("order").child(pref.getString("storename", "")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Calendar calendar = Calendar.getInstance();
//                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//                SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//
//                time = format2.format(calendar.getTime());
                Log.d("daon_test", "time = "+pref.getString("storename", ""));
                FirebaseDatabase.getInstance().getReference().child("order").child(pref.getString("storename", "")).child(time).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot item : snapshot.getChildren()) {
                            PrintOrderModel printOrderModel = item.getValue(PrintOrderModel.class);
                            if (printOrderModel.getPrintStatus().equals("x")) {
                                print(printOrderModel);
                                printOrderModel.setPrintStatus("o");
                                FirebaseDatabase.getInstance().getReference().child("order").child(pref.getString("storename", "")).child(time).child(item.getKey()).setValue(printOrderModel);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("service").child(pref.getString("storename", "")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Calendar calendar = Calendar.getInstance();
//                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//                SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//
//                time = format2.format(calendar.getTime());
                Log.d("daon_test", "time = "+time);
                FirebaseDatabase.getInstance().getReference().child("service").child(pref.getString("storename", "")).child(time).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot item : snapshot.getChildren()) {
                            PrintOrderModel printOrderModel = item.getValue(PrintOrderModel.class);
                            if (printOrderModel.getPrintStatus().equals("x")) {
                                print(printOrderModel);
                                printOrderModel.setPrintStatus("o");
                                FirebaseDatabase.getInstance().getReference().child("service").child(pref.getString("storename", "")).child(time).child(item.getKey()).setValue(printOrderModel);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    public void print(PrintOrderModel printOrderModel) {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(app.IsConnected1()==false)
        {
            Sam4sPrint sam4sPrint1 = app.getPrinter();
            try {
                sam4sPrint1.openPrinter(Sam4sPrint.DEVTYPE_ETHERNET, "192.168.1.100", 9100);
                Thread.sleep(300);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            app.setPrinter(sam4sPrint1);
        }
        if(app.IsConnected2()==false)
        {
            Sam4sPrint sam4sPrint2 = app.getPrinter2();
            try {
                sam4sPrint2.openPrinter(Sam4sPrint.DEVTYPE_ETHERNET, "192.168.1.101", 9100);
                Thread.sleep(300);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            app.setPrinter2(sam4sPrint2);
        }
        sam4sPrint = app.getPrinter();
        sam4sPrint2 = app.getPrinter2();

        Sam4sBuilder builder = new Sam4sBuilder("ELLIX30", Sam4sBuilder.LANG_KO);
        try {
            builder.addTextAlign(Sam4sBuilder.ALIGN_CENTER);
            builder.addFeedLine(1);
            builder.addTextSize(3, 3);
            builder.addText(printOrderModel.getTable());
            builder.addFeedLine(2);
            builder.addTextSize(2, 2);
//            builder.addTextAlign(Sam4sBuilder.ALIGN_RIGHT);
            builder.addText(printOrderModel.getOrder().replace("해바라기아줌마",""));
            builder.addFeedLine(2);
            builder.addTextSize(1, 1);
            builder.addText(printOrderModel.getTime());
            builder.addFeedLine(1);
            builder.addCut(Sam4sBuilder.CUT_FEED);
            sam4sPrint.sendData(builder);
            sam4sPrint2.sendData(builder);
//            if (printOrderModel.getTable().contains("주문")) {
//                sam4sPrint.sendData(builder);
////                sam4sPrint2.sendData(builder);
//            }else{
//                sam4sPrint.sendData(builder);
//            }
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.bell);
            mp.start();
            sam4sPrint.closePrinter();
            sam4sPrint2.closePrinter();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    class BackThread extends Thread {  // Thread 를 상속받은 작업스레드 생성
        @Override
        public void run() {
            while (true) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                time = format2.format(calendar.getTime());
                Log.d("daon_test", "time = " + time);
                try {
                    Thread.sleep(60000);   // 1000ms, 즉 1초 단위로 작업스레드 실행
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void print2(PrintOrderModel printOrderModel){

        if(app.IsConnected1()==false)
        {
            Sam4sPrint sam4sPrint1 = app.getPrinter();
            try {
                sam4sPrint1.openPrinter(Sam4sPrint.DEVTYPE_ETHERNET, "192.168.1.100", 9100);
                Thread.sleep(300);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            app.setPrinter(sam4sPrint1);
        }
        Sam4sPrint sam4sPrint = app.getPrinter();
        String[] orderArr = printOrderModel.getOrder().split("###");
        Log.d("daon_test", orderArr[0]);

        String order = printOrderModel.getOrder();
        order = order.replace("###", "");
        order = order.replace("##", "");
        try {
            Log.d("daon_test","print ="+sam4sPrint.getPrinterStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Sam4sBuilder builder = new Sam4sBuilder("ELLIX30", Sam4sBuilder.LANG_KO);
        try {
            // top
            builder.addTextAlign(Sam4sBuilder.ALIGN_CENTER);
            builder.addFeedLine(2);
            builder.addTextBold(true);
            builder.addTextSize(2,1);
            builder.addText("신용매출");
            builder.addFeedLine(1);
            builder.addTextBold(false);
            builder.addTextSize(1,1);
            builder.addTextAlign(Sam4sBuilder.ALIGN_LEFT);
            builder.addText("[고객용]");
            builder.addFeedLine(1);
            builder.addText(printOrderModel.getTime());
            builder.addFeedLine(1);
            builder.addText("돈내코두부");
            builder.addFeedLine(1);
            builder.addText("김경애 \t");
            builder.addText("101-25-66308 \t");
            builder.addText("Tel : 064-796-0517");
            builder.addFeedLine(1);
            builder.addText("제주 서귀포시 배낭골로 21길 19");
            builder.addFeedLine(1);
            // body
            builder.addText("------------------------------------------");
            builder.addFeedLine(1);
            builder.addText("TID:\t");
            builder.addText("AT0292221A \t");
            builder.addText("A-0000 \t");
            builder.addText("0017");
            builder.addFeedLine(1);
            builder.addText("카드종류: ");
            builder.addTextSize(2,1);
            builder.addTextBold(true);
            builder.addText(printOrderModel.getCardname());
            builder.addTextSize(1,1);
            builder.addTextBold(false);
            builder.addFeedLine(1);
            builder.addText("카드번호: ");
            builder.addText(printOrderModel.getCardnum());
            builder.addFeedLine(1);
            builder.addTextPosition(0);
            builder.addText("거래일시: ");
            builder.addText(printOrderModel.getAuthdate());
            builder.addTextPosition(65535);
            builder.addText("(일시불)");
            builder.addFeedLine(1);
            builder.addText("------------------------------------------");
            builder.addFeedLine(2);
            //menu
            DecimalFormat myFormatter = new DecimalFormat("###,###");

            for (int i = 0; i < orderArr.length; i++) {
                String arrOrder = orderArr[i];
                String[] subOrder = arrOrder.split("##");
                builder.addTextAlign(Sam4sBuilder.ALIGN_LEFT);
                builder.addText(subOrder[0]);
                builder.addText(subOrder[1]);
                builder.addFeedLine(1);
                builder.addTextAlign(Sam4sBuilder.ALIGN_RIGHT);
                builder.addText(subOrder[2]);
                builder.addFeedLine(2);
            }
            builder.addText("------------------------------------------");
            builder.addFeedLine(1);
            // footer
            builder.addTextAlign(Sam4sBuilder.ALIGN_LEFT);
            builder.addText("IC승인");
            builder.addTextPosition(120);
            builder.addText("금  액 : ");
            //builder.addTextPosition(400);
            int a = (Integer.parseInt(printOrderModel.getPrice()))/10;
            builder.addText(myFormatter.format(a*9)+"원");
            builder.addFeedLine(1);
            builder.addText("DDC매출표");
            builder.addTextPosition(120);
            builder.addText("부가세 : ");
            builder.addText(myFormatter.format(a*1)+"원");
            builder.addFeedLine(1);
            builder.addTextPosition(120);
            builder.addText("합  계 : ");
            builder.addTextSize(2,1);
            builder.addTextBold(true);
            builder.addText(myFormatter.format(Integer.parseInt(printOrderModel.getPrice()))+"원");
            builder.addFeedLine(1);
            builder.addTextSize(1,1);
            builder.addTextPosition(120);
            builder.addText("승인No : ");
            builder.addTextBold(true);
            builder.addTextSize(2,1);
            builder.addText(printOrderModel.getAuthnum());
            builder.addFeedLine(1);
            builder.addTextBold(false);
            builder.addTextSize(1,1);
            builder.addText("매입사명 : ");
            builder.addText(printOrderModel.getNotice());
            builder.addFeedLine(1);
            builder.addText("가맹점번호 : ");
            builder.addText("AT0292221A");
            builder.addFeedLine(1);
            builder.addText("거래일련번호 : ");
            builder.addText(printOrderModel.getVantr());
            builder.addFeedLine(1);
            builder.addText("------------------------------------------");
            builder.addFeedLine(1);
            builder.addTextAlign(Sam4sBuilder.ALIGN_CENTER);
            builder.addText("감사합니다.");
            builder.addCut(Sam4sBuilder.CUT_FEED);
            sam4sPrint.sendData(builder);
            sam4sPrint.closePrinter();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}