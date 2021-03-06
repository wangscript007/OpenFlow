package server.rtsp.protocol;

/**
 * Created by Dell on 8/9/2017.
 */

public class RtspProtocol {
    private static final String VERSION = " RTSP/1.0/r/n";
    public static final String STATUS_OK = "RTSP/1.0 200 OK";
    public static final String STATUS_BAD_REQUEST = "RTSP/1.0 400 Bad Request";
    public static final String STATUS_NOT_FOUND = "404 Not Found";
    public static final String STATUS_INTERNAL_SERVER_ERROR = "500 Internal Server Error";
    private static final String USER_AGENT = "User-Agent: YDZ Streaming Media v2017.08.10)";
    private static final String SERVER = "Server: YDZ RTSP Server";
    private static int seq = 2;
    public static String SESSION = "session123;timeout=60";

    private RtspProtocol() {
    }

    public static String requestTeardown(String address, String sessionId) {
        StringBuilder sb = new StringBuilder();
        sb.append("TEARDOWN ");
        sb.append(address);
        sb.append("/");
        sb.append(VERSION);
        sb.append("\r\n");
        sb.append("CSeq: ");
        sb.append(seq++);
        sb.append("\r\n");
        sb.append("User-Agent: YDZ Player /r/n");
        sb.append("Session: ");
        sb.append(sessionId);
        sb.append("\r\n");
        sb.append(USER_AGENT);
        sb.append("\r\n");
        return sb.toString();
    }

    public static String requestPlay(String address, String sessionId, long time) {
        StringBuilder sb = new StringBuilder();
        sb.append("PLAY ");
        sb.append(address);
        sb.append(VERSION);
        sb.append("\r\n");
        sb.append("Session: ");
        sb.append(sessionId);
        sb.append("CSeq: ");
        sb.append(seq++);
        sb.append("\r\n");
        sb.append(USER_AGENT);
        sb.append("\r\n");
        sb.append("Range: npt=");
        sb.append(time);
        sb.append("-\r\n");
        sb.append("\r\n");
        return sb.toString();
    }

    public static String requestSetup(String address, String trackID, int vPort, int aPort) {
        StringBuilder sb = new StringBuilder();
        sb.append("SETUP ");
        sb.append(address);
        sb.append("/");
        sb.append("trackID=");
        sb.append(trackID);
        sb.append(VERSION);
        sb.append("\r\n");
        sb.append("CSeq: ");
        sb.append(seq++);
        sb.append("\r\n");
        sb.append("Transport: RTP/AVP;unicast;client_port=");//RTP/AVP;UNICAST;client_port=16264-16265;mode=requestPlay/r/n
        sb.append(vPort);
        sb.append("-");
        sb.append(aPort);
        sb.append("\r\n");
        sb.append(USER_AGENT);
        sb.append("\r\n");
        return sb.toString();
    }

    public static String requestOptions(String address) {
        StringBuilder sb = new StringBuilder();
        sb.append("OPTIONS ");
        sb.append(address);
        sb.append(VERSION);
        sb.append("\r\n");
        sb.append("CSeq: ");
        sb.append(seq++);
        sb.append("\r\n");
        sb.append(USER_AGENT);
        sb.append("\r\n");
        return sb.toString();
    }

    public static String requestDescribe(String address) {
        StringBuilder sb = new StringBuilder();
        sb.append("DESCRIBE ");
        sb.append(address);
        sb.append(VERSION);
        sb.append("\r\n");
        sb.append("CSeq: ");
        sb.append(seq++);
        sb.append("\r\n");
        sb.append(USER_AGENT);
        sb.append("\r\n");
        return sb.toString();
    }

    public static String requestPause(String address, String sessionId) {
        StringBuilder sb = new StringBuilder();
        sb.append("PAUSE ");
        sb.append(address);
        sb.append("/");
        sb.append(VERSION);
        sb.append("\r\n");
        sb.append("CSeq: ");
        sb.append(seq++);
        sb.append("\r\n");
        sb.append("Session: ");
        sb.append(sessionId);
        sb.append("\r\n");
        sb.append(USER_AGENT);
        sb.append("\r\n");
        return sb.toString();
    }

