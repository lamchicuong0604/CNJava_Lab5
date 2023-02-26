package tdtu.edu.vn;

import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {       
    	String errorMessage = request.getParameter("errorMessage");
    	request.getSession().setAttribute("errorMessage",errorMessage);
    	response.sendRedirect("login.jsp");
     }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String remember = request.getParameter("remember");
        String errorMessage = null;
        
        // Check if username and password are provided
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            errorMessage = "Username and password are required.";
         
        } else {
            // Check if username and password are correct
            boolean authenticated;
			try {
				authenticated = authenticateUser(username, password);
				if (!authenticated) {
	                errorMessage = "Invalid username or password.";
	            
	            } else {
	                // Create session for the user
	                HttpSession session = request.getSession(true);
	                session.setAttribute("username", username)	;
	                
	                // Set session expiration time (30 minutes)
	                session.setMaxInactiveInterval(30 * 60);
	                
	                // Set remember me cookie if checkbox is selected
	                if (remember != null && remember.equals("true")) {
	                    setRememberMeCookie(response, username, password);
	                   
	                } 
	                
	                // Redirect to home page
	                response.sendRedirect(request.getContextPath() + "/home");
	                return;
	            }
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
			}          
        }
        
        // Set error message as flash attribute
        //request.getSession().setAttribute("errorMessage", errorMessage);
        
        // Redirect to login page
        response.sendRedirect(request.getContextPath() + "/login?errorMessage="+ errorMessage);
    }
    
    private boolean authenticateUser(String username, String password)throws ClassNotFoundException {
    	Class.forName("com.mysql.cj.jdbc.Driver");
    	String url = "jdbc:mysql://localhost:3306/lab05?user=root&password=admin";
        String query = "SELECT COUNT(*) FROM users WHERE username = ? AND password = ?";
        
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            
            int count = rs.getInt(1);
            return count > 0;
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return false;
    }
    
    private void setRememberMeCookie(HttpServletResponse response, String username, String password) {
        String rememberMeToken = username + ":" + password + ":" + new Date().getTime();
        String rememberMeTokenBase64 = Base64.getEncoder().encodeToString(rememberMeToken.getBytes());
        
        Cookie rememberMeCookie = new Cookie("rememberMe", rememberMeTokenBase64);
        rememberMeCookie.setMaxAge(30 * 24 * 60 * 60);
        response.addCookie(rememberMeCookie);
    }
}