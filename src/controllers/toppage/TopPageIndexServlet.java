package controllers.toppage;

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
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class TopPageIndexServlet
 */
@WebServlet("/index.html")
public class TopPageIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public TopPageIndexServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");

        int page;
        try{
            page = Integer.parseInt(request.getParameter("page"));
        } catch(Exception e) {
            page = 1;
        }
        List<Report> reports = em.createNamedQuery("getMyAllReports", Report.class)
                                    .setParameter("employee", login_employee)
                                    .setFirstResult(15 * (page -1))
                                    .setMaxResults(15)
                                    .getResultList();

        long reports_count = (long)em.createNamedQuery("getMyReportsCount", Long.class)
                                        .setParameter("employee", login_employee)
                                        .getSingleResult();

       // List<Follow> my_followed = em.createNamedQuery("forToppage_followed", Follow.class)
         //                               .setParameter("log_emp",login_employee)
           //                             .getResultList();

        //for (int i = 0; i < my_followed.size(); i++) {
          //  System.out.println(my_followed.get(i) + "あいうえお");
        //}


        //List<Report> followed_reports = em.createNamedQuery("getMyAllFollowedReports", Report.class)
          //                                      .setParemeter("follow_relationship", )
            //                                    .setFirstResult(15 * (page -1))
              //                                  .setMaxResult(15)
                //                                .getResultList();

        //long followed_reports_count = em.createNamedQuery("getMyAllFollowedReportsCount", Report.class)
          //                                  .setParameter("follow_relationship", )
            //                                .getSingleResult();


        em.close();

        request.setAttribute("reports", reports);
        request.setAttribute("reports_count", reports_count);
        request.setAttribute("page", page);



        if(request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/views/topPage/index.jsp");
        rd.forward(request, response);
    }

}
