package to.co.divinesolutions.tenors.bill_and_payment.service;

import to.co.divinesolutions.tenors.bill_and_payment.dto.BillDetails;
import to.co.divinesolutions.tenors.bill_and_payment.dto.BillDto;
import to.co.divinesolutions.tenors.entity.Bill;
import to.co.divinesolutions.tenors.entity.Payment;
import to.co.divinesolutions.tenors.utils.Response;

import java.util.List;
import java.util.Optional;

public interface BillService {
    Response<Bill> saveBill(BillDto dto);

    Optional<Bill> getOptionalByUid(String uid);

    Optional<Bill> getOptionalByBillReference(String billReference);

    List<BillDetails> billDetailsList(String userUid);

    List<Bill>  getPropertyBills(String userUid);

    Response<BillDetails> findByUid(String uid);

    Response<Bill> delete(String uid);

    void affectBill(Payment payment);
}
