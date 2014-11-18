package org.jboss.jbpm;

import org.jboss.jbpm.impl.bpmsClientConfig;

/**
 * Created by aubbiali on 05/11/14.
 */


public class bpmsClientMain {

       //  private static bpmsClientApp app;

        final static int MAX_INSTANCES=1;





        public static void main(String[] args) {

            String s=null;
            if (args.length> 0) s=args[0];

            bpmsClientConfig config = new bpmsClientConfig();

            config.parseJSON(s);

            for (int i=0; i <MAX_INSTANCES; i++) {
                bpmsClientThread t = new bpmsClientThread("T"+i);
            }




           // app = new bpmsClientApp();

           //  app.run();

        }


    }
