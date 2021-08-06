package controllers.follow;

import java.io.IOException;

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
 * Servlet implementation class FollowServlet
 */
@WebServlet("/follow")
public class FollowServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        // Followのインスタンスを生成
        Follow f = new Follow();

        // fの各フィールドにデータを代入  データを入れることでテーブルが自動で作成される
        // セッションからログインしている人の情報を一式取ってくる（セッションは1つだけ情報を取るということができない）
        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");
        // セッションから取ってきたlogin_employeeの中のIdを条件として、em.findを使ってEmployeeクラス（の中のemployeesテーブル）から１件（1人分の(データベースの1行分）だけ情報を取る
        // １件だけとは、従業員1人だけのその人の情報一式すべてという意味で、Idだけを1件取得するということではない
        Employee er_id = em.find(Employee.class, login_employee.getId());
        f.setFollower_id(er_id);

        int followed_employee = (int)request.getSession().getAttribute("followed_id");
        Employee ed_id = em.find(Employee.class, (int)followed_employee);
        f.setFollowed_id(ed_id);


        // データベースに保存
        em.getTransaction().begin();
        // データベースに保存するときは1人の従業員の情報の中のIdだけが保存されるようになっている
        // Followクラスのフィールド(follower_id, followed_id)は、EmployeeクラスとJoinColumnで繋がっていて、
        // Employeeクラスの主キーのみが保存される仕組みだから
        em.persist(f);
        em.getTransaction().commit();


        // フォローされる側のIDまで指定する必要がある
        int ed_employee = (int)request.getSession().getAttribute("followed_id");
        // パラメータ名（ここでは id であるが、何でもよい）の記述まで忘れずに
        // EmployeeShowサーブレットは、何を条件にデータベースから情報を取ってきたらいいのか分からないから、showの後にどの従業員の詳細なのか条件を付けてあげる必要がある
        response.sendRedirect(request.getContextPath() + "/employees/show?id=" + ed_employee);




    }

}
