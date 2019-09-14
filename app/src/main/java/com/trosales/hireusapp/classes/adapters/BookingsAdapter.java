package com.trosales.hireusapp.classes.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trosales.hireusapp.R;
import com.trosales.hireusapp.classes.wrappers.BookingsDO;

import java.util.List;

public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.ViewHolder>{
    private List<BookingsDO> bookingsDOList;
    private Context context;

    public BookingsAdapter(List<BookingsDO> bookingsDOList, Context context) {
        this.bookingsDOList = bookingsDOList;
        this.context = context;
    }

    @NonNull
    @Override
    public BookingsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.talents_list, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingsAdapter.ViewHolder viewHolder, int i) {
        final BookingsDO bookingsDO = bookingsDOList.get(i);
        //insert logic here
    }

    @Override
    public int getItemCount() {
        return bookingsDOList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
