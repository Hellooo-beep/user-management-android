package com.example.usermanagementapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    // CREATE - POST to add new user
    @POST("/api/users")
    Call<User> createUser(@Body User user);

    // READ - GET all users
    @GET("/api/users")
    Call<List<User>> getAllUsers();

    // READ - GET user by ID
    @GET("/api/users/{id}")
    Call<User> getUserById(@Path("id") Long id);

    // UPDATE - PUT to update user
    @PUT("/api/users/{id}")
    Call<User> updateUser(@Path("id") Long id, @Body User user);

    // DELETE - DELETE to remove user
    @DELETE("/api/users/{id}")
    Call<Void> deleteUser(@Path("id") Long id);
}