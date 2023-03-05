package com.flexpag.paymentscheduler.service;

import com.flexpag.paymentscheduler.entities.Payment;

import java.util.List;
import java.util.Map;

public interface PaymentServiceInterface {

    Map<String, Long> createSchedule(Payment p);
}
