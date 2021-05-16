package com.example.barbeariahgg.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AgendamentoModel {

    private String id;
    private String id_cliente;
    private String data;
    private String horario;


    public AgendamentoModel(String id, String id_cliente, String data, String horario) {
        this.id = id;
        this.id_cliente = id_cliente;
        this.data = data;
        this.horario = horario;
    }

    public AgendamentoModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(String id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public  void salvar(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("agendamentos").push().setValue(this);
    }
}
