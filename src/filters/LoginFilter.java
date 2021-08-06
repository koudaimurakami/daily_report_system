package filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Employee;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter("/*")
public class LoginFilter implements Filter {

    /**
     * Default constructor.
     */
    public LoginFilter() {
    }

    /**
     * @see Filter#destroy()
     */
    public void destroy() {
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

         String context_path = ((HttpServletRequest)request).getContextPath();
            String servlet_path = ((HttpServletRequest)request).getServletPath();

            if(!servlet_path.matches("/css.*")) {       // CSSフォルダ内は認証処理から除外する
                HttpSession session = ((HttpServletRequest)request).getSession();

                // セッションスコープに保存された従業員（ログインユーザ）情報を取得
                Employee e = (Employee)session.getAttribute("login_employee");

                if(!servlet_path.equals("/login")) {        // ログイン画面以外について
                    // ログアウトしている状態であれば
                    // ログイン画面にリダイレクト
                    if(e == null) {
                        ((HttpServletResponse)response).sendRedirect(context_path + "/login");
                        return;
                    }

                    // 従業員の新規登録、従業員の削除、従業員情報の編集、の機能は管理者のみが操作できるようにする
                    if(servlet_path.matches("/employees.create") && e.getAdmin_flag() == 0) {
                        ((HttpServletResponse)response).sendRedirect(context_path + "/");
                        return;
                    } else if(servlet_path.matches("/employees.destroy") && e.getAdmin_flag() == 0) {
                        ((HttpServletResponse)response).sendRedirect(context_path + "/");
                    } else if(servlet_path.matches("/employees.edit") && e.getAdmin_flag() == 0) {
                        ((HttpServletResponse)response).sendRedirect(context_path + "/");
                    } else if(servlet_path.matches("/employees.new") && e.getAdmin_flag() == 0) {
                        ((HttpServletResponse)response).sendRedirect(context_path + "/");
                    } else if(servlet_path.matches("/employees.update") && e.getAdmin_flag() == 0) {
                        ((HttpServletResponse)response).sendRedirect(context_path + "/");
                    }
                } else {                                    // ログイン画面について
                    // ログインしているのにログイン画面を表示させようとした場合は
                    // システムのトップページにリダイレクト
                    if(e != null) {
                        ((HttpServletResponse)response).sendRedirect(context_path + "/");
                        return;
                    }
                }
            }

        chain.doFilter(request, response);
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
    }

}
