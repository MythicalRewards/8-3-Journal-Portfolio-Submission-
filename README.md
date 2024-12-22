# Inventory Tracker with SMS Notifications

## Project Overview
The **Inventory Tracker with SMS Notifications** is a mobile app designed to help small business owners, inventory managers, and individuals efficiently manage inventory. The app provides an easy-to-use interface for adding, updating, deleting, and viewing inventory items while offering SMS notifications for low inventory alerts. It prioritizes user-centered design, secure data storage, and seamless user experiences.

---

## App Requirements and Goals
The app was designed to address user needs for managing inventory effectively while ensuring users stay informed about inventory levels through SMS notifications. The primary goals were:
- To allow users to perform CRUD (Create, Read, Update, Delete) operations on inventory items.
- To notify users via SMS about critical inventory levels.
- To create a secure and intuitive user authentication system.
- To provide a lightweight, fast, and easy-to-navigate user interface.

---

## Screens and Features
### Screens
1. **Login Screen**: Enables secure user authentication.
2. **Registration Screen**: Allows new users to create accounts.
3. **Inventory Management Screen**: Displays inventory data in a grid layout and supports CRUD operations.
4. **SMS Notification Screen**: Requests runtime permissions for sending SMS notifications.

### Features
- **User Authentication**: Secure login and registration system.
- **CRUD Functionality**: Users can add, edit, view, and delete inventory items.
- **SMS Alerts**: Users receive notifications for low inventory levels.
- **Data Persistence**: SQLite database for secure and reliable data storage.

### User-Centered Design
The UI was designed with the user in mind by focusing on intuitive navigation and clear layouts.  Screens were laid out to follow a logical flow, ensuring users could easily perform tasks such as managing inventory or logging in.  The use of Material Design principles ensured consistency and accessibility.

---

## Development Approach
The app was developed iteratively, starting with setting up the UI screens and then implementing backend functionality. Key strategies included:
- Breaking down the app into smaller, manageable components (e.g., authentication, inventory CRUD, SMS notifications).
- Writing modular code to ensure maintainability.
- Using in-line comments and consistent naming conventions for better readability.

These techniques are universally applicable in software development and will be used in future projects to ensure scalability and clarity.

---

## Testing and Quality Assurance
To ensure code functionality:
- Features were tested using the Android Emulator.
- Edge cases, such as missing permissions for SMS notifications, were explicitly tested.
- User actions like logging in, registering, and CRUD operations were simulated to confirm expected behaviors.

This process was crucial in revealing minor logic flaws, such as improperly handled permissions or missing validations, allowing them to be fixed before final deployment.

---

## Challenges and Innovations
The biggest challenge was handling runtime SMS permissions seamlessly while maintaining full app functionality if the permission was denied.  To overcome this, the app would be designed to handle denied permissions by disabling SMS specific features while keeping other functionalities intact.

Another innovation was the implementation of a user-friendly grid layout for inventory management, ensuring clarity and ease of navigation.

---

## Highlights of Knowledge and Skills
The integration of SMS notifications demonstrated a strong understanding of Android runtime permissions and user interaction design.  Additionally, the CRUD implementation using SQLite showcased proficiency in database management and UI DB integration.  These components highlight the app’s adherence to best practices and user centered development principles.
