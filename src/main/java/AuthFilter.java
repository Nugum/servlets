import dao.UserDao;
import entities.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static java.util.Objects.nonNull;

@WebFilter(filterName = "AuthFilter", urlPatterns = "/")
public class AuthFilter implements Filter {
    private UserDao userDao = new UserDao();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        String login = req.getParameter("name");
        String password = req.getParameter("password");
        HttpSession session = req.getSession();

        Cookie[] cookies = req.getCookies();
        String message = "";
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("message")) {
                    message = c.getValue();
                }
            }
        }

        if (nonNull(session) && nonNull(session.getAttribute("user"))) {
            req.setAttribute("hasCookie", !message.equals(""));
            req.setAttribute("message", message);
            req.getRequestDispatcher("/main.jsp").forward(req, resp);
        } else {
            User user = userDao.findUser(login, password);
            if (user != null) {
                req.setAttribute("hasCookie", !message.equals(""));
                req.setAttribute("message", message);
                req.getSession().setAttribute("user", user);
                req.getRequestDispatcher("/main.jsp").forward(req, resp);
            } else {
                if (login != null) {
                    req.setAttribute("message", "Неверный логин / пароль");
                }
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
            }
        }
    }
}
