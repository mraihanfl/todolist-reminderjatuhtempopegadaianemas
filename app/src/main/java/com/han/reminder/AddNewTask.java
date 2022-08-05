package com.han.reminder;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "AddNewTask";

    private TextView setDatePegadaian, tvOnceTime, tvOnceDate, ibOnceTime, ibOnceDate, etOnceMessage;

    private EditText mTaskEdit, mTaskEdit2, mTaskEdit3, mTaskEdit4, mTaskEdit5, mTaskEdit6, etB1, etB2, etB3, etB4;
    private Button mSaveBtn, mHitung, btnSetOnceAlarm, btnCancelRepeatingAlarm;
    private FirebaseFirestore firestore;
    public Context context;
    private String DatePegadaian = "";
    private String DateJatuhTempo = "";
    private String JamJatuhTempo = "";
    private String id = "";
    private String DatePegadaianUpdate = "";
    int inputkemas, inputhde, inputberatbersih, hasilPG;
    double hasilUjroh, hasilB1, hasilB2, hasilB3, hasilB4;

    private AlarmReceiver alarmReceiver;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int mHourRepeat, mMinuteRepeat;


    public static AddNewTask newInstance(){
        return new AddNewTask();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_new_task , container , false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setDatePegadaian = view.findViewById(R.id.set_due_tv);
        mTaskEdit = view.findViewById(R.id.task_edittext);
        mTaskEdit2 = view.findViewById(R.id.task_edittext2);
        mTaskEdit3 = view.findViewById(R.id.task_edittext3);
        mTaskEdit4 = view.findViewById(R.id.task_edittext4);
        mTaskEdit5 = view.findViewById(R.id.task_edittext5);
        mTaskEdit6 = view.findViewById(R.id.task_edittext6);
        etB1 = view.findViewById(R.id.et_B1);
        etB2 = view.findViewById(R.id.et_B2);
        etB3 = view.findViewById(R.id.et_B3);
        etB4 = view.findViewById(R.id.et_B4);
        mSaveBtn = view.findViewById(R.id.save_btn);
        mHitung = view.findViewById(R.id.hitung);

        etOnceMessage = view.findViewById(R.id.et_once_message);
        tvOnceTime = view.findViewById(R.id.tv_once_time);
        tvOnceDate = view.findViewById(R.id.tv_once_date);
        ibOnceTime = view.findViewById(R.id.ib_once_time);
        ibOnceDate = view.findViewById(R.id.ib_once_date);


        firestore = FirebaseFirestore.getInstance();

        alarmReceiver = new AlarmReceiver();

        Calendar calendarJT = Calendar.getInstance();
        mYear = calendarJT.get(Calendar.YEAR);
        mMonth = calendarJT.get(Calendar.MONTH);
        mDay = calendarJT.get(Calendar.DAY_OF_MONTH);
        mHour = calendarJT.get(Calendar.HOUR_OF_DAY);
        mMinute = calendarJT.get(Calendar.MINUTE);




        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if (bundle != null){
            isUpdate = true;
            String task = bundle.getString("nasabah");
            id = bundle.getString("id");
            DatePegadaianUpdate = bundle.getString("tanggalpegadaian");
            String DateJatuhTempoUpdate = bundle.getString("tanggalJatuhTempo");
            String karataseUpdate = bundle.getString("karatase");
            String hdeUpdate = bundle.getString("hde");
            String beratbersihUpdate = bundle.getString("beratbersih");
            String pinjamangadaiUpdate = bundle.getString("pinjamangadai");
            String ujrohper4bulanUpdate = bundle.getString("ujrohper4bulan");
            String ujrohB1Update = bundle.getString("ujrohB1");
            String ujrohB2Update = bundle.getString("ujrohB2");
            String ujrohB3Update = bundle.getString("ujrohB3");
            String ujrohB4Update = bundle.getString("ujrohB4");
            String jamJatuhTempoUpdate = bundle.getString("jamJatuhTempo");
            String pesanJatuhTempoUpdate = bundle.getString("pesanJatuhTempo");

            mTaskEdit.setText(task);
            setDatePegadaian.setText(DatePegadaianUpdate);
            mTaskEdit2.setText(karataseUpdate);
            mTaskEdit3.setText(hdeUpdate);
            mTaskEdit4.setText(beratbersihUpdate);
            mTaskEdit5.setText(pinjamangadaiUpdate);
            mTaskEdit6.setText(ujrohper4bulanUpdate);
            etB1.setText(ujrohB1Update);
            etB2.setText(ujrohB2Update);
            etB3.setText(ujrohB3Update);
            etB4.setText(ujrohB4Update);
            etB1.setText(ujrohB1Update);
            tvOnceDate.setText(DateJatuhTempoUpdate);
            tvOnceTime.setText(jamJatuhTempoUpdate);
            etOnceMessage.setText(pesanJatuhTempoUpdate);

            if (task.length() > 0){
                mSaveBtn.setEnabled(false);
                mSaveBtn.setBackgroundColor(Color.GRAY);
            }


        }

        mTaskEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")){
                    mSaveBtn.setEnabled(false);
                    mSaveBtn.setBackgroundColor(Color.GRAY);
                }else{
                    mSaveBtn.setEnabled(true);
                    mSaveBtn.setBackgroundColor(getResources().getColor(R.color.greenbsi));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mTaskEdit2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")){
                    mSaveBtn.setEnabled(false);
                    mSaveBtn.setBackgroundColor(Color.GRAY);
                }else{
                    mSaveBtn.setEnabled(true);
                    mSaveBtn.setBackgroundColor(getResources().getColor(R.color.greenbsi));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mTaskEdit3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")){
                    mSaveBtn.setEnabled(false);
                    mSaveBtn.setBackgroundColor(Color.GRAY);
                }else{
                    mSaveBtn.setEnabled(true);
                    mSaveBtn.setBackgroundColor(getResources().getColor(R.color.greenbsi));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        setDatePegadaian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                int MONTH = calendar.get(Calendar.MONTH);
                int YEAR = calendar.get(Calendar.YEAR);
                int DAY = calendar.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        setDatePegadaian.setText(dayOfMonth + "/" + month + "/" + year);
                        DatePegadaian = dayOfMonth + "/" + month + "/" + year;
                     }
                } , YEAR, MONTH, DAY);

                datePickerDialog.show();
            }
        });

        mHitung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mTaskEdit2.getText().toString().length() == 0) {
                    Toast.makeText(context, "Karatase Emas Kosong!", Toast.LENGTH_SHORT).show();
                    dismiss();
                } else if (mTaskEdit3.getText().toString().length() == 0) {
                    Toast.makeText(context, "HDE Kosong!", Toast.LENGTH_SHORT).show();
                    dismiss();
                } else if (mTaskEdit4.getText().toString().length() == 0) {
                    Toast.makeText(context, "Berat Bersih Kosong!", Toast.LENGTH_SHORT).show();
                    dismiss();
                } else {

                    inputkemas = Integer.parseInt(mTaskEdit2.getText().toString());
                    inputhde = Integer.parseInt(mTaskEdit3.getText().toString());
                    inputberatbersih = Integer.parseInt(mTaskEdit4.getText().toString());


                    hasilPG = (inputhde * inputberatbersih) - inputhde;
                    hasilUjroh = (inputhde * inputberatbersih) * 0.0144 * 4;

                    hasilB1 = hasilUjroh / 4;
                    hasilB2 = hasilB1 * 2;
                    hasilB3 = hasilB2 + hasilB1;
                    hasilB4 = hasilUjroh;

                    String Hujroh = String.format("%.2f", hasilUjroh);
                    String HUB1 = String.format("%.2f", hasilB1);
                    String HUB2 = String.format("%.2f", hasilB2);
                    String HUB3 = String.format("%.2f", hasilB3);
                    String HUB4 = String.format("%.2f", hasilB4);

                    mTaskEdit5.setText("" + hasilPG + "");
                    mTaskEdit6.setText("" + Hujroh + "");
                    etB1.setText("" + HUB1 + "");
                    etB2.setText("" + HUB2 + "");
                    etB3.setText("" + HUB3 + "");
                    etB4.setText("" + HUB4 + "");

                }

            }
        });



        ibOnceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tvOnceDate.setText(String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth));
                        mYear = year;
                        mMonth = month;
                        mDay = dayOfMonth;
                        DateJatuhTempo = dayOfMonth + "/" + month + "/" + year;
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        ibOnceTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tvOnceTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                        mHour = hourOfDay;
                        mMinute = minute;
                        JamJatuhTempo = hourOfDay + "/" + minute;
                    }
                }, mHour, mMinute, true);
                timePickerDialog.show();
            }
        });




        boolean finalIsUpdate = isUpdate;
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nasabah = mTaskEdit.getText().toString();
                String karatase = mTaskEdit2.getText().toString();
                String hde = mTaskEdit3.getText().toString();
                String beratbersih = mTaskEdit4.getText().toString();
                String pinjamangadai = mTaskEdit5.getText().toString();
                String ujroh = mTaskEdit6.getText().toString();
                String ujrohb1 = etB1.getText().toString();
                String ujrohb2 = etB2.getText().toString();
                String ujrohb3 = etB2.getText().toString();
                String ujrohb4 = etB4.getText().toString();
                String PesanJatuhTempo = etOnceMessage.getText().toString();





                if (finalIsUpdate){
                    firestore.collection("nasabah").document(id).update("nasabah" , nasabah , "tanggalpegadaian" , DatePegadaian , "tanggalJatuhTempo" , DateJatuhTempo , "karatase" , karatase , "hde" , hde , "beratbersih" , beratbersih , "pinjamangadai" , pinjamangadai , "ujrohper4bulan" , ujroh , "ujrohB1" , ujrohb1 , "ujrohB2" , ujrohb2 , "ujrohB3" , ujrohb3 , "ujrohB4" , ujrohb4 , "jamJatuhTempo" , JamJatuhTempo , "pesanJatuhTempo" , PesanJatuhTempo);
                    Toast.makeText(context, "Task Updated", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (nasabah.isEmpty()) {
                        Toast.makeText(context, "Nama Nasabah Tidak boleh kosong!", Toast.LENGTH_SHORT).show(); }
                    else if (karatase.isEmpty()) {
                        Toast.makeText(context, "Karatase Tidak boleh kosong!", Toast.LENGTH_SHORT).show(); }
                    else if (hde.isEmpty()) {
                        Toast.makeText(context, "HDE Tidak boleh kosong!", Toast.LENGTH_SHORT).show(); }
                    else if (beratbersih.isEmpty()) {
                        Toast.makeText(context, "Berat Bersih Tidak boleh kosong!", Toast.LENGTH_SHORT).show(); }
                    else if (pinjamangadai.isEmpty()) {
                        Toast.makeText(context, "Hitung terlebih dahulu!", Toast.LENGTH_SHORT).show(); }
                    else if (DatePegadaian.equalsIgnoreCase("")){
                        Toast.makeText(context, "Pilih Tanggal Pegadaian", Toast.LENGTH_SHORT).show(); }
                    else if (DateJatuhTempo.equalsIgnoreCase("")){
                        Toast.makeText(context, "Pilih Tanggal Jatuh Tempo", Toast.LENGTH_SHORT).show(); }
                    else if (JamJatuhTempo.equalsIgnoreCase("")){
                        Toast.makeText(context, "Pilih Jam Jatuh Tempo", Toast.LENGTH_SHORT).show(); }

                    else {


                        if (tvOnceDate.getText().toString().equalsIgnoreCase("")){
                            Toast.makeText(context, "Date is empty", Toast.LENGTH_SHORT).show();
                        }else if (tvOnceTime.getText().toString().equalsIgnoreCase("")){
                            Toast.makeText(context, "Time is empty", Toast.LENGTH_SHORT).show();
                        }else if (TextUtils.isEmpty(etOnceMessage.getText().toString())){
                            etOnceMessage.setError("Nasabah Tidak boleh kosong!");
                        } else {
                            alarmReceiver.setOneTimeAlarm(context, AlarmReceiver.TYPE_ONE_TIME,
                                    tvOnceDate.getText().toString(), tvOnceTime.getText().toString(),
                                    etOnceMessage.getText().toString());
                        }


                        Map<String, Object> taskMap = new HashMap<>();

                        taskMap.put("nasabah", nasabah);
                        taskMap.put("karatase", karatase);
                        taskMap.put("hde", hde);
                        taskMap.put("beratbersih", beratbersih);
                        taskMap.put("pinjamangadai", pinjamangadai);
                        taskMap.put("ujrohper4bulan", ujroh);
                        taskMap.put("ujrohB1", ujrohb1);
                        taskMap.put("ujrohB2", ujrohb2);
                        taskMap.put("ujrohB3", ujrohb3);
                        taskMap.put("ujrohB4", ujrohb4);
                        taskMap.put("tanggalpegadaian", DatePegadaian);
                        taskMap.put("tanggalJatuhTempo", DateJatuhTempo);
                        taskMap.put("jamJatuhTempo", JamJatuhTempo);
                        taskMap.put("pesanJatuhTempo", PesanJatuhTempo);
                        taskMap.put("status", 0);
                        taskMap.put("time", FieldValue.serverTimestamp());

                        firestore.collection("nasabah").add(taskMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> nasabah) {
                                if (nasabah.isSuccessful()) {
                                    Toast.makeText(context, "Nasabah Saved", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, nasabah.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                dismiss();

            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof  OnDialogCloseListner){
            ((OnDialogCloseListner)activity).onDialogClose(dialog);
        }
    }
}
