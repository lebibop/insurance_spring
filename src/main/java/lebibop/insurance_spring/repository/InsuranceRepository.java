package lebibop.insurance_spring.repository;

import lebibop.insurance_spring.entity.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsuranceRepository extends JpaRepository<Insurance, Integer> {
    @Query(value = "SELECT * FROM insurance  WHERE " +
            "LOWER(company) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(type) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(fio) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(contractnumber) LIKE LOWER(CONCAT('%', :query, '%'))",
            nativeQuery = true)
    List<Insurance> searchByQuery(@Param("query") String query);
}
