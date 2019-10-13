package cfig.p2psniper.ppdsdk.params

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test

class PpdRespTest {
    @Test
    fun parseNewBidResp() {
        val retMsg = "{\"Amount\":50,\"ListingId\":124323972,\"OrderId\":\"5b7845b99e0c57405eefe0ae\",\"Result\":9999,\"ResultCode\":null,\"ResultMessage\":\"投标处理中\"}"
        val ret = ObjectMapper().readValue(retMsg, PpdResp.BidResp2::class.java)
        Assert.assertEquals(50, ret.Amount)
        Assert.assertEquals(9999, ret.Result)
    }

    @Test
    fun parseQueryNewBidResp() {
        val retMsg = "{\"result\":1,\"resultMessage\":\"成功\",\"resultContent\":{\"bidMessage\":\"成功\",\"bidAmount\":50.0000,\"participationAmount\":50.0000,\"succCouponAmount\":0.0000,\"bidId\":579094335,\"listingId\":124323972}}"
        ObjectMapper().readValue(retMsg, PpdResp.BidQueryResp2::class.java)
    }
}