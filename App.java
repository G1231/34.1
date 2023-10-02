//Saved App
/*34.1 (Access and update a Staff table) Write a program that views, inserts, and updates staff
information stored in a database, as shown in Figure 34.27a. The View button displays a
record with a specified ID. The Insert button inserts a new record. The Update button
updates the record for the specified ID. The Staff table is created as follows

create table staff ( id char(9) not
null, lastName varchar(15),
firstName varchar(15),
mi char(1), address varchar(20),
city varchar(20), state char(2),
telephone char(10), email
varchar(40), primary key (id) );

*/
//importing javafx and sql packages
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.*;
// initializing variables for textfields and buttons
public class App extends Application {
    TextField idField;
    TextField lastName;
    TextField firstName;
    TextField mi;
    TextField address;
    TextField city;
    TextField state;
    TextField telephone;
    TextField email;
    Button viewButton;
    Button insertButton;
    Button updateButton;
    Connection connection;

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Staff");

        // Initialize UI components
        idField = new TextField();
        lastName = new TextField();
        firstName = new TextField();
        mi = new TextField();
        address = new TextField();
        city = new TextField();
        state = new TextField();
        telephone = new TextField();
        email = new TextField();
        viewButton = new Button("View");
        insertButton = new Button("Insert");
        updateButton = new Button("Update");

        viewButton.setOnAction(e -> viewRecord());
        insertButton.setOnAction(e -> insertRecord());
        updateButton.setOnAction(e -> updateRecord());
        //setting all the fields to a lone on the grid
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.addRow(0, new Label("ID:"), idField, viewButton);
        grid.addRow(1, new Label("Last Name:"), lastName);
        grid.addRow(2, new Label("First Name:"), firstName);
        grid.addRow(3, new Label("MI:"), mi);
        grid.addRow(4, new Label("Address:"), address);
        grid.addRow(5, new Label("City:"), city);
        grid.addRow(6, new Label("State:"), state);
        grid.addRow(7, new Label("Telephone:"), telephone);
        grid.addRow(8, new Label("Email:"), email);
        grid.addRow(9, insertButton, updateButton);

        //making scene
        Scene scene = new Scene(grid, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Starting conneciton to database
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbname", "root", "root"); //I created a database balled dbname
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //method to see all the fields of an employee if the ID# is in the ID field
    private void viewRecord() {
        String id = idField.getText();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Staff WHERE id = ?");
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                lastName.setText(resultSet.getString("lastName"));
                firstName.setText(resultSet.getString("firstName"));
                mi.setText(resultSet.getString("mi"));
                address.setText(resultSet.getString("address"));
                city.setText(resultSet.getString("city"));
                state.setText(resultSet.getString("state"));
                telephone.setText(resultSet.getString("telephone"));
                email.setText(resultSet.getString("email"));
            } else {
                showAlert("Record not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //method to input record into database
    private void insertRecord() {
    //initializing fields again
    String id_R = idField.getText();
    String lastName_R = lastName.getText();
    String firstName_R = firstName.getText();
    String mi_R = mi.getText();
    String address_R = address.getText();
    String city_R = city.getText();
    String state_R = state.getText();
    String telephone_R = telephone.getText();
    String email_R = email.getText();

    try {//statements to insert records into database
        PreparedStatement insertStatement = connection.prepareStatement(
            "INSERT INTO Staff (id, lastName, firstName, mi, address, city, state, telephone, email) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
        );  
        insertStatement.setString(1, id_R);
        insertStatement.setString(2, lastName_R);
        insertStatement.setString(3, firstName_R);
        insertStatement.setString(4, mi_R);
        insertStatement.setString(5, address_R);
        insertStatement.setString(6, city_R);
        insertStatement.setString(7, state_R);
        insertStatement.setString(8, telephone_R);
        insertStatement.setString(9, email_R);

        int rowsInserted = insertStatement.executeUpdate();
        //indicater of success or failure
        if (rowsInserted > 0) {
            showAlert("Inserted record.");
            clearFields();
        } else {
            showAlert("Error: couldn't insert record.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        showAlert("Error inserting record: " + e.getMessage());
    }
}
//method to update already existing record
private void updateRecord() {
    //making fields again
    String id_U = idField.getText();
    String lastName_U = lastName.getText();
    String firstName_U = firstName.getText();
    String mi_U = mi.getText();
    String address_U = address.getText();
    String city_U = city.getText();
    String state_U = state.getText();
    String telephone_U = telephone.getText();
    String email_U = email.getText();

    try {//statements to replace current fields.
        PreparedStatement updateStatement = connection.prepareStatement(
            "UPDATE Staff SET lastName=?, firstName=?, mi=?, address=?, city=?, state=?, telephone=?, email=? " +
            "WHERE id=?"
        );
        updateStatement.setString(1, lastName_U);
        updateStatement.setString(2, firstName_U);
        updateStatement.setString(3, mi_U);
        updateStatement.setString(4, address_U);
        updateStatement.setString(5, city_U);
        updateStatement.setString(6, state_U);
        updateStatement.setString(7, telephone_U);
        updateStatement.setString(8, email_U);
        updateStatement.setString(9, id_U);

        int rowsUpdated = updateStatement.executeUpdate();
        if (rowsUpdated > 0) {
            showAlert("Record updated successfully.");
            clearFields();
        } else {
            showAlert("Record with ID " + idField + " not found for updating.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        showAlert("Error updating record: " + e.getMessage());
    }
}
//method to clear fields
private void clearFields() {
    idField.clear();
    lastName.clear();
    firstName.clear();
    mi.clear();
    address.clear();
    city.clear();
    state.clear();
    telephone.clear();
    email.clear();
}

// alert method for for other buttons or errors in above code
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void stop() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
