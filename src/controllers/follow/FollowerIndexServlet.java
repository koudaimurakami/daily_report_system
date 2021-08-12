package controllers.follow;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Follow;
import utils.DBUtil;

/**
 * Servlet implementation class FollowerIndexServlet
 */
@WebServlet("/follower/index")
public class FollowerIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowerIndexServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");

        int page = 1;
        try{
            page = Integer.parseInt(request.getParameter("page"));
        } catch(NumberFormatException e) { }
        List<Follow> login_follower = em.createNamedQuery("getMyFollower", Follow.class)
                                            .setParameter("followed", login_employee)  //Queryの :followedに対応
                                            .setFirstResult(15 * (page - 1))
                                            .setMaxResults(15)
                                            .getResultList();
        long login_follower_count = (long)em.createNamedQuery("getMyFollowerCount", Long.class)
                                                    .setParameter("followed", login_employee)
                                                    .getSingleResult();

        em.close();



        request.setAttribute("login_follower", login_follower);
        request.setAttribute("loginf_follower_count", login_follower_count);
        request.setAttribute("page", page);

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/follow/follower_index.jsp");
        rd.forward(request, response);

    }


}
