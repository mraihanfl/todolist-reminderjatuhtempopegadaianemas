package com.han.reminder.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.han.reminder.AddNewTask;
import com.han.reminder.HomeActivity;
import com.han.reminder.Model.ToDoModel;
import com.han.reminder.R;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {

    private List<ToDoModel> todolist;
    private HomeActivity activity;
    private FirebaseFirestore firestore;

    public  ToDoAdapter(HomeActivity homeActivity , List<ToDoModel> todolist){
        this.todolist = todolist;
        activity = homeActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.each_task , parent , false);
        firestore = FirebaseFirestore.getInstance();

        return new MyViewHolder(view);
    }

    public Context getContext(){
        return activity;
    }

    public void deleteTask(int position){
        ToDoModel toDoModel = todolist.get(position);
        firestore.collection("nasabah").document(toDoModel.TaskId).delete();
        todolist.remove(position);
        notifyItemRemoved(position);
    }

    public void editTask(int position){
        ToDoModel toDoModel = todolist.get(position);

        Bundle bundle = new Bundle();
        bundle.putString("nasabah" , toDoModel.getNasabah());
        bundle.putString("tanggalpegadaian" , toDoModel.getTanggalpegadaian());
        bundle.putString("tanggalJatuhTempo" , toDoModel.getTanggalJatuhTempo());
        bundle.putString("karatase" , toDoModel.getKaratase());
        bundle.putString("hde" , toDoModel.getHde());
        bundle.putString("beratbersih" , toDoModel.getBeratbersih());
        bundle.putString("pinjamangadai" , toDoModel.getPinjamangadai());
        bundle.putString("ujrohper4bulan" , toDoModel.getUjrohper4bulan());
        bundle.putString("ujrohB1" , toDoModel.getUjrohB1());
        bundle.putString("ujrohB2" , toDoModel.getUjrohB2());
        bundle.putString("ujrohB3" , toDoModel.getUjrohB3());
        bundle.putString("ujrohB4" , toDoModel.getUjrohB4());
        bundle.putString("jamJatuhTempo" , toDoModel.getJamJatuhTempo());
        bundle.putString("pesanJatuhTempo" , toDoModel.getPesanJatuhTempo());

        bundle.putString("id" , toDoModel.TaskId);

        AddNewTask addNewTask = new AddNewTask();
        addNewTask.setArguments(bundle);
        addNewTask.show(activity.getSupportFragmentManager() , addNewTask.getTag());
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoAdapter.MyViewHolder holder, int position) {

        ToDoModel toDoModel = todolist.get(position);
        holder.mCheckBox.setText(toDoModel.getNasabah());
        holder.mDueDateTv.setText("Dari " + toDoModel.getTanggalpegadaian());
        holder.tgljatuhtempo.setText("Sampai " + toDoModel.getTanggalJatuhTempo());
        holder.karataseemas.setText("Emas " + toDoModel.getKaratase() + " Karat");
        holder.beratbersih.setText("BB " + toDoModel.getBeratbersih());
        holder.hde.setText("Rp. " + toDoModel.getHde());
        holder.ujrohb1.setText("B1 " + toDoModel.getUjrohB1() + "-");
        holder.ujrohb2.setText("B2 " + toDoModel.getUjrohB2() + "-");
        holder.ujrohb3.setText("B3 " + toDoModel.getUjrohB3() + "-");
        holder.ujrohb4.setText("B4 " + toDoModel.getUjrohB4());


        holder.mCheckBox.setChecked(toBoolean(toDoModel.getStatus()));

        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    firestore.collection("nasabah").document(toDoModel.TaskId).update("status" , 1);
                }else{
                    firestore.collection("nasabah").document(toDoModel.TaskId).update("status" , 0);
                }
            }
        });

    }

    private boolean toBoolean(int status){
        return status !=0;
    }


    @Override
    public int getItemCount() {
        return todolist.size();
    }

    public class  MyViewHolder extends  RecyclerView.ViewHolder{

        TextView mDueDateTv, karataseemas, beratbersih, hde, tgljatuhtempo, ujrohb1, ujrohb2, ujrohb3, ujrohb4;
        CheckBox mCheckBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mDueDateTv = itemView.findViewById(R.id.due_date_tv);
            mCheckBox = itemView.findViewById(R.id.mcheckbox);
            karataseemas = itemView.findViewById(R.id.karatase_emas);
            beratbersih = itemView.findViewById(R.id.berat_bersih);
            hde = itemView.findViewById(R.id.hde);
            tgljatuhtempo = itemView.findViewById(R.id.tgljatuhtempo);
            ujrohb1 = itemView.findViewById(R.id.ujrohb1);
            ujrohb2 = itemView.findViewById(R.id.ujrohb2);
            ujrohb3 = itemView.findViewById(R.id.ujrohb3);
            ujrohb4 = itemView.findViewById(R.id.ujrohb4);

        }
    }
}
