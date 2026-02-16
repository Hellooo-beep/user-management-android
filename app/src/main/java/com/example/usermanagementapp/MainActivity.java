package com.example.usermanagementapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements UserAdapter.OnUserActionListener {

    // UI Components
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText roleEditText;
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
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        roleEditText = findViewById(R.id.roleEditText);
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
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String role = roleEditText.getText().toString().trim();

        // Validation
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || role.isEmpty()) {
            showToast("Please fill all fields");
            return;
        }

        // Create user object
        User newUser = new User(firstName, lastName, email, password, role);

        // Make API call
        apiService.createUser(newUser).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    showToast("User added successfully!");
                    clearFields();
                    fetchAllUsers(); // Refresh list
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        showToast("Error: " + errorBody);
                    } catch (IOException e) {
                        showToast("Error adding user: " + response.code());
                    }
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

        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String role = roleEditText.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || role.isEmpty()) {
            showToast("Please fill all fields");
            return;
        }

        User updatedUser = new User(editingUserId, firstName, lastName, email, password, role);

        apiService.updateUser(editingUserId, updatedUser).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    showToast("User updated successfully!");
                    clearFields();
                    editingUserId = null;
                    fetchAllUsers();
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        showToast("Error: " + errorBody);
                    } catch (IOException e) {
                        showToast("Error updating user: " + response.code());
                    }
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
        firstNameEditText.setText("");
        lastNameEditText.setText("");
        emailEditText.setText("");
        passwordEditText.setText("");
        roleEditText.setText("");
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
        firstNameEditText.setText(user.getFirstName());
        lastNameEditText.setText(user.getLastName());
        emailEditText.setText(user.getEmail());
        passwordEditText.setText("");
        roleEditText.setText(user.getRole());

        // Store the user ID for update
        editingUserId = user.getId();

        // Show message
        showToast("Editing user: " + user.getFirstName());
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
                    try {
                        String errorBody = response.errorBody().string();
                        showToast("Error: " + errorBody);
                    } catch (IOException e) {
                        showToast("Error deleting user: " + response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                showToast("Failed to delete user: " + t.getMessage());
            }
        });
    }
}
