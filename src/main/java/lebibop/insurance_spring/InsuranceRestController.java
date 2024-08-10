package lebibop.insurance_spring;

import lebibop.insurance_spring.enums.Company;
import lebibop.insurance_spring.enums.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
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
    public List<String> getCompanies() {
        return Arrays.stream(Company.values())
                .map(Company::getDisplayName)
                .collect(Collectors.toList());
    }

    @GetMapping("/types")
    public List<String> getTypes() {
        return Arrays.stream(Type.values())
                .map(Type::getDisplayName)
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInsurance(@PathVariable Integer id) {
        if (insuranceService.getInsuranceById(id).isPresent()) {
            insuranceService.deleteInsuranceById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
