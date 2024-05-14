//package DAO.Restaurant;
//
//import Entity.Restaurant.RestaurantEntity;
//import Repository.Restaurant.RestaurantRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//public class RestaurantDAO {
//    private final RestaurantRepository restaurantRepository;
//
//    @Autowired
//    public RestaurantDAO(RestaurantRepository restaurantRepository) {
//        this.restaurantRepository = restaurantRepository;
//    }
//
//    public List<RestaurantEntity> findRestaurants(String type, String city, String country, String name) {
//        return restaurantRepository.findByFilters(type, city, country, name);
//    }
//}

//package DAO.Restaurant;
//
//import Entity.Restaurant.RestaurantEntity;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.transaction.Transactional;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//@Transactional
//public class RestaurantDAO {
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    public void saveOrUpdate(RestaurantEntity restaurant) {
//        entityManager.merge(restaurant);
//    }
//
//    public RestaurantEntity getById(int id) {
//        return entityManager.find(RestaurantEntity.class, id);
//    }
//
//    public void delete(RestaurantEntity restaurant) {
//        entityManager.remove(restaurant);
//    }
//
//    public List<RestaurantEntity> getAllRestaurants() {
//        return entityManager.createQuery("SELECT r FROM RestaurantEntity r", RestaurantEntity.class).getResultList();
//    }
//
//    public List<RestaurantEntity> findRestaurants(String type, String city, String country, String name) {
//        StringBuilder queryString = new StringBuilder("SELECT r FROM RestaurantEntity r WHERE 1=1");
//        if (type != null) {
//            queryString.append(" AND r.type = :type");
//        }
//        if (city != null) {
//            queryString.append(" AND r.address.city = :city");
//        }
//        if (country != null) {
//            queryString.append(" AND r.address.country = :country");
//        }
//        if (name != null) {
//            queryString.append(" AND r.name = :name");
//        }
//
//        // Create the query
//        return entityManager.createQuery(queryString.toString(), RestaurantEntity.class)
//                .setParameter("type", type)
//                .setParameter("city", city)
//                .setParameter("country", country)
//                .setParameter("name", name)
//                .getResultList();
//    }
//}
package DAO.Restaurant;

import Entity.Restaurant.RestaurantEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class RestaurantDAO {

    @PersistenceContext
    private EntityManager entityManager;


    public void saveOrUpdate(RestaurantEntity restaurant) {
        entityManager.merge(restaurant);
    }

    public void delete(RestaurantEntity restaurant) {
        entityManager.remove(restaurant);
    }

    public List<RestaurantEntity> getAllRestaurants() {
        return entityManager.createQuery("SELECT r FROM RestaurantEntity r", RestaurantEntity.class).getResultList();
    }
    public RestaurantEntity findById(int id) {
        return entityManager.find(RestaurantEntity.class, id);
    }
    public List<RestaurantEntity> findRestaurants(String type, String city, String country, String name) {
        StringBuilder queryString = new StringBuilder("SELECT r FROM RestaurantEntity r WHERE 1=1");
        if (type != null) {
            queryString.append(" AND r.type = :type");
        }
        if (city != null) {
            queryString.append(" AND r.address.city = :city");
        }
        if (country != null) {
            queryString.append(" AND r.address.country = :country");
        }
        if (name != null) {
            queryString.append(" AND r.name = :name");
        }

        // Create the query
        return entityManager.createQuery(queryString.toString(), RestaurantEntity.class)
                .setParameter("type", type)
                .setParameter("city", city)
                .setParameter("country", country)
                .setParameter("name", name)
                .getResultList();
    }
}
