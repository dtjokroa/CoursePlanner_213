package ca.CoursePlanner.Logic;

import java.util.ArrayList;

/**
 * ToDump Class, holds on static function which is to print the entire list of data in a
 * Human Readable format in the User Terminal to provide for easy debugging.
 */

public class ToDump {

    public static void printNicelyToTerminal(EntryHandler entryHandler) {
        for (ArrayList<Entry> listOfEntries : entryHandler.getListBySubject()) {
            for (int i = 0; i < listOfEntries.size(); i++) {
                Entry entry = listOfEntries.get(i);
                if (i == 0) {
                    System.out.println(entry.getSubject() + " " + entry.getCatalogueNum());
                    System.out.println("\t" + entry.getSemester()
                            + " in " + entry.getLocation()
                            + " by " + entry.getInstructor());
                    System.out.println("\t\t" + "Type=" + entry.getComponentCode() + ", Enrollment="
                            + entry.getEnrollTotal() + "/" + entry.getEnrollCapacity());
                } else {
                    Entry prevEntry = listOfEntries.get(i - 1);
                    if (!prevEntry.getCatalogueNum().equals(entry.getCatalogueNum())) {
                        System.out.println(entry.getSubject() + " " + entry.getCatalogueNum());
                        System.out.println("\t" + entry.getSemester()
                                + " in " + entry.getLocation()
                                + " by " + entry.getInstructor());
                        System.out.println("\t\t" + "Type=" + entry.getComponentCode() + ", Enrollment="
                                + entry.getEnrollTotal() + "/" + entry.getEnrollCapacity());
                    } else {
                        if (entry.getSemester() == prevEntry.getSemester()) {
                            if (!entry.getLocation().equals(prevEntry.getLocation())) {
                                System.out.print("\t" + entry.getSemester()
                                        + " in " + entry.getLocation());
                                if (i < listOfEntries.size() - 1) {
                                    Entry nextEntry = listOfEntries.get(i + 1);
                                    if (nextEntry.getInstructor().contains(entry.getInstructor())) {
                                        System.out.println(" by " + nextEntry.getInstructor());
                                    } else {
                                        System.out.println(" by " + entry.getInstructor());
                                    }
                                } else {
                                    System.out.println(" by " + entry.getInstructor());
                                }
                            }
                        } else {
                            System.out.println("\t" + entry.getSemester()
                                    + " in " + entry.getLocation()
                                    + " by " + entry.getInstructor());
                        }
                        System.out.println("\t\t" + "Type=" + entry.getComponentCode() + ", Enrollment="
                                + entry.getEnrollTotal() + "/" + entry.getEnrollCapacity());
                    }
                }
            }
            System.out.println();
        }
    }
}
