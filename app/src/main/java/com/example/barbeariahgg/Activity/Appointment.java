package com.example.barbeariahgg.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.barbeariahgg.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

public class Appointment extends AppCompatActivity {

    private DatePicker date_agendamento;
    private Button  btn_agendar;
    private RadioGroup rg_grupo;
    private ListView lst_horarios;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final ArrayList<String> list = new ArrayList<>();
        final ArrayList<String> list2 = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_horarios, list);
        int dia;
        int mes;
        int ano;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        date_agendamento = findViewById(R.id.date_agendamento);
        btn_agendar = findViewById(R.id.btn_agendar);
        lst_horarios = findViewById(R.id.lst_horarios);

        lst_horarios.setAdapter(adapter);

        dia = date_agendamento.getDayOfMonth();
        mes = date_agendamento.getMonth();
        ano = date_agendamento.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(ano, mes, dia);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String formatedDate = sdf.format(calendar.getTime());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("horarios_disponiveis");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                list.clear();
                for(DataSnapshot snapshot : datasnapshot.getChildren()){
                    list.add(snapshot.child("hora").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("agendamento");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                for(DataSnapshot snapshot : datasnapshot.getChildren()){
                    if (formatedDate == snapshot.child("data").getValue().toString()){
                     for(int i=0; i<list.size();i++){
                        if (snapshot.child("horario").getValue().toString() == list.get(i) ){
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
}