package to.co.divinesolutions.tenors.bill_and_payment.projection;

import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface SoftwareCommissionUnpaid {
    @Value("#{target.group_ownership_id}")
    BigInteger getGroupOwnershipId();
    @Value("#{target.group_ownership_name}")
    String getGroupOwnershipName();
    @Value("#{target.currency}")
    String getCurrency();
    @Value("#{target.software_bill}")
    BigDecimal getSoftwareBill();

    @Value("#{target.total_bill_amount}")
    BigDecimal getTotalBillAmount();
}
