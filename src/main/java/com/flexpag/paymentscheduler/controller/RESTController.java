package com.flexpag.paymentscheduler.controller;

import com.flexpag.paymentscheduler.entities.Payment;
import com.flexpag.paymentscheduler.entities.RegisterPayment;
import com.flexpag.paymentscheduler.entities.updatePayment;
import com.flexpag.paymentscheduler.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@CrossOrigin
@RestController
@RequestMapping("/payment/scheduler")
public class RESTController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create")
    public Map<String, Long> register(@RequestBody RegisterPayment args){
        try{
            return paymentService.createSchedule(new Payment(  args.getValue(), Payment.STATE.PENDING, (Long) args.getTimestap()));
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid date to schedule payment.");
        }
    }

    @GetMapping("/state")
    public Map<String, Object> consult(@RequestParam Long id){
        try{
            Map<String, Object> rt = new LinkedHashMap<>();
            rt.put("state",paymentService.FindById(id).get().getState_payment());
            return rt;
        }catch (NoSuchElementException e){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Schedule payment not found.");
        }
    }

    @GetMapping("/state/{id}")
    public Map<String, Object> consultURL(@PathVariable("id") Long id){
        try{
            Map<String, Object> rt = new LinkedHashMap<>();
            rt.put("state",paymentService.FindById(id).get().getState_payment());
            return rt;
        }catch (NoSuchElementException e){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Schedule payment not found.");
        }
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam Long id){
        try{
            paymentService.Delete(id);
        }catch (NoSuchElementException e){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Schedule payment not found.");
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_MODIFIED);
        }
    }

    @DeleteMapping("/delete/{id}")
    public void deleteURL(@PathVariable("id") Long id){
        try{
            paymentService.Delete(id);
        }catch (NoSuchElementException e){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Schedule payment not found.");
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_MODIFIED);
        }
    }

    @PutMapping("/update")
    public void update(@RequestBody updatePayment update){
        try{
            paymentService.update(update.getId(), update.getTimestamp());
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_MODIFIED);
        }
    }

}
