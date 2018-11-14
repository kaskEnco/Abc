package com.kasksolutions.lyricist.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kasksolutions.lyricist.R;
import com.kasksolutions.lyricist.allclasses.SingleYear;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by home on 22-Feb-18.
 */

public class YearsRecAdapter extends RecyclerView.Adapter<YearsRecAdapter.YearsViewHolder>
{
    ArrayList YearsAl;
    Context YearsContext;

    String yearsKeys[]={"yearId","lyircYear"};
    Activity activity123;


    public YearsRecAdapter(ArrayList yearsAl, Context yearsContext)
    {
        YearsAl = yearsAl;
        YearsContext = yearsContext;
    }

    @Override
    public YearsViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater li=LayoutInflater.from(YearsContext);

        //create a new layout for cardview

        View v1=li.inflate(R.layout.years_card_style,null);


        return  new YearsViewHolder(v1);
    }

    @Override
    public void onBindViewHolder(YearsViewHolder holder, int position)
    {
        HashMap hm=(HashMap)YearsAl.get(position);

        int yearId=(Integer)hm.get(yearsKeys[0]);
        final int lyricYear=(Integer)hm.get(yearsKeys[1]);

        String n=Integer.toString(lyricYear);

        String a=n.substring(0,2);
        String b=n.substring(2,4);

            holder.yearsTv.setText(a+"\n"+b);

            holder.cardView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {

                    Intent intent=new Intent(YearsContext,SingleYear.class);
                    intent.putExtra("yearOnly",lyricYear);

                    YearsContext.startActivity(intent);

                }
            });
    }

    @Override
    public int getItemCount()
    {
        return YearsAl.size();
    }

    public class YearsViewHolder extends RecyclerView.ViewHolder
    {


        CardView cardView;
        TextView yearsTv;
        public YearsViewHolder(View itemView)
        {
            super(itemView);
            yearsTv=itemView.findViewById(R.id.yearsTv);
            cardView=itemView.findViewById(R.id.cardyears);
        }
    }
}
