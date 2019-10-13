package cfig.component

import org.apache.mina.core.service.IoHandlerAdapter
import org.apache.mina.core.session.IdleStatus
import org.apache.mina.core.session.IoSession
import org.springframework.stereotype.Component
import org.slf4j.LoggerFactory
import java.io.PrintWriter
import java.io.StringWriter
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import cfig.Jargon
import cfig.Jargon.MSG_TYPE
import cfig.p2phunter.component.*
import cfig.p2phunter.th.SendListingIdReq2
import java.net.InetSocketAddress
import cfig.p2phunter.th.SniperReq
import cfig.p2phunter.th.SniperResp
import cfig.p2psniper.ppdsdk.PpdServiceBase
import cfig.p2psniper.ppdsdk.params.PpdResp
import org.springframework.context.ApplicationContext
import org.springframework.context.support.AbstractApplicationContext

@Component
class ObjHandler : IoHandlerAdapter() {
    companion object {
        private val log = LoggerFactory.getLogger(ObjHandler::class.java)
        private val REMOTE_IP = "remote_ip"
    }

    private val mapper2 = ObjectMapper()

    init {
        mapper2.propertyNamingStrategy = PpdServiceBase.PpdNamingStrategy()
    }

    @Throws(Exception::class)
    override fun exceptionCaught(session: IoSession, cause: Throwable) {
        val sw = StringWriter()
        cause.printStackTrace(PrintWriter(sw))
        log.error(sw.toString())
    }

    override fun sessionCreated(session: IoSession?) {
        super.sessionCreated(session)
        session!!.setAttribute(REMOTE_IP, (session.remoteAddress as InetSocketAddress).address.hostAddress.toString())
    }

    private fun consumeJaron(message: Jargon, session: IoSession) {
        log.debug("jargon ${message.theMsgType}")
        when (message.theMsgType) {
            MSG_TYPE.TIME -> {
                log.info("msg: calibrate time");
                val tsNow = System.currentTimeMillis()
                val tsBeforeSend = message.theParam as Long
                log.info("time cost ${tsNow - tsBeforeSend}ms")
            }
            MSG_TYPE.PPD_LISTING -> {
                val msgData = ObjectMapper().readValue(message.theCmd, SendListingIdReq2::class.java)
                fetcher!!.onListingReceived4(msgData.ids, session.getAttribute(REMOTE_IP).toString())
            }
            MSG_TYPE.BID_STATISTICS -> {
                log.info("calc BID_STATISTICS")
                bs!!.calcBid(null)
            }
            MSG_TYPE.SYS_EXIT -> {
                log.info("msg: " + message.theMsgType + ", shutdown now ...")
                (ctx as AbstractApplicationContext).close()
            }
            MSG_TYPE.PPD_PRE_DIG -> {
                detailFetcher!!.discoverListing()
            }
            MSG_TYPE.PPD_REVIEW_PENDING -> {
                if (message.theCmd.isNotBlank() && message.theCmd == "bottom") {
//                    detailFetcher!!.historyReviewListing()
                } else {
//                    detailFetcher!!.reviewListing()
                }
            }
            MSG_TYPE.PPD_LISTING_DETAIL -> {
                val msgData = ObjectMapper().readValue(message.theCmd, SniperResp.PreBidResp::class.java)
                log.debug("Got listingDetail: ${msgData.loanList.size}")
                detailFetcher!!.eatListings(msgData)
            }
            MSG_TYPE.PPD_REQ_LOAN_STATUS -> {
//                val msgData = ObjectMapper().readValue(message.theCmd, SniperReq.LoanIdList::class.java)
//                log.info(ObjectMapper().writeValueAsString(msgData))
                histoyLoanWorker!!.requestLoanStatus(message)
            }
            MSG_TYPE.PPD_RESP_LOAN_STATUS -> {
                val msgData = ObjectMapper().readValue(message.theCmd, PpdResp.ListingStatusResp::class.java)
                histoyLoanWorker!!.responseLoanStatus(msgData)
            }
            else -> {
                log.error("Unsupported MSG_TYPE:" + message.theMsgType)
            }
        }
    }

    @Throws(Exception::class)
    override fun messageReceived(session: IoSession, message: Any) {
        if (message is Jargon) {
            Thread(Runnable { consumeJaron(message, session) }).start()
        } else {
            log.info("Unknown message")
        }
        //session.closeNow()
    }

    @Throws(Exception::class)
    override fun sessionIdle(session: IoSession, status: IdleStatus) {
        log.info("IDLE " + session.getIdleCount(status))
        if (session.getIdleCount(status) > 10) {
            session.closeOnFlush()
        }
    }

    @Autowired
    private val histoyLoanWorker: HistoryLoanWorker? = null

    @Autowired
    private val fetcher: FullPagesFetcher? = null

    @Autowired
    private val bs: BidStatistics? = null

    @Autowired
    private val loanDigger: LoanDigger? = null

    @Autowired
    private val detailFetcher: DetailFetcher? = null

    @Autowired
    private val ctx: ApplicationContext? = null
}
