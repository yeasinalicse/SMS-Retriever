package yeasinalicse.com.sms_retriever.util;

/**
 * Created by Yeasin on 1/23/18.
 */

public class API {

    public static class URL {
        private static String _TYPE = "http";

      public static String _HOST = "192.168.111.194";
        public static String _PORT = "8080";
        private static String _URL = _TYPE + "://" + _HOST + ":" + _PORT + "/your_api/";
        public static class COMMON {
            private static String _controller = _URL + "CommonApi/";
            public static String SEND_VERIFY_SMS = _controller + "send-verify-code";
        }






    }



}
