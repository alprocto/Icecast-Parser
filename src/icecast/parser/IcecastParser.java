/*
 * Copyright (C) 2014 alprocto
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
        IcecastLog.filterRemoveIP(newLogList, "127.0.0.1");
        
        System.out.println(IcecastLog.athPerDay(newLogList));
        System.out.println(IcecastLog.listenersPerDay(newLogList, 500));
        System.out.println(IcecastLog.athPerHourPerDay(newLogList));
        // TODO code application logic here
    }

}
