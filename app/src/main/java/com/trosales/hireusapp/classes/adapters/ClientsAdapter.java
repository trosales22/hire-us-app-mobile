package com.trosales.hireusapp.classes.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.trosales.hireusapp.R;
import com.trosales.hireusapp.classes.wrappers.ClientsBookedDO;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClientsAdapter extends RecyclerView.Adapter<ClientsAdapter.ViewHolder>{
    private List<ClientsBookedDO> clientsBookedDOList;
    private Context context;

    public ClientsAdapter(List<ClientsBookedDO> clientsBookedDOList, Context context) {
        this.clientsBookedDOList = clientsBookedDOList;
        this.context = context;
    }

    @NonNull
    @Override
    public ClientsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.clients_list, viewGroup, false);

        return new ViewHolder(view);
    }

    private String getPaymentOption(String paymentOption){
        String returnValue = null;
        switch (paymentOption){
            case "DEBIT_CREDIT_CARD":
                returnValue = "Debit/Credit Card";
                break;
            case "BANK_TRANSFER":
                returnValue = "Bank Transfer";
                break;
            case "BANK_DEPOSIT":
                returnValue = "Bank Deposit";
                break;

        }
        return returnValue;
    }

    @Override
    public void onBindViewHolder(@NonNull ClientsAdapter.ViewHolder viewHolder, int i) {
        final ClientsBookedDO clientsBookedDO = clientsBookedDOList.get(i);

        int clientDisplayPhoto = 0;

        switch (clientsBookedDO.getClientDetailsDO().getClientRoleCode()){
            case "CLIENT_COMPANY":
                clientDisplayPhoto = R.drawable.customer_company;
                break;
            case "CLIENT_INDIVIDUAL":
                switch (clientsBookedDO.getClientDetailsDO().getClientGender()){
                    case "Male":
                        clientDisplayPhoto = R.drawable.customer_male;
                        break;
                    case "Female":
                        clientDisplayPhoto = R.drawable.customer_female;
                        break;
                }

                break;
        }

        Glide
                .with(context)
                .load(clientDisplayPhoto)
                .placeholder(R.drawable.customer_company)
                .into(viewHolder.imgClientDisplayPhoto);

        viewHolder.lblClientName.setText(clientsBookedDO.getClientDetailsDO().getClientFullname());
        viewHolder.lblClientType.setText(clientsBookedDO.getClientDetailsDO().getClientRoleName());
        viewHolder.lblClientSelectedTime.setText(clientsBookedDO.getSelectedTime());
        viewHolder.lblClientSelectedDate.setText(clientsBookedDO.getSelectedDate());

        StringBuilder sbBookingOtherDetails = new StringBuilder();
        sbBookingOtherDetails
                .append("Payment Option: ")
                .append(getPaymentOption(clientsBookedDO.getSelectedPaymentOption()))
                .append("\nDate Paid: ")
                .append(clientsBookedDO.getDatePaid())
                .append("\nTotal Amount: ")
                .append(Html.fromHtml("&#8369;"))
                .append(clientsBookedDO.getTotalAmount());

        viewHolder.lblBookingOtherDetails.setText( sbBookingOtherDetails.toString());
    }

    @Override
    public int getItemCount() {
        return clientsBookedDOList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgClientDisplayPhoto) ImageView imgClientDisplayPhoto;
        @BindView(R.id.lblClientName) TextView lblClientName;
        @BindView(R.id.lblClientType) TextView lblClientType;
        @BindView(R.id.lblClientSelectedTime) TextView lblClientSelectedTime;
        @BindView(R.id.lblClientSelectedDate) TextView lblClientSelectedDate;
        @BindView(R.id.lblBookingOtherDetails) TextView lblBookingOtherDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
