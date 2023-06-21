package uni.makarov.verification;

import java.io.*;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {
    /*
    Завдання В.4:
    Знайти слова в файлі, що були розмежовані символами
    Слова не мають бути довші за 30 символів
    Вивести тільки ті слова, що мають не повторюванні літери
    */

    //Checks if words have repeating letters
    public boolean checkIfRepeatingLetters(String str) {
        //Takes out hyphen and uppercase letters before splitting by characters
        Map<String, Long> map = Arrays.stream(str.replaceAll("-", "").toLowerCase(Locale.ROOT).split(""))
                .collect(Collectors.groupingBy(Function.identity(),
                        Collectors.counting()));
        return map.values().stream().anyMatch(count -> count > 1);
    }

    public BufferedReader setTextSource(String text) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(text));
        } catch (FileNotFoundException e){
            //Assume Text
            reader = new BufferedReader(new StringReader(text));
        } catch (NullPointerException e) {
            //Assume null as empty
            //reader = new BufferedReader(new StringReader(""));
            throw e;
        }
        return reader;
    }

    public ArrayList<String> removeLetterRepeatingWords(String text) {
        ArrayList<String> checkedWords = null;
        try(BufferedReader in = setTextSource(text)) {
            checkedWords = new ArrayList<>();
            String read;
            while ((read = in.readLine()) != null) {
                //Divide by spaces
                String[] splitted = read.split("\\s+");
                for (String part : splitted) {

                    //Check if the word is a dash to avoid deleting hyphens
                    if (!part.equals("-")) {
                        String filteredWord = Normalizer.normalize(part, Normalizer.Form.NFKD);          //Replace accent letters (ex. 'è')
                        filteredWord = filteredWord.replaceAll("[^a-zA-Z-]", "");    //Only leave alphabetic letters and hyphen
                        filteredWord = filteredWord.substring(0, Math.min(filteredWord.length(), 30));  //Trim words
                        //Skip if word is empty, has repeating letters or was already checked
                        if (filteredWord.equals("") || checkedWords.contains(filteredWord))
                            continue;
                        if (!checkIfRepeatingLetters(filteredWord)) {
                            checkedWords.add(filteredWord);
                        }
                    }
                }
            }
        } catch (NullPointerException e) {
            System.out.println("No text source: " + e);
            e.printStackTrace();
            throw e;
        }
        catch (IOException e) {
            System.out.println("IO Exception: " + e);
            e.printStackTrace();
        }
        catch (Exception e) {
            System.out.println("Exception: " + e);
            e.printStackTrace();
        }
        return checkedWords;
    }

    public static void main(String[] args) {
        /*final String text = "UnfilteredText.txt";
        Main app = new Main();

        ArrayList<String> processedWords = null;
        try {
            processedWords = app.removeLetterRepeatingWords(text);
        }
        catch (Exception e) {
            System.out.println("Exception: " + e);
            e.printStackTrace();
        }

        System.out.println(processedWords);*/
    }
}
