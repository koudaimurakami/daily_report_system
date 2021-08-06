<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="../layout/app.jsp">
    <c:param name="content">
        <h2>【あなたのことをフォローしている従業員  一覧】</h2>
        <table id="follower_employee_list">
            <tbody>
                <tr>
                    <th>社員番号</th>
                    <th>氏名</th>
                    <th>操作</th>
                </tr>
                <c:forEach var="follower" items="${login_follower}" varStatus="status">
                    <tr class="row${status.count % 2}">
                        <td><c:out value="${follower.follower_id.code}" /></td>
                        <td><c:out value="${follower.follower_id.name}" /></td>
                        <td>
                            <c:choose>
                                <c:when test="${follower.follower_id.delete_flag == 1}">
                                    (削除済み)
                                </c:when>
                                <c:otherwise>
                                    <a href="<c:url value='/employees/show?id=${follower.follower_id.id}' />">詳細を表示</a>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div id="pagination">
            (全 ${login_follower_count} 件) <br />
            <c:forEach var="i" begin="1" end="${((login_follower_count - 1) / 15) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='/follower/index?page=${i}' />"><c:out value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
    </c:param>
</c:import>