package com.example.barbeariahgg.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.barbeariahgg.MainActivity;
import com.example.barbeariahgg.R;
import com.example.barbeariahgg.model.AgendamentoModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class Appointment extends AppCompatActivity {

    private Button buttonDatePicker;
    private Button  btn_agendar;
    private RadioGroup rg_grupo;
    private Spinner spinner_horario;
    private DatePickerDialog datePickerDialog;
    private ArrayList<String> list ;
    private ArrayAdapter<String> adapter ;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        initDatePicker();

        list = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, R.layout.list_horarios, list);
        initHorarios();
        verificaDisponibilidade();
        buttonDatePicker = findViewById(R.id.buttonDatePicker);
        buttonDatePicker.setText(getTodaysDate());
        btn_agendar = findViewById(R.id.btn_agendar);
        spinner_horario = findViewById(R.id.spinner_horario);
        spinner_horario.setAdapter(adapter);

        btn_agendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                AgendamentoModel agendamento = new AgendamentoModel();

                agendamento.setId_cliente(currentUser.getUid());
                agendamento.setData(buttonDatePicker.getText().toString());
                agendamento.setHorario(spinner_horario.getSelectedItem().toString());

                agendamento.salvar();
                Toast.makeText(Appointment.this, "Agendamento efetuado com sucesso!", Toast.LENGTH_SHORT).show();
                abrirTelaPrincipal();
            }
        });
    }

    private void initHorarios() {
        list.add("09:00");
        list.add("10:00");
        list.add("11:00");
        list.add("12:00");
        list.add("13:00");
        list.add("14:00");
        list.add("15:00");
        list.add("16:00");
        list.add("17:00");
        list.add("18:00");
    }

    private void abrirTelaPrincipal() {
        Intent intent = new Intent(Appointment.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private String getTodaysDate() {
        Calendar calendar = Calendar.getInstance();
        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        mes += 1;
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        return makeDateString(dia,mes,ano);
    }

    public void initDatePicker(){

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = makeDateString(dayOfMonth,month,year);
                buttonDatePicker.setText(date);
                list.clear();
                initHorarios();
                verificaDisponibilidade();
            }
        };

        Calendar calendar = Calendar.getInstance();
        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, ano, mes, dia);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
    }

    private void verificaDisponibilidade() {
        DatabaseReference referenceAgendamento = FirebaseDatabase.getInstance().getReference().child("agendamentos");
        referenceAgendamento.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                for(DataSnapshot snapshot : datasnapshot.getChildren()){
                    if (buttonDatePicker.getText().toString().equals(snapshot.child("data").getValue().toString())){
                        for (int i=0; i<list.size();i++){
                            if (list.get(i).equals(snapshot.child("horario").getValue().toString())){
                                list.remove(i);
                            }
                        }
                    }
                }
                Collections.sort(list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String makeDateString(int dayOfMonth, int month, int year) {
        return dayOfMonth + " " + getMonthFormat(month) + " " + year;
    }

    private String getMonthFormat(int month) {
        switch (month){
            case 1:
                return "JAN";
            case 2:
                return "FEV";
            case 3:
                return "MAR";
            case 4:
                return "ABR";
            case 5:
                return "MAI";
            case 6:
                return "JUN";
            case 7:
                return "JUL";
            case 8:
                return "AGO";
            case 9:
                return "SET";
            case 10:
                return "OUT";
            case 11:
                return "NOV";
            case 12:
                return "DEZ";

            default:
                return "JAN";
        }
    }

    public void openDataPicker(View view) {
        datePickerDialog.show();
    }
}