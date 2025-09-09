package to.co.divinesolutions.tenors.bill_and_payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import to.co.divinesolutions.tenors.bill_and_payment.dto.PaymentDetails;
import to.co.divinesolutions.tenors.bill_and_payment.dto.PaymentDto;
import to.co.divinesolutions.tenors.bill_and_payment.dto.PaymentSearchRequest;
import to.co.divinesolutions.tenors.bill_and_payment.repository.PaymentRepository;
import to.co.divinesolutions.tenors.entity.*;
import to.co.divinesolutions.tenors.enums.PaymentStatus;
import to.co.divinesolutions.tenors.property.service.PropertyService;
import to.co.divinesolutions.tenors.rentals.repository.RentalRepository;
import to.co.divinesolutions.tenors.sms.dto.Recipient;
import to.co.divinesolutions.tenors.sms.dto.SMSDto;
import to.co.divinesolutions.tenors.sms.service.SMSService;
import to.co.divinesolutions.tenors.utils.Response;
import to.co.divinesolutions.tenors.utils.ResponseCode;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{
    private final PaymentRepository paymentRepository;
    private final RentalRepository rentalRepository;
    private final PropertyService propertyService;
    private final BillService billService;
    private final SMSService smsService;
    @Override
    public Response<Payment> receivePayment(PaymentDto dto){
        try {

            Optional<Bill> optionalBill = billService.getOptionalByBillReference(dto.getBillReferenceNumber());
            if (optionalBill.isEmpty()){
                return new Response<>(false, ResponseCode.NO_DATA_FOUND, "Bill could not be found or may have been deleted from the system", null);
            }
            Bill bill = optionalBill.get();

            Optional<Payment> optionalPayment = getOptionalByUid(dto.getUid());
            if (dto.getUid() != null && !dto.getUid().isEmpty() && optionalPayment.isEmpty()){
                return new Response<>(false, ResponseCode.NO_DATA_FOUND, "Payment could not be found or may have been deleted from the system", null);
            }

            Payment payment = optionalPayment.orElse(new Payment());
            payment.setCurrency(dto.getCurrency());
            payment.setFspCode(dto.getFspCode());
            payment.setFspName(dto.getFspName());
            payment.setBill(bill);
            payment.setPaymentDate(dto.getPaymentDate() != null && !dto.getPaymentDate().isEmpty() ? LocalDate.parse(dto.getPaymentDate()) : LocalDate.now());
            payment.setPaidAmount(dto.getPaidAmount());
            payment.setCurrency(dto.getCurrency());
            payment.setPaymentChannel(dto.getPaymentChannel());
            payment.setBillReferenceNumber(dto.getBillReferenceNumber());
            payment.setPaymentStatus(PaymentStatus.SUCCESS);
            payment.setThirdPartyReference(dto.getThirdPartyReference());
            Payment saved = paymentRepository.save(payment);
            //send sms
//            sendPaymentSMS(saved);
            //affect bill related
            billService.affectBill(saved);

            return new Response<>(true, ResponseCode.SUCCESS, "Payment saved successfully", saved);
        }
        catch (Exception e){
            log.error("***** Error on saving payment: {}",e.getMessage());
            return new Response<>(true, ResponseCode.INVALID_INPUT, "Error when saving payment", null);
        }
    }

    @Override
    public Optional<Payment> getOptionalByUid(String uid){
        return uid != null && !uid.isEmpty() ? paymentRepository.findFirstByUid(uid) : Optional.empty();
    }

    @Override
    public Page<PaymentDetails> paymentDetailsPage(String userUid, Pageable pageable) {
        // Get the bills for this user
        List<Bill> userBills = billService.getPropertyBills(userUid);

        // If no bills found, return empty page
        if (userBills.isEmpty()) {
            return Page.empty(pageable);
        }

        // Find payments only for this user's bills using your existing repository method
        return paymentRepository.findAllByBillIn(userBills, pageable).map(payment -> {
            PaymentDetails details = new PaymentDetails();
            details.setUid(payment.getUid());
            details.setPaymentChannel(payment.getPaymentChannel());
            details.setPaymentDate(payment.getPaymentDate() != null ? payment.getPaymentDate().toString() : null);
            details.setCurrency(payment.getCurrency());
            details.setFspCode(payment.getFspCode());
            details.setFspName(payment.getFspName());
            details.setBillUid(payment.getBill().getUid());
            details.setPaidAmount(payment.getPaidAmount());
            details.setAmountDue(payment.getBill().getAmountDue());
            details.setThirdPartyReference(payment.getThirdPartyReference());
            details.setBillReferenceNumber(payment.getBillReferenceNumber());
            return details;
        });
    }

    @Override
    public Response<PaymentDetails> findByUid(String uid) {
        try {
            Optional<Payment> optionalPayment = getOptionalByUid(uid);
            if (optionalPayment.isPresent()){
                Payment payment = optionalPayment.get();
                PaymentDetails details = new PaymentDetails();
                details.setUid(payment.getUid());
                details.setPaymentChannel(payment.getPaymentChannel());
                details.setPaymentDate(payment.getPaymentDate() != null ? payment.getPaymentDate().toString() : null);
                details.setCurrency(payment.getCurrency());
                details.setFspCode(payment.getFspCode());
                details.setFspName(payment.getFspName());
                details.setBillUid(payment.getBill().getUid());
                details.setPaidAmount(payment.getPaidAmount());
                details.setAmountDue(payment.getBill().getAmountDue());
                details.setThirdPartyReference(payment.getThirdPartyReference());
                details.setBillReferenceNumber(payment.getBillReferenceNumber());
                return new Response<>(true,ResponseCode.SUCCESS,"Success", details);
            }
            else {
                return new Response<>(false,ResponseCode.NO_DATA_FOUND,"Payment could not be found or may have been deleted from the system", null);
            }
        } catch (Exception e) {
            return new Response<>(false, ResponseCode.INTERNAL_SERVER_ERROR, "An error when loading payments", null);
        }
    }


    void sendPaymentSMS(Payment payment){
        SMSDto smsDto = new SMSDto();
        Optional<Rental>  optionalRental =  rentalRepository.findById(payment.getBill().getBillableId());
        if (optionalRental.isPresent()){
            Rental rental = optionalRental.get();
            User user = rental.getClient().getUser();
            String clientName = user.getFirstname()+" "+user.getLastname();
            String clientMobile = user.getMsisdn();
            Property property = rental.getUnitSection().getUnit().getProperty();
            String senderName = property.getSenderName() != null && !property.getSenderName().isEmpty() ? property.getSenderName() : "HOMES APP";
            smsDto.setMessage("Ndugu "+clientName+" malipo yako ya kodi ya kuanzia "+rental.getStartDate()+" mpaka "+rental.getEndDate()+" sawa na "+payment.getCurrency()+" "+payment.getPaidAmount()+" yamekamilika stakabadhi ya malipo "+payment.getThirdPartyReference()+" karibu sana na endelea kufurahia huduma zetu.");
            smsDto.setSourceAddr(senderName);
            log.info("Sender Name: {}", senderName);
            Recipient recipient = new Recipient();
            recipient.setRecipient_id(1);
            recipient.setDest_addr(clientMobile);
            List<Recipient> recipients = Collections.singletonList(recipient);
            smsDto.setRecipients(recipients);
            smsService.sendSms(smsDto);
        }
    }
}
