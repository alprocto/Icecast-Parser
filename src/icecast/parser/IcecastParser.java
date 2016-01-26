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
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

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

//       for(int i=0; i<newLogList.size(); i++){
//           IcecastLog current = newLogList.get(i);
//           System.out.println(current.toStringTab());
//       }
        
        
        
        IcecastLog.addIPFilter("127.0.0.1");
        IcecastLog.addIPFilter("152.18.67.19");
        IcecastLog.addRequestLineFilter("GET /music ");
        IcecastLog.addReplaceString("\"R2\"", "R2");

        String currentPath = "";
        final JFileChooser fc = new JFileChooser();

        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            currentPath = fc.getSelectedFile().getAbsolutePath();
            
            LinkedList<IcecastLog> logList = createListFromFile(currentPath);

            fc.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
            if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                currentPath = fc.getSelectedFile().getAbsolutePath();
            }
            try (PrintWriter writer = new PrintWriter(currentPath + "\\ATH_per_day.csv")) {
                writer.print(IcecastLog.athPerDay(logList).replace("\t", ","));
            }
            try (PrintWriter writer = new PrintWriter(currentPath + "\\listeners_per_day.csv")) {
                writer.print(IcecastLog.listenersPerDay(logList).replace("\t", ","));
            }
            try (PrintWriter writer = new PrintWriter(currentPath + "\\ATH_per_hour_per_day.csv")) {
                writer.print(IcecastLog.athPerHourPerDay(logList).replace("\t", ","));
            }

        }

//        System.out.println(IcecastLog.athPerDay(newLogList));
//        System.out.println(IcecastLog.listenersPerDay(newLogList));
//        System.out.println(IcecastLog.athPerHourPerDay(newLogList));
        // TODO code application logic here
    }

    public static LinkedList<IcecastLog> createListFromFile(String path) throws FileNotFoundException, IOException {
        BufferedReader input = new BufferedReader(new FileReader(path));
        LinkedList<IcecastLog> logList = new LinkedList<IcecastLog>();
        while (input.ready()) {
            IcecastLog temp = new IcecastLog(input.readLine());
            if (!IcecastLog.contansFilteredIP(temp) && !IcecastLog.contansFilteredRequestLine(temp)) {
                logList.add(temp);
            }
        }
        return logList;
    }

}
