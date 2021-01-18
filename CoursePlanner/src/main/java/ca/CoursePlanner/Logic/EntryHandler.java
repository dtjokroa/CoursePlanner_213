package ca.CoursePlanner.Logic;

import java.util.ArrayList;

/***
 * Entry Handler Organizes the aggregated information given from csvReader
 * It will then reorganize it based on which information the controller needs from the
 * backend
 */
public class EntryHandler {
    private ArrayList<Entry> organizedList;
    private ArrayList<ArrayList<Entry>> listBySubject = new ArrayList<>();
    private ArrayList<String> subjectList = new ArrayList<>();
    private ArrayList<Entry> offeringList = new ArrayList<>();
    private ArrayList<Entry> sectionList = new ArrayList<>();


    public EntryHandler(CsvReader reader) {
        this.organizedList = reader.getOrganizedList();
        categorizeBySubject();
    }


    public ArrayList<String> getSubjectList() {
        return subjectList;
    }

    public ArrayList<ArrayList<Entry>> getListBySubject() {
        return listBySubject;
    }

    public void findSession(int sectionCode, String professor, String catalogueNumber) {
        sectionList.clear();
        for (Entry entry : organizedList) {
            if (entry.getSemester() == sectionCode && professor.contains(entry.getInstructor()) && catalogueNumber.equals(entry.getCatalogueNum())) {
                sectionList.add(entry);
            }
        }
    }

    public ArrayList<Entry> getSessions() {
        return sectionList;
    }

    public void categorizeBySubject() {
        int numOfSubjects = 0;
        subjectList.clear();
        listBySubject.clear();
        for (Entry entry : organizedList) {
            String subject = entry.getSubject();
            if (numOfSubjects == 0) {
                subjectList.add(subject);
                numOfSubjects++;
            } else {
                String target = subjectList.get(numOfSubjects - 1);
                if (!subject.equals(target)) {
                    subjectList.add(subject);
                    numOfSubjects++;
                }
            }
        }
        for (int i = 0; i < numOfSubjects; i++) {
            ArrayList<Entry> newEntry = new ArrayList<>();
            listBySubject.add(newEntry);
        }

        for (Entry entry : organizedList) {
            String currentSubject = entry.getSubject();
            for (int i = 0; i < subjectList.size(); i++) {
                String string = subjectList.get(i);
                if (currentSubject.equals(string)) {
                    listBySubject.get(i).add(entry);
                }
            }
        }
    }


    public ArrayList<ArrayList<String>> categorizeByCourses() {
        int numOfSubjects = subjectList.size();
        ArrayList<ArrayList<String>> listByCourses = new ArrayList<>();
        for (int i = 0; i < numOfSubjects; i++) {
            ArrayList<String> newArrayList = new ArrayList<>();
            listByCourses.add(newArrayList);
        }

        int index = 0;
        for (ArrayList<Entry> list : listBySubject) {
            for (int i = 0; i < list.size(); i++) {
                Entry entry = list.get(i);
                if (i == 0) {
                    listByCourses.get(index).add(entry.getCatalogueNum());
                } else {
                    Entry prevEntry = list.get(i - 1);
                    if (!prevEntry.getCatalogueNum().equals(entry.getCatalogueNum())) {
                        listByCourses.get(index).add(entry.getCatalogueNum());
                    }
                }
            }
            index++;
        }
        return listByCourses;
    }

    public void findOfferings(String courseNumber, String subjectName) {
        offeringList.clear();
        for (int i = 0; i < organizedList.size(); i++) {
            if (i == 0) {
                if (courseNumber.equals(organizedList.get(i).getCatalogueNum()) && subjectName.equals(organizedList.get(i).getSubject())) {
                    offeringList.add(organizedList.get(i));
                }
            } else {
                Entry currentEntry = organizedList.get(i);
                if (courseNumber.equals(organizedList.get(i).getCatalogueNum()) && subjectName.equals(organizedList.get(i).getSubject())) {
                    Entry prevEntry = organizedList.get(i - 1);
                    if (!prevEntry.getInstructor().equals(currentEntry.getInstructor())) {
                        if (i < organizedList.size() - 1) {
                            Entry nextEntry = organizedList.get(i + 1);
                            if ((nextEntry.getInstructor().contains(currentEntry.getInstructor())) && nextEntry.getInstructor().length() != currentEntry.getInstructor().length()) {
                                offeringList.add(nextEntry);
                                i++;
                            } else if (currentEntry.getInstructor().contains(nextEntry.getInstructor()) && nextEntry.getInstructor().length() != currentEntry.getInstructor().length()) {
                                i++;
                                offeringList.add(nextEntry);
                            } else {
                                offeringList.add(currentEntry);
                            }
                        } else {
                            if (!prevEntry.getInstructor().contains(currentEntry.getInstructor())) {
                                offeringList.add(currentEntry);
                            }
                        }
                    }
                }
            }
        }
    }

    public ArrayList<Entry> getOfferingList() {
        return offeringList;
    }

}
