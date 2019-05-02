package com.example.cf17inigoreal.jda_uf2_examen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class listActivity extends AppCompatActivity {
    private RecyclerView recycledView;
    private LlistaAdapter adapter;
    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabase;
    private List<UploadElement> elements;
    private Bitmap bitmap;
    private MediaPlayer mediaPlayer;
    private Boolean checked=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        if(!checked) {
            playSong("https://firebasestorage.googleapis.com/v0/b/jdauf2examen-cefbe.appspot.com/o/SampleAudio_0.4mb.mp3?alt=media&token=cd98e03b-8e7d-49f1-89c1-7cd34f59f38f");
        }
        adapter = new LlistaAdapter();

        recycledView = findViewById(R.id.recycledViewId);
        recycledView.setLayoutManager(new LinearLayoutManager(this));

        elements = new ArrayList<>();
        recycledView.setAdapter(adapter);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference().child("elements").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {


                UploadElement uploadElement = dataSnapshot.getValue(UploadElement.class);
                elements.add(uploadElement);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        firebaseStorage = FirebaseStorage.getInstance();
    }

    public class LlistaAdapter extends RecyclerView.Adapter<LlistaAdapter.LlistaViewHolder> {

        @Override
        public LlistaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view =  getLayoutInflater().inflate(R.layout.list_items,viewGroup,false);

            return new LlistaViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final LlistaViewHolder llistaViewHolder, int i) {
            final UploadElement uploadElement = elements.get(i);
            llistaViewHolder.nameImage.setText(uploadElement.getDescIncUpload());
            llistaViewHolder.descImage.setText(uploadElement.getAulaIncUpload());

            firebaseStorage.getReferenceFromUrl(uploadElement.getUrlImageUpload())
                    .getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    llistaViewHolder.fotoImage.setImageBitmap(bitmap);
                }
            });
        }


        @Override
        public int getItemCount() {
            return elements.size();
        }

        public class LlistaViewHolder extends RecyclerView.ViewHolder{
            public TextView nameImage,descImage;
            public ImageView fotoImage, checkImage;
            public CheckBox checkBoxList;

            public LlistaViewHolder(View itemView) {
                super(itemView);
                nameImage = itemView.findViewById(R.id.aulaIncList);
                descImage = itemView.findViewById(R.id.descIncList);
                fotoImage = itemView.findViewById(R.id.fotoImageList);
                checkImage = itemView.findViewById(R.id.crossImageList);
                checkBoxList = itemView.findViewById(R.id.checkBoxList);

                if(!checkBoxList.isChecked()) {
                    checkImage.animate().alpha(0);
                    checkImage.animate().translationX((float) 1000);
                }
                checkBoxList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(checkBoxList.isChecked()){
                            checkImage.animate().alpha(1);
                            checkImage.animate().translationX(0);

                        }
                        else{
                            checkImage.animate().alpha(0);
                            checkImage.animate().translationX((float) 1000);

                        }


                    }
                });
            }
        }

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                    if(mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        checked=false;
                    }
                    else{
                        mediaPlayer.start();
                        checked=true;
                    }

                return true;
            case R.id.item2:
                openActivity();

                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }
    private void openActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    public void playSong(String url){
        mediaPlayer = new MediaPlayer();

        try{
            mediaPlayer.setDataSource(url);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            mediaPlayer.prepare();
        }catch (Exception e){
            e.printStackTrace();
        }
        checked=true;
        mediaPlayer.setLooping(true);
    }
}