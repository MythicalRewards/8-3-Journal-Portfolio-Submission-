package com.example.projectthree;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    // Constant for SMS permission request
    private static final int SMS_PERMISSION_CODE = 1;

    // Views for login functionality
    private EditText usernameInput, passwordInput;
    private Button loginButton, registerButton;

    // Views for registration functionality
    private EditText usernameRegisterInput, passwordRegisterInput;
    private Button nextButton;

    // Views for data grid functionality
    private LinearLayout dataGrid;
    private Button addDataButton, deleteDataButton;

    // Views for SMS notifications
    private Button requestPermissionButton, sendTestSmsButton;
    private TextView statusMessage;

    // Database helper instance
    private SQLiteOpenHelper databaseHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize database helper
        databaseHelper = new UserDatabaseHelper(this);

        // Start with the login layout
        setContentView(R.layout.login_layout);
        initializeLoginViews();
    }

    // Initializes views for the login screen and sets up event listeners.

    private void initializeLoginViews() {
        // Bind login views
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        // Handle login button click
        loginButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();

            if (checkUserCredentials(username, password)) {
                Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                showDataGridLayout(); // Navigate to data grid layout
            } else {
                Toast.makeText(MainActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle registration navigation
        registerButton.setOnClickListener(v -> showRegistrationLayout());
    }


    private void showRegistrationLayout() {
        setContentView(R.layout.register_form);

        // Bind registration views
        usernameRegisterInput = findViewById(R.id.usernameRegisterInput);
        passwordRegisterInput = findViewById(R.id.passwordRegisterInput);
        nextButton = findViewById(R.id.nextButton);

        // Handle registration logic
        nextButton.setOnClickListener(v -> {
            String username = usernameRegisterInput.getText().toString();
            String password = passwordRegisterInput.getText().toString();

            if (registerUser(username, password)) {
                Toast.makeText(MainActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                setContentView(R.layout.login_layout); // Navigate back to login
                initializeLoginViews(); // Reinitialize login views
            } else {
                Toast.makeText(MainActivity.this, "Registration failed. Username may already exist.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showDataGridLayout() {
        setContentView(R.layout.activity_data_grid);

        // Bind data grid views
        dataGrid = findViewById(R.id.dataGrid);
        addDataButton = findViewById(R.id.addDataButton2);
        deleteDataButton = findViewById(R.id.deleteDataButton);

        // Populate data grid with database content
        populateDataGrid();

        // Add item to inventory
        addDataButton.setOnClickListener(v -> addItem("New Item", "10")); // Replace with user input

        // Delete item from inventory
        deleteDataButton.setOnClickListener(v -> deleteItem("New Item")); // Replace with user input
    }

    // Populates the data grid with items from the inventory database.

    private void populateDataGrid() {
        // Clear existing data grid
        dataGrid.removeAllViews();

        db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("inventory", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String itemName = cursor.getString(cursor.getColumnIndexOrThrow("item_name"));
                String itemQuantity = cursor.getString(cursor.getColumnIndexOrThrow("item_quantity"));

                // Create a row for each item
                LinearLayout row = new LinearLayout(this);
                row.setOrientation(LinearLayout.HORIZONTAL);

                TextView itemNameView = new TextView(this);
                itemNameView.setText(itemName);
                itemNameView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

                TextView itemQuantityView = new TextView(this);
                itemQuantityView.setText(itemQuantity);
                itemQuantityView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

                row.addView(itemNameView);
                row.addView(itemQuantityView);

                dataGrid.addView(row);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    // Handles SMS permissions and functionality.

    private void showSmsNotificationLayout() {
        setContentView(R.layout.activity_sms_notification);

        // Bind SMS views
        requestPermissionButton = findViewById(R.id.requestPermissionButton);
        sendTestSmsButton = findViewById(R.id.sendTestSmsButton);
        statusMessage = findViewById(R.id.statusMessage);

        // Request SMS permissions
        requestPermissionButton.setOnClickListener(v -> requestSmsPermission());

        // Send test SMS
        sendTestSmsButton.setOnClickListener(v -> sendSms("1234567890", "Test SMS from the app!")); // Replace with actual input
    }

    // Requests SMS permissions from the user.

    private void requestSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            enableSmsFeature();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
        }
    }

    // Enables SMS functionality when permissions are granted.

    private void enableSmsFeature() {
        sendTestSmsButton.setVisibility(View.VISIBLE);
        statusMessage.setText("SMS Permission Granted");
    }


    private void sendSms(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(this, "SMS Sent", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to send SMS", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean checkUserCredentials(String username, String password) {
        db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("users", new String[]{"username", "password"}, "username = ? AND password = ?",
                new String[]{username, password}, null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Registers a new user in the database.

    private boolean registerUser(String username, String password) {
        db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);

        long result = db.insert("users", null, values);
        return result != -1;
    }

    // Adds a single item to the inventory.

    private boolean addItem(String itemName, String itemQuantity) {
        db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("item_name", itemName);
        values.put("item_quantity", itemQuantity);

        long result = db.insert("inventory", null, values);
        populateDataGrid(); // Refresh the data grid
        return result != -1;
    }

    // Deletes the item from the inventory.

    private boolean deleteItem(String itemName) {
        db = databaseHelper.getWritableDatabase();
        int result = db.delete("inventory", "item_name = ?", new String[]{itemName});
        populateDataGrid(); // Refresh the data grid
        return result > 0;
    }

    // SQLite helper for managing database operations.

    private static class UserDatabaseHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "app.db";
        private static final int DATABASE_VERSION = 2;

        public UserDatabaseHelper(MainActivity context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT);");
            db.execSQL("CREATE TABLE inventory (id INTEGER PRIMARY KEY AUTOINCREMENT, item_name TEXT UNIQUE, item_quantity TEXT);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS users");
            db.execSQL("DROP TABLE IF EXISTS inventory");
            onCreate(db);
        }
    }
}
