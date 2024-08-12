package lebibop.insurance_spring.controllers;

import lebibop.insurance_spring.entity.Insurance;
import lebibop.insurance_spring.service.InsuranceService;
import lebibop.insurance_spring.enums.COMPANY;
import lebibop.insurance_spring.enums.TYPE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/insurances")
public class InsuranceRestController {
    @Autowired
    private InsuranceService insuranceService;

    @GetMapping
    public List<Insurance> getAllInsurances() {
        return insuranceService.getAllInsurances();
    }

    @GetMapping("/near-conclusion-date")
    public List<Insurance> getInsurancesNearConclusionDate() {
        return insuranceService.getInsurancesWithinTwoMonths();
    }

    @GetMapping("/companies")
    public Map<String, String> getCompanies() {
        return Arrays.stream(COMPANY.values())
                .collect(Collectors.toMap(COMPANY::getName, COMPANY::getColor));
    }

    @GetMapping("/types")
    public List<String> getTypes() {
        return Arrays.stream(TYPE.values())
                .map(TYPE::getDisplayName)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Insurance> getInsuranceById(@PathVariable Integer id) {
        Optional<Insurance> insurance = insuranceService.getInsuranceById(id);
        return insurance.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public Insurance createInsurance(@RequestBody Insurance insurance) {
        return insuranceService.saveInsurance(insurance);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteInsurance(@PathVariable Integer id) {
        if (insuranceService.getInsuranceById(id).isPresent()) {
            insuranceService.deleteInsuranceById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/update-status/{id}")
    public Insurance updateInsuranceStatus(@PathVariable Integer id,
                                           @RequestBody Insurance request) {
        return insuranceService.updateInsuranceStatus(id, request.getStatus_kv1(), request.getStatus_kv2());
    }

    @GetMapping("/search")
    public List<Insurance> searchInsurances(@RequestParam String query) {
        return insuranceService.searchInsurances(query);
    }

    @PostMapping("/profit")
    public ResponseEntity<Integer> calculateProfit(
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate) {
        Integer profit = insuranceService.calculateProfitBetweenDates(startDate, endDate);
        return ResponseEntity.ok(profit);
    }



}
