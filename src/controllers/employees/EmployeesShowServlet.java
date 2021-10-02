package controllers.employees;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import utils.DBUtil;

/**
 * Servlet implementation class EmployeesShowServlet
 */
@WebServlet("/employees/show")
public class EmployeesShowServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesShowServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         EntityManager em = DBUtil.createEntityManager();

            Employee e = em.find(Employee.class, Integer.parseInt(request.getParameter("id")));

            //int test = e.getId(); (オブジェクトの内容をそのままコンソールに出力することはできないから、必要な情報を取得し、変数に収めておく
            //System.out.println(test + "あいうえお");
            // 例えば、Employee e というオブジェクトの中身を確認したければ、このように目印となる言葉と共にコンソールに出力させてみる
            // コンソールへの出力は、Tomcatを起動し、自分が想定している処理のところまでアプリケーションを動かしてみる


            // NamedQuery実行のためにlogin_employeeをセッションスコープから取得
            Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");



            request.setAttribute("employee", e);


            // フォローされる側のIDをセッションスコープに登録
            request.getSession().setAttribute("followed_id", e.getId());



            // ログイン中の従業員にフォローされている人のリスト
            long show_followed_count = (long)em.createNamedQuery("forUnFollow_count", Long.class)
                                                .setParameter("login_employee", login_employee)
                                                .setParameter("destroy_followed", e)
                                                .getSingleResult();


            // show_followed_countで指定している条件に何件情報が含まれているか、目印となるワードと共にコンソールに出力してみる
            // NamedQueryのSELECT COUNT(Followクラスで記述)では、情報が何件あるのかを取得することができる
            //System.out.println(show_followed_count + "フォローを外す人が1件取れているかの確認");



            em.close();

            // employees.show.jspのif文(when)用に、show_followed_count をリクエストに登録
            // 実行したサーブレット後にjspでのみセッションを使用するのはバグの温床となるため、セッションよりリクエストが良い
            request.setAttribute("show_followed_count", show_followed_count);

            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/show.jsp");
            rd.forward(request, response);
    }

}
