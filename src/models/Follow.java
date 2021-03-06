package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table(name = "follow")
@NamedQueries({
    // ログイン中の従業員がフォローしている人を集める
	@NamedQuery(
            name = "getMyFollowed",
            query = "SELECT f FROM Follow AS f WHERE f.follower_id = :follower ORDER BY f.id DESC"
    ),
    @NamedQuery(
            name = "getMyFollowedCount",
            query = "SELECT COUNT(f) FROM Follow AS f WHERE f.follower_id = :follower"
            ),
    // ログイン中の従業員をフォローしている人を集める
    @NamedQuery(
            name = "getMyFollower",
            query = "SELECT f FROM Follow AS f WHERE f.followed_id = :followed ORDER BY f.id DESC"
            ),
    @NamedQuery(
            name = "getMyFollowerCount",
            query = "SELECT COUNT(f) FROM Follow AS f WHERE f.followed_id = :followed"
            ),
    // フォローを外す人が1件だけきちんと取得できているかの確認をするため、EmployeesShowServetに記述
    @NamedQuery(
            name = "forUnFollow_count",
            query = "SELECT COUNT(f) FROM Follow AS f WHERE f.follower_id = :login_employee AND f.followed_id = :destroy_followed"
            ),
    // フォローを外す人を集める
    @NamedQuery(
            name = "forUnFollow",
            query = "SELECT f FROM Follow AS f WHERE f.follower_id = :login_emp AND f.followed_id = :destroy_ed"
            ),

    // トップページ用に、ログイン中の従業員がフォローしている人を集めるためのクエリを書く
    @NamedQuery(
            name = "forToppage_followed",
            query = "SELECT f.followed_id FROM Follow AS f WHERE f.follower_id = :login_emp"
            ),
})
@Entity
public class Follow {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private Employee follower_id;

    @ManyToOne
    @JoinColumn(name = "followed_id", nullable = false)
    private Employee followed_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employee getFollower_id() {
        return follower_id;
    }

    public void setFollower_id(Employee follower_id) {
        this.follower_id = follower_id;
    }

    public Employee getFollowed_id() {
        return followed_id;
    }

    public void setFollowed_id(Employee followed_id) {
        this.followed_id = followed_id;
    }










}
