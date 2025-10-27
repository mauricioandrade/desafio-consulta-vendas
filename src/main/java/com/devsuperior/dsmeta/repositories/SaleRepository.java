package com.devsuperior.dsmeta.repositories;

import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import com.devsuperior.dsmeta.entities.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("SELECT obj.id, obj.date, obj.amount, obj.seller.name " +
            "FROM Sale obj " +
            "WHERE obj.date >= :minDate AND obj.date <= :maxDate " +
            "AND (:nameFragment IS NULL OR LOWER(obj.seller.name) LIKE LOWER(CONCAT('%', :nameFragment, '%')))")
    Page<Object[]> findReport(
            @Param("minDate") LocalDate minDate,
            @Param("maxDate") LocalDate maxDate,
            @Param("nameFragment") String nameFragment,
            Pageable pageable);

    @Query("SELECT new com.devsuperior.dsmeta.dto.SaleSummaryDTO(obj.seller.name, SUM(obj.amount)) " +
            "FROM Sale obj " +
            "WHERE obj.date >= :minDate AND obj.date <= :maxDate " +
            "GROUP BY obj.seller.name " +
            "ORDER BY obj.seller.name")
    List<SaleSummaryDTO> findSummary(
            @Param("minDate") LocalDate minDate,
            @Param("maxDate") LocalDate maxDate);
}
