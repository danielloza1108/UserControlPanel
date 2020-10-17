package pl.coderslab.user;

import pl.coderslab.utils.User;
import pl.coderslab.utils.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/user/edit")
public class UserEdit extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserDAO userDAO = new UserDAO();
        User user = new User();
        String name = request.getParameter("userName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        int id = Integer.parseInt(request.getParameter("id"));
        user.setId(id);
        user.setEmail(email);
        user.setPassword(password);
        user.setUserName(name);
        userDAO.update(user);
        response.sendRedirect(request.getContextPath() + "/user/list");

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        UserDAO userDAO = new UserDAO();
        User read = userDAO.read(Integer.parseInt(id));
        request.setAttribute("user", read);
    getServletContext().getRequestDispatcher("/users/edit.jsp").forward(request,response);
    }
}
