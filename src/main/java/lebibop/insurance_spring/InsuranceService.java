package lebibop.insurance_spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InsuranceService{
    @Autowired
    private InsuranceRepository insuranceRepository;

    public List<Insurance> getAllInsurances() {
        return insuranceRepository.findAll(Sort.by(Sort.Direction.DESC, "conclusionDate"));
    }

    public List<Insurance> getInsurancesWithinTwoMonths() {
        LocalDate today = LocalDate.now();
        LocalDate twoMonthsBefore = today.minusMonths(2);
        LocalDate twoMonthsAfter = today.plusMonths(2);

        return insuranceRepository.findAll().stream()
                .filter(insurance -> insurance.getConclusionDate() != null &&
                        !insurance.getEndDate().isBefore(twoMonthsBefore) &&
                        !insurance.getEndDate().isAfter(twoMonthsAfter))
                .collect(Collectors.toList());
    }

    public Optional<Insurance> getInsuranceById(Integer id) {
        return insuranceRepository.findById(id);
    }

    public Insurance saveInsurance(Insurance insurance) {
        return insuranceRepository.save(insurance);
    }

    public void deleteInsuranceById(Integer id) {
        insuranceRepository.deleteById(id);
    }
}
