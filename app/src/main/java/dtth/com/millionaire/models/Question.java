package dtth.com.millionaire.models;

public class Question {
    public int id;
    public String question;
    public String caseA;
    public String caseB;
    public String caseC;
    public String caseD;
    public int trueCase;

    public Question(int id, String question, String caseA, String caseB, String caseC, String caseD, int trueCase) {
        this.id = id;
        this.question = question;
        this.caseA = caseA;
        this.caseB = caseB;
        this.caseC = caseC;
        this.caseD = caseD;
        this.trueCase = trueCase;
    }
}
