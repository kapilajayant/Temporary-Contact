package com.example.temporarycontact.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.temporarycontact.R;
import de.hdodenhof.circleimageview.CircleImageView;
import com.example.temporarycontact.Model.TempContact;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ViewHolder> {

    List<TempContact> tempContactList;
    Context mContext;

    public ContactsAdapter(List<TempContact> tempContactList, Context context) {
        this.tempContactList = tempContactList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.contact_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_name.setText(tempContactList.get(position).getContactName());
        holder.tv_number.setText(tempContactList.get(position).getContactNumber());
    }

    @Override
    public int getItemCount() {
        return tempContactList.size();
    }

    public void restoreItem(TempContact item, int position) {
        tempContactList.add(position, item);
        notifyItemInserted(position);
    }

}
class ViewHolder extends RecyclerView.ViewHolder {

    public TextView tv_name, tv_number;
    public CircleImageView iv;

    public ViewHolder(final View itemView) {
        super(itemView);
        tv_name = itemView.findViewById(R.id.tv_name);
        tv_number = itemView.findViewById(R.id.tv_number);
        iv = itemView.findViewById(R.id.iv);
    }



}

