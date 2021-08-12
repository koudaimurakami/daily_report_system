package controllers.follow;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Follow;
import utils.DBUtil;

/**
 * Servlet implementation class UnFollowServlet
 */
@WebServlet("/unfollow")
public class UnFollowServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UnFollowServlet() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();


        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");
        // FollowServletでもやったように、セッションで取ったものを条件にデータベースから取る必要がある
        Employee login_emp_id = em.find(Employee.class, login_employee.getId());

        // ここは、int にする必要がある EmployeesShowServlet で、e.getId()としてあるから
        int followed_employee = (int)request.getSession().getAttribute("followed_id");
        Employee destroy_ed_id = em.find(Employee.class, followed_employee);

        List<Follow> destroy_data = em.createNamedQuery("forUnFollow", Follow.class)
                                        .setParameter("login_emp", login_emp_id)
                                        .setParameter("destroy_ed", destroy_ed_id)
                                        .getResultList();


        em.getTransaction().begin();
        // List内の値を取得する方法（List型変数をそのまま使用することはできない）
        // List型変数.get(インデックス番号）
        // インデックス番号は、要素が何番目にあるかを示す番号（今回は1件だけなので、0番目を記述）
        em.remove(destroy_data.get(0));
        em.getTransaction().commit();
        em.close();

        // セッションスコープ上の不要になったデータを削除
        //request.getSession().removeAttribute("login_employee");
        //request.getSession().removeAttribute("followed_id");

        // フォローされている側のidのパラメータとなる変数を作成
        int destroy_ed_employee = (int)request.getSession().getAttribute("followed_id");


        // EmployeeShowServletにリダイレクト
        response.sendRedirect(request.getContextPath() + "/employees/show?id=" + destroy_ed_employee);
    }

}
