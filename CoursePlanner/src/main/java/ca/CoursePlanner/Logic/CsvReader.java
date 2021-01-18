package ca.CoursePlanner.Logic;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/***
 * CsvReader will read the Csv and translate the information into a lists of entries
 * using the entry class. Then it will sort and organize the lists into sorted order
 * and aggregate the lectures and labs
 */
public class CsvReader {
    private ArrayList<Entry> entryList = new ArrayList<>();
    private ArrayList<Entry> organizedList = new ArrayList<>();


    public CsvReader(String file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            br.readLine();
            String input = br.readLine();

            while (input != null) {
                Entry entry = new Entry();
                ArrayList<String> savedString = new ArrayList<>();

                int index1 = 0;
                while (true) {
                    int firstQuotation = input.indexOf("\"");
                    if (index1 == firstQuotation) {
                        int index2 = input.indexOf("\"", index1 + 1);
                        String subString = input.substring(index1 + 1, index2 - 1);
                        savedString.add(subString);
                        index1 = input.indexOf(",", index2 + 1);
                        index1++;
                    } else {
                        int index2 = input.indexOf(",", index1 + 1);
                        if (index2 == -1) {
                            break;
                        }
                        String subString = input.substring(index1, index2);
                        savedString.add(subString);
                        index1 = index2;
                        index1++;
                    }
                }
                String subString = input.substring(index1);
                savedString.add(subString);

                entry.setSemester(Integer.parseInt(savedString.get(0)));
                entry.setSubject(savedString.get(1).trim());
                entry.setCatalogueNum(savedString.get(2).trim());
                entry.setLocation(savedString.get(3).trim());
                entry.setEnrollCapacity(Integer.parseInt(savedString.get(4).trim()));
                entry.setEnrollTotal(Integer.parseInt(savedString.get(5).trim()));
                entry.setInstructor(savedString.get(6).trim());
                entry.setComponentCode(savedString.get(7).trim());
                entryList.add(entry);
                input = br.readLine();
                sortCSVList();
            }


            CSVAggregate();
            //categorizeBySubject();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<Entry> getOrganizedList() {
        return organizedList;
    }


    private void sortCSVList() {
        entryList.sort((entry, entry1) -> {
            String entrySubject = entry.getSubject();
            String entry1Subject = entry1.getSubject();
            int entryCompared = entrySubject.compareTo(entry1Subject);

            if (entryCompared != 0) {
                return entryCompared;
            }

            String entryCatalogNum = entry.getCatalogueNum();
            String entry1CatalogNum = entry1.getCatalogueNum();
            entryCompared = entryCatalogNum.compareTo(entry1CatalogNum);

            if (entryCompared != 0) {
                return entryCompared;
            }

            Integer entrySemester = entry.getSemester();
            Integer entry1Semester = entry1.getSemester();
            entryCompared = entrySemester.compareTo(entry1Semester);

            if (entryCompared != 0) {
                return entryCompared;
            }

            String entryLocation = entry.getLocation();
            String entry1Location = entry1.getLocation();
            entryCompared = entryLocation.compareTo(entry1Location);

            if (entryCompared != 0) {
                return entryCompared;
            }

            String entryComponentCode = entry.getComponentCode();
            String entry1ComponentCode = entry1.getComponentCode();
            entryCompared = entryComponentCode.compareTo(entry1ComponentCode);

            return entryCompared;
        });
    }

    private void CSVAggregate() {
        int tmpTotal;
        int tmpCapacity;
        StringBuilder prof;
        int count = 0;
        Entry entry = entryList.get(count);
        tmpTotal = entry.getEnrollTotal();
        tmpCapacity = entry.getEnrollCapacity();
        prof = new StringBuilder(entry.getInstructor());
        boolean nameCollected = false;
        while (count < entryList.size() - 1) {
            Entry entryToCompare = entryList.get(count + 1);
            if (entry.equals(entryToCompare)) {
                tmpTotal += entryToCompare.getEnrollTotal();
                tmpCapacity += entryToCompare.getEnrollCapacity();
                if (!entry.getInstructor().equals(entryToCompare.getInstructor()) && !nameCollected) {
                    prof = new StringBuilder(entryToCompare.getInstructor());
                    prof.append(", ").append(entry.getInstructor());
                    nameCollected = true;
                }
                count++;
            } else {
                Entry newEntry = new Entry(entry.getSemester(), entry.getSubject(),
                        entry.getCatalogueNum(), entry.getLocation(), tmpCapacity, tmpTotal,
                        prof.toString(), entry.getComponentCode());
                organizedList.add(newEntry);
                count++;
                entry = entryList.get(count);
                tmpTotal = entry.getEnrollTotal();
                tmpCapacity = entry.getEnrollCapacity();
                prof = new StringBuilder(entry.getInstructor());
                nameCollected = false;


            }

        }
        Entry newEntry = new Entry(entry.getSemester(), entry.getSubject(),
                entry.getCatalogueNum(), entry.getLocation(), tmpCapacity, tmpTotal,
                prof.toString(), entry.getComponentCode());
        organizedList.add(newEntry);
    }


}
