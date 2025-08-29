package to.co.divinesolutions.tenors.bill_and_payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import to.co.divinesolutions.tenors.bill_and_payment.dto.BillDetails;
import to.co.divinesolutions.tenors.bill_and_payment.service.BillService;
import to.co.divinesolutions.tenors.utils.Response;

import java.util.List;

@RestController
@RequestMapping("bills")
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;

    @GetMapping("/my-properties/{uid}")
    public List<BillDetails> paymentList(@PathVariable("uid") String uid){
        return billService.billDetailsList(uid);
    }

    @GetMapping("{uid}")
    public Response<BillDetails> findByUid(@PathVariable String uid){
        return billService.findByUid(uid);
    }
}
