package br.ufmg.dcc.unit_test_quiz;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class QuestionTest {

    Question question;

    @Test
    public void loadBinaryQuestion_isCorrect() throws Exception {

        String expectedTitle = "Teste para questao com somente 2 alternativas";
        String expectedText = "Texto qualquer 1";
        String expectedOptionA = "A";
        String expectedOptionB = "B";
        int expectedAnswer = 0;

        String jsonString = "{\"title\":\"Teste para questao com somente 2 alternativas\", \"text\": \"Texto qualquer 1\", \"options\": [\"A\", \"B\"], \"answer\": 0}";
        InputStream inputStream = new ByteArrayInputStream(jsonString.getBytes());
        Question question = new Question(inputStream);

        assertEquals(question.getTitle(), expectedTitle);
        assertEquals(question.getText(), expectedText);
        assertEquals(question.getOptions().get(0), expectedOptionA);
        assertEquals(question.getOptions().get(1), expectedOptionB);
        assertEquals(question.getAnswer(), expectedAnswer);
    }

    @Test
    public void loadMultiQuestion_isCorrect() throws Exception {

        String expectedTitle = "Teste para questao com mais de 2 alternativas";
        String expectedText = "Texto qualquer 2";
        String expectedOptionA = "A";
        String expectedOptionB = "B";
        String expectedOptionC = "C";
        String expectedOptionD = "D";
        int expectedAnswer = 3;

        String jsonString = "{\"title\":\"Teste para questao com mais de 2 alternativas\", \"text\": \"Texto qualquer 2\", \"options\": [\"A\", \"B\", \"C\", \"D\"], \"answer\": 3}";
        InputStream inputStream = new ByteArrayInputStream(jsonString.getBytes());
        Question question = new Question(inputStream);

        assertEquals(question.getTitle(), expectedTitle);
        assertEquals(question.getText(), expectedText);
        assertEquals(question.getOptions().get(0), expectedOptionA);
        assertEquals(question.getOptions().get(1), expectedOptionB);
        assertEquals(question.getOptions().get(2), expectedOptionC);
        assertEquals(question.getOptions().get(3), expectedOptionD);
        assertEquals(question.getAnswer(), expectedAnswer);
    }

}