import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

class Information{
    String sentence;
    String result;

    public Information(String sentence, String result){
        this.sentence = sentence;
        this.result = result;
    }
}
class wordCount{
    int countS;
    int countNS;
    String word;
    int occ;
    public wordCount(String word){
        this.word = word;
        this.countS = 0;
        this.countNS = 0;
        this.occ = 0;
    }
}
public class NaiveBayes {
    static ArrayList<Information> ar = new ArrayList<Information>();
    static ArrayList<String> words = new ArrayList<>();
    static ArrayList<wordCount> freq = new ArrayList<wordCount>();
    static int cSports=0, cNSports=0;
    static int countWordsinS = 0, countWordsinNS = 0;
    public static void laplace(){
        for(int i=0;i<words.size();i++){
            freq.get(i).countNS ++;
            freq.get(i).countS ++;
        }
        countWordsinS += words.size();
        countWordsinNS += words.size();
    }
    public static String calcProbab(String statement){
        float ps=1, pns=1;
        String [] stest = statement.split(" ");
        Iterator itr = freq.iterator();
        System.out.println("");
        System.out.print("Calculating probability of being Sports: ");
        for(int i=0;i<stest.length;i++){
            while(itr.hasNext()){
                wordCount w = (wordCount) itr.next();
                if(w.word.equals(stest[i])){
                    ps = ps * (w.countS/(float)countWordsinS);
                }
            }
        }
        ps = ps * (cSports/(float)ar.size());
        System.out.print(ps);
        System.out.println("");
        System.out.print("Calculating probability of being Not Sports: ");
        Iterator itr2 = freq.iterator();
        for(int i=0;i<stest.length;i++){
            while(itr2.hasNext()){
                wordCount w = (wordCount) itr2.next();
                if(w.word.equals(stest[i])){
                    pns = pns * (w.countNS/(float)countWordsinNS);
                }
            }
        }
        pns = pns * (cNSports/(float) ar.size());
        System.out.print(pns);
        System.out.println("");
        if(ps > pns){
            System.out.println("'"+statement+"'"+" categorised as SPORTS");
            return "Sports";
        }
        else{
            System.out.println("'"+statement+"'"+" categorised as NOT SPORTS");
            return "Not sports";
        }
    }
    public static void initialiseData(){
        ar.add(new Information("A great game","Sports"));
        ar.add(new Information("the election was over","Not sports"));
        ar.add(new Information("very clean match","Sports"));
        ar.add(new Information("A clean but forgetable match","Sports"));
        ar.add(new Information("it was close election","Not sports"));
        for(int i=0;i<ar.size();i++){
            if(ar.get(i).result.equals("Sports"))
                cSports++;
            else
                cNSports++;
        }
        for(int i=0;i<ar.size();i++){
            String [] s = ar.get(i).sentence.split(" ");
            for(String sen : s){
                if(!words.contains(sen))
                    words.add(sen);
            }
        }
        for(int i=0;i<words.size();i++){
            wordCount w = new wordCount(words.get(i));
            for(int j=0;j<ar.size();j++){
                if(ar.get(j).sentence.contains(words.get(i)) 
                        && ar.get(j).result.equals("Sports"))
                    w.countS++;
                else if(ar.get(j).sentence.contains(words.get(i)) 
                        && ar.get(j).result.equals("Not sports"))
                    w.countNS++;
                else
                ;
            }
            w.occ = w.countNS + w.countS;
            freq.add(w);
        }
        for(int i=0;i<words.size();i++){
            if(freq.get(i).countNS == 0 || freq.get(i).countS == 0)
                laplace();
            break;
        }
        for(int i=0;i<ar.size();i++){
            String [] p = ar.get(i).sentence.split(" ");
            if(ar.get(i).result.equals("Sports"))
                    countWordsinS += p.length;
            else
                countWordsinNS += p.length;
        }
    }
    public static void calcAccuracy(){
        ArrayList<Information> test = new ArrayList<Information>();
        int counter = 0;
        test.add(new Information("A match was over","Sports"));
        test.add(new Information("the election was close","Not sports"));
        test.add(new Information("the election was clean but forgetable","Not sports"));
        test.add(new Information("A very close game","Sports"));
        test.add(new Information("great election was over","Not sports"));
        ArrayList<String> testRes = new ArrayList<String>();
        for(int i=0;i<test.size();i++)
            testRes.add(calcProbab(test.get(i).sentence));
        for(int i=0;i<test.size();i++){
            if(test.get(i).result.equals(testRes.get(i)))
                counter++;
        }
        System.out.println("");
        System.out.println("Accuracy of Naive Bayes: "+(counter*100/(float)test.size())+"%");
    }
    public static void main(String args[]){
        Scanner sc = new Scanner(System.in);
        initialiseData();
        System.out.println("Enter statement for prediction: ");
        String statement = sc.nextLine();
        calcProbab(statement);
        calcAccuracy();
    }
}
