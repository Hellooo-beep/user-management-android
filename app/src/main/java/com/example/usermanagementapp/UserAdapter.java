package com.example.usermanagementapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> userList;
    private Context context;
    private OnUserActionListener listener;

    // Interface for handling user actions (edit, delete)
    public interface OnUserActionListener {
        void onEdit(User user);
        void onDelete(Long userId);
    }

    // Constructor
    public UserAdapter(List<User> userList, Context context, OnUserActionListener listener) {
        this.userList = userList;
        this.context = context;
        this.listener = listener;
    }

    // Creates a new ViewHolder (view for each item)
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate (create) the item layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    // Binds data to each ViewHolder
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.bind(user);
    }

    // Returns total number of items
    @Override
    public int getItemCount() {
        return userList.size();
    }

    // Inner class for individual item views
    public class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView emailTextView;
        private TextView roleTextView;
        private Button editButton;
        private Button deleteButton;
        private User currentUser;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            // Find views by ID
            nameTextView = itemView.findViewById(R.id.itemNameTextView);
            emailTextView = itemView.findViewById(R.id.itemEmailTextView);
            roleTextView = itemView.findViewById(R.id.itemRoleTextView);
            editButton = itemView.findViewById(R.id.itemEditButton);
            deleteButton = itemView.findViewById(R.id.itemDeleteButton);
        }

        public void bind(User user) {
            this.currentUser = user;
            // Set the text for each field
            nameTextView.setText(user.getFirstName() + " " + user.getLastName());
            emailTextView.setText("Email: " + user.getEmail());
            roleTextView.setText("Role: " + user.getRole());

            // Edit button click listener
            editButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEdit(user);
                }
            });

            // Delete button click listener
            deleteButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDelete(user.getId());
                }
            });
        }
    }
}