    //==============================下面是服务端回应========================================

    private static StringBuilder createResponse(String seq, boolean isNoData) {
        StringBuilder sb = new StringBuilder();
        sb.append(STATUS_OK);
        sb.append("\r\n");
        sb.append("CSeq: ");
        sb.append(seq);
        sb.append("\r\n");
        sb.append(SERVER);
        sb.append("\r\n");
        sb.append("Cache-Control: no-cache\r\n");
        if (isNoData) {
            sb.append("Content-Length: 0\r\n");
        }
        return sb;
    }

    public static String responseGetParameter(String seq) {
        StringBuilder sb = createResponse(seq, false);
        sb.append("\r\n");
        return sb.toString();
    }


    public static String responseTeardown(String seq) {
        StringBuilder sb = createResponse(seq, false);
        sb.insert(0, "TEARDOWN \r\n");
        return sb.toString();
    }

    public static String responsePlay(String seq, String address, int port, boolean isHasVideo, boolean isHasSound) {
        StringBuilder sb = createResponse(seq, false);
        sb.append("RTP-Info: ");
        if (isHasVideo) {
            sb.append("url=rtsp://");
            sb.append(address);
//            sb.append(":");
//            sb.append(port);
            sb.append("/trackID=0;seq=1;rtptime=0,");
        }
        if (isHasSound) {
            sb.append("url=rtsp://");
            sb.append(address);
//            sb.append(":");
//            sb.append(port);
            sb.append("/trackID=1;seq=1;rtptime=0");
        }
        sb.append("\r\n");
//        sb.append("Range: npt=0.0-596.48\r\n");
        sb.append("Session: ");
        sb.append(SESSION);
        sb.append("\r\n\r\n");
        return sb.toString();
    }

//    public static String responsePlay(String seq, String rtpInfo) {
//        StringBuilder sb = createResponse(seq);
//        sb.append("RTP-Info: ");
//        sb.append(rtpInfo);
//        sb.append("\r\n");
//        sb.append("Session: ");
//        sb.append(SESSION);
//        sb.append("\r\n");
//        return sb.toString();
//    }

    public static String responseSetup(String seq, String transport) {
        StringBuilder sb = createResponse(seq, false);
        sb.append("Transport: ");
        sb.append(transport);
        sb.append("\r\n");
        sb.append("Session: ");
        sb.append(SESSION);
        sb.append("\r\n\r\n");
        return sb.toString();
    }

    public static String responseOptions(String seq) {
        StringBuilder sb = createResponse(seq, true);
        sb.append("Public: DESCRIBE, SETUP, TEARDOWN, PLAY, PAUSE, OPTIONS, ANNOUNCE, RECORD, GET_PARAMETER\r\n");
        sb.append("Supported: play.basic, con.persistent\r\n");
        sb.append("\r\n");
        return sb.toString();
    }

    /**
     * Describe 回应
     *
     * @param address 推流端ip和端口
     * @param seq
     * @param data
     * @return
     */
    public static String responseDescribe(String address, String seq, String data) {
        StringBuilder sb = createResponse(seq, false);
        sb.append("Content-Length: " + data.length());
        sb.append("\r\n");
        sb.append("Content-Base: rtsp://" + address + "/\r\n");//推流端ip和端口
        sb.append("Content-Type: application/sdp\r\n");
        sb.append("Session: ");
        sb.append(SESSION);
        sb.append("\r\n\r\n");
        sb.append(data);
        return sb.toString();
    }

    public static String responsePause(String seq) {
        return responseGetParameter(seq);
//        StringBuilder sb = createResponse(seq, false);
//        sb.append("\r\n");
//        return sb.toString();
    }

    /**
     * 返回错误响应码
     *
     * @param status RtspProtocol.STATUS_BAD_REQUEST RtspProtocol.STATUS_NOT_FOUND RtspProtocol.STATUS_INTERNAL_SERVER_ERROR
     * @return
     */
    public static String responseError(String status, String seq) {
        StringBuilder sb = createResponse(seq, false);
        sb.replace(0, STATUS_OK.length(), status);
        return sb.toString();
    }
}
