# User Management Android App

An Android mobile application built with Java and Retrofit that connects to a Spring Boot backend API for user management operations.

## Features

- **Add Users** - Create new users with name, email, and phone
- **View Users** - Fetch and display all users in a scrollable list
- **Edit Users** - Update existing user information
- **Delete Users** - Remove users from the system
- **Real-time Updates** - List updates automatically after operations

## Technology Stack

- **Frontend:** Android (Java)
- **Backend:** Spring Boot with REST API
- **Networking:** Retrofit 2
- **JSON Parsing:** Gson
- **UI Components:** RecyclerView, EditText, Button, LinearLayout

## Project Structure
```
UserManagementApp/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/usermanagementapp/
│   │   │   │   ├── MainActivity.java          - Main activity with all logic
│   │   │   │   ├── User.java                  - User data model
│   │   │   │   ├── UserAdapter.java           - RecyclerView adapter
│   │   │   │   ├── ApiService.java            - Retrofit API interface
│   │   │   │   └── RetrofitClient.java        - Retrofit configuration
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   │   ├── activity_main.xml      - Main UI layout
│   │   │   │   │   └── item_user.xml          - User list item layout
│   │   │   │   └── xml/
│   │   │   │       └── network_security_config.xml
│   │   │   └── AndroidManifest.xml
│   │   └── test/
│   └── build.gradle
└── README.md
```

## Getting Started

### Prerequisites

- Android Studio (latest version)
- Android SDK API 26 or higher
- Spring Boot backend running on local network
- Java 8 or higher

### Installation

1. **Clone the repository:**
```bash
git clone https://github.com/Hellooo-beep/user-management-android.git
cd UserManagementApp
```

2. **Open in Android Studio:**
   - Open Android Studio
   - Click File → Open
   - Select the UserManagementApp folder

3. **Update Backend IP:**
   - Open `RetrofitClient.java`
   - Change BASE_URL to your backend IP:
```java
   private static final String BASE_URL = "http://YOUR_IP:8080/";
```

4. **Update Network Security Config:**
   - Open `res/xml/network_security_config.xml`
   - Replace IP with your backend IP:
```xml
   <domain includeSubdomains="true">YOUR_IP</domain>
```

5. **Run the app:**
   - Click Run → Run 'app'
   - Select emulator or physical device
   - Click OK

## Usage

### Adding a User
1. Enter name, email, and phone in the input fields
2. Click "Add User" button
3. User appears in the list below

### Viewing Users
1. Click "Fetch All Users" button
2. All users from backend are displayed

### Editing a User
1. Click "Edit" button next to a user
2. Input fields populate with current data
3. Modify the information
4. Click "Update" button
5. User is updated and list refreshes

### Deleting a User
1. Click "Delete" button next to a user
2. User is removed from backend
3. List automatically updates

## Dependencies
```gradle
dependencies {
    // Retrofit for API calls
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    
    // Gson for JSON parsing
    implementation 'com.google.code.gson:gson:2.8.9'
    
    // RecyclerView for list display
    implementation 'androidx.recyclerview:recyclerview:1.3.0'
}
```

## API Endpoints

The app connects to these backend endpoints:

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/users` | Create a new user |
| GET | `/api/users` | Get all users |
| GET | `/api/users/{id}` | Get user by ID |
| PUT | `/api/users/{id}` | Update user |
| DELETE | `/api/users/{id}` | Delete user |

## Backend Repository

The Spring Boot backend can be found at:
[user-management-backend](https://github.com/Hellooo-beep/user-management-backend)

## Troubleshooting

### "Failed to add user" Error
- **Check:** Spring Boot backend is running
- **Check:** IP address in RetrofitClient.java is correct
- **Check:** Network security config has correct IP
- **Check:** Both devices are on same network

### "Connection refused" Error
- Backend is not running
- IP address is incorrect
- Firewall is blocking connection

## Architecture

### Data Flow
```
UI (EditText, Button) 
  ↓
MainActivity (handles events, calls API)
  ↓
Retrofit (makes HTTP request)
  ↓
Spring Boot Backend
  ↓
H2 Database
  ↓
Response → RecyclerView updates
```

## Learning Concepts Used

1. **Activity Lifecycle** - onCreate, onStart, onResume
2. **RecyclerView** - Efficient list rendering
3. **Retrofit** - REST API client
4. **Callbacks** - Handling async responses
5. **Adapter Pattern** - Adapting data to views
6. **Network Security** - Configuring cleartext traffic
7. **REST API** - Understanding HTTP methods

## Future Enhancements

- [ ] Add user authentication (login/signup)
- [ ] Implement search functionality
- [ ] Add sorting by name/email
- [ ] Implement pagination
- [ ] Add local database caching (Room)
- [ ] Add form validation
- [ ] Implement pull-to-refresh
- [ ] Add unit tests

