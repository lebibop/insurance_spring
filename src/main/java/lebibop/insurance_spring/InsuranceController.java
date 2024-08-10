package lebibop.insurance_spring;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InsuranceController {
    @GetMapping("/")
    public String startingPage() {
        return "index";
    }

    @GetMapping("/add")
    public String addInsurance() {
        return "add";
    }

}
