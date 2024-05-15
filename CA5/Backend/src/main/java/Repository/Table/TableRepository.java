package Repository.Table;

import Entity.Table.TableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableRepository extends JpaRepository<TableEntity, Long> {
    TableEntity findByIdAndRestaurantName(Long id, String restaurantName);
    List<TableEntity> findByRestaurantName(String restaurantName);
}
