package com.example.oj.controller;

import com.example.oj.constant.Utils;
import com.example.oj.document.DetailContest;
import com.example.oj.document.ProblemDocument;
import com.example.oj.document.SubmissionDocument;
import com.example.oj.dto.Execute;
import com.example.oj.dto.ResultTestCase;
import com.example.oj.service.DetailContestService;
import com.example.oj.service.ProblemService;
import com.example.oj.service.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Controller
@RequiredArgsConstructor
public class DetailContestController {

    private final SimpMessagingTemplate messagingTemplate;
    private final DetailContestService detailContestService;
    private final ProblemService problemService;
    private final Utils utils;
    @MessageMapping("/contest/execute")
    public void executeCode(@Payload String id) throws IOException, ExecutionException, InterruptedException {
        DetailContest detailContest = detailContestService.findById(id).get();
        ProblemDocument problemDocument = problemService.findOne(detailContest.getProblemId()).get();
        double maxTime = 0;
        double maxMemory = 0;
        int rightTest = 0;
        File file = new File("Main.java");
        FileWriter writer = new FileWriter(file);
        writer.write(detailContest.getSource());
        writer.close();
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        int compilationResult = compiler.run(null, null, null, file.getPath());
        if(compilationResult != 0) {
            detailContest.setResult("COMPILATION ERROR");
            detailContestService.update(detailContest);
            Map<String, String> map = new HashMap<>();
            map.put("result", "COMPILATION ERROR");
            messagingTemplate.convertAndSendToUser(
                    detailContest.getUserId(), "/queue/messages",
                    map
            );
            return;
        }
        Runtime runtime = Runtime.getRuntime();
        List<String> outputs = new ArrayList<>();
        for (int i = 0; i < problemDocument.getTestcase().size(); i++) {
            Map<String, String> test = problemDocument.getTestcase().get(i);

            Execute execute = utils.executeJudgeCode(test, Double.parseDouble(problemDocument.getTimeLimit()),runtime, "java");
            ResultTestCase resultTestCase = new ResultTestCase();
            if(execute.getOutput() != null) {
                if(execute.getResult() == null) {
                    File outputFile = new File(test.get("output"));
                    String outputTest = utils.handleTestResult(outputFile);

                    String temp = utils.checkResult(outputTest, execute.getOutput());

                    resultTestCase.setResult(temp);
                    resultTestCase.setTime(execute.getTime());
                    resultTestCase.setMemory(execute.getMemory());

                    outputs.add(temp);
                }
                else {
                    resultTestCase.setResult(execute.getResult());
                    resultTestCase.setTime(execute.getTime());
                    resultTestCase.setMemory(execute.getMemory());
                    outputs.add(execute.getResult());
                }
                maxTime = Math.max(maxTime, execute.getTime());
                maxMemory = Math.max(maxMemory, execute.getMemory());
            }
            else {
                resultTestCase.setResult(execute.getResult());
                outputs.add(execute.getResult());
                detailContest.setCompilerReport(execute.getCompilerReport());
            }

            detailContest.getTestcases().add(resultTestCase);
            messagingTemplate.convertAndSendToUser(
                    detailContest.getUserId(), "/queue/messages",
                    resultTestCase
            );
        }

        Map<String, String> map = new HashMap<>();
        String res = utils.checkFinalResult(outputs);
        if(res.equals("")) {
            detailContest.setResult("ACCEPTED");
            map.put("result", "ACCEPTED");
        }
        else {
            detailContest.setResult(res);
            map.put("result", res);
        }

        detailContest.setTime((float) maxTime);
        detailContest.setMemory((float) maxMemory);
        for(String output: outputs) {
            if(output.equals("ACCEPTED"))
                ++rightTest;
        }
        detailContest.setRightTest(rightTest);
        detailContest.setPoint(utils.calculateScore(rightTest, detailContest.getTotalTest(), detailContest.getMaxScored()));
        detailContestService.update(detailContest);
        map.put("scored", utils.calculateScore(rightTest, detailContest.getTotalTest(), detailContest.getMaxScored()).toString());
        messagingTemplate.convertAndSendToUser(
                detailContest.getUserId(), "/queue/messages",
                map
        );

        file.delete();
    }

    @MessageMapping("/contest/execute-python")
    public void execute(@Payload String id) throws IOException {
        DetailContest detailContest = detailContestService.findById(id).get();
        ProblemDocument problemDocument = problemService.findOne(detailContest.getProblemId()).get();
        double maxTime = 0;
        double maxMemory = 0;
        int rightTest = 0;
        File file = new File("main.py");
        FileWriter writer = new FileWriter(file);
        writer.write(detailContest.getSource());
        writer.close();

        Runtime runtime = Runtime.getRuntime();
        List<String> outputs = new ArrayList<>();
        for (int i = 0; i < problemDocument.getTestcase().size(); i++) {
            Map<String, String> test = problemDocument.getTestcase().get(i);

            Execute execute = utils.executeJudgeCode(test, Double.parseDouble(problemDocument.getTimeLimit()),runtime, "python");
            ResultTestCase resultTestCase = new ResultTestCase();
            if(execute.getOutput() != null) {
                if(execute.getResult() == null) {
                    File outputFile = new File(test.get("output"));
                    String outputTest = utils.handleTestResult(outputFile);

                    String temp = utils.checkResult(outputTest, execute.getOutput());

                    resultTestCase.setResult(temp);
                    resultTestCase.setTime(execute.getTime());
                    resultTestCase.setMemory(execute.getMemory());

                    outputs.add(temp);
                }
                else {
                    resultTestCase.setResult(execute.getResult());
                    resultTestCase.setTime(execute.getTime());
                    resultTestCase.setMemory(execute.getMemory());
                    outputs.add(execute.getResult());
                }
                maxTime = Math.max(maxTime, execute.getTime());
                maxMemory = Math.max(maxMemory, execute.getMemory());
            }
            else {
                resultTestCase.setResult(execute.getResult());
                outputs.add(execute.getResult());
                detailContest.setCompilerReport(execute.getCompilerReport());
            }

            detailContest.getTestcases().add(resultTestCase);
            messagingTemplate.convertAndSendToUser(
                    detailContest.getUserId(), "/queue/messages",
                    resultTestCase
            );
        }

        Map<String, String> map = new HashMap<>();
        String res = utils.checkFinalResult(outputs);
        if(res.equals("")) {
            detailContest.setResult("ACCEPTED");
            map.put("result", "ACCEPTED");
        }
        else {
            detailContest.setResult(res);
            map.put("result", res);
        }

        detailContest.setTime((float) maxTime);
        detailContest.setMemory((float) maxMemory);
        for(String output: outputs) {
            if(output.equals("ACCEPTED"))
                ++rightTest;
        }
        detailContest.setRightTest(rightTest);
        detailContest.setPoint(utils.calculateScore(rightTest, detailContest.getTotalTest(), detailContest.getMaxScored()));
        detailContestService.update(detailContest);
        map.put("scored", utils.calculateScore(rightTest, detailContest.getTotalTest(), detailContest.getMaxScored()).toString());
        messagingTemplate.convertAndSendToUser(
                detailContest.getUserId(), "/queue/messages",
                map
        );
        file.delete();


    }
}
