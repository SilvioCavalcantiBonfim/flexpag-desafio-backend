package com.flexpag.paymentscheduler.service;

import com.flexpag.paymentscheduler.entities.Payment;
import com.flexpag.paymentscheduler.repository.PaymentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.naming.NoPermissionException;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Teste de serviços")
class PaymentServiceTest {
/*
*
*
* */
    @MockBean
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentService paymentService;

    private Payment p1,p2;
    @BeforeEach
    void before(){
//      Pagamento não efetuado ainda
        p1 = new Payment(1L, Payment.STATE.PENDING,16779888050000L);
        p2 = new Payment(2L, Payment.STATE.PAID,System.currentTimeMillis());
//      Mocks do repositório
        Mockito.when(paymentRepository.findById(1L)).thenReturn(Optional.of(p1));
        Mockito.when(paymentRepository.findById(2L)).thenReturn(Optional.of(p2));
    }
    @Test
    @DisplayName("Data de criação invalida. Não é possível criar um pagamento agendado no passado.")
    void createSchedulewithtimestapinvalid() {
        Assertions.assertThrows(IllegalArgumentException.class,() -> paymentService.createSchedule(0L));
    }
    @Test
    @DisplayName("Solicita um registro existente.")
    void findByIdwithexistentPayment() {
        Assertions.assertEquals(Optional.of(p1),paymentService.FindById(1l));
    }
    @Test
    @DisplayName("Solicita um registro não existente. Esperado um NoSuchElementException.")
    void findByIdwithinexistentPayment() {
        Assertions.assertThrows(NoSuchElementException.class,() -> paymentService.FindById(3l));
    }
    @Test
    @DisplayName("Solicita apagar um registro inexistente. Esperado um NoSuchElementException.")
    void deletenotexist() {
        Assertions.assertThrows(NoSuchElementException.class, () -> paymentService.Delete(3L));
    }
    @Test
    @DisplayName("Solicita apagar um registro existente que não pode ser apagado. Esperado um NoPermissionException.")
    void deleteexistnopermission() {
        Assertions.assertThrows(NoPermissionException.class, () -> paymentService.Delete(2L));
    }
    @Test
    @DisplayName("Solicita apagar um registro existente.")
    void deleteexist() throws NoPermissionException {
        paymentService.Delete(1L);
    }
    @Test
    @DisplayName("Solicita a atualização de um registro não existente.")
    void updatenotexist() {
        Assertions.assertThrows(NoSuchElementException.class, () -> paymentService.update(3L,System.currentTimeMillis()+60000L));
    }
    @Test
    @DisplayName("Solicita a atualização de um registro existente com uma data inválida (no passado).")
    void updateexistanddatenotaccept(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> paymentService.update(1L,60000L));
    }
    @Test
    @DisplayName("Solicita a atualização de um registro que não pode ser atualizada.")
    void updateexistanddatenotpermission(){
        Assertions.assertThrows(NoPermissionException.class, () -> paymentService.update(2L,System.currentTimeMillis()+10000L));
    }
}