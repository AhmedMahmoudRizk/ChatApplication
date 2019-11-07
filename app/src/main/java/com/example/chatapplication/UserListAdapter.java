package com.example.chatapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class UserListAdapter extends BaseAdapter {

    List<User> users = new ArrayList<>();
    Context context;

    public UserListAdapter(Context context) {
        this.context = context;
    }

    public void addItem(User user) {
        this.users.add(user);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserViewHolder holder = new UserViewHolder();
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        User user = users.get(position);

        convertView = messageInflater.inflate(R.layout.custom_item_list, null);
        holder.avatar = convertView.findViewById(R.id.icon);
        holder.name = convertView.findViewById(R.id.name);
        convertView.setTag(holder);
        holder.name.setText(user.getNamee());
        return convertView;
    }

    class UserViewHolder {
        public ImageView avatar;
        public TextView name;
    }
}
