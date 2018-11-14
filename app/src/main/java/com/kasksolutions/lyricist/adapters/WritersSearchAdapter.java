package com.kasksolutions.lyricist.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kasksolutions.lyricist.R;
import com.kasksolutions.lyricist.allclasses.SingleWriter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by home on 28-Mar-18.
 */

public class WritersSearchAdapter extends RecyclerView.Adapter<WritersSearchAdapter.WritersSearchViewHolder>
{
    Context writersContext;
    ArrayList writersAl;

    public WritersSearchAdapter(Context writersContext, ArrayList writersAl) {
        this.writersContext = writersContext;
        this.writersAl = writersAl;
    }

    @Override
    public WritersSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_writer_name_card_style, parent, false);
        return  new WritersSearchViewHolder(v);
    }

    @Override
    public void onBindViewHolder(WritersSearchViewHolder holder, int position)
    {
        HashMap hm=(HashMap)writersAl.get(position);

        final String writerName=(String)hm.get("100");
      //  int movieId=(Integer) hm.get("image");

        String WRITERNAME=writerName.replace(" ","_");
        final String imageUrl= "https://s3.amazonaws.com/lyricist/"+WRITERNAME+".jpg";
        Glide.with(writersContext).
                load(imageUrl).
                into(holder.writerImage);


        holder.writerName.setText(writerName);

        holder.writerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                Intent i=new Intent(writersContext,SingleWriter.class);
                i.putExtra("writerName",writerName);
                i.putExtra("writerImage",imageUrl);
                writersContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount()
    {

        return Math.min(writersAl.size(),3);
    }

    public class WritersSearchViewHolder extends RecyclerView.ViewHolder
    {
        private TextView writerName;
        private ImageView writerImage;
        private CardView writerCard;
        public WritersSearchViewHolder(View itemView)
        {
            super(itemView);
            writerName=itemView.findViewById(R.id.search_writer_name);
            writerImage=itemView.findViewById(R.id.search_writer_imageView);
            writerCard=itemView.findViewById(R.id.search_writer_card);

        }
    }
}
