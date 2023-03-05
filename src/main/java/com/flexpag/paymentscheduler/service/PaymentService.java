package com.flexpag.paymentscheduler.service;

import com.flexpag.paymentscheduler.entities.Payment;
import com.flexpag.paymentscheduler.repository.PaymentRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PaymentService implements PaymentServiceInterface {
    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Map<String, Long> createSchedule(Payment p) {
        Map<String, Long> rt = new LinkedHashMap<>();
        if(p.getDate_payment() < System.currentTimeMillis()) {
            throw new IllegalArgumentException("Invalid date to schedule payment.");
        }
        Payment schedule = paymentRepository.save(p);
        rt.put("id", schedule.getId());
        return rt;
    }

//    public Map<String, Object>
    public Optional<Payment> FindById(Long id) throws NoSuchElementException{
        Optional<Payment> p = paymentRepository.findById(id);
        if(p.get().getState_payment() == Payment.STATE.PENDING)
            updateState(p.get());
        return p;
    }

    public void Delete(Long id) throws Exception {
        Payment p = FindById(id).get();
        if(p.getState_payment() == Payment.STATE.PAID)
            throw new Exception();
        else
            updateState(p);
        paymentRepository.delete(p);
    }

    public Payment update(Long id, Long newTimestamp) throws Exception {
        Payment p = FindById(id).get();
        if (newTimestamp > System.currentTimeMillis())
            throw new IllegalArgumentException();
        if(p.getState_payment() == Payment.STATE.PENDING)
            throw new Exception();
        return p;
    }

    private void updateState(@NotNull Payment p){
        if(p.getDate_payment() < System.currentTimeMillis())
            p.setState_payment(Payment.STATE.PAID);
        paymentRepository.save(p);
    }
}
