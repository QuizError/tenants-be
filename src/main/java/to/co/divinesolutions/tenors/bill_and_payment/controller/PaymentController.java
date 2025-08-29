package to.co.divinesolutions.tenors.bill_and_payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import to.co.divinesolutions.tenors.bill_and_payment.dto.PaymentDetails;
import to.co.divinesolutions.tenors.bill_and_payment.dto.PaymentDto;
import to.co.divinesolutions.tenors.bill_and_payment.dto.PaymentSearchRequest;
import to.co.divinesolutions.tenors.bill_and_payment.service.PaymentService;
import to.co.divinesolutions.tenors.entity.Payment;
import to.co.divinesolutions.tenors.utils.Response;

@RestController
@RequestMapping("payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("receive-payment")
    public Response<Payment> receivePayment(@RequestBody PaymentDto dto){
        return paymentService.receivePayment(dto);
    }

    @PostMapping("search")
    public Page<PaymentDetails> paymentList(@RequestBody PaymentSearchRequest request ,Pageable pageable){
        return paymentService.paymentDetailsPage(request.getUserUid(),pageable);
    }

    @GetMapping("{uid}")
    public Response<PaymentDetails> findByUid(@PathVariable String uid){
        return paymentService.findByUid(uid);
    }
}
