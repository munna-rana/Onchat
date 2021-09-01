
package onchat.in;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;


import onchat.in.Adapters.ChatAdapter;
import onchat.in.Models.MessageModel;
import onchat.in.databinding.ActivityChatDetailBinding;

public class  ChatDetailActivity extends AppCompatActivity {
   ActivityChatDetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding .getRoot());
        getSupportActionBar().hide();

        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();


        //
       final String senderId=auth.getUid();
        String receveId=getIntent().getStringExtra("userId");
        String  userName=getIntent().getStringExtra("userName");
        String  profilePic=getIntent().getStringExtra("profilePic");

        binding.userNamelist.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.profile).into(binding.profileImage);
        binding.userNamelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChatDetailActivity.this,profileActivity.class);
                startActivity(intent);
            }
        });

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChatDetailActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        final ArrayList<MessageModel>messageModels=new ArrayList<>();
        final ChatAdapter chatAdapter=new ChatAdapter(messageModels,this,receveId);



        binding.chatRecyclarView.setAdapter(chatAdapter);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        binding.chatRecyclarView.setLayoutManager(layoutManager);

        final String senderRoom=senderId+receveId;
        final String recieverRoom=receveId+senderId;

        database.getReference().child("chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageModels.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            MessageModel mode1 = snapshot1.getValue(MessageModel.class);
                            mode1.setMessageId(snapshot1.getKey());

                          messageModels.add(mode1);
                        }
                        chatAdapter.notifyDataSetChanged();
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.attachFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String message =binding.etMessage.getText().toString();
               final MessageModel model=new MessageModel(senderId,message);
               binding.etMessage.setText("");



                database.getReference().child("chats")
                        .child(senderRoom)
                        .push()
                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        database.getReference().child("chats")
                                .child(recieverRoom)
                                .push()
                                .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                        binding.Calling.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                            }
                        });
                    }

                });
            }
        });
    }

}