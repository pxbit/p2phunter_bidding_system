package cfig;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Jargon implements Serializable {
    // static
    private static final long serialVersionUID = 8877540726879659479L;
    private static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final int payloadLength = 60;
    private static final boolean DBG = false;
    private static final Logger log = LoggerFactory.getLogger(Jargon.class);

    // members - id
    private InetAddress theSrcIP;
    private InetAddress theDstIP;
    private int theSrcPort = 0;
    private int theDstPort = 0;
    private UUID theUUId;
    private Timestamp theTime;

    // members - real message
    private MSG_TYPE theMsgType;
    private String theCmd;
    private Object theParam;

    private String autoWrapper(String firstPre, String inPre, String inS, String inSuf) {
        // set parameters
        inPre = (null == inPre) ? " " : inPre;// prefix: line leader
        inSuf = (null == inSuf) ? " " : inSuf;// suffix: line ender
        firstPre = (null == firstPre) ? inPre : firstPre;// firstPre: 1st line leader

        // null String will be set to " "
        inS = (null == inS) ? " " : inS;
        String retS = "";
        String[] convertedS = inS.split("\n");
        int outTrimmer = 0;
        for (int i = 0; i < convertedS.length; i++) {
            // System.out.println("String " + i + ":" + convertedS[i]);
            retS += autoWrapperSlave(firstPre, inPre, convertedS[i], inSuf, outTrimmer);
            // System.out.println("==" + retS);
            outTrimmer++;
        }

        return retS;
    }

    // trimmer: is this the 1st line or not
    private String autoWrapperSlave(String firstPre, String inPre, String inS, String inSuf, int trimmer) {
        String retS = "";
        String lastString = inS;
        while (lastString.length() > payloadLength) {
            if (DBG) {
                log.info("lastStringLength: " + lastString.length() + ", is: " + lastString);
            }
            if (0 == trimmer) {
                retS += (firstPre + lastString.substring(0, payloadLength) + inSuf + "\n");

            } else {
                retS += (inPre + lastString.substring(0, payloadLength) + inSuf + "\n");
            }
            trimmer++;
            lastString = lastString.substring(payloadLength);
        }
        // format last line
        String postPadding = new String(new char[payloadLength - lastString.length()]).replace("\0", " ");
        if (0 == trimmer) {
            retS += (firstPre + lastString + postPadding + inSuf + "\n");
        } else {
            retS += (inPre + lastString + postPadding + inSuf + "\n");
        }
        return retS;
    }

    @Override
    public String toString() {
        String aS = "";
        aS += "\n/---------------------------------------------------------------------\\\n";
        aS += autoWrapper("| From-To:", "|        :", theSrcIP.toString() + ":" + theSrcPort + "-->" + theDstIP.toString() + ":" + theDstPort, "|");
        aS += autoWrapper("| UUID   :", "|        :", (null == theUUId ? null : theUUId.toString()), "|");
        aS += autoWrapper("| Time   :", "|        :", (null == theTime ? null : sdf1.format(theTime)), "|");
        aS += autoWrapper("| MsgType:", "|        :", theMsgType.toString(), "|");
        aS += autoWrapper("| Command:", "|        :", theCmd, "|");
        aS += autoWrapper("| Param  :", "|        :", ((null == theParam ? null : theParam.toString())), "|");
        aS += "\\---------------------------------------------------------------------/";
        return aS;
    }

    // ---------------------------------------------------------------------------------
    //                         Auto Generated Code
    // ---------------------------------------------------------------------------------

    public InetAddress getTheSrcIP() {
        return theSrcIP;
    }

    public void setTheSrcIP(InetAddress theSrcIP) {
        this.theSrcIP = theSrcIP;
    }

    public InetAddress getTheDstIP() {
        return theDstIP;
    }

    public void setTheDstIP(InetAddress theDstIP) {
        this.theDstIP = theDstIP;
    }

    public UUID getTheUUId() {
        return theUUId;
    }

    public void setTheUUId(UUID theUUId) {
        this.theUUId = theUUId;
    }

    public Timestamp getTheTime() {
        return theTime;
    }

    public void setTheTime(Timestamp theTime) {
        this.theTime = theTime;
    }

    public MSG_TYPE getTheMsgType() {
        return theMsgType;
    }

    public void setTheMsgType(MSG_TYPE theMsgType) {
        this.theMsgType = theMsgType;
    }

    public String getTheCmd() {
        return theCmd;
    }

    public void setTheCmd(String theCmd) {
        this.theCmd = theCmd;
    }

    public Object getTheParam() {
        return theParam;
    }

    public void setTheParam(Object theParam) {
        this.theParam = theParam;
    }

    public int getTheSrcPort() {
        return theSrcPort;
    }

    public void setTheSrcPort(int theSrcPort) {
        this.theSrcPort = theSrcPort;
    }

    public int getTheDstPort() {
        return theDstPort;
    }

    public void setTheDstPort(int theDstPort) {
        this.theDstPort = theDstPort;
    }

    public Jargon() {
        this.theSrcIP = InetAddress.getLoopbackAddress();
        this.theDstIP = InetAddress.getLoopbackAddress();
        this.theSrcPort = 4570;
        this.theDstPort = 4571;
        this.theUUId = UUID.randomUUID();
        this.theTime = new Timestamp(System.currentTimeMillis());
        this.theMsgType = MSG_TYPE.MSG_TYPE_BYE;
        this.theCmd = "";
        this.theParam = "";
    }

    public Jargon(InetAddress theSrcIP, InetAddress theDstIP, int theSrcPort, int theDstPort, UUID theUUId,
                  Timestamp theTime, MSG_TYPE theMsgType, String theCmd, Object theParam) {
        super();
        this.theSrcIP = theSrcIP;
        this.theDstIP = theDstIP;
        this.theSrcPort = theSrcPort;
        this.theDstPort = theDstPort;
        this.theUUId = theUUId;
        this.theTime = theTime;
        this.theMsgType = theMsgType;
        this.theCmd = theCmd;
        this.theParam = theParam;
    }

    public Jargon ackJargon() {
        Jargon tempJ = new Jargon();
        tempJ.setTheCmd(this.theCmd);
        tempJ.setTheDstIP(this.theSrcIP);
        tempJ.setTheDstPort(this.theSrcPort);
        tempJ.setTheMsgType(MSG_TYPE.MSG_TYPE_ACK);
        tempJ.setTheParam(this.theParam);
        tempJ.setTheSrcIP(this.theDstIP);
        tempJ.setTheSrcPort(this.theDstPort);
        tempJ.setTheUUId(this.theUUId);
        tempJ.setTheTime(new Timestamp(System.currentTimeMillis()));
        return tempJ;
    }

    public enum MSG_TYPE {
        TIME,
        PPD_LISTING,
        PPD_DIG_REQUEST,
        BID_STATISTICS,
        SYS_EXIT,
        PPD_PRE_DIG,
        PPD_REVIEW_PENDING,
        PPD_LISTING_DETAIL,
        PPD_REQ_LOAN_STATUS,
        PPD_RESP_LOAN_STATUS,

        // general - ack 200 OK
        MSG_TYPE_ACK,
        // general - got ack, 88
        MSG_TYPE_BYE
    }
}

/******************************************************************************
 * <EOF>
 *****************************************************************************/
