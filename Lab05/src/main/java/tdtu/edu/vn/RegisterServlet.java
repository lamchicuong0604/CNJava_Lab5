package tdtu.edu.vn;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.*;


public class RegisterServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	String errorMessage = request.getParameter("errorMessage");
    	request.getSession().setAttribute("errorMessage",errorMessage);
    	response.sendRedirect("register.jsp");
    	
     }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	request.setCharacterEncoding("UTF-8");
        String fullName = request.getParameter("fullname");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm_password");

        // Validate full name
        if (fullName == null || fullName.isEmpty()) {
            setErrorMessage(request, "Full name is required.");
        }

        // Validate username
        if (username == null || username.isEmpty()) {
            setErrorMessage(request, "Username is required.");
        } else {
            // Check if username already exists in the database
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                String url = "jdbc:mysql://localhost:3306/lab05?user=root&password=admin";
                Connection connection = DriverManager.getConnection(url);
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
                statement.setString(1, username);
                ResultSet result = statement.executeQuery();
                if (result.next()) {
                    setErrorMessage(request, "Username already exists.");
                }
                statement.close();
                connection.close();
            } catch (ClassNotFoundException e) {
                // Handle JDBC driver not found error
            } catch (SQLException e) {
                // Handle SQL error
            }
        }

        // Validate email
        if (email == null || email.isEmpty()) {
            setErrorMessage(request, "Email is required.");
        } else {
            String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
            Pattern emailPattern = Pattern.compile(emailRegex);
            Matcher emailMatcher = emailPattern.matcher(email);
            if (!emailMatcher.matches()) {
                setErrorMessage(request, "Invalid email address.");         
            } else {
                // Check if email already exists in the database
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    String url = "jdbc:mysql://localhost:3306/lab05?user=root&password=admin";
                    Connection connection = DriverManager.getConnection(url);
                    PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE email = ?");
                    statement.setString(1, email);
                    ResultSet result = statement.executeQuery();
                    if (result.next()) {
                        setErrorMessage(request, "Email already exists.");
                    }
                    statement.close();
                    connection.close();
                } catch (ClassNotFoundException e) {
                    // Handle JDBC driver not found error
                } catch (SQLException e) {
                    // Handle SQL error
                }
            }
        }

        // Validate password
        if (password == null || password.isEmpty()) {
            setErrorMessage(request, "Password is required.");
        }
        if (password.length() < 6) {
            setErrorMessage(request, "Password must have at least 6 characters.");
        }

        // Validate confirm password
        if (!password.equals(confirmPassword)) {
            setErrorMessage(request, "Passwords do not match.");
        }

        // Check if any field is empty
        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            setErrorMessage(request, "Please fill out all fields.");
        }

        // Check if there are any error messages
        String errorMessage = (String) request.getSession().getAttribute("errorMessage");
        if (errorMessage != null && !errorMessage.isEmpty()) {
            response.sendRedirect("register.jsp");
            return;
        }

        Connection connection = null;
        PreparedStatement statement = null;
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Create a connection to the database
        	String url = "jdbc:mysql://localhost:3306/lab05?user=root&password=admin";
            connection = DriverManager.getConnection(url);
            
            // Prepare a SQL statement to insert a new user into the database
            String sql = "INSERT INTO users (name, username, password, email) VALUES (?, ?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, fullName);
            statement.setString(2, username);
            statement.setString(3, password);
            statement.setString(4, email);
            
            // Execute the SQL statement
            statement.executeUpdate();
            
            // Set success message
            setSuccessMessage(request, "Account created successfully. Please login.");
            response.sendRedirect("register.jsp");
        } catch (ClassNotFoundException e) {
            // Handle JDBC driver not found error
        } catch (SQLException e) {
            // Handle SQL error
        } finally {
            // Close statement and connection
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // Handle error closing statement/connection
            }
        }
    }

    private void setErrorMessage(HttpServletRequest request, String message) {
        HttpSession session = request.getSession();
        session.setAttribute("errorMessage", message);
    }

    private void setSuccessMessage(HttpServletRequest request, String message) {
        HttpSession session = request.getSession();
        session.setAttribute("successMessage", message);
    }

}
