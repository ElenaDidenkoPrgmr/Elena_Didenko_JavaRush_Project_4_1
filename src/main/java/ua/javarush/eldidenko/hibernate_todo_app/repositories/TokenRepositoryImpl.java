package ua.javarush.eldidenko.hibernate_todo_app.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ua.javarush.eldidenko.hibernate_todo_app.entites.User;
import ua.javarush.eldidenko.hibernate_todo_app.entites.UserToken;

public class TokenRepositoryImpl implements TokenRepository {
    private SessionFactory sessionFactory;
    public TokenRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(UserToken userToken,Long userId) {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            //userToken.setId(userId);
            //User user = ;
            /*user.getUserToken().setAccessToken(userToken.getAccessToken());
            user.getUserToken().setRefreshToken(userToken.getRefreshToken());*/
            //user.setUserToken(userToken);
            /*User user = (new UserRepositoryImpl(sessionFactory)).fetchById(userId);

            UserToken newUserToken = new UserToken();
            newUserToken.setUser(user);
            newUserToken.setRefreshToken(userToken.getRefreshToken());
            newUserToken.setAccessToken(userToken.getAccessToken());*/
            User user = session.get(User.class, userId);
            UserToken newUserToken = session.get(UserToken.class,userId);
            if (newUserToken == null) {
                newUserToken = new UserToken();
                newUserToken.setUser(user);
            }
            newUserToken.setRefreshToken(userToken.getRefreshToken());
            newUserToken.setAccessToken(userToken.getAccessToken());

            //user.setUserToken(newUserToken);
            session.persist(newUserToken);

            //userToken.setUser(user);

           // userToken.setUser(user);
           // userToken.setId(userId);
            //UserToken userTokenPresent = user.getUserToken();
            //userTokenPresent = userToken;

            //user.setUserToken(userToken);
            //userToken.setUser(user);
            //session.persist(userToken);

           //session.flush();
            //session.persist(userToken);
            transaction.commit();
        }
    }



    @Override
    public String fetchAccessTokenByUserId(Long userId) {
        try(Session session = sessionFactory.openSession()) {
            Query<String> query = session.createQuery(
                    "select u.accessToken from UserToken u where user.id = :user_id",
                    String.class);
            query.setParameter("user_id", userId);

            return query.getSingleResult();
        }
    }

    @Override
    public UserToken fetchByRefreshToken(String refreshToken) {
        try(Session session = sessionFactory.openSession()) {
            Query<UserToken> query = session.createQuery(
                    "from UserToken where refreshToken = :refresh_Token",
                    UserToken.class);
            query.setParameter("refresh_Token", refreshToken);

            return query.getSingleResult();
        }
    }
}
