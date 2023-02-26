package tdtu.edu.vn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.io.IOException;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteProductServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String productId = request.getParameter("productId");

    Connection connection = null;
    PreparedStatement statement = null;

    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      String url = "jdbc:mysql://localhost:3306/lab05?user=root&password=admin";
      connection = DriverManager.getConnection(url);

      statement = connection.prepareStatement("DELETE FROM products WHERE id = ?");
      statement.setString(1, productId);
      statement.executeUpdate();

      response.sendRedirect(request.getContextPath() + "/home");
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
      String errorMessage = "Error deleting product. Please try again.";
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

