package com.example.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class useradapter extends RecyclerView.Adapter<useradapter.userViewHolder> {
    private Context mcontext;
    private ArrayList<handledata>  mhandledata;

    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }

    public useradapter(Context context,ArrayList<handledata> Handledata){
        mcontext=context;
        mhandledata=Handledata;

    }

    @NonNull
    @Override
    public userViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.useritem,parent,false);
        return new userViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull userViewHolder holder, int position) {
        handledata currentItem=mhandledata.get(position);
        String imageUrl=currentItem.getImageURL();
        String name=currentItem.getNamme();
        String Age=currentItem.getmAge();
        String address=currentItem.getAddress();
        holder.Tnamme.setText(name);
        holder.Tgender.setText("Age: "+Age);
        holder.Taddress.setText(address);
        Picasso.get().load(imageUrl).fit().centerInside().into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return mhandledata.size();
    }

    public class userViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView Tnamme;
        public TextView Tgender;
        public TextView Taddress;

        public userViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView);
            Tnamme=itemView.findViewById(R.id.Name);
            Tgender=itemView.findViewById(R.id.gender);
            Taddress=itemView.findViewById(R.id.address);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener!=null){
                        int postion=getAdapterPosition();
                        if(postion!=RecyclerView.NO_POSITION){
                            mListener.onItemClick(postion);
                        }
                    }
                }
            });
        }
    }
}
