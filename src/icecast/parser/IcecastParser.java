 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icecast.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alprocto@gmail.com
 * @version 2014/10/16
 */
public class IcecastParser {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        BufferedReader input = new BufferedReader(new FileReader("test.log"));
        ArrayList<IcecastLog> logList = new ArrayList<IcecastLog>();
        while (input.ready()) {
            logList.add(new IcecastLog(input.readLine()));
        }
        ArrayList<IcecastLog> newLogList = IcecastLog.filterByRequestLine(logList, "GET /music ");
        IcecastLog.filterRemoveIP(newLogList, "152.18.67.19");
        IcecastLog.filterRemoveIP(newLogList, "127.0.0.1");
//        System.out.println(IcecastLog.athPerDay(newLogList));
//        System.out.println("");
//        System.out.println("");
        System.out.println(IcecastLog.listenersPerDay(newLogList, 500));
//        System.out.println(IcecastLog.athPerHourPerDay(newLogList));
//        
//        for(int i = 0; i<newLogList.size(); i++){
//            IcecastLog current = newLogList.get(i);
//            System.out.println(current.toString());
//        }

        
//        IcecastLog test = new IcecastLog("108.192.131.2 - - [09/Sep/2013:22:00:36 -0400] \"GET /music HTTP/1.1\" 404 106 \"-\" \"iTunes/11.0.5 (Windows; Microsoft Windows 7 x64 Home Premium Edition Service Pack 1 (Build 7601)) AppleWebKit/536.30.1\" 1234567890");
//        System.out.println(test.ipAddress);
//        System.out.println(test.userIdentifier);
//        System.out.println(test.userID);
//        System.out.println(test.day);
//        System.out.println(test.month);
//        System.out.println(test.year);
//        System.out.println(test.hour);
//        System.out.println(test.minute);
//        System.out.println(test.second);
//        System.out.println(test.timeZone);
//        System.out.println(test.requestLine);
//        System.out.println(test.httpStatusCode);
//        System.out.println(test.size);
//        System.out.println(test.referer);
//        System.out.println(test.clientSoftware);
//        System.out.println(test.listenerTime);
        // TODO code application logic here
    }

}
