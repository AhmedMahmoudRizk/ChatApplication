package com.example.chatapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MessageAdapter extends BaseAdapter {

    List<Message> messages = new ArrayList<Message>();
    Context context;

    public MessageAdapter(Context context) {
        this.context = context;
    }


    public void add(Message message) {
        this.messages.add(message);
        notifyDataSetChanged();
    }

    public void removeIfTyping() {
        if (messages.size() > 0 && messages.get(messages.size() - 1).getText() == null && messages.get(messages.size() - 1).isTyping()) {
            this.messages.remove(messages.size() - 1);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        MessageViewHolder holder = new MessageViewHolder();
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Message message = messages.get(i);

        if (message.isBelongsToCurrentUser()) {
            convertView = messageInflater.inflate(R.layout.my_message, null);
            holder.messageBody = convertView.findViewById(R.id.message_body);
            holder.date = convertView.findViewById(R.id.date);
            convertView.setTag(holder);
            holder.messageBody.setText(message.getText());
//            holder.date.setText(message.getDate());
        } else {
            if (!message.isTyping() || message.getText() != null) {
                convertView = messageInflater.inflate(R.layout.their_message, null);
                holder.avatar = convertView.findViewById(R.id.avatar);
                holder.name = convertView.findViewById(R.id.name);
                holder.messageBody = convertView.findViewById(R.id.message_body);
                holder.date = convertView.findViewById(R.id.date);
                convertView.setTag(holder);

                holder.name.setText(message.getMemberData().getName());
                holder.messageBody.setText(message.getText());
//            holder.date.setText(message.getDate());
                GradientDrawable drawable = (GradientDrawable) holder.avatar.getBackground();
//            drawable.setColor(Color.parseColor(message.getMemberData().getColor()));
            } else {

                convertView = messageInflater.inflate(R.layout.typing, null);
                holder.avatar = convertView.findViewById(R.id.avatar);
                holder.name = convertView.findViewById(R.id.name);
                convertView.setTag(holder);
                holder.name.setText(message.getMemberData().getName());
                GradientDrawable drawable = (GradientDrawable) holder.avatar.getBackground();
            }
        }

        return convertView;
    }

}

class MessageViewHolder {
    public View avatar;
    public TextView name;
    public TextView messageBody;
    public TextView date;
}