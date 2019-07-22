package com.example.instargram.Adapter;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.instargram.Fragments.PostDetailFragment;
import com.example.instargram.Fragments.ProfileFragment;
import com.example.instargram.Model.Post;
import com.example.instargram.Model.User;
import com.example.instargram.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.instargram.Model.Notification;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context mContext;
    private List<Notification> mNotification;


    public NotificationAdapter(Context mContext, List<Notification> mNotification) {
        this.mContext = mContext;
        this.mNotification = mNotification;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notification_item, viewGroup, false);
        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewholder, int i) {


        final Notification notification = mNotification.get(i);
        viewholder.text.setText(notification.getText());

        getUserInfo(viewholder.image_profile, viewholder.username, notification.getUserid());

        if (notification.isIspost()) {
            viewholder.post_image.setVisibility(View.VISIBLE);
            getPostImage(viewholder.post_image, notification.getPostid());
        } else {
            viewholder.post_image.setVisibility(View.GONE);
        }

        viewholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (notification.isIspost()) {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    editor.putString("postid", notification.getPostid());
                    editor.apply();

                    ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new PostDetailFragment()).commit();
                } else {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    editor.putString("profileid", notification.getUserid());
                    editor.apply();

                    ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ProfileFragment()).commit();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNotification.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView image_profile,post_image;
        public TextView username,text;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_profile = itemView.findViewById(R.id.image_profile);
            post_image = itemView.findViewById(R.id.post_image);
            username = itemView.findViewById(R.id.username);
            text = itemView.findViewById(R.id.comment);

        }
    }

    private void getUserInfo(final ImageView imageView, final TextView username, String publisherid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(publisherid);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Glide.with(mContext).load(user.getImageurl()).into(imageView);
                username.setText(user.getUsername());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getPostImage(final ImageView post_image, String postid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Posts").child(postid);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Post post = dataSnapshot.getValue(Post.class);
                Glide.with(mContext).load(post.getPostimage()).into(post_image);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
