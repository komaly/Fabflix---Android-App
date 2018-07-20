

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

/**
 * Servlet implementation class Login
 */
//@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JsonObject responseJsonObject = new JsonObject();

		response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
		String loginUser = "root";
        String loginPasswd = "MySQLPassword123";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb?autoReconnect=true&useSSL=false";
        String email = request.getParameter("username");
		String password = request.getParameter("password");
        
        try {
        	Class.forName("com.mysql.jdbc.Driver").newInstance();

            Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            // Declare our statement
            PreparedStatement statement = dbcon.prepareStatement("Select email, password,id, firstName,lastName from customers where email = ? and password = ?");
            statement.setString(1, email);
            statement.setString(2, password);

            // Perform the query
            ResultSet rs = statement.executeQuery();
            
            if (rs.next()) {
    			// login success:
    			responseJsonObject.addProperty("status", "success");
    			response.getWriter().write(responseJsonObject.toString());    			
            }
            
            else
            {
            	// login fail
    			responseJsonObject.addProperty("status", "fail");
    			response.getWriter().write(responseJsonObject.toString());
            }
         
        }
        catch(Exception e){
        	response.getWriter().print("error");
        }
       
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
