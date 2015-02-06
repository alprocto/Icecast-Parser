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

import java.util.ArrayList;

/**
 *
 * @author alprocto@gmail.com
 * @version 2014/10/16
 */
public class IcecastLog {

    private static final String[] monthsOfTheYear = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    long ipAddressDec;
    String ipAddressString;
    String userIdentifier;
    String userID;
    int dayDec;
    String dayString;
    int monthDec;
    String monthString;
    int yearDec;
    String yearString;
    int hourDec;
    String hourString;
    int minuteDec;
    String minuteString;
    int secondDec;
    String secondString;
    int timeZoneDec;
    String timeZoneString;
    String requestLine;
    int httpStatusCode;
    long size;
    String referer;
    String clientSoftware;
    long listenerTime;

    public IcecastLog(String input) {
        int currentIndex = 0;
        int markerIndex;
        String date;
        String temp;

        //get ipAddressDec
        markerIndex = input.indexOf(" ", currentIndex);                         //find ip address
        ipAddressString = input.substring(currentIndex, markerIndex);                        //get ip address
        //convert ip address from string to long
        String[] addrArray = ipAddressString.split("\\.");
        ipAddressDec = 0;
        for (int i = 0; i < addrArray.length; i++) {
            int power = 3 - i;
            ipAddressDec += ((Integer.parseInt(addrArray[i]) % 256 * Math.pow(256, power)));
        }
        currentIndex = markerIndex + 1;                                         //increment currentIndex

        //get userIndentifier
        markerIndex = input.indexOf(" ", currentIndex);                         //find userIndentifier
        userIdentifier = input.substring(currentIndex, markerIndex);            //set userIndentifier
        currentIndex = markerIndex + 1;                                         //increment currentIndex

        //get userID
        markerIndex = input.indexOf(" ", currentIndex);
        userID = input.substring(currentIndex, markerIndex);
        currentIndex = markerIndex + 1;

        //time and date
        markerIndex = input.indexOf("]", currentIndex);
        date = input.substring(currentIndex, markerIndex);                      //format [10/Oct/2000:13:55:36 -0700]

        dayString = date.substring(1, 3);                                            // get dayDec
        dayDec = Integer.parseInt(dayString);                                           // set dayDec

        monthString = date.substring(4, 7);                                            // get monthDec
        for (int i = 0; i < monthsOfTheYear.length; i++) {                      // convert from string to int
            if (monthString.equals(monthsOfTheYear[i])) {
                monthDec = i + 1;                                                  // set monthDec
                break;
            }
        }

        yearString = date.substring(8, 12);                                           // get yearDec
        yearDec = Integer.parseInt(yearString);                                          // set yearDec

        hourString = date.substring(13, 15);                                          // get hourDec
        hourDec = Integer.parseInt(hourString);                                          // set hourDec

        minuteString = date.substring(16, 18);                                          // get minuteDec
        minuteDec = Integer.parseInt(minuteString);                                        // set minuteDec

        secondString = date.substring(19, 21);                                          // get secondDec
        secondDec = Integer.parseInt(secondString);                                        // set secondDec

        timeZoneString = date.substring(22, 27);                                          // get time zone
        timeZoneDec = Integer.parseInt(timeZoneString);                                      // set time zone

        currentIndex = markerIndex + 3;                                         //increment currentIndex

        //get requestLine
        markerIndex = input.indexOf("\"", currentIndex);
        requestLine = input.substring(currentIndex, markerIndex);
        currentIndex = markerIndex + 2;

        //get httpStatusCode
        markerIndex = input.indexOf(" ", currentIndex);
        temp = input.substring(currentIndex, markerIndex);
        httpStatusCode = Integer.parseInt(temp);
        currentIndex = markerIndex + 1;

        //get size
        markerIndex = input.indexOf(" ", currentIndex);
        temp = input.substring(currentIndex, markerIndex);
        size = Long.parseLong(temp);
        currentIndex = markerIndex + 2;

        //get referer
        markerIndex = input.indexOf("\"", currentIndex);
        referer = input.substring(currentIndex, markerIndex);
        currentIndex = markerIndex + 3;

        //get clientSoftware
        markerIndex = input.indexOf("\"", currentIndex);
        clientSoftware = input.substring(currentIndex, markerIndex);
        currentIndex = markerIndex + 2;

        //get listenerTime
        markerIndex = input.length();
        temp = input.substring(currentIndex, markerIndex);
        listenerTime = Long.parseLong(temp);

    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return ipAddressString + " " + userIdentifier + " " + userID + " [" + dayString + "/" + monthString + "/" + yearString + " " + timeZoneString + "] \"" + requestLine + "\" " + Integer.toString(httpStatusCode) + " " + Long.toString(size) + " \"" + referer + "\" \"" + clientSoftware + "\" " + Long.toString(listenerTime);
    }

    public String toStringTab() {
        return ipAddressString + "\t" + userIdentifier + "\t" + userID + "\t[" + dayString + "/" + monthString + "/" + yearString + " " + timeZoneString + "]\t\"" + requestLine + "\"\t" + Integer.toString(httpStatusCode) + "\t" + Long.toString(size) + "\t\"" + referer + "\"\t\"" + clientSoftware + "\"\t" + Long.toString(listenerTime);
    }

