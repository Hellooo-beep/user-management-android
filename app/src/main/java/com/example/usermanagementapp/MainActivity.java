package com.example.usermanagementapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements UserAdapter.OnUserActionListener {

    // UI Components
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private Button addButton;
    private Button updateButton;
    private Button fetchButton;
    private RecyclerView usersRecyclerView;

    // Adapter and data
    private UserAdapter userAdapter;
    private List<User> userList;
    private ApiService apiService;

    // For editing
    private Long editingUserId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views (connect Java to XML)
        initializeViews();

        // Set up RecyclerView
        setupRecyclerView();

        // Get API service
        apiService = RetrofitClient.getApiService();

        // Set up button listeners
        setupButtonListeners();

        // Fetch users when app starts
        fetchAllUsers();
    }

    // Initialize all views from XML layout
    private void initializeViews() {
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        addButton = findViewById(R.id.addButton);
        updateButton = findViewById(R.id.updateButton);
        fetchButton = findViewById(R.id.fetchButton);
        usersRecyclerView = findViewById(R.id.usersRecyclerView);
    }

    // Set up RecyclerView with adapter
    private void setupRecyclerView() {
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList, this, this);

        // Set layout manager (how to arrange items)
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        usersRecyclerView.setLayoutManager(layoutManager);

        // Set adapter
        usersRecyclerView.setAdapter(userAdapter);
    }

    // Set up button click listeners
    private void setupButtonListeners() {
        addButton.setOnClickListener(v -> handleAddUser());
        updateButton.setOnClickListener(v -> handleUpdateUser());
        fetchButton.setOnClickListener(v -> fetchAllUsers());
    }

    // HANDLE ADD USER
    private void handleAddUser() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();

        // Validation
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            showToast("Please fill all fields");
            return;
        }

        // Create user object
        User newUser = new User(name, email, phone);

        // Make API call
        apiService.createUser(newUser).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    showToast("User added successfully!");
                    clearFields();
                    fetchAllUsers(); // Refresh list
                } else {
                    showToast("Error adding user");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                showToast("Failed to add user: " + t.getMessage());
            }
        });
    }

    // HANDLE UPDATE USER
    private void handleUpdateUser() {
        if (editingUserId == null) {
            showToast("Please select a user to update");
            return;
        }

        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            showToast("Please fill all fields");
            return;
        }

        User updatedUser = new User(editingUserId, name, email, phone);

        apiService.updateUser(editingUserId, updatedUser).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    showToast("User updated successfully!");
                    clearFields();
                    editingUserId = null;
                    fetchAllUsers();
                } else {
                    showToast("Error updating user");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                showToast("Failed to update user: " + t.getMessage());
            }
        });
    }

    // FETCH ALL USERS
    private void fetchAllUsers() {
        apiService.getAllUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userList.clear();
                    userList.addAll(response.body());
                    userAdapter.notifyDataSetChanged();
                    showToast("Users fetched: " + userList.size());
                } else {
                    showToast("Error fetching users");
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                showToast("Failed to fetch users: " + t.getMessage());
            }
        });
    }

    // CLEAR INPUT FIELDS
    private void clearFields() {
        nameEditText.setText("");
        emailEditText.setText("");
        phoneEditText.setText("");
        editingUserId = null;
    }

    // SHOW TOAST (pop-up message)
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // IMPLEMENT OnUserActionListener methods
    @Override
    public void onEdit(User user) {
        // Fill fields with user data
        nameEditText.setText(user.getName());
        emailEditText.setText(user.getEmail());
        phoneEditText.setText(user.getPhone());

        // Store the user ID for update
        editingUserId = user.getId();

        // Show message
        showToast("Editing user: " + user.getName());
    }

    @Override
    public void onDelete(Long userId) {
        // Show confirmation and delete
        apiService.deleteUser(userId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    showToast("User deleted successfully!");
                    fetchAllUsers(); // Refresh list
                } else {
                    showToast("Error deleting user");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                showToast("Failed to delete user: " + t.getMessage());
            }
        });
    }
}