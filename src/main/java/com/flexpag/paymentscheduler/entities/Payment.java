package com.flexpag.paymentscheduler.entities;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
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

    private STATE state_payment;

    private Long timestamp_payment;

    public Payment(STATE state_payment, Long timestamp_payment) {
        this.state_payment = state_payment;
        this.timestamp_payment = timestamp_payment;
    }
}
