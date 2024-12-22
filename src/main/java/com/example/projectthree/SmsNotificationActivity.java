package com.example.projectthree;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SmsNotificationActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_REQUEST_CODE = 101;

    private Button requestPermissionButton;
    private Button sendTestSmsButton;
    private TextView statusMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_notification);

        // Bind views
        requestPermissionButton = findViewById(R.id.requestPermissionButton);
        sendTestSmsButton = findViewById(R.id.sendTestSmsButton);
        statusMessage = findViewById(R.id.statusMessage);

        // Check SMS permission on startup
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            enableSmsFunctionality();
        }

        // Handle request permission button click
        requestPermissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestSmsPermission();
            }
        });

        // Handle send test SMS button click
        sendTestSmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTestSms();
            }
        });
    }

    private void requestSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // Request SMS permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_CODE);
        } else {
            enableSmsFunctionality();
        }
    }

    private void enableSmsFunctionality() {
        sendTestSmsButton.setVisibility(View.VISIBLE);
        statusMessage.setText("SMS functionality is enabled.");
        Toast.makeText(this, "SMS Permission Granted.", Toast.LENGTH_SHORT).show();
    }

    private void disableSmsFunctionality() {
        sendTestSmsButton.setVisibility(View.GONE);
        statusMessage.setText("SMS functionality is disabled.");
        Toast.makeText(this, "SMS Permission Denied.", Toast.LENGTH_SHORT).show();
    }

    private void sendTestSms() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage("5551234567", null, "Test SMS: Inventory Notification", null, null);
                Toast.makeText(this, "Test SMS sent successfully.", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Failed to send SMS: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "SMS permission not granted. Cannot send SMS.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableSmsFunctionality();
            } else {
                disableSmsFunctionality();
            }
        }
    }
}
