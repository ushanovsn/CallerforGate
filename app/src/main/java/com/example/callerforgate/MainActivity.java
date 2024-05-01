package com.example.callerforgate;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //для запроса разрешений нужно объявить константу здесь
    private static final int PERMISSION_REQUEST_CODE = 1;

    //объекты и константы для сохранения параметров
    public static final String APP_PREFERENCES = "myDialSettings";
    public static final String APP_PREFERENCES_NUMBER = "myNumberForDial";

    //переменная класса SharedPreferences для работы с настройками
    private SharedPreferences mSettings;

    //Объявляем кнопки и элементы глобально для всей активити
    Button mDialButton;
    Button mSaveButton;
    Button mDialCurButton;
    TextView mCurNum;
    EditText mPhoneNoEt;

    //переменные и объекты для таймера
    private int timeForTimer;
    Timer timerForStop;
    TimerTask mTimerTask;


    //здесь будет лежать строка с отформатированным номером для вызова абонента
    private String dial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //дальше пишем свой код

        //Toast.makeText(MainActivity.this, "переходим в onCreate", Toast.LENGTH_SHORT).show();

        //сбрасываем таймер, если вернулись к приложению
        if (timerForStop != null) {
            //если таймер уже был в работе - сбрасываем
            timerForStop.cancel();
            timerForStop = null;

            //Toast.makeText(MainActivity.this, "сброс таймера в onCreate", Toast.LENGTH_SHORT).show();
        }


        //изменяем текст заголовка Активити
        setTitle("" + "Быстрый вызов шлагбаума");

        //инициализируем объекты
        mDialButton = (Button) findViewById(R.id.btn_dial);
        mSaveButton = (Button) findViewById(R.id.btnSave);
        mDialCurButton = (Button) findViewById(R.id.btnCurDial);
        mCurNum = (TextView) findViewById(R.id.curNumText);
        mPhoneNoEt = (EditText) findViewById(R.id.et_phone_no);

        //отлавливаем нажатие кнопки вызова
        mDialButton.setOnClickListener(this);
        mSaveButton.setOnClickListener(this);
        mDialCurButton.setOnClickListener(this);

        //инициализируем переменную для работы с настройками
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        //проверяем, есть ли сохраненный номер "по умолчанию" - если есть - сразу вызываем его
        if (mSettings.contains(APP_PREFERENCES_NUMBER)) {
            String strCurNum = mSettings.getString(APP_PREFERENCES_NUMBER,"");
            //записываем сохраненный номер в соответствующее поле на экране
            if (!TextUtils.isEmpty(strCurNum)) {
                mCurNum.setText("" + strCurNum);
                //активируем кнопку вызова сохраненного номера
                mDialCurButton.setEnabled(true);
                //делаем звонок по ранее сохраненному номеру
                makeCall("tel:" + strCurNum);
            }
        }else {
            Toast.makeText(MainActivity.this, "нет сохраненных настроек", Toast.LENGTH_SHORT).show();
        }

        timeForTimer = 10000;
    }



    @Override
    public void onResume() {
        super.onResume();

        //Toast.makeText(MainActivity.this, "переходим в onResume", Toast.LENGTH_SHORT).show();
        //сбрасываем таймер, если вернулись к приложению
        if (timerForStop != null) {
            //если таймер уже был в работе - сбрасываем
            timerForStop.cancel();
            timerForStop = null;
            //Toast.makeText(MainActivity.this, "сброс таймера в onResume", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        //При жмакании кнопки "назад" в андройде - приложение закрывается, а таймер продолжает тикать, его нужно убирать вручную
        //Toast.makeText(MainActivity.this, "переходим в onDestroy", Toast.LENGTH_SHORT).show();
        //сбрасываем таймер, если вернулись к приложению
        if (timerForStop != null) {
            //если таймер уже был в работе - сбрасываем
            timerForStop.cancel();
            timerForStop = null;
            //Toast.makeText(MainActivity.this, "сброс таймера в onDestroy", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();

        //Toast.makeText(MainActivity.this, "переходим в onStop", Toast.LENGTH_SHORT).show();




        //далее, после того как приложение остановлено, запускаем таймер -по истечение которого завершаем приложение окончательно
        //завершаем приложение для того, чтобы при повторном открытии - происходил вызов абонента.
        if (timerForStop != null) {
            //если таймер уже был в работе - сбрасываем
            timerForStop.cancel();
            timerForStop = null;
            //Toast.makeText(MainActivity.this, "сброс таймера в onStop", Toast.LENGTH_SHORT).show();
        }

        timerForStop = new Timer();
        mTimerTask=new MyTimerTask();

        timerForStop.schedule(mTimerTask, timeForTimer);

    }


    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            finish ();
        }
    }



    public void onClick(View view) {
        //сюда считываем введенный номер
        String phoneNo;
        //проверяем какая кнопка кликнута
        switch (view.getId()){

            case R.id.btn_dial:
                //получаем номер из соответствующего поля
                phoneNo=mPhoneNoEt.getText().toString();
                //если поле для ввода номера не пустое - форматируем, иначы выходим из процедуры
                if(!TextUtils.isEmpty(phoneNo)){
                    dial = "tel:" + phoneNo;
                    makeCall(dial);
                }else{
                    Toast.makeText(MainActivity.this, "Необходимо ввести номер телефона", Toast.LENGTH_SHORT).show();
                };
                break;

            case R.id.btnCurDial:
                //получаем номер из соответствующего поля
                phoneNo=mCurNum.getText().toString();
                if(!TextUtils.isEmpty(phoneNo)) {
                    dial = "tel:" + phoneNo;
                    makeCall(dial);
                } else {
                    Toast.makeText(MainActivity.this, "Нет сохраненных номеров...", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btnSave:
                //получаем номер из соответствующего поля
                phoneNo=mPhoneNoEt.getText().toString();
                if(!TextUtils.isEmpty(phoneNo)) {
                    //сохраняем номер из строки в состояние "по умолчанию"
                    //оздаем объект для работы с сохранениями
                    SharedPreferences.Editor editor = mSettings.edit();
                    editor.putString(APP_PREFERENCES_NUMBER, phoneNo);
                    editor.apply();
                    //сообщенька
                    Toast.makeText(MainActivity.this, "Номер " + phoneNo + " сохранен", Toast.LENGTH_SHORT).show();
                    //отображаем сохраненный номер
                    mCurNum.setText("" + phoneNo);
                    mDialCurButton.setEnabled(true);
                } else {
                    //если сохраняем пустое поле - значит настройки текущего номера будут сброшены
                    mSettings.edit().remove(APP_PREFERENCES_NUMBER).commit();
                    //деактивируем кнопку вызова по умолчанию
                    mDialCurButton.setEnabled(false);
                    mCurNum.setText("" + "+7(___)___-__-__");
                    //сообщенька
                    Toast.makeText(MainActivity.this, "Номер по умолчанию удален", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }



    //делаем отдельную процедуру для вызова абонента по номера
    void makeCall (String receivedNum){

        //проверяем текущий статус разрешения на возможность вызова
        int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

        //при наличии разрешений - делаев вызов
        if (permissionStatus== PackageManager.PERMISSION_GRANTED) {
            startActivity(new Intent(Intent.ACTION_CALL,  Uri.parse(receivedNum)));
        }else{
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_CODE);
        }
    }



    //переопределяем действия на результат запроса разрешения у пользователя
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch (requestCode){
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //если ользователь дал разрешение - повторно выполняем вызов абонента (теперь с действующим разрешением)
                    makeCall(dial);
                }else{
                    //Разрешения не дадено... чо делать незнаю
                }
                return;
        }
    }













}
