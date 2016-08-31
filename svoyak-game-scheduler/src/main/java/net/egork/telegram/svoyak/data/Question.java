package net.egork.telegram.svoyak.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author egor@egork.net
 */
public class Question {
    public int cost;
    public String question;
    List<String> answers;
    String comment;

    public Question(int cost, String question, List<String> answers) {
        this(cost, question, answers, "");
    }

    public Question(int cost, String question, List<String> answers, String comment) {
        this.cost = cost;
        this.question = fix(question);
        this.answers = new ArrayList<>(answers);
        this.comment = fix(comment);
        for (int i = 0; i < answers.size(); i++) {
            this.answers.set(i, fix(answers.get(i)));
        }
    }

    private String fix(String s) {
        return s.replace("<", "&lt;").replace(">", "&gt;").replace("&", "&amp;");
    }

    public boolean checkAnswer(String answer) {
        answer = answer.trim().toLowerCase();
        for (String expected : answers) {
            if (noSpace(expected).toLowerCase().equals(noSpace(answer))) {
                return true;
            }
        }
        return false;
    }

    private String noSpace(String answer) {
        StringBuilder builder = new StringBuilder();
        int parentheses = 0;
        for (int i = 0; i < answer.length(); i++) {
            char c = answer.charAt(i);
            if (c == '(' || c == '[' || c == '{') {
                parentheses++;
            } else if (c == ')' || c == ']' || c == '}') {
                parentheses--;
            } else if (parentheses == 0 && Character.isLetterOrDigit(c)) {
                if (c == 'ё' || c == 'Ё') {
                    c = 'е';
                }
                builder.append(c);
            }
        }
        return builder.toString();
    }

    public String authorAnswers() {
        StringBuilder result = new StringBuilder();
        for (String answer : answers) {
            if (result.length() != 0) {
                result.append("\n<b>Зачет</b>: ");
            }
            result.append(answer);
        }
        if (!comment.isEmpty()) {
            result.append("\n<b>Комментарий</b>: ");
            result.append(comment);
        }
        return result.toString();
    }
}
