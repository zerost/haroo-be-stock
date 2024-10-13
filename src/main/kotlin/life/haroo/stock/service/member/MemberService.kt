package life.haroo.stock.service.member;

import life.haroo.stock.repository.account.MemberSecuritiesInfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono
import java.util.UUID

@Service
@Transactional(readOnly = true)
class MemberService(
  private val memberSecuritiesInfoRepository: MemberSecuritiesInfoRepository
) {
    fun getMemberSecuritiesInfoMono(memberId: UUID) = memberSecuritiesInfoRepository.findByMemberId(memberId)
            .collectList()
            .flatMap { memberInfos ->
        if (!memberInfos.isEmpty()) {
            if (memberInfos.size == 1) {
                Mono.just(memberInfos.first())
            } else {
                Mono.error(RuntimeException("error")) // TODO EXCEPTION 처리 - 여러개의 정보가 있을때
            }
        } else {
            Mono.error(RuntimeException("error")) // TODO EXCEPTION 처리 - 정보가 없을때
        }
    }
}
