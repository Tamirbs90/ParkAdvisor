package com.example.myapplication;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ImageDetailsFinder {
    private static final int DAYS_IN_WEEK = 7;
    private static final int HOURS_IN_DAY = 24;
    private static final int MINUTES_IN_HOUR = 60;
    private static final int SECONDS_IN_MINUTE = 60;
    private static final String CAN_PARK = "You can park here";
    private static final String CAN_NOT_PARK = "You are not allowed to park here";
    private static final String YES = "Yes";
    private static final String TEL_AVIV_PARKING_PERMIT = "Yes, unless you have Tel Aviv-Yafo parking permit.";
    private static final String GIVATAYIM_PARKING_PERMIT = "Yes, unless you have Giv'atayim parking permit.";
    private static final String NO = "No";
    private static final String BLANK = "-";
    private static final String UNLIMITED = "Unlimited Time";
    private long timer;
    private boolean matchFound = true;
    private String resultString;
    private String endOfTimeString;
    private String paymentString;
    private String remarksString;

    public long getTimer() {
        return timer;
    }

    public boolean getMatchFound() {
        return matchFound;
    }

    public String getResultString() {
        return resultString;
    }

    public String getEndOfTimeString() {
        return endOfTimeString;
    }

    public String getPaymentString() {
        return paymentString;
    }

    public String getRemarksString() {
        return remarksString;
    }

    public String getCanNotPark() {
        return CAN_NOT_PARK;
    }

    public String getUnlimited() {
        return UNLIMITED;
    }

    public void ShowDetails(int imageID, VehicleType type) {
        StringBuilder sbResult = new StringBuilder();
        StringBuilder sbEndOfParking = new StringBuilder();
        StringBuilder sbPayment = new StringBuilder();
        StringBuilder sbRemarks = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        String remarkString;
        int dayOfTheWeek = calendar.get(Calendar.DAY_OF_WEEK);
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        String currentTime = df.format(Calendar.getInstance().getTime());
        int nextDay = dayOfTheWeek + 1;

        switch (imageID) {
            case 0:
                matchFound = false;
                break;
            case 1:
                remarkString = "Parking sign validity is for the next 80 meters.";
                sbRemarks.append(remarkString);
                if (type == VehicleType.SCHOOL_BUS) {
                    sbResult.append(CAN_PARK);
                    sbPayment.append(NO);
                    sbEndOfParking.append(UNLIMITED);
                }
                else {
                    if (dayOfTheWeek == Calendar.SATURDAY || (dayOfTheWeek == Calendar.FRIDAY && currentTime.compareTo("13:00:00") > 0)) {
                        sbResult.append(CAN_PARK);
                        sbPayment.append(NO);
                        sbEndOfParking.append("Sunday at 08:30");
                        setTimer(dayOfTheWeek, 1, currentTime, "08:30:00");
                    }
                    else if (currentTime.compareTo("13:00:00") > 0 || currentTime.compareTo("08:30:00") < 0) {
                        if(currentTime.compareTo("13:00:00") > 0) {
                            sbEndOfParking.append("Tomorrow at 08:30");
                            timer = setTimer(dayOfTheWeek, nextDay, currentTime, "08:30:00");
                        }
                        else{
                            sbEndOfParking.append("08:30");
                            timer = setTimer(dayOfTheWeek, dayOfTheWeek, currentTime, "08:30:00");
                        }

                        sbResult.append(CAN_PARK);
                        sbPayment.append(NO);
                    }
                    else {
                        sbResult.append(CAN_NOT_PARK);
                        sbRemarks.append("\nParking area is for school bus boarding and drop-off.\nAfter 13:00 the parking is free of charge.");
                        sbEndOfParking.append("13:00");
                    }
                }
                break;
            case 2:
                if ((dayOfTheWeek == Calendar.FRIDAY && currentTime.compareTo("13:00:00") > 0) || dayOfTheWeek == Calendar.SATURDAY) {
                    sbResult.append(CAN_PARK);
                    sbRemarks.append("From Sunday at 08:00 the parking is for 3 hours in payment.");
                    sbPayment.append(NO);
                    sbEndOfParking.append("Sunday at 13:00");
                    timer = setTimer(dayOfTheWeek, 1, currentTime, "08:00:00");
                }
                else if(dayOfTheWeek == Calendar.FRIDAY && currentTime.compareTo("10:00:00") < 0 && currentTime.compareTo("08:00:00") >= 0){
                    sbResult.append(CAN_PARK);
                    sbRemarks.append(NO);
                    sbPayment.append(YES);

                    sbEndOfParking.append(showLimitOfParkingTime(currentTime, 3, 0, 0));
                    timer = setTimer(dayOfTheWeek, dayOfTheWeek, currentTime, showLimitOfParkingTime(currentTime, 3, 0, 0));
                }
                else if(dayOfTheWeek == Calendar.FRIDAY && currentTime.compareTo("10:00:00") >= 0){
                    if(dayOfTheWeek == Calendar.FRIDAY && currentTime.compareTo("13:00:00") < 0){
                        sbPayment.append(YES);
                        sbRemarks.append("After 13:00 the parking is free of charge.\n");
                    }
                    else{
                        sbPayment.append(YES);
                    }
                    sbResult.append(CAN_PARK);
                    sbRemarks.append("From Sunday at 08:00 the parking is in payment for 3 hours.");
                    sbEndOfParking.append("Sunday at 08:00");
                    timer = setTimer(dayOfTheWeek, 1, currentTime, "08:00:00");
                }
                else if (currentTime.compareTo("18:00:00") > 0) {
                    sbResult.append(CAN_PARK);
                    sbRemarks.append("After tomorrow at 08:00 the parking is for 3 hours in payment.");
                    sbPayment.append(NO);
                    sbEndOfParking.append("Tomorrow at 08:00");
                    timer = setTimer(dayOfTheWeek, nextDay, currentTime, "08:00:00");
                }
                else if(currentTime.compareTo("08:00:00") < 0){
                    sbResult.append(CAN_PARK);
                    sbRemarks.append("After 08:00 the parking is in payment for 3 hours.");
                    sbPayment.append(NO);
                    sbEndOfParking.append("08:00");
                    timer = setTimer(dayOfTheWeek, dayOfTheWeek, currentTime, "08:00:00");
                }
                else if (currentTime.compareTo("15:00:00") >= 0) {
                    if(currentTime.compareTo("18:00:00") >= 0){
                        sbPayment.append(NO);
                    }
                    else{
                        sbPayment.append(YES);
                        sbRemarks.append("After 18:00 the parking is free of charge until tomorrow at 08:00.\n");
                    }
                    sbResult.append(CAN_PARK);
                    sbRemarks.append("From tomorrow at 08:00 the parking is in payment for 3 hours.");
                    sbEndOfParking.append("Tomorrow at 08:00");
                    timer = setTimer(dayOfTheWeek, nextDay, currentTime, "08:00:00");
                } else {
                    sbResult.append(CAN_PARK);
                    sbRemarks.append(NO);
                    sbPayment.append(YES);
                    sbEndOfParking.append(showLimitOfParkingTime(currentTime, 3, 0, 0));
                    timer = setTimer(dayOfTheWeek, dayOfTheWeek, currentTime, showLimitOfParkingTime(currentTime, 3, 0, 0));
                }
                break;
            case 3:
                if (type == VehicleType.PRIVATE || type == VehicleType.TAXI || type == VehicleType.TWO_AND_HELF_UNTIL_4_TON) {
                    sbResult.append(CAN_PARK);
                    sbRemarks.append(NO);
                    sbPayment.append(NO);
                    sbEndOfParking.append(UNLIMITED);
                }
                else {
                    if (currentTime.compareTo("19:00:00") > 0 || currentTime.compareTo("06:00:00") < 0) {
                        sbResult.append(CAN_NOT_PARK);
                        sbRemarks.append("Parking not allowed for vehicles above 4 Ton.");
                        if(currentTime.compareTo("19:00:00") > 0 ) {
                            sbEndOfParking.append("Tomorrow at 06:00");
                        }
                        else{
                            sbEndOfParking.append("Tomorrow at 06:00");
                        }
                    } else {
                        sbResult.append(CAN_PARK);
                        sbRemarks.append("After 19:00 the parking is not allowed until tomorrow at 06:00.");
                        sbPayment.append(NO);
                        sbEndOfParking.append("19:00");
                        timer = setTimer(dayOfTheWeek, nextDay, currentTime, "19:00:00");
                    }
                }
                break;
            case 4:
                if (dayOfTheWeek == Calendar.SATURDAY || (dayOfTheWeek == Calendar.FRIDAY && currentTime.compareTo("13:00:00") > 0)) {
                    sbResult.append(CAN_PARK);
                    sbRemarks.append("From Sunday at 09:00 the parking is in payment unless you have Tel Aviv-Yafo parking permit.");
                    sbPayment.append(NO);
                    sbEndOfParking.append("Sunday at 09:00");
                    timer = setTimer(dayOfTheWeek, 1, currentTime, "09:00:00");
                } else if ((dayOfTheWeek == calendar.FRIDAY && currentTime.compareTo("09:00:00") > 0 && currentTime.compareTo("13:00:00") < 0)) {
                    sbResult.append(CAN_PARK);
                    sbRemarks.append("After 13:00 the parking is free of charge.");
                    sbPayment.append(TEL_AVIV_PARKING_PERMIT);
                    sbEndOfParking.append("Sunday at 09:00");
                    timer = setTimer(dayOfTheWeek, 1, currentTime, "09:00:00");
                } else if (!(dayOfTheWeek == calendar.SATURDAY) && !(dayOfTheWeek == calendar.FRIDAY) && currentTime.compareTo("09:00:00") > 0 && currentTime.compareTo("17:00:00") < 0) {
                    sbResult.append(CAN_PARK);
                    sbRemarks.append("From 17:00 the parking is just for vehicles with regional parking permit.");
                    sbPayment.append(TEL_AVIV_PARKING_PERMIT);
                    sbEndOfParking.append("17:00");
                    timer = setTimer(dayOfTheWeek, dayOfTheWeek, currentTime, "17:00:00");
                } else if (currentTime.compareTo("17:00:00") > 0 && !(dayOfTheWeek == calendar.SATURDAY) && !(dayOfTheWeek == calendar.FRIDAY)) {
                    sbResult.append(CAN_NOT_PARK);
                    sbRemarks.append("Parking is allowed only for vehicles with regional parking permit.\nFrom tomorrow at 09:00 the parking is in payment unless you have Tel Aviv-Yafo parking permit.");
                    sbEndOfParking.append("Tomorrow at 09:00");
                }
                else if(currentTime.compareTo("09:00:00") < 0 && !(dayOfTheWeek == calendar.SATURDAY)){
                    sbResult.append(CAN_NOT_PARK);
                    sbRemarks.append("Parking is allowed only for vehicles with regional parking permit.\nAfter 09:00 the parking is in payment unless you have Tel Aviv-Yafo parking permit.");
                    sbEndOfParking.append("09:00");
                }
                break;
            case 5:
                remarkString = "Parking sign validity is for the next 12 meters.";
                sbRemarks.append(remarkString);
                if(type == VehicleType.SCHOOL_BUS){
                    sbResult.append(CAN_PARK);
                    sbPayment.append(NO);
                    sbEndOfParking.append(UNLIMITED);
                }
                else {
                    if (!(dayOfTheWeek == calendar.SATURDAY) && currentTime.compareTo("07:30:00") > 0 && currentTime.compareTo("08:30:00") < 0) {
                        sbResult.append(CAN_NOT_PARK);
                        sbRemarks.append("\nParking area is just for passengers boarding and drop-off until 08:30.");
                        sbEndOfParking.append("08:30");
                    } else {
                        sbResult.append(CAN_PARK);
                        sbRemarks.append("\nFrom ");
                        sbPayment.append(NO);
                        if(dayOfTheWeek == calendar.SATURDAY || (dayOfTheWeek == calendar.FRIDAY && currentTime.compareTo("08:30:00") > 0)){
                            sbEndOfParking.append("Sunday at 07:30");
                            sbRemarks.append("Sunday at 07:30 ");
                            timer = setTimer(dayOfTheWeek, 1, currentTime, "07:30:00");
                        }
                        else if(currentTime.compareTo("07:30:00") < 0){
                            sbEndOfParking.append("07:30");
                            sbRemarks.append("07:30 ");
                            timer = setTimer(dayOfTheWeek, dayOfTheWeek, currentTime, "07:30:00");
                        }
                        else{
                            sbEndOfParking.append("Tomorrow at 07:30");
                            sbRemarks.append("Tomorrow at 07:30 ");
                            timer = setTimer(dayOfTheWeek, nextDay, currentTime, "07:30:00");
                        }

                        sbRemarks.append("the parking is forbidden for one hour.");
                    }
                }
                break;
            case 6:
                remarkString = "Parking sign validity is for the next 16 meters.";
                sbRemarks.append(remarkString);
                if (type == VehicleType.SCHOOL_BUS || type == VehicleType.ABOVE_4_TON || type == VehicleType.TWO_AND_HELF_UNTIL_4_TON) {
                    sbResult.append(CAN_NOT_PARK);
                    sbRemarks.append("Parking is not allowed for vehicles above 2.5 Ton.");
                    sbEndOfParking.append(BLANK);
                }                else {
                    if ((dayOfTheWeek == calendar.FRIDAY && currentTime.compareTo("09:00:00") > 0 && currentTime.compareTo("14:00:00") < 0) ||
                            (!(dayOfTheWeek == calendar.FRIDAY) && !(dayOfTheWeek == calendar.SATURDAY) && currentTime.compareTo("09:00:00") > 0 && currentTime.compareTo("17:00:00") < 0)) {
                        sbResult.append(CAN_NOT_PARK);
                        if (dayOfTheWeek == calendar.FRIDAY) {
                            sbEndOfParking.append("14:00.");
                            sbRemarks.append("\nAfter 14:00 ");
                        } else {
                            sbEndOfParking.append("17:00.");
                            sbRemarks.append("\nAfter 17:00 ");
                        }
                        sbRemarks.append("the parking is free of charge.");
                    }
                    else {
                        if (dayOfTheWeek == calendar.SATURDAY || (dayOfTheWeek == calendar.FRIDAY && currentTime.compareTo("14:00:00") > 0)) {
                            sbResult.append(CAN_PARK);
                            sbRemarks.append(remarkString);
                            sbPayment.append(NO);
                            sbEndOfParking.append("Sunday at 09:00");
                            sbRemarks.append("\nFrom Sunday at 09:00 the parking is forbidden.");
                            timer = setTimer(dayOfTheWeek, 1, currentTime, "09:00:00");
                        }
                        else if(!(dayOfTheWeek == calendar.FRIDAY) && currentTime.compareTo("17:00:00") > 0) {
                            sbResult.append(CAN_PARK);
                            sbRemarks.append(remarkString);
                            sbPayment.append(NO);
                            sbEndOfParking.append("Tomorrow at 09:00");
                            sbRemarks.append("\n From Tomorrow at 09:00 the parking is forbidden.");
                            timer = setTimer(dayOfTheWeek, nextDay, currentTime, "09:00:00");
                        }
                        else if(currentTime.compareTo("09:00:00") < 0){
                            sbResult.append(CAN_PARK);
                            sbRemarks.append(remarkString);
                            sbPayment.append(NO);
                            sbEndOfParking.append("09:00");
                            sbRemarks.append("\n After 09:00 the parking is forbidden.");
                            timer = setTimer(dayOfTheWeek, dayOfTheWeek, currentTime, "09:00:00");
                        }
                    }
                }
                break;
            case 7:
                remarkString = "Parking sign validity is for the next 12 meters.";
                sbRemarks.append(remarkString);
                if ((!(dayOfTheWeek == calendar.SATURDAY) && !(dayOfTheWeek == calendar.FRIDAY) && currentTime.compareTo("07:30:00") > 0 && currentTime.compareTo("08:30:00") < 0) ||
                        (!(dayOfTheWeek == calendar.SATURDAY) && !(dayOfTheWeek == calendar.FRIDAY) && currentTime.compareTo("15:30:00") > 0 && currentTime.compareTo("16:30:00") < 0) ||
                        (dayOfTheWeek == calendar.FRIDAY && currentTime.compareTo("07:30:00") > 0 && currentTime.compareTo("08:30:00") < 0) ||
                        (dayOfTheWeek == calendar.FRIDAY && currentTime.compareTo("12:30:00") > 0 && currentTime.compareTo("13:30:00") < 0)) {
                    sbResult.append(CAN_NOT_PARK);
                    sbRemarks.append("\nParking area is just for passengers boarding and drop-off until ");
                    if(currentTime.compareTo("08:30:00") < 0){
                        sbRemarks.append("08:30\nAfter 08:30 ");
                        sbEndOfParking.append("08:30");
                    }
                    else if(dayOfTheWeek == calendar.FRIDAY && currentTime.compareTo("13:30:00") < 0){
                        sbRemarks.append("13:30\nAfter 13:30 ");
                        sbEndOfParking.append("13:30");
                    }
                    else{
                        sbRemarks.append("16:30\nAfter 16:30 ");
                        sbEndOfParking.append("16:30");
                    }

                    sbRemarks.append("the parking is free of charge");
                }
                else{
                    sbResult.append(CAN_PARK);
                    sbPayment.append(NO);
                    sbRemarks.append("\nAfter ");
                    if((!(dayOfTheWeek == calendar.SATURDAY) && currentTime.compareTo("07:30:00") < 0)){
                        sbEndOfParking.append("07:30");
                        sbRemarks.append("07:30");
                        timer = setTimer(dayOfTheWeek, dayOfTheWeek, currentTime, "07:30:00");
                    }
                    else if(dayOfTheWeek == calendar.FRIDAY && currentTime.compareTo("12:30:00") < 0){
                        sbEndOfParking.append("12:30");
                        sbRemarks.append("12:30");
                        timer = setTimer(dayOfTheWeek, dayOfTheWeek, currentTime, "12:30:00");
                    }
                    else if(!(dayOfTheWeek == calendar.SATURDAY) && !(dayOfTheWeek == calendar.FRIDAY) && currentTime.compareTo("15:30:00") < 0){
                        sbEndOfParking.append("15:30");
                        sbRemarks.append("15:30");
                        timer = setTimer(dayOfTheWeek, dayOfTheWeek, currentTime, "15:30:00");
                    }
                    else if((dayOfTheWeek == calendar.FRIDAY && currentTime.compareTo("13:30:00") > 0) || dayOfTheWeek == calendar.SATURDAY){
                        sbEndOfParking.append("Sunday at 07:30");
                        sbRemarks.append("Sunday at 07:30");
                        timer = setTimer(dayOfTheWeek, 1, currentTime, "07:30:00");
                    }
                    else if(!(dayOfTheWeek == calendar.SATURDAY) && !(dayOfTheWeek == calendar.FRIDAY) && currentTime.compareTo("16:30:00") > 0){
                        sbEndOfParking.append("Tomorrow at 07:30");
                        sbRemarks.append("Tomorrow at 07:30");
                        timer = setTimer(dayOfTheWeek, nextDay, currentTime, "07:30:00");
                    }

                    sbRemarks.append(" the parking is forbidden for one hour.");
                }
                break;
            case 8:
                sbResult.append(CAN_NOT_PARK);
                sbRemarks.append("Parking area for police vehicles only.");
                sbEndOfParking.append(BLANK);
                break;
            case 9:
                if (dayOfTheWeek == Calendar.SATURDAY || (dayOfTheWeek == Calendar.FRIDAY && currentTime.compareTo("13:00:00") > 0)) {
                    sbResult.append(CAN_PARK);
                    sbPayment.append(NO);
                    sbEndOfParking.append("Sunday at 09:00");
                    sbRemarks.append("From Sunday at 09:00 the parking is in payment unless you have Tel Aviv-Yafo parking permit.");
                    timer = setTimer(dayOfTheWeek, 1, currentTime, "09:00:00");
                } else if ((dayOfTheWeek == calendar.FRIDAY && currentTime.compareTo("09:00:00") > 0 && currentTime.compareTo("13:00:00") < 0)) {
                    sbResult.append(CAN_PARK);
                    sbRemarks.append("After 13:00 the parking is free of charge.");
                    sbPayment.append(TEL_AVIV_PARKING_PERMIT);
                    sbEndOfParking.append("Sunday at 09:00:00");
                    timer = setTimer(dayOfTheWeek, 1, currentTime, "09:00:00");
                } else if (!(dayOfTheWeek == calendar.SATURDAY) && !(dayOfTheWeek == calendar.FRIDAY) && currentTime.compareTo("09:00:00") > 0 && currentTime.compareTo("19:00:00") < 0) {
                    sbResult.append(CAN_PARK);
                    sbRemarks.append("After 19:00 the parking is free of charge.");
                    sbPayment.append(TEL_AVIV_PARKING_PERMIT);
                    sbEndOfParking.append("Tomorrow at 09:00");
                    timer = setTimer(dayOfTheWeek, nextDay, currentTime, "09:00:00");
                } else if (currentTime.compareTo("19:00:00") > 0 && !(dayOfTheWeek == calendar.SATURDAY) && !(dayOfTheWeek == calendar.FRIDAY)) {
                    sbResult.append(CAN_PARK);
                    sbRemarks.append("From tomorrow at 09:00 the parking is in payment unless you have Tel Aviv-Yafo parking permit.");
                    sbPayment.append(NO);
                    sbEndOfParking.append("Tomorrow at 09:00");
                    timer = setTimer(dayOfTheWeek, nextDay, currentTime, "09:00:00");
                }
                else if(currentTime.compareTo("09:00:00") < 0 && !(dayOfTheWeek == calendar.SATURDAY)){
                    sbResult.append(CAN_PARK);
                    sbRemarks.append("After 09:00 the parking is in payment unless you have Tel Aviv-Yafo parking permit.");
                    sbPayment.append(NO);
                    sbEndOfParking.append("09:00");
                    timer = setTimer(dayOfTheWeek, dayOfTheWeek, currentTime, "09:00:00");
                }
                break;
            case 10:
                if((currentTime.compareTo("20:00:00") > 0 || currentTime.compareTo("08:00:00") < 0) && (type == VehicleType.ABOVE_4_TON || type == VehicleType.SCHOOL_BUS)) {
                    sbResult.append(CAN_NOT_PARK);
                    sbRemarks.append("Parking is not allowed for vehicle above 4 Ton.\n");
                    if (currentTime.compareTo("20:00:00") > 0) {
                        sbEndOfParking.append("Tomorrow at 08:00");
                        sbRemarks.append("After tomorrow at 08:00 the parking is free of charge.");
                    } else {
                        sbEndOfParking.append("08:00");
                        sbRemarks.append("After 08:00 the parking is free of charge.");
                    }
                }
                else if((currentTime.compareTo("08:00:00") > 0 && currentTime.compareTo("20:00:00") < 0) && (type == VehicleType.ABOVE_4_TON || type == VehicleType.SCHOOL_BUS)){
                    sbResult.append(CAN_PARK);
                    sbPayment.append(NO);
                    sbEndOfParking.append("20:00");
                    sbRemarks.append(NO);
                    timer = setTimer(dayOfTheWeek, dayOfTheWeek, currentTime, "20:00:00");
                }
                else {
                    sbResult.append(CAN_PARK);
                    sbPayment.append(NO);
                    sbEndOfParking.append(UNLIMITED);
                    sbRemarks.append(NO);
                }
                break;
            case 11:
                if((currentTime.compareTo("19:00:00") > 0 || currentTime.compareTo("06:00:00") < 0) && (type == VehicleType.ABOVE_4_TON || type == VehicleType.SCHOOL_BUS)) {
                    sbResult.append(CAN_NOT_PARK);
                    sbRemarks.append("Parking is not allowed for vehicle above 4 Ton.\n");
                    if (currentTime.compareTo("19:00:00") > 0) {
                        sbRemarks.append("After tomorrow at 06:00 the parking is free of charge.");
                        sbEndOfParking.append("Tomorrow at 06:00");
                    } else {
                        sbRemarks.append("After 06:00 the parking is free of charge.");
                        sbEndOfParking.append("06:00");
                    }
                }
                else{
                    sbResult.append(CAN_PARK);
                    sbPayment.append(NO);
                    sbEndOfParking.append(UNLIMITED);
                    sbRemarks.append(NO);
                }
                break;
            case 12:
                if(!(type == VehicleType.TAXI)){
                    sbResult.append(CAN_NOT_PARK);
                    sbEndOfParking.append(BLANK);

                }
                else {
                    sbResult.append(CAN_PARK);
                    sbPayment.append(NO);
                    sbEndOfParking.append(UNLIMITED);
                }
                sbRemarks.append("Taxi station area");
                break;
            case 13:
                if((!(dayOfTheWeek == calendar.SATURDAY) && !(dayOfTheWeek == calendar.FRIDAY) && currentTime.compareTo("07:00:00") > 0 && currentTime.compareTo("14:00:00") < 0)){
                    sbResult.append(CAN_NOT_PARK);
                    sbEndOfParking.append("14:00");
                    sbRemarks.append("After 14:00 the parking is free of charge.");
                }
                else if((!(dayOfTheWeek == calendar.SATURDAY) && !(dayOfTheWeek == calendar.FRIDAY) && !(dayOfTheWeek == calendar.THURSDAY) && currentTime.compareTo("14:00:00") > 0)) {
                    sbResult.append(CAN_PARK);
                    sbPayment.append(NO);
                    sbEndOfParking.append("Tomorrow at 07:00");
                    sbRemarks.append(NO);
                    timer = setTimer(dayOfTheWeek, nextDay, currentTime, "07:00:00");
                }
                else if((dayOfTheWeek == calendar.SATURDAY || dayOfTheWeek == calendar.FRIDAY || (dayOfTheWeek == calendar.THURSDAY) && currentTime.compareTo("14:00:00") > 0)) {
                    sbResult.append(CAN_PARK);
                    sbPayment.append(NO);
                    sbEndOfParking.append("Sunday at 07:00");
                    sbRemarks.append(NO);
                    timer = setTimer(dayOfTheWeek, 1, currentTime, "07:00:00");
                }
                else if((!(dayOfTheWeek == calendar.SATURDAY) && !(dayOfTheWeek == calendar.FRIDAY) && currentTime.compareTo("07:00:00") < 0)){
                    sbResult.append(CAN_PARK);
                    sbPayment.append(NO);
                    sbEndOfParking.append("07:00");
                    sbRemarks.append(NO);
                    timer = setTimer(dayOfTheWeek, dayOfTheWeek, currentTime, "07:00:00");
                }
                break;
            case 14:
                if(type == VehicleType.PRIVATE || type == VehicleType.TAXI) {
                    if ((!(dayOfTheWeek == calendar.SATURDAY) && !(dayOfTheWeek == calendar.FRIDAY) && currentTime.compareTo("07:00:00") > 0 && currentTime.compareTo("19:00:00") < 0)) {
                        sbResult.append(CAN_NOT_PARK);
                        sbEndOfParking.append("19:00");
                        sbRemarks.append("Parking area for loading and unloading for construction.After 19:00 the parking is free of charge until tomorrow at 07:00.");
                    } else if ((!(dayOfTheWeek == calendar.SATURDAY) && !(dayOfTheWeek == calendar.FRIDAY) && !(dayOfTheWeek == calendar.THURSDAY) && currentTime.compareTo("19:00:00") > 0)) {
                        sbResult.append(CAN_PARK);
                        sbPayment.append(NO);
                        sbEndOfParking.append("Tomorrow at 07:00");
                        sbRemarks.append(NO);
                        timer = setTimer(dayOfTheWeek, nextDay, currentTime, "07:00:00");
                    } else if ((dayOfTheWeek == calendar.SATURDAY || dayOfTheWeek == calendar.FRIDAY || dayOfTheWeek == calendar.THURSDAY) && currentTime.compareTo("19:00:00") > 0) {
                        sbResult.append(CAN_PARK);
                        sbPayment.append(NO);
                        sbEndOfParking.append("Sunday at 07:00");
                        sbRemarks.append(NO);
                        timer = setTimer(dayOfTheWeek, 1, currentTime, "07:00:00");
                    } else if ((!(dayOfTheWeek == calendar.SATURDAY) && !(dayOfTheWeek == calendar.FRIDAY) && currentTime.compareTo("07:00:00") < 0)) {
                        sbResult.append(CAN_PARK);
                        sbPayment.append(NO);
                        sbEndOfParking.append("07:00");
                        sbRemarks.append(NO);
                        timer = setTimer(dayOfTheWeek, dayOfTheWeek, currentTime, "07:00:00");
                    }
                }
                else{
                    sbResult.append(CAN_NOT_PARK);
                    sbEndOfParking.append(BLANK);
                    sbRemarks.append("Parking is not allowed for vehicles above 2.5 Ton.");
                }
                break;
            case 15:
                if(type == VehicleType.ABOVE_4_TON || type == VehicleType.SCHOOL_BUS){
                    sbResult.append(CAN_NOT_PARK);
                    sbEndOfParking.append(BLANK);
                    sbRemarks.append("Parking is not allowed for vehicle above 4 Ton");
                }
                else {
                    if (dayOfTheWeek == Calendar.SATURDAY || (dayOfTheWeek == Calendar.FRIDAY && currentTime.compareTo("13:00:00") > 0)) {
                        sbResult.append(CAN_PARK);
                        sbRemarks.append("From Sunday at 09:00 the parking is in payment unless you have Giv'atayim parking permit.");
                        sbPayment.append(NO);
                        sbEndOfParking.append("Sunday at 09:00");
                        timer = setTimer(dayOfTheWeek, 1, currentTime, "09:00:00");
                    } else if ((dayOfTheWeek == calendar.FRIDAY && currentTime.compareTo("09:00:00") > 0 && currentTime.compareTo("13:00:00") < 0)) {
                        sbResult.append(CAN_PARK);
                        sbRemarks.append("After 13:00 the parking is free of charge.");
                        sbPayment.append(GIVATAYIM_PARKING_PERMIT);
                        sbEndOfParking.append("Sunday at 09:00");
                        timer = setTimer(dayOfTheWeek, 1, currentTime, "09:00:00");
                    } else if (!(dayOfTheWeek == calendar.SATURDAY) && !(dayOfTheWeek == calendar.FRIDAY) && currentTime.compareTo("09:00:00") > 0 && currentTime.compareTo("17:00:00") < 0) {
                        sbResult.append(CAN_PARK);
                        sbRemarks.append("From 17:00 the parking is just for vehicles with regional parking permit.");
                        sbPayment.append(GIVATAYIM_PARKING_PERMIT);
                        sbEndOfParking.append("17:00");
                        timer = setTimer(dayOfTheWeek, dayOfTheWeek, currentTime, "17:00:00");
                    } else if (currentTime.compareTo("17:00:00") > 0 && !(dayOfTheWeek == calendar.SATURDAY) && !(dayOfTheWeek == calendar.FRIDAY)) {
                        sbResult.append(CAN_NOT_PARK);
                        sbRemarks.append("Parking is allowed only for vehicles with regional parking permit until tomorrow at 09:00.\nAfter it the parking is in payment unless you have Giv'atayim parking permit.");
                        sbEndOfParking.append("Tomorrow at 09:00");
                    } else if (currentTime.compareTo("09:00:00") < 0 && !(dayOfTheWeek == calendar.SATURDAY)) {
                        sbResult.append(CAN_NOT_PARK);
                        sbRemarks.append("Parking is allowed only for vehicles with regional parking permit until 09:00.\nAfter it the parking is in payment unless you have Giv'atayim parking permit.");
                        sbEndOfParking.append("09:00");
                    }
                }
                break;
            case 16:
                if(type == VehicleType.ABOVE_4_TON || type == VehicleType.SCHOOL_BUS){
                    sbResult.append(CAN_NOT_PARK);
                    sbEndOfParking.append(BLANK);
                    sbRemarks.append("Parking is not allowed for vehicle above 4 Ton");
                }
                else {
                    if (dayOfTheWeek == Calendar.SATURDAY || (dayOfTheWeek == Calendar.FRIDAY && currentTime.compareTo("13:00:00") > 0)) {
                        sbResult.append(CAN_PARK);
                        sbPayment.append(NO);
                        sbEndOfParking.append("Sunday at 09:00");
                        sbRemarks.append("From Sunday at 09:00 the parking is in payment unless you have Giv'atayim parking permit.");
                        timer = setTimer(dayOfTheWeek, 1, currentTime, "09:00:00");
                    } else if ((dayOfTheWeek == calendar.FRIDAY && currentTime.compareTo("09:00:00") > 0 && currentTime.compareTo("13:00:00") < 0)) {
                        sbResult.append(CAN_PARK);
                        sbRemarks.append("After 13:00 the parking is free of charge.");
                        sbPayment.append(GIVATAYIM_PARKING_PERMIT);
                        sbEndOfParking.append("Sunday at 09:00");
                        timer = setTimer(dayOfTheWeek, 1, currentTime, "09:00:00");
                    } else if (!(dayOfTheWeek == calendar.SATURDAY) && !(dayOfTheWeek == calendar.FRIDAY) && currentTime.compareTo("09:00:00") > 0 && currentTime.compareTo("19:00:00") < 0) {
                        sbResult.append(CAN_PARK);
                        sbRemarks.append("After 19:00 the parking is free of charge.");
                        sbPayment.append(GIVATAYIM_PARKING_PERMIT);
                        sbEndOfParking.append("Tomorrow at 09:00");
                        timer = setTimer(dayOfTheWeek, nextDay, currentTime, "09:00:00");
                    } else if (currentTime.compareTo("19:00:00") > 0 && !(dayOfTheWeek == calendar.SATURDAY) && !(dayOfTheWeek == calendar.FRIDAY)) {
                        sbResult.append(CAN_PARK);
                        sbRemarks.append("From tomorrow at 09:00 the parking is in payment unless you have Giv'atayim parking permit.");
                        sbPayment.append(NO);
                        sbEndOfParking.append("Tomorrow at 09:00");
                        timer = setTimer(dayOfTheWeek, nextDay, currentTime, "09:00:00");
                    } else if (currentTime.compareTo("09:00:00") < 0 && !(dayOfTheWeek == calendar.SATURDAY)) {
                        sbResult.append(CAN_PARK);
                        sbRemarks.append("After 09:00 the parking is in payment unless you have Giv'atayim parking permit.");
                        sbPayment.append(NO);
                        sbEndOfParking.append("09:00");
                        timer = setTimer(dayOfTheWeek, dayOfTheWeek, currentTime, "09:00:00");
                    }
                }
                break;
            case 17:
                remarkString = "Parking sign validity is for the next 18 meters.";
                if (dayOfTheWeek == Calendar.SATURDAY || (dayOfTheWeek == Calendar.FRIDAY && currentTime.compareTo("14:00:00") > 0)) {
                    sbResult.append(CAN_PARK);
                    sbRemarks.append(remarkString);
                    sbPayment.append(NO);
                    sbEndOfParking.append("Sunday at 09:00");
                    timer = setTimer(dayOfTheWeek, 1, currentTime, "09:00:00");
                }
                else if (currentTime.compareTo("17:00:00") > 0 || currentTime.compareTo("09:00:00") < 0) {
                    if(currentTime.compareTo("17:00:00") > 0) {

                        sbEndOfParking.append("Tomorrow at 09:00");
                        timer = setTimer(dayOfTheWeek, nextDay, currentTime, "09:00:00");
                    }
                    else{
                        sbEndOfParking.append("09:00");
                        timer = setTimer(dayOfTheWeek, dayOfTheWeek, currentTime, "09:00:00");
                    }

                    sbResult.append(CAN_PARK);
                    sbRemarks.append(remarkString);
                    sbPayment.append(NO);
                }
                else if(!(dayOfTheWeek == calendar.FRIDAY) && currentTime.compareTo("17:00:00") < 0) {
                    sbResult.append(CAN_NOT_PARK);
                    sbRemarks.append(remarkString);
                    sbEndOfParking.append("17:00");
                    sbRemarks.append("\nParking area for loading and unloading for construction.\nAfter 17:00 the parking is free of charge.");
                }
                else if(dayOfTheWeek == calendar.FRIDAY && currentTime.compareTo("14:00:00") < 0){
                    sbResult.append(CAN_NOT_PARK);
                    sbRemarks.append(remarkString);
                    sbEndOfParking.append("14:00");
                    sbRemarks.append("\nParking area for loading and unloading for construction.\nAfter 14:00 the parking is free of charge.");
                }
        }

        resultString = sbResult.toString();
        if(resultString.contains(CAN_NOT_PARK)){
            sbPayment.append("-");
        }

        endOfTimeString = sbEndOfParking.toString();
        paymentString = sbPayment.toString();
        remarksString = sbRemarks.toString();
    }

    private long setTimer(int dayOfWeek, int dayOfParkTimeEnd, String currentTime, String parkTimeEnd) {
        int daysLeft = 0, hoursLeft = 0, minutesLeft = 0, secondsLeft = 0;
        long totalSeconds = 0;
        String[] currentTimeUnits = currentTime.split(":");
        String[] endOfParkingUnits = parkTimeEnd.split(":");
        int currentHours = Integer.parseInt(currentTimeUnits[0]);
        int parkTimeEndHours = Integer.parseInt(endOfParkingUnits[0]);
        int currentMinutes = Integer.parseInt(currentTimeUnits[1]);
        int parkTimeEndMinutes = Integer.parseInt(endOfParkingUnits[1]);
        int currentSeconds = Integer.parseInt(currentTimeUnits[2]);
        int parkTimeEndSeconds = Integer.parseInt(endOfParkingUnits[2]);
        secondsLeft = parkTimeEndSeconds - currentSeconds;
        minutesLeft = parkTimeEndMinutes - currentMinutes;
        if(secondsLeft < 0){
            secondsLeft += SECONDS_IN_MINUTE;
            minutesLeft--;
        }
        if (dayOfParkTimeEnd == dayOfWeek) {
            hoursLeft = parkTimeEndHours - currentHours;
            if (minutesLeft < 0) {
                hoursLeft--;
                minutesLeft += MINUTES_IN_HOUR;
            }
        } else {
            int daysMargin = dayOfParkTimeEnd - dayOfWeek;
            if (daysMargin < 0) {
                daysMargin += DAYS_IN_WEEK;
            }
            hoursLeft = parkTimeEndHours + ((daysMargin * HOURS_IN_DAY) - currentHours);
            if (minutesLeft < 0) {
                hoursLeft--;
                minutesLeft += MINUTES_IN_HOUR;
            }
            if (hoursLeft >= HOURS_IN_DAY) {
                daysLeft = hoursLeft / HOURS_IN_DAY;
                hoursLeft = hoursLeft - (daysLeft * HOURS_IN_DAY);
            }

        }

        totalSeconds = daysLeft * HOURS_IN_DAY * MINUTES_IN_HOUR * SECONDS_IN_MINUTE;
        totalSeconds = totalSeconds + (hoursLeft * MINUTES_IN_HOUR * SECONDS_IN_MINUTE);
        totalSeconds = totalSeconds + (minutesLeft * SECONDS_IN_MINUTE);
        totalSeconds += secondsLeft;

        return totalSeconds;
    }

    private String showLimitOfParkingTime(String currentTime, int hoursChange, int minutesChange, int secondsChange){
        String[] currentTimeUnits = currentTime.split(":");
        int limitHours = Integer.parseInt(currentTimeUnits[0]);
        int limitMinutes = Integer.parseInt(currentTimeUnits[1]);
        int limitSeconds = Integer.parseInt(currentTimeUnits[2]);
        limitHours += hoursChange;
        limitMinutes += minutesChange;
        limitSeconds += secondsChange;
        StringBuilder sbLimitOfParking = new StringBuilder();
        sbLimitOfParking.append(limitHours);
        sbLimitOfParking.append(":");
        sbLimitOfParking.append(limitMinutes);
        sbLimitOfParking.append(":");
        sbLimitOfParking.append(limitSeconds);
        return sbLimitOfParking.toString();
    }

}
