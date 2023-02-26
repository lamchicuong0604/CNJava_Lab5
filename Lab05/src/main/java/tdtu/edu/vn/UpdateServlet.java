package tdtu.edu.vn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class UpdateServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;
  @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String productId = request.getParameter("id");
		request.setAttribute("productId", productId);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("update.jsp");
        dispatcher.forward(request, response);
	}
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String productId = request.getParameter("productId");
    String name = request.getParameter("name");
    double price = Double.parseDouble(request.getParameter("price"));
    Connection connection = null;
    PreparedStatement statement = null;

    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      String url = "jdbc:mysql://localhost:3306/lab05?user=root&password=admin";
      connection = DriverManager.getConnection(url);

      statement = connection.prepareStatement("UPDATE products SET name=?, price=? WHERE id=?");
      statement.setString(1, name);
      statement.setDouble(2, price);
      statement.setString(3, productId);
      statement.executeUpdate();
      System.out.println(statement);
      response.sendRedirect(request.getContextPath() + "/home");
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
      String errorMessage = "Error updating product. Please try again.";
      request.getSession().setAttribute("errorMessage", errorMessage);
      response.sendRedirect(request.getContextPath() + "/home");
    } finally {
      try {
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
  }
}