    public static ArrayList<IcecastLog> filterByRequestLine(ArrayList<IcecastLog> input, String filter) {
        ArrayList<IcecastLog> output = new ArrayList<IcecastLog>();
        for (int i = 0; i < input.size(); i++) {
            IcecastLog current = input.get(i);
            if (current.requestLine.startsWith(filter)) {
                output.add(current);
            }
        }
        return output;
    }

    public static void filterRemoveIP(ArrayList<IcecastLog> input, String filter) {
        String[] addrArray = filter.split("\\.");
        long filterDec = 0;
        for (int i = 0; i < addrArray.length; i++) {
            int power = 3 - i;
            filterDec += ((Integer.parseInt(addrArray[i]) % 256 * Math.pow(256, power)));
        }
        for (int i = 0; i < input.size();) {
            IcecastLog current = input.get(i);
            if (filterDec == current.ipAddressDec) {
                input.remove(i);
            } else {
                i++;
            }
        }
    }

    public static String athPerDay(ArrayList<IcecastLog> input) {
        String output = "\n";
        IcecastLog first = input.get(0);
        int currentYear = first.yearDec;
        int currentMonth = first.monthDec;
        int currentDay = first.dayDec;
        long currentATH = 0;

        for (int i = 0; i < input.size(); i++) {

            IcecastLog current = input.get(i);

            if (currentYear == current.yearDec && currentMonth == current.monthDec && currentDay == current.dayDec) {
                currentATH += current.listenerTime;
            } else {
                output = new String(output + (Integer.toString(currentYear) + "/" + Integer.toString(currentMonth) + "/" + Integer.toString(currentDay) + "\t" + Double.toString((double)currentATH / 3600) + "\n"));
                currentYear = current.yearDec;
                currentMonth = current.monthDec;
                currentDay = current.dayDec;
                currentATH = current.listenerTime;
            }

        }
        return output;
    }

    public static String athPerHourPerDay(ArrayList<IcecastLog> input) {
        String output = "\n";
        long[] currentDayATH = new long[24];
        long[] nextDayATH = new long[24];
        IcecastLog first = input.get(0);
        int currentYear = first.yearDec;
        int currentMonth = first.monthDec;
        int currentDay = first.dayDec;

        for (int i = 0; i < input.size(); i++) {

            IcecastLog current = input.get(i);

            if (currentYear == current.yearDec && currentMonth == current.monthDec && currentDay == current.dayDec) {
                long timeOffSet = current.minuteDec * 60 + current.secondDec;
                long timeLeft = current.listenerTime;
                if (timeLeft <= (3600 - timeOffSet)) {
                    currentDayATH[current.hourDec] += timeLeft;
                    timeLeft=0;
                } else {
                    currentDayATH[current.hourDec] += (3600 - timeOffSet);
                    timeLeft -= (3600 - timeOffSet);

                    for (int j = current.hourDec + 1; j < 24; j++) {
                        if (timeLeft <= 3600) {
                            currentDayATH[j] += timeLeft;
                            timeLeft=0;
                        } else {
                            currentDayATH[j] += 3600;
                            timeLeft -= 3600;
                        }
                    }
                    if (timeLeft > 0) {
                        for (int j = 0 + 1; j < 24; j++) {
                            if (timeLeft <= 3600) {
                                nextDayATH[j] += timeLeft;
                                timeLeft=0;
                            } else {
                                nextDayATH[j] += 3600;
                                timeLeft -= 3600;
                            }

                        }
                    }

                }
            } else {
                String ATH = "";
 
                for(int j=0;j<24;j++){
                    ATH = new String(ATH + "\t" + Double.toString((double)currentDayATH[j]/3600));
                }
                output = new String(output + (Integer.toString(currentYear) + "/" + Integer.toString(currentMonth) + "/" + Integer.toString(currentDay) + ATH + "\n"));
                currentDayATH=nextDayATH;
                nextDayATH = new long[24];
                currentYear = current.yearDec;
                currentMonth = current.monthDec;
                currentDay = current.dayDec;
                i--;
            }

        }
        return output;
    }

    public static String listenersPerDay(ArrayList<IcecastLog> input, int maxPossibleListeners) {
        String output = "\n";
        long[] listenerList = new long[maxPossibleListeners];
        IcecastLog first = input.get(0);
        int currentYear = first.yearDec;
        int currentMonth = first.monthDec;
        int currentDay = first.dayDec;
        int currentListenerCount = 0;
        int next = 0;

        for (int i = 0; i < input.size(); i++) {

            IcecastLog current = input.get(i);

            if (currentYear == current.yearDec && currentMonth == current.monthDec && currentDay == current.dayDec) {
                boolean newUser = true;
                for (int j = 0; j < next; j++) {
                    if (current.ipAddressDec == listenerList[j]) {
                        newUser = false;
                    }
                }
                if (newUser) {
                    currentListenerCount++;
                    listenerList[next] = current.ipAddressDec;
                    next++;
                }

            } else {
                output = new String(output + (Integer.toString(currentYear) + "/" + Integer.toString(currentMonth) + "/" + Integer.toString(currentDay) + "\t" + Integer.toString(currentListenerCount) + "\n"));
                currentYear = current.yearDec;
                currentMonth = current.monthDec;
                currentDay = current.dayDec;
                currentListenerCount = 1;

                listenerList = new long[maxPossibleListeners];
                listenerList[0] = current.ipAddressDec;
                next = 1;
            }

        }
        return output;
    }

}
