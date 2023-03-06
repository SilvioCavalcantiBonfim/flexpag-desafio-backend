package com.flexpag.paymentscheduler.controller;

import com.flexpag.paymentscheduler.entities.Payment;
import com.flexpag.paymentscheduler.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.naming.NoPermissionException;
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
    public Map<String, Long> register(@RequestBody Map<String,Long> args){
        try{
            return paymentService.createSchedule(args.get("timestamp"));
        }catch (IllegalArgumentException | NullPointerException e){
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
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Scheduled payment not found.");
        }
    }

    @GetMapping("/state/{id}")
    public Map<String, Object> consultURL(@PathVariable("id") Long id){
        try{
            Map<String, Object> rt = new LinkedHashMap<>();
            rt.put("state",paymentService.FindById(id).get().getState_payment());
            return rt;
        }catch (NoSuchElementException e){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Scheduled payment not found.");
        }
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam Long id){
        try{
            paymentService.Delete(id);
        }catch (NoSuchElementException e){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Scheduled payment not found.");
        }catch (NoPermissionException e){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Payment was executed.");
        }
    }

    @DeleteMapping("/delete/{id}")
    public void deleteURL(@PathVariable("id") Long id){
        try{
            paymentService.Delete(id);
        }catch (NoSuchElementException e){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Scheduled payment not found.");
        }catch (NoPermissionException e){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Payment was executed.");
        }
    }

    @PutMapping("/update")
    public Payment update(@RequestBody Map<String, Long> update){
        try{
            return paymentService.update(update.get("id"), update.get("timestamp"));
        }catch (IllegalArgumentException | NullPointerException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid date to update scheduled payment.");
        } catch (NoPermissionException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Payment was executed.");
        }catch (NoSuchElementException e){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Scheduled payment not found.");
        }
    }

}
