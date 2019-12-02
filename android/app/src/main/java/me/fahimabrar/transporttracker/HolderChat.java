package me.fahimabrar.transporttracker;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by jonesrandom on 12/26/17.
 *
 * @site www.androidexample.web.id
 * @github @alfianyusufabdullah
 */

public class HolderChat extends RecyclerView.ViewHolder {
    CardView cardFrom, cardTo;
    TextView fromMessage, toMessage;




    public HolderChat(View itemView) {
        super(itemView);
        cardFrom = itemView.findViewById(R.id.cardFrom);
        cardTo = itemView.findViewById(R.id.cardTo);
        fromMessage = itemView.findViewById(R.id.fromMessage);
        toMessage = itemView.findViewById(R.id.toMessage);
    }

    public void setContent(ModelChat chat, String user) {
        if(user!=null)
            System.out.println("user="+user);
        else
            System.out.println("null");

        String User = chat.getUser();

        if(user!=null)
            System.out.println("user2="+User);
        else
            System.out.println("null2");

        if (User.equals(user)) {

            cardTo.setVisibility(View.VISIBLE);
            cardFrom.setVisibility(View.GONE);

            toMessage.setText(chat.getMessage());

        } else {

            cardTo.setVisibility(View.GONE);
            cardFrom.setVisibility(View.VISIBLE);

            fromMessage.setText(chat.getMessage());
        }
    }
}