package to.co.divinesolutions.tenors.notice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import to.co.divinesolutions.tenors.entity.Notice;
import to.co.divinesolutions.tenors.notice.dto.NoticeDto;
import to.co.divinesolutions.tenors.notice.dto.NoticeResponse;
import to.co.divinesolutions.tenors.notice.service.NoticeService;
import to.co.divinesolutions.tenors.utils.Response;

import java.util.List;
@RestController
@RequestMapping("notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @PostMapping
    public Response<Notice> save(@RequestBody NoticeDto dto){
        return noticeService.save(dto);
    }

    @GetMapping("{uid}")
    public Response<NoticeResponse> findByUid(@PathVariable String uid){
        return noticeService.findByUid(uid);
    }

    @DeleteMapping("{uid}")
    public Response<Notice> delete(@PathVariable String uid){
        return noticeService.delete(uid);
    }

    @GetMapping
    public List<NoticeResponse> notices(){
        return noticeService.notices();
    }

    @GetMapping("/rental/{uid}")
    public List<Notice> rentalNotices(@PathVariable String uid){
        return noticeService.getNoticeByRental(uid);
    }

    @GetMapping("/property/{uid}")
    public List<Notice> propertyNotices(@PathVariable String uid){
        return noticeService.getNoticeByProperty(uid);
    }
}
