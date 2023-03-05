package com.flexpag.paymentscheduler.entities;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "payment")
public class Payment {

    public enum STATE{
        PENDING,
        PAID
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private float value_payment;

    private STATE state_payment;

    private Long date_payment;

    public Payment(float value_payment, STATE state_payment, Long date_payment) {
        this.value_payment = value_payment;
        this.state_payment = state_payment;
        this.date_payment = date_payment;
    }
}
