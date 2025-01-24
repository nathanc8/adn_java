import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        String adnFilePath = "src/ressources/adn.txt";
        String adnText = readFile(adnFilePath);
        ArrayList<String> splittedAdnTextList = splitStringIntoEqualsLengthString(adnText, 25);
        String splittedAdnText = splittedAdnTextList.toString();
        String proteinList = convertAdnToProtein(adnText).toString();
        ArrayList<ArrayList<ArrayList<String>>> adnSplittedToGroupsOf5 = splitAdnToGroupsOf5(splittedAdnTextList);
        ArrayList<Map<String, String>> repeatedNucleotides = findRepeatedNucleotides(adnSplittedToGroupsOf5);

        System.out.println(repeatedNucleotides.toString());

    }

    public static String readFile(String filePath) throws FileNotFoundException {
        StringBuilder fileContent = new StringBuilder();
        try {
            File file = new File(filePath);
            Scanner textReader = new Scanner(file);
            while (textReader.hasNextLine()) {
                String data = textReader.nextLine();
                fileContent.append(data);
            }
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File not found: " + filePath);
        }

        return fileContent.toString();
    }

    public static ArrayList<String> splitStringIntoEqualsLengthString(String stringToSplit, int desiredLength) {
        ArrayList<String> equalsLengthStringsList = new ArrayList<>();

        for (int i = 0; i < stringToSplit.length(); i += desiredLength) {
            if (i + desiredLength <= stringToSplit.length()) {
                equalsLengthStringsList.add(stringToSplit.substring(i, i + desiredLength));
            }
        }

        return equalsLengthStringsList;
    }

    public static ArrayList<String> convertAdnToProtein(String stringToSplit) {
        ArrayList<String> adnList = splitStringIntoEqualsLengthString(stringToSplit, 3);

        ArrayList<String> proteinList = new ArrayList<>();

        for (String codon : adnList) {
            String protein = Constants.ADN_CONVERSION_TABLE.getOrDefault(codon, "?");
            proteinList.add(protein);
        }
        return proteinList;
    }

    public static ArrayList<ArrayList<ArrayList<String>>> splitAdnToGroupsOf5(ArrayList<String> listToSplit) {
        ArrayList<ArrayList<ArrayList<String>>> adnSequences = new ArrayList<>();

        for (int i = 0; i < listToSplit.size(); i++) {
            ArrayList<ArrayList<String>> groupsOf5 = new ArrayList<>();
            String adnString = listToSplit.get(i);
            ArrayList<String> iterativeStrings = splitStringIntoEqualsLengthString(adnString, 5);

            for (String group : iterativeStrings) {
                ArrayList<String> adnGroupedBy5 = new ArrayList<>();
                adnGroupedBy5.add(group);
                groupsOf5.add(adnGroupedBy5);
            }
            adnSequences.add(groupsOf5);
        }
        return adnSequences;
    }

    //Ne fonctionne pas, il semblerait que count ne soit pas mis à jour
    public static ArrayList<Map<String, String>> findRepeatedNucleotides(ArrayList<ArrayList<ArrayList<String>>> adnSequences) {
        ArrayList<Map<String, String>> repeatedNucleotides = new ArrayList<>();

        for (ArrayList<ArrayList<String>> group : adnSequences) {
            String[] nucleotides = {"A", "C", "G", "T"};
            Map<String, int[]> counts = new HashMap<>();
            counts.put("A", new int[]{0, 0, 0, 0, 0});
            counts.put("C", new int[]{0, 0, 0, 0, 0});
            counts.put("G", new int[]{0, 0, 0, 0, 0});
            counts.put("T", new int[]{0, 0, 0, 0, 0});

            for (ArrayList<String> sequence : group) {
                for (int i = 0; i < sequence.size(); i++) {
                    String nucleotide = sequence.get(i);
                    if (counts.containsKey(nucleotide)) {
                        counts.get(nucleotide)[i]++;
                    }
                }
            }

            Map<String, String> stringCounts = new HashMap<>();
            for (String nucleotide : nucleotides) {
                stringCounts.put(nucleotide, arrayToString(counts.get(nucleotide)));
            }

            repeatedNucleotides.add(stringCounts);
        }

        return repeatedNucleotides;
    }

    private static String arrayToString(int[] array) {
        StringBuilder sb = new StringBuilder();
        for (int count : array) {
            sb.append(count).append(" ");
        }
        return sb.toString().trim();
    }
}