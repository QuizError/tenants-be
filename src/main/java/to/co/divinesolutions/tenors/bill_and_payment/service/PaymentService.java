package to.co.divinesolutions.tenors.bill_and_payment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import to.co.divinesolutions.tenors.bill_and_payment.dto.PaymentDetails;
import to.co.divinesolutions.tenors.bill_and_payment.dto.PaymentDto;
import to.co.divinesolutions.tenors.entity.Payment;
import to.co.divinesolutions.tenors.utils.Response;

import java.util.Optional;

public interface PaymentService {
    Response<Payment> receivePayment(PaymentDto dto);

    Optional<Payment> getOptionalByUid(String uid);

    Page<PaymentDetails> paymentDetailsPage(Pageable pageable);

    Response<PaymentDetails> findByUid(String uid);
}
