import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;

public class BabyBirths {
    public void printNames () {
        FileResource fr = new FileResource();
        for (CSVRecord rec : fr.getCSVParser(false)) {
            int numBorn = Integer.parseInt(rec.get(2));
            if (numBorn <= 100) {
                System.out.println("Name " + rec.get(0) +
                        " Gender " + rec.get(1) +
                        " Num Born " + rec.get(2));
            }
        }
    }

    public void totalBirths (FileResource fr) {
        int totalBirths = 0;
        int totalBoys = 0;
        int totalGirls = 0;
        for (CSVRecord rec : fr.getCSVParser(false)) {
            int numBorn = Integer.parseInt(rec.get(2));
            totalBirths += numBorn;
            if (rec.get(1).equals("M")) {
                totalBoys += numBorn;
            }
            else {
                totalGirls += numBorn;
            }
        }
        System.out.println("The total names: " + totalBirths);
        System.out.println("The number of unique girls names: " + totalGirls);
        System.out.println("The number of unique boys names: " + totalBoys);
    }

    public void testTotalBirths () {
        FileResource fr = new FileResource("babynamedata/us_babynames_by_year/yob2014.csv");
        totalBirths(fr);
    }

    public static int getRank (int year, String name, String gender) {
        FileResource fr = new FileResource("babynamedata/us_babynames_test/yob" + String.valueOf(year) +"short.csv");
        int pivot = 0;
        for (CSVRecord rec : fr.getCSVParser(false)) {
            if (rec.get(1).equals(gender)) {
                pivot++;
                if(rec.get(0).equals(name))
                    return pivot;
            }
        }
        return 0;
    }

    public static String getName(int year, int rank, String gender) {
        FileResource fr = new FileResource("babynamedata/us_babynames_test/yob" + String.valueOf(year) +"short.csv");

        int pivot = 0;
        for(CSVRecord rec : fr.getCSVParser(false)){
            if(rec.get(1).equals(gender)){

                pivot ++;
                if(pivot == rank) return rec.get(0);
            }
        }

        System.out.println("The rank: " + rank + "... The last one rank " + pivot + ".");
        return "NO NAME";

    }

    public static void whatIsNameInYear(String name, int year, int newYear, String gender) {
        int rank = getRank(year, name, gender);
        String equalName = getName(newYear, rank, gender);

        if (gender == "F"){
            System.out.println( name + " born in " + year + " would be " + equalName + " if she was born in " + newYear);
        }
        else if (gender == "M"){
            System.out.println( name + " born in " + year + " would be " + equalName + " if he was born in " + newYear);
        }
    }

    public static int yearOfHighestRank(String name, String gender) {

        int rank = 1000000;
        int yearHigh = 0;

        DirectoryResource dr = new DirectoryResource();
        for(File fi : dr.selectedFiles()){

            String fileName = fi.getName();
            int year = Integer.parseInt(fileName.replaceAll("[\\D]", ""));

            FileResource fr = new FileResource(fi);
            int currRank = -1;
            int pivot = 0;
            for(CSVRecord record : fr.getCSVParser(false)){

                if(record.get(1).equals(gender)) {

                    pivot++;

                    if(record.get(0).equals(name)) {
                        currRank = pivot;
                        break;
                    }

                }

            }
            if(currRank != -1 && currRank < rank){
                rank = currRank;
                yearHigh = year;
            }
        }
        return yearHigh;
    }

    public static double getAverageRank(String name, String gender) {
        DirectoryResource dr = new DirectoryResource();
        int fileNum = 0;
        int totalRank = 0;

        for(File fi : dr.selectedFiles()){
            fileNum++;
            FileResource fr = new FileResource(fi);

            int pivot = 0;
            int currRank = 0;
            for(CSVRecord record : fr.getCSVParser(false) ){
                if(record.get(1).equals(gender)) {
                    pivot++;
                    if(record.get(0).equals(name)) {
                        currRank = pivot;
                        break;
                    }
                }
            }
            totalRank += currRank;
        }
        if(totalRank == 0) return -1;
        else return (double)(totalRank)/fileNum;
    }

    public static int getTotalBirthsRankedHigher(int year, String name, String gender) {
        FileResource fr = new FileResource("babynamedata/us_babynames_test/yob" + String.valueOf(year) +"short.csv");

        int sum = 0;
        for(CSVRecord record : fr.getCSVParser(false)){

            if(record.get(1).equals(gender)){

                if(record.get(0).equals(name))
                    return sum;

                sum += Integer.parseInt(record.get(2));

            }

        }

        return sum;
    }

    public static void getRankTest() {
        System.out.println(getRank(2012, "Mason", "M"));
        System.out.println(getRank(2012, "Mason", "F"));
    }

    public static void whatIsNameInYearTest() {
        whatIsNameInYear("Isabella", 2012, 2014, "F");
    }

    public static void yearOfHighestRankTest() {
        System.out.println(yearOfHighestRank("Mason", "M"));
    }

    public static void getAverageRankTest() {
        System.out.println(getAverageRank("Jacob", "M"));
    }

    public static void getTotalBirthsRankedHigherTest(){
        System.out.println(getTotalBirthsRankedHigher(2012, "Ethan", "M"));
    }

    public static void main(String[] args) {
        getRankTest();
        whatIsNameInYearTest();
        yearOfHighestRankTest();
        getAverageRankTest();
        getTotalBirthsRankedHigherTest();
    }
}
