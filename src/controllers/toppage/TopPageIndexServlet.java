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


        //  ↓トップページには、フォローしている人の日報一覧のみ表示させる ↓ （自分の日報は、マイページに表示させる）

        // ① 【FollowクラスのList<Employee>を作る】------------------------------------------------------------------------------
        	//  query = "SELECT f.followed_id FROM Follow AS f WHERE f.follower_id = :login_emp"  ←（Followクラスに書いたクエリ）
        	// <Employee>型にするのは、select f.followed_id の followed_id というフィールド変数が Employee型だから。
        	// つまり、Employee型だけが集まる Listを作成する必要があるから、<Employee>型にしなければならない。
        	// さらに、③のパラメータとして使用できるのは Employee型なので、Employee型のデータのかたまりを作る必要がある。
        	// Reportクラスに書いたクエリは、where r.employee の employeeが Employee型なので、パラメータには Employee型しか使用できない。

        List<Employee> my_followed = em.createNamedQuery("forToppage_followed", Employee.class)
                                        .setParameter("login_emp",login_employee)
                                        .getResultList();


        // ② 【ReportクラスのList<Report>を作る】----------------------------------------------------------------------------------
    		// query = "SELECT r FROM Report AS r WHERE r.employee IN(:followed_reports)" ← (Reportクラスに書いたクエリ)
        	//  query = "SELECT COUNT(r) FROM Report AS r WHERE r.employee IN(:followed_reports)"  ←（Reportクラスに書いたクエリ）
        	// どちらのクエリも、where句の employeeというフィールド変数は Employee型なので、パラメータには Employee型のデータを入れる。

        List<Report> ed_reports = em.createNamedQuery("getMyAllFollowedReports", Report.class)
                                        .setParameter("followed_reports", my_followed)
                                        .setFirstResult(15 * (page -1))
                                        .setMaxResults(15)
                                        .getResultList();

        long ed_reports_count = em.createNamedQuery("getMyAllFollowedReportsCount", Long.class)
                                        .setParameter("followed_reports", my_followed)
                                        .getSingleResult();


        em.close();

        // ③で作成した2つの変数を、JSPに送るパラメータとして使用
        request.setAttribute("followed_reports", ed_reports);
        request.setAttribute("followed_reports_count", ed_reports_count);
        request.setAttribute("page", page);



        if(request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/views/topPage/index.jsp");
        rd.forward(request, response);
    }

}
