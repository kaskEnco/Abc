package com.kasksolutions.lyricist.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.kasksolutions.lyricist.R;
import com.kasksolutions.lyricist.allclasses.SingleYear;

import java.util.ArrayList;
import java.util.HashMap;

public class YearsSeeAllAdapter extends RecyclerView.Adapter<YearsSeeAllAdapter.SeeAllView>
{
    private ArrayList alSeeAll;
    private Context contextSeeAll;

    private int lastPosition = -1;
    String yearsKeys[]={"yearId","lyircYear"};

    public YearsSeeAllAdapter(ArrayList alSeeAll, Context contextSeeAll) {
        this.alSeeAll = alSeeAll;
        this.contextSeeAll = contextSeeAll;
    }

    @Override
    public SeeAllView onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v=LayoutInflater.from(contextSeeAll).inflate(R.layout.years_seeall_style,null);

        return new SeeAllView(v);
    }

    @Override
    public void onBindViewHolder(final SeeAllView holder, int position)
    {
        HashMap hm=(HashMap)alSeeAll.get(position);

        int id=(Integer)hm.get(yearsKeys[0]);
        final int year=(Integer)hm.get(yearsKeys[1]);

        String n=Integer.toString(year);

        String a=n.substring(0,2);
        String b=n.substring(2,4);

        Log.d("yearStyle",""+a+"\t"+b);

        holder.yearsTv.setText(""+a+"\n"+b);
        holder.cardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(contextSeeAll,SingleYear.class);
                intent.putExtra("yearOnly",year);
                contextSeeAll.startActivity(intent);
            }
        });
        setAnimation(holder.itemView,position);
    }

    @Override
    public int getItemCount()
    {
        return alSeeAll.size();
    }

    public class SeeAllView extends RecyclerView.ViewHolder
    {
       TextView yearsTv;
        CardView cardView;

        public SeeAllView(View view)
        {

            super(view);
            //showYear=itemView.findViewById(R.id.yearsId);
            cardView=itemView.findViewById(R.id.cardyears);
            yearsTv=itemView.findViewById(R.id.yearsTv);
        }
    }
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(contextSeeAll,R.anim.latest_anim);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
