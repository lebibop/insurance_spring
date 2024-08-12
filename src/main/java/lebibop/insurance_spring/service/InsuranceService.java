package lebibop.insurance_spring.service;

import lebibop.insurance_spring.entity.Insurance;
import lebibop.insurance_spring.repository.InsuranceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        LocalDate twoMonthsAfter = today.plusMonths(2);

        return insuranceRepository.findAll().stream()
                .filter(insurance ->
                        !insurance.getEndDate().isBefore(today) &&
                        !insurance.getEndDate().isAfter(twoMonthsAfter))
                .sorted((i1, i2) -> i2.getEndDate().compareTo(i1.getEndDate()))
                .collect(Collectors.toList());
    }

    public Insurance updateInsuranceStatus(Integer id, String statusKV1, String statusKV2) {
        Insurance insurance = insuranceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insurance not found"));

        insurance.setStatus_kv1(statusKV1);
        insurance.setStatus_kv2(statusKV2);

        return insuranceRepository.save(insurance);
    }

    public List<Insurance> searchInsurances(String query) {
        return insuranceRepository.searchByQuery(query);
    }

    public Integer calculateProfitBetweenDates(LocalDate startDate, LocalDate endDate) {
        return (int) insuranceRepository.findAll().stream()
                .filter(insurance -> (!insurance.getConclusionDate().isBefore(startDate)  && !insurance.getConclusionDate().isAfter(endDate)))
                .mapToDouble(insurance -> insurance.getKv1() * insurance.getPaymentsNumber())
                .sum();
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
