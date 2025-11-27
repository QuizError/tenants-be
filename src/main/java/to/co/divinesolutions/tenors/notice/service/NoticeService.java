package to.co.divinesolutions.tenors.notice.service;

import to.co.divinesolutions.tenors.entity.Notice;
import to.co.divinesolutions.tenors.notice.dto.NoticeDto;
import to.co.divinesolutions.tenors.notice.dto.NoticeResponse;
import to.co.divinesolutions.tenors.utils.Response;

import java.util.List;
import java.util.Optional;

public interface NoticeService {
    Response<Notice> save(NoticeDto dto);

    Optional<Notice> getOptionalByUid(String uid);

    Response<NoticeResponse> findByUid(String uid);

    Response<Notice> delete(String uid);

    List<NoticeResponse> notices();

    List<Notice> getNoticeByRental(String rentalUid);

    List<Notice> getNoticeByProperty(String propertyUid);
}
