package tdtu.edu.vn;

import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String errorMessage = request.getParameter("errorMessage");
    	request.getSession().setAttribute("errorMessage",errorMessage);
    	List<Product> productList = new ArrayList<Product>();
        // Retrieve list of products from the database
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
        	Class.forName("com.mysql.cj.jdbc.Driver");
        	String url = "jdbc:mysql://localhost:3306/lab05?user=root&password=admin";
            connection = DriverManager.getConnection(url);
            
            statement = connection.prepareStatement("SELECT * FROM products");
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                
                Product product = new Product(id, name, (float) price);
                productList.add(product);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // Store list of products in HttpSession object
        request.setAttribute("productList", productList);
        
        // Forward to home.jsp
        RequestDispatcher dispatcher = request.getRequestDispatcher("home.jsp");
        dispatcher.forward(request, response);
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
        String productName = request.getParameter("name");
        String price = request.getParameter("price");
        
        if (productName == null || productName.isEmpty()) {
            setErrorMessage(request, "Product name is required.");
        }

        if (price == null || price.isEmpty()) {
            setErrorMessage(request, "Price is required.");
        } else {
            try {
                double parsedPrice = Double.parseDouble(price);
                if (parsedPrice <= 0) {
                    setErrorMessage(request, "Price must be greater than 0.");
                }
            } catch (NumberFormatException e) {
                setErrorMessage(request, "Price must be a number.");
            }
        }
        
        if (productName.isEmpty() || price.isEmpty()) {
        	setErrorMessage(request, "Please fill out all fields.");
        }
            
     // Check if there are any error messages
        String errorMessage = (String) request.getSession().getAttribute("errorMessage");
        if (errorMessage != null && !errorMessage.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/home");
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
            
            // Prepare a SQL statement to insert a new product into the database
            String sql = "INSERT INTO products (name, price) VALUES (?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, productName);
            statement.setString(2, price);
            
            // Execute the SQL statement
            statement.executeUpdate();
            
            // Set success message
            setSuccessMessage(request, "Product added successfully.");
            response.sendRedirect(request.getContextPath() + "/home");

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
