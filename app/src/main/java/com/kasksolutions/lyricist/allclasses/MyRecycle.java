package com.kasksolutions.lyricist.allclasses;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.kasksolutions.lyricist.R;

import java.util.ArrayList;
import java.util.HashMap;
    
public class MyRecycle extends RecyclerView.Adapter<MyRecycle.MyViewHolder>
{
    Context c;
    ArrayList al;
    String keys[]={"id","name","date"};

    public MyRecycle(Context c, ArrayList al) {
        this.c = c;
        this.al = al;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater li=LayoutInflater.from(c);
        View v1=li.inflate(R.layout.mycard_style,null);
        return  new MyViewHolder(v1);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {

        //By using Hash Map we can get position
        HashMap hm=(HashMap)al.get(position);

        final String id=(String) hm.get(keys[0]);
        final String name=(String) hm.get(keys[1]);
        final String date=(String) hm.get(keys[2]);
        holder.title.setText(name);
        
        //It is URL for Image baed on id
        final String imageUrl= "https://s3.amazonaws.com/liriceapp/"+id+"/"+id+".jpg";


        Glide.with(c)
                .load(imageUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource)
                    {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource)
                    {

                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.thumbnail);


        holder.thumbnail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                holder.thumbnail.buildDrawingCache();


                Intent intent = new Intent(c, Songs.class);


              //  By using Intents we can goto Songs Activity

                intent.putExtra("BitmapImage", imageUrl);
                intent.putExtra("idno",id);
                intent.putExtra("moviename",name);
                intent.putExtra("date123",MainActivity.date);
              //  Log.d("date1234",""+date);
                c.startActivity(intent);

            }
        });
        holder.cardll.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                holder.thumbnail.buildDrawingCache();


                Intent intent = new Intent(c, Songs.class);


                //  By using Intents we can goto Songs Activity

                intent.putExtra("BitmapImage", imageUrl);
                intent.putExtra("idno",id);
                intent.putExtra("moviename",name);
                intent.putExtra("date123",MainActivity.date);
                c.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount()
    {
        return al.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public ProgressBar progressBar;
        public TextView title;
        public ImageView thumbnail;
        public LinearLayout cardll;


        public MyViewHolder(View view)
        {
            super(view);
            title = view.findViewById(R.id.title);
            thumbnail =  view.findViewById(R.id.thumbnail);
            cardll =  view.findViewById(R.id.cardll);
            progressBar=view.findViewById(R.id.indeterminateBar);
        }
    }
}
