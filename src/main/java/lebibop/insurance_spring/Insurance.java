package lebibop.insurance_spring;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "Insurance")
public class Insurance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "conclusiondate")
    private LocalDate conclusionDate;
    private String company;
    private String type;
    @Column(name = "begindate")
    private LocalDate beginDate;
    @Column(name = "enddate")
    private LocalDate endDate;
    private String fio;
    @Column(name = "contractnumber")
    private String contractNumber;
    private String phone;
    private Integer cost;
    private Integer percentage;
    @Column(name = "paymentsnumber")
    private Integer paymentsNumber;
    private Integer kv1;
    private Integer kv2;
    @Column(name = "signaturedate1")
    private LocalDate signatureDate1;
    @Column(name = "signaturedate2")
    private LocalDate signatureDate2;
    @Column(name = "paymentdate1")
    private LocalDate paymentDate1;
    @Column(name = "paymentdate2")
    private LocalDate paymentDate2;
}