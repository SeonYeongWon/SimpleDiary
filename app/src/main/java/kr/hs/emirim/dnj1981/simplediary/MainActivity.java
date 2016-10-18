package kr.hs.emirim.dnj1981.simplediary;

import android.annotation.TargetApi;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    DatePicker datePic;
    EditText editDiary;
    Button butSave;
    String fileName;
    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        datePic=(DatePicker)findViewById(R.id.date_picker);
        editDiary=(EditText)findViewById(R.id.edit_content);
        butSave=(Button)findViewById(R.id.but_save);

        //현재 날짜 구하기
        Calendar calendar=Calendar.getInstance();
        int nowYear=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH)+1;
        int date=calendar.get(Calendar.DATE);

        //DatePicker에 현재 날짜 설정
        datePic.init(nowYear,month,date,new DatePicker.OnDateChangedListener(){
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayofMonth){
                fileName=year+"_"+(monthOfYear+1)+"_"+dayofMonth+".txt";
                String content=readDiary(fileName);
                editDiary.setText(content);
                butSave.setEnabled(true);

            }
        });

        butSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FileOutputStream out=openFileOutput(fileName, Context.MODE_WORLD_WRITEABLE);
                    String diaryContents=editDiary.getText().toString();
                    out.write(diaryContents.getBytes());
                    out.close();
                    Toast.makeText(getApplicationContext(),"저장이 완료됨",Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    String readDiary(String fileName){
        String diaryContents = null;
        try {
            FileInputStream in=openFileInput(fileName);
            byte[] txt=new byte[500];
            in.read(txt);
            in.close();
            diaryContents=new String(txt);
            butSave.setText("수정하기");
        } catch (IOException e) {
            editDiary.setHint("읽어올 일기가 없음");
            butSave.setText("새로 저장");
        }
        return null;
    }

}
