package Repository.Table;

import Entity.Table.TableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableRepository extends JpaRepository<TableEntity, Long> {
    TableEntity findByIdAndRestaurantName(Long id, String restaurantName);
}
