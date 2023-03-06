package com.flexpag.paymentscheduler.service;

import com.flexpag.paymentscheduler.entities.Payment;
import com.flexpag.paymentscheduler.repository.PaymentRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.NoPermissionException;
import java.util.*;

import static java.lang.System.currentTimeMillis;

@Service
public class PaymentService{
    @Autowired
    private PaymentRepository paymentRepository;

    public Map<String, Long> createSchedule(Long timestamp) {
        Map<String, Long> rt = new LinkedHashMap<>();
        if(timestamp < currentTimeMillis())
            throw new IllegalArgumentException("Invalid date to schedule payment.");
        Payment schedule = paymentRepository.save(new Payment(Payment.STATE.PENDING,timestamp));
        rt.put("id", schedule.getId());
        return rt;
    }

    public Optional<Payment> FindById(Long id) throws NoSuchElementException{
        Optional<Payment> p = paymentRepository.findById(id);
        if(p.get().getState_payment() == Payment.STATE.PENDING)
            updateState(p.get());
        return p;
    }

    public void Delete(Long id) throws NoPermissionException, NoSuchElementException {
        Optional<Payment> p = FindById(id);
        if(p.get().getState_payment() == Payment.STATE.PAID)
            throw new NoPermissionException();
        else
            updateState(p.get());
        paymentRepository.deleteById(p.get().getId());
    }

    public Payment update(Long id, Long newTimestamp) throws NoSuchElementException, IllegalArgumentException, NoPermissionException{
        Optional<Payment> p = FindById(id);
        if(newTimestamp < System.currentTimeMillis())
            throw new IllegalArgumentException();
        if(p.get().getState_payment() == Payment.STATE.PAID)
            throw new NoPermissionException();
        else
            updateState(p.get());
        Payment rt = p.get();
        rt.setTimestamp_payment(newTimestamp);
        paymentRepository.save(rt);
        return rt;
    }

    private void updateState(@NotNull Payment p){
        if(p.getTimestamp_payment() < currentTimeMillis())
            p.setState_payment(Payment.STATE.PAID);
        paymentRepository.save(p);
    }
}
