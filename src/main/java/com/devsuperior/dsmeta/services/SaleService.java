package com.devsuperior.dsmeta.services;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
public class SaleService {

    @Autowired
    private SaleRepository repository;

    public SaleMinDTO findById(Long id) {
        Optional<Sale> result = repository.findById(id);
        Sale entity = result.get();
        return new SaleMinDTO(entity);
    }

    private LocalDate resolveMaxDate(String maxDateString) {
        if (maxDateString == null || maxDateString.isEmpty()) {
            return LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
        }
        try {
            return LocalDate.parse(maxDateString);
        } catch (DateTimeParseException e) {
            return LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
        }
    }

    private LocalDate resolveMinDate(String minDateString, LocalDate maxDate) {
        if (minDateString == null || minDateString.isEmpty()) {
            return maxDate.minusYears(1L);
        }
        try {
            return LocalDate.parse(minDateString);
        } catch (DateTimeParseException e) {
            return maxDate.minusYears(1L);
        }
    }

    public Page<Object[]> findReport(String minDateString, String maxDateString, String nameFragment, Pageable pageable) {
        LocalDate maxDate = resolveMaxDate(maxDateString);
        LocalDate minDate = resolveMinDate(minDateString, maxDate);
        String sellerNameParam = (nameFragment == null || nameFragment.isEmpty()) ? null : nameFragment;
        return repository.findReport(minDate, maxDate, sellerNameParam, pageable);
    }

    public List<SaleSummaryDTO> findSummary(String minDateString, String maxDateString) {
        LocalDate maxDate = resolveMaxDate(maxDateString);
        LocalDate minDate = resolveMinDate(minDateString, maxDate);
        return repository.findSummary(minDate, maxDate);
    }
}

